package me.djtheredstoner.devauth.common.auth.microsoft.token;

import com.google.gson.JsonObject;
import me.djtheredstoner.devauth.common.util.Util;

public class OAuthToken extends Token{
    private final String refreshToken;

    public OAuthToken(String token, String refreshToken, long expiry) {
        super(token, expiry);
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static OAuthToken fromJson(JsonObject object) {
        return new OAuthToken(
            object.get("access_token").getAsString(),
            object.get("refresh_token").getAsString(),
            object.get("expires_in").getAsInt() + Util.secondsSinceEpoch()
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