package me.djtheredstoner.devauth.common.auth.microsoft.oauth;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import me.djtheredstoner.devauth.common.auth.microsoft.Constants;
import me.djtheredstoner.devauth.common.auth.microsoft.MSAUtil;
import me.djtheredstoner.devauth.common.auth.microsoft.token.OAuthToken;
import me.djtheredstoner.devauth.common.util.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CodeOAuthProvider extends OAuthProvider {

    private static final String REDIRECT_URI = "http://127.0.0.1:%d";
    private static final String OAUTH_URL = "https://login.live.com/oauth20_authorize.srf";
    private static final String OAUTH_TOKEN_URL = "https://login.live.com/oauth20_token.srf";

    public CodeOAuthProvider(Logger logger, String scopes) {
        super(logger, scopes);
    }

    @Override
    public OAuthToken getOAuthToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        AuthorizationCodeAndPort oAuthCode = getAuthorizationCode(codeVerifier);

        return getAuthorizationToken(oAuthCode.getPort(), Util.stringMap(
            "code", oAuthCode.getCode(),
            "grant_type", "authorization_code",
            "code_verifier", codeVerifier
        ));
    }

    @Override
    public OAuthToken refreshToken(OAuthToken token) {
        try {
            // Previously the port was hardcoded to 3000, so default to that if it's not set
            int port = token.getPort() != null ? token.getPort() : 3000;
            return getAuthorizationToken(port, Util.stringMap(
                "grant_type", "refresh_token",
                "refresh_token", token.getRefreshToken()
            ));
        } catch (Exception e) {
            logger.error("Error refreshing OAuth token, trying to get new token!", e);
            return getOAuthToken();
        }
    }

    private OAuthToken getAuthorizationToken(int port, Map<String, String> extraParams) {
        Map<String, String> params = Util.stringMap(
            "client_id", Constants.CLIENT_ID,
            "scope", scopes,
            "redirect_uri", String.format(REDIRECT_URI, port)
        );
        params.putAll(extraParams);

        JsonObject res = Util.client.urlEncodedJsonPost(OAUTH_TOKEN_URL, params);

        return OAuthToken.fromJson(res, port);
    }

    public AuthorizationCodeAndPort getAuthorizationCode(String codeVerifier) {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", 0), 0);
            CompletableFuture<AuthorizationCodeAndPort> future = new CompletableFuture<>();
            int port = server.getAddress().getPort();

            server.createContext("/", (req) -> {
                URI uri = req.getRequestURI();

                // all other paths are 404
                if (!uri.getPath().equals("/")) {
                    req.sendResponseHeaders(404, 0);
                    req.getResponseBody().close();
                    return;
                }

                byte[] response;
                try (InputStream is = CodeOAuthProvider.class.getResourceAsStream("/assets/devauth/oauth_redirect.html")) {
                    Objects.requireNonNull(is);
                    response = IOUtils.toByteArray(is);
                }
                req.getResponseHeaders().add("Content-Type", "text/html");
                req.sendResponseHeaders(200, response.length);
                req.getResponseBody().write(response);
                req.getResponseBody().close();

                Map<String, String> query = MSAUtil.parseQuery(req.getRequestURI().getRawQuery());

                if (query.containsKey("error")) {
                    future.completeExceptionally(
                        new RuntimeException("OAuth error: " + query.get("error") + ": " + query.get("error_description"))
                    );
                }

                future.complete(new AuthorizationCodeAndPort(query.get("code"), port));
            });
            server.start();

            String queryString = MSAUtil.buildQuery(Util.stringMap(
                "client_id", Constants.CLIENT_ID,
                "response_type", "code",
                "redirect_uri", String.format(REDIRECT_URI, server.getAddress().getPort()),
                "scope", scopes,
                "prompt", "select_account",
                "code_challenge", Base64.getUrlEncoder().withoutPadding().encodeToString(DigestUtils.sha256(codeVerifier)),
                "code_challenge_method", "S256"
            ));

            logger.info("OAuth URL, open this in a browser to complete authentication: " + OAUTH_URL + "?" + queryString);

            return future.join();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get authorization code", e);
        } finally {
            if (server != null) server.stop(0);
        }
    }
}
