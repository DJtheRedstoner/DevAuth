package me.djtheredstoner.devauth.fabric;

import me.djtheredstoner.devauth.common.DevAuth;

public class FabricBootstrap {

    private static final DevAuth devAuth = new DevAuth();

    public static DevAuth getDevAuth() {
        return devAuth;
    }

}
