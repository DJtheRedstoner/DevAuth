package me.djtheredstoner.devauth.common.auth;

public class SessionData {

    private final String accessToken;
    private final String uuid;
    private final String username;
    private final String userType;
    private final String userProperties;

    public SessionData(String accessToken, String uuid, String username, String userType, String userProperties) {
        this.accessToken = accessToken;
        this.uuid = uuid;
        this.username = username;
        this.userType = userType;
        this.userProperties = userProperties;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserProperties() {
        return userProperties;
    }
}
