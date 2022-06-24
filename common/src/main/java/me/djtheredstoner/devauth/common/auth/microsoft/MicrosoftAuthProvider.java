package me.djtheredstoner.devauth.common.auth.microsoft;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.djtheredstoner.devauth.common.DevAuth;
import me.djtheredstoner.devauth.common.auth.IAuthProvider;
import me.djtheredstoner.devauth.common.auth.SessionData;
import me.djtheredstoner.devauth.common.auth.microsoft.oauth.CodeOAuthProvider;
import me.djtheredstoner.devauth.common.auth.microsoft.oauth.OAuthProvider;
import me.djtheredstoner.devauth.common.auth.microsoft.token.OAuthToken;
import me.djtheredstoner.devauth.common.auth.microsoft.token.Token;
import me.djtheredstoner.devauth.common.auth.microsoft.token.TokenKey;
import me.djtheredstoner.devauth.common.auth.microsoft.token.XBLToken;
import me.djtheredstoner.devauth.common.config.Account;
import me.djtheredstoner.devauth.common.util.Util;
import me.djtheredstoner.devauth.common.util.request.Http;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * (the third rewrite of) <br/>
 * A nasty little thing. <br/><br/>
 * References: <ul>
 * <li><a href="https://wiki.vg/Microsoft_Authentication_Scheme">wiki.vg</a></li>
 * <li><a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow">Microsoft docs</a></li>
 * <li><a href="https://datatracker.ietf.org/doc/html/rfc7636">RFC 7636: Proof Key for Code Exchange</a></li>
 * </ul>
 *
 * @author DJtheRedstoner
 */
public class MicrosoftAuthProvider implements IAuthProvider {

    private static final String SCOPES = "XboxLive.signin XboxLive.offline_access";
    private static final String XBL_URL = "https://user.auth.xboxlive.com/user/authenticate";
    private static final String XSTS_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";
    private static final String MINECRAFT_URL = "https://api.minecraftservices.com/authentication/login_with_xbox";
    private static final String MINECRAFT_PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile";

    private static final Logger logger = LogManager.getLogger("DevAuth/Microsoft");

    private final DevAuth devAuth;
    private final OAuthProvider oAuthProvider = new CodeOAuthProvider(logger, SCOPES);

    private final Map<TokenKey<?>, Supplier<? extends Token>> tokenRegistry = new LinkedHashMap<>();
    private final Map<TokenKey<?>, Token> tokenStore = new LinkedHashMap<>();

    private JsonObject accountsData;

    public MicrosoftAuthProvider(DevAuth devAuth) {
        this.devAuth = devAuth;
        initTokenRegistry();
    }

    protected void initTokenRegistry() {
        register(TokenKey.OAUTH_TOKEN, this::getOAuthToken);
        register(TokenKey.XBL_TOKEN, this::getXBLToken);
        register(TokenKey.XSTS_TOKEN, this::getXSTSToken);
        register(TokenKey.SESSION_TOKEN, this::getMcSession);
    }

    protected <T extends Token> void register(TokenKey<T> tokenKey, Supplier<T> supplier) {
        tokenRegistry.put(tokenKey, supplier);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Token> T get(TokenKey<T> tokenKey) {
        Supplier<T> supplier = (Supplier<T>) tokenRegistry.get(tokenKey);
        T token = (T) tokenStore.get(tokenKey);
        if (token == null || token.isExpired()) {
            logger.info("Fetching token " + tokenKey.getName());
            if (token instanceof OAuthToken) {
                try {
                    OAuthToken oAuthToken = (OAuthToken) token;
                    logger.info("Attempting to refresh OAuth token");
                    token = (T) oAuthProvider.refreshToken(oAuthToken);
                } catch (Exception e) {
                    logger.error("Failed to refresh OAuth token", e);
                    token = supplier.get();
                }
            } else {
                token = supplier.get();
            }

            OffsetDateTime expiry = OffsetDateTime.ofInstant(Instant.ofEpochSecond(token.getExpiry()), ZoneOffset.UTC);
            Instant now = Instant.now();
            Duration duration = Duration.between(now, expiry);
            String formattedExpiry = DateTimeFormatter.ISO_DATE_TIME.format(expiry);
            String formattedDuration = DurationFormatUtils.formatDuration(duration.toMillis(), "dd:HH:mm:ss");
            logger.info("Fetched token " + tokenKey.getName() + " (Expiry: " + formattedExpiry + " or in " + formattedDuration + ")");
            tokenStore.put(tokenKey, token);
        }
        return token;
    }

    protected OAuthToken getOAuthToken() {
        return oAuthProvider.getOAuthToken();
    }

    protected XBLToken getXBLToken() {
        OAuthToken oAuthToken = get(TokenKey.OAUTH_TOKEN);

        JsonObject object = new JsonObject();

        JsonObject properties = new JsonObject();
        object.add("Properties", properties);
        properties.addProperty("AuthMethod", "RPS");
        properties.addProperty("SiteName", "user.auth.xboxlive.com");
        properties.addProperty("RpsTicket", "d=" + oAuthToken.getToken());

        //noinspection HttpUrlsUsage
        object.addProperty("RelyingParty", "http://auth.xboxlive.com");
        object.addProperty("TokenType", "JWT");

        JsonObject res = Util.jsonPost(XBL_URL, object);

        return XBLToken.fromJson(res, true);
    }

    protected XBLToken getXSTSToken() {
        XBLToken xblToken = get(TokenKey.XBL_TOKEN);

        JsonObject object = new JsonObject();

        JsonObject properties = new JsonObject();
        object.add("Properties", properties);
        properties.addProperty("SandboxId", "RETAIL");
        JsonArray userTokens = new JsonArray();
        userTokens.add(new JsonPrimitive(xblToken.getToken()));
        properties.add("UserTokens", userTokens);

        object.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        object.addProperty("TokenType", "JWT");

        JsonObject res = Util.jsonPost(XSTS_URL, object);

        return XBLToken.fromJson(res, true);
    }

    protected Token getMcSession() {
        XBLToken xstsToken = get(TokenKey.XSTS_TOKEN);

        JsonObject object = new JsonObject();
        object.addProperty("identityToken", "XBL3.0 x=" + xstsToken.getUserHash() + ";" + xstsToken.getToken());

        JsonObject res = Util.jsonPost(MINECRAFT_URL, object);

        return Token.fromJson(res);
    }

    @Override
    public SessionData login(Account account) {
        readAccountsJson(account);
        try {
            SessionData data = getMinecraftProfile();
            writeAccountsJson(account, true);
            return data;
        } catch (Exception e) {
            writeAccountsJson(account, false);
            throw new RuntimeException("Failed to login", e);
        }
    }

    protected SessionData getMinecraftProfile() {
        Token mcSession = get(TokenKey.SESSION_TOKEN);

        try {
            HttpGet request = new HttpGet(MINECRAFT_PROFILE_URL);
            request.setHeader("Authorization", "Bearer " + mcSession.getToken());

            HttpResponse response = Util.client.execute(request);
            String body = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() == 404) {
                throw new RuntimeException("404 received for minecraft profile, does the user own the game?");
            }
            Http.checkStatus(response, body);

            JsonObject profileObject = Util.parser.parse(body).getAsJsonObject();

            return new SessionData(
                mcSession.getToken(),
                profileObject.get("id").getAsString(),
                profileObject.get("name").getAsString(),
                "msa",
                "{}"
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch minecraft profile", e);
        }
    }

    private void readAccountsJson(Account account) {
        File accountsJson = new File(devAuth.getConfig().getConfigDir(), "microsoft_accounts.json");

        try {
            String json = FileUtils.readFileToString(accountsJson, StandardCharsets.UTF_8);
            JsonObject parsed = Util.parser.parse(json).getAsJsonObject();

            if (!parsed.has("version") || parsed.get("version").getAsInt() != 1) {
                logger.info("microsoft_accounts.json has unknown version. Ignoring.");
                return;
            }

            accountsData = parsed;

            if (parsed.has(account.getName())) {
                JsonObject accountData = parsed.get(account.getName()).getAsJsonObject();
                for (TokenKey<?> tokenKey : tokenRegistry.keySet()) {
                    if (accountData.has(tokenKey.getName())) {
                        Token token = Util.gson.fromJson(accountData.get(tokenKey.getName()), tokenKey.getClazz());
                        tokenStore.put(tokenKey, token);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to parse microsoft_accounts.json", e);
        }
    }

    private void writeAccountsJson(Account account, boolean writeCurrentAccount) {
        File accountsJson = new File(devAuth.getConfig().getConfigDir(), "microsoft_accounts.json");

        if (accountsData == null) accountsData = new JsonObject();
        accountsData.addProperty("version", 1);
        accountsData.remove(account.getName());

        try {
            if (writeCurrentAccount) {
                JsonObject accountData = new JsonObject();
                for (Map.Entry<TokenKey<?>, Token> entry : tokenStore.entrySet()) {
                    accountData.add(entry.getKey().getName(), Util.gson.toJsonTree(entry.getValue()));
                }
                accountsData.add(account.getName(), accountData);
            }

            FileUtils.writeStringToFile(accountsJson, Util.gson.toJson(accountsData), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Failed to write microsoft_accounts.json", e);
        }
    }
}
