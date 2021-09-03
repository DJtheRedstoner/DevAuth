package me.djtheredstoner.devauth.common.auth;

import me.djtheredstoner.devauth.common.DevAuth;

public interface IAuthProviderFactory {

    IAuthProviderFactory MOJANG = devAuth -> new MojangAuthProvider();
    IAuthProviderFactory MICROSOFT = MicrosoftAuthProvider::new;

    IAuthProvider create(DevAuth devAuth);

}
