package me.djtheredstoner.devauth.common.auth;

public record SessionData(
        String accessToken,
        String uuid,
        String username,
        String userType,
        String userProperties
) {
}
