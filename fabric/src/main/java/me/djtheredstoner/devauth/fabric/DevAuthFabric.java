package me.djtheredstoner.devauth.fabric;

import me.djtheredstoner.devauth.common.DevAuth;
import me.djtheredstoner.devauth.common.Environment;

public class DevAuthFabric extends DevAuth {
    @Override
    protected Environment getEnvironment() {
        return Environment.FABRIC;
    }
}
