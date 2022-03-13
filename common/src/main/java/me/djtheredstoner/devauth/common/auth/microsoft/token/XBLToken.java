package me.djtheredstoner.devauth.common.auth.microsoft.token;

import com.google.gson.JsonObject;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class XBLToken extends Token {
    private final String userHash;

    public XBLToken(String token, String userHash, long expiry) {
        super(token, expiry);
        this.userHash = userHash;
    }

    public String getUserHash() {
        return userHash;
    }

    public static XBLToken fromJson(JsonObject object, boolean hasUserHash) {
        String token = object.get("Token").getAsString();
        String userHash = null;
        if (hasUserHash) {
             userHash = object
                .get("DisplayClaims").getAsJsonObject()
                .get("xui").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("uhs").getAsString();
        }
        OffsetDateTime expiryTime = OffsetDateTime.parse(object.get("NotAfter").getAsString(), DateTimeFormatter.ISO_DATE_TIME);
        long expiry = Instant.from(expiryTime).getEpochSecond();

        return new XBLToken(token, userHash, expiry);
    }

    @Override
    public String toString() {
        return "Token{" +
            "token='" + getToken() + '\'' +
            ", userHash='" + userHash + '\'' +
            ", expiry=" + getExpiry() +
            '}';
    }
}
