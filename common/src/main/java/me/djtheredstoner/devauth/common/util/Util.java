package me.djtheredstoner.devauth.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static File getDefaultConfigDir() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Linux") || osName.startsWith("FreeBSD") || osName.startsWith("SunOS") || osName.startsWith("Unix")) {
            String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            if (xdgConfigHome == null) xdgConfigHome = System.getProperty("user.home") + File.separator + ".config";
            return new File(xdgConfigHome, "devauth");
        } else {
            return new File(System.getProperty("user.home"), ".devauth");
        }
    }

    public static long secondsSinceEpoch() {
        return System.currentTimeMillis() / 1000;
    }

    public static Map<String, String> stringMap(String... entries) {
        if (entries.length % 2 != 0) throw new IllegalArgumentException("Number of entries must be even");

        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            map.put(entries[i], entries[i + 1]);
        }

        return map;
    }
}
