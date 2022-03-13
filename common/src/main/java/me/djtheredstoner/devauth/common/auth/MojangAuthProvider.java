package me.djtheredstoner.devauth.common.auth;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import me.djtheredstoner.devauth.common.config.Account;

import java.net.Proxy;

public class MojangAuthProvider implements IAuthProvider {

    @Override
    public SessionData login(Account account) {
        UserAuthentication auth = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "1")
            .createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(account.getUsername());
        auth.setPassword(account.getPassword());

        try {
            auth.logIn();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Failed to login", e);
        }

        String accessToken = auth.getAuthenticatedToken();
        String uuid = auth.getSelectedProfile().getId().toString().replace("-", "");
        String username = auth.getSelectedProfile().getName();
        String userType = auth.getUserType().getName();

        return new SessionData(accessToken, uuid, username, userType, "{}");
    }
}
