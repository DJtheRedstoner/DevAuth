package me.djtheredstoner.devauth.forge.latest;

import net.minecraftforge.fml.common.Mod;

@Mod("devauth")
public class ForgeLatestBootstrap {

    private static DevAuthForgeLatest devAuth = new DevAuthForgeLatest();

    public static String[] processArguments(String[] args) {
        return devAuth.processArguments(args);
    }

}
