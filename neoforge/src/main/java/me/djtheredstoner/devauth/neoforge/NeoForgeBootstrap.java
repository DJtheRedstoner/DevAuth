package me.djtheredstoner.devauth.neoforge;

import me.djtheredstoner.devauth.common.DevAuth;
import net.neoforged.fml.common.Mod;

@Mod("devauth")
public class NeoForgeBootstrap {

    private static final DevAuth devAuth = new DevAuth();

    public static String[] processArguments(String[] args) {
        return devAuth.processArguments(args);
    }

}
