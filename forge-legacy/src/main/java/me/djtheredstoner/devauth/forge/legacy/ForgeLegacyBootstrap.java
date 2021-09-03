package me.djtheredstoner.devauth.forge.legacy;

public class ForgeLegacyBootstrap {

    private static final DevAuthForgeLegacy devAuth = new DevAuthForgeLegacy();

    public static String[] processArguments(String[] args) {
        return devAuth.processArguments(args);
    }

}
