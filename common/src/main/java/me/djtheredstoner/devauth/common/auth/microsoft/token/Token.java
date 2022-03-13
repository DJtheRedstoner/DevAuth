package me.djtheredstoner.devauth.common.auth.microsoft.token;

import com.google.gson.JsonObject;
import me.djtheredstoner.devauth.common.util.Util;

public class Token {
    private final String token;
    private final long expiry;

    public Token(String token, long expiry) {
        this.token = token;
        this.expiry = expiry;
    }

    public String getToken() {
        return token;
    }

    public long getExpiry() {
        return expiry;
    }

    public boolean isExpired() {
        // Add 30s, so it leans on the side of returning an unexpired token as expired
        // to prevent client/server time desyncs.
        return Util.secondsSinceEpoch() + 30 > expiry;
    }

    public static Token fromJson(JsonObject object) {
        return new Token(
            object.get("access_token").getAsString(),
            object.get("expires_in").getAsInt() + Util.secondsSinceEpoch()
        );
    }

    @Override
    public String toString() {
        return "Token{" +
            "token='" + token + '\'' +
            ", expiry=" + expiry +
            '}';
    }
}