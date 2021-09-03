package me.djtheredstoner.devauth.forge.latest;

import me.djtheredstoner.devauth.common.DevAuth;
import me.djtheredstoner.devauth.common.Environment;

public class DevAuthForgeLatest extends DevAuth {
    @Override
    protected Environment getEnvironment() {
        return Environment.FORGE;
    }
}
