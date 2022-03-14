package me.djtheredstoner.devauth.forge.latest;

import me.djtheredstoner.devauth.common.DevAuth;
import net.minecraftforge.fml.common.Mod;

@Mod("devauth")
public class ForgeLatestBootstrap {

    private static final DevAuth devAuth = new DevAuth();

    public static String[] processArguments(String[] args) {
        return devAuth.processArguments(args);
    }

}
