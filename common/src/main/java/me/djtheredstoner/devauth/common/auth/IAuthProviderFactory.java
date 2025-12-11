package me.djtheredstoner.devauth.common.auth;

import me.djtheredstoner.devauth.common.DevAuth;
import me.djtheredstoner.devauth.common.auth.microsoft.MicrosoftAuthProvider;

public interface IAuthProviderFactory {

    IAuthProviderFactory MICROSOFT = MicrosoftAuthProvider::new;

    IAuthProvider create(DevAuth devAuth);

}
