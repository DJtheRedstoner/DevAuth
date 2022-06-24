package me.djtheredstoner.devauth.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.djtheredstoner.devauth.common.auth.microsoft.Constants;
import me.djtheredstoner.devauth.common.util.request.Http;
import me.djtheredstoner.devauth.common.util.request.HttpBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final JsonParser parser = new JsonParser();
    public static final HttpClient client = HttpClients.custom()
        .setUserAgent(Constants.USER_AGENT)
        .build();

    public static File getDefaultConfigDir() {
        return new File(getSystemConfigDir(), ".devauth");
    }

    public static File getSystemConfigDir() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Linux") || osName.startsWith("FreeBSD") || osName.startsWith("SunOS") || osName.startsWith("Unix")) {
            String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            if (xdgConfigHome != null) return new File(xdgConfigHome);
            return new File(System.getProperty("user.home"), ".config");
        } else {
            return new File(System.getProperty("user.home"));
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

    public static JsonObject jsonPost(String url, JsonObject body) {
        return new HttpBuilder<JsonObject, JsonObject>(url)
            .header("Accept", "application/json")
            .body(Http::jsonBody, body)
            .responseHandler(Http::checkStatus)
            .execute()
            .into(Http::jsonResponse);
    }
}
