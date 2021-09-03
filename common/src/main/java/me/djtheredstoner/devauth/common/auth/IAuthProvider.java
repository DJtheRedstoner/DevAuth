package me.djtheredstoner.devauth.common.auth;

import me.djtheredstoner.devauth.common.config.Account;

public interface IAuthProvider {

    SessionData login(Account account);

}
