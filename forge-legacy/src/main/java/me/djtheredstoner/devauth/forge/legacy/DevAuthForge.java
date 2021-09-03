package me.djtheredstoner.devauth.forge.legacy;

import me.djtheredstoner.devauth.common.DevAuth;
import me.djtheredstoner.devauth.common.Environment;

public class DevAuthForge extends DevAuth {
    @Override
    protected Environment getEnvironment() {
        return Environment.FORGE;
    }
}
