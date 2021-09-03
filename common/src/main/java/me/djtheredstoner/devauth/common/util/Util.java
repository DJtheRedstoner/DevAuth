package me.djtheredstoner.devauth.common.util;

import java.io.File;

public class Util {

    public static File getDefaultConfigDir() {
        return new File(System.getProperty("user.home"), ".devauth");
    }

    public static long secondsSinceEpoch() {
        return System.currentTimeMillis() / 1000;
    }

}
