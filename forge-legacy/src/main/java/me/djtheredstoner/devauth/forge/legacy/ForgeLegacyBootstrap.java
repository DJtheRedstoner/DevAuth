package me.djtheredstoner.devauth.forge.legacy;

import me.djtheredstoner.devauth.common.DevAuth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeLegacyBootstrap {

    private static final Logger LOGGER = LogManager.getLogger("DevAuth/Bootstrap");

    private static final DevAuth devAuth = new DevAuth();

    private static boolean loggedIn = false;

    public static String[] processArguments(String[] args) {
        if (!loggedIn) {
            loggedIn = true;
            return devAuth.processArguments(args);
        } else {
            LOGGER.warn("DevAuth called twice, you may have loaded DevAuth twice (eg. from a jar and from maven).");
            return args;
        }
    }

}
