package me.djtheredstoner.devauth.common.auth.microsoft.token;

public class TokenKey<T extends Token> {

    public static final TokenKey<OAuthToken> OAUTH_TOKEN = TokenKey.of("oauth", OAuthToken.class);
    public static final TokenKey<XBLToken> XBL_TOKEN = TokenKey.of("xbl", XBLToken.class);
    public static final TokenKey<XBLToken> XSTS_TOKEN = TokenKey.of("xsts", XBLToken.class);
    public static final TokenKey<Token> SESSION_TOKEN = TokenKey.of("session", Token.class);

    private final String name;
    private final Class<T> clazz;

    private TokenKey(String name, Class<T> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public static <T extends Token> TokenKey<T> of(String name, Class<T> clazz) {
        return new TokenKey<>(name, clazz);
    }

}
