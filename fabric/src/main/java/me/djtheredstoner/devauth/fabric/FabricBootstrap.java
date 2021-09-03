package me.djtheredstoner.devauth.fabric;

public class FabricBootstrap {

    private static final DevAuthFabric devAuth = new DevAuthFabric();

    public static DevAuthFabric getDevAuth() {
        return devAuth;
    }

}
