package me.djtheredstoner.devauth.forge.legacy;

import me.djtheredstoner.devauth.common.DevAuth;

public class ForgeLegacyBootstrap {

    private static final DevAuth devAuth = new DevAuth();

    public static String[] processArguments(String[] args) {
        return devAuth.processArguments(args);
    }

}
