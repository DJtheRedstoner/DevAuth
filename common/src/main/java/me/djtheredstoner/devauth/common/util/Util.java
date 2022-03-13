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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final JsonParser parser = new JsonParser();
    public static final HttpClient client = HttpClients.custom()
        .setUserAgent(Constants.USER_AGENT)
        .build();

    public static File getDefaultConfigDir() {
        return new File(System.getProperty("user.home"), ".devauth");
    }

    public static long secondsSinceEpoch() {
        return System.currentTimeMillis() / 1000;
    }

    // https://stackoverflow.com/questions/61860104/converting-p1363-format-to-asn-1-der-format-using-java
    public static byte[] decodeSignature(byte[] der) {
        int n = 32;

        if (der[0] != 0x30) throw new RuntimeException();
        int idx = der[1] == (byte) 0x81 ? 3 : 2; // the 0x81 case only occurs for curve over 488 bits
        if (der[idx] != 2) throw new RuntimeException();
        BigInteger r = new BigInteger(1, Arrays.copyOfRange(der, idx + 2, idx + 2 + der[idx + 1]));
        idx += der[idx + 1] + 2;
        if (der[idx] != 2) throw new RuntimeException();
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(der, idx + 2, idx + 2 + der[idx + 1]));
        if (idx + der[idx + 1] + 2 != der.length) throw new RuntimeException();
        // common output
        byte[] out = new byte[2 * n];
        toFixed(r, out, 0, n);
        toFixed(s, out, n, n);

        return out;
    }

    public static void toFixed(BigInteger x, byte[] a, int off, int len) {
        byte[] t = x.toByteArray();
        if (t.length == len + 1 && t[0] == 0) System.arraycopy(t, 1, a, off, len);
        else if (t.length <= len) System.arraycopy(t, 0, a, off + len - t.length, t.length);
        else throw new RuntimeException();
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
