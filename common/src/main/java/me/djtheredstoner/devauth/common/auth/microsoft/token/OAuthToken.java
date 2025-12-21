package me.djtheredstoner.devauth.common.auth.microsoft.token;

import com.google.gson.JsonObject;
import me.djtheredstoner.devauth.common.util.Util;

public class OAuthToken extends Token{
    private final String refreshToken;
    private final Integer port;

    public OAuthToken(String token, String refreshToken, long expiry, int port) {
        super(token, expiry);
        this.refreshToken = refreshToken;
        this.port = port;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getPort() {
        return port;
    }

    public static OAuthToken fromJson(JsonObject object, int port) {
        return new OAuthToken(
            object.get("access_token").getAsString(),
            object.get("refresh_token").getAsString(),
            object.get("expires_in").getAsInt() + Util.secondsSinceEpoch(),
            port
        );
    }

    @Override
    public String toString() {
        return "OAuthToken{" +
            "token='" + getToken() + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", expiry=" + getExpiry() + '\'' +
            '}';
    }
}