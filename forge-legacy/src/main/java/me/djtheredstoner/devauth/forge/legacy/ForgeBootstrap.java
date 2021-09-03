package me.djtheredstoner.devauth.forge.legacy;

public class ForgeBootstrap {

    private static final DevAuthForge devAuth = new DevAuthForge();

    public static String[] processArguments(String[] args) {
        return devAuth.processArguments(args);
    }

}
