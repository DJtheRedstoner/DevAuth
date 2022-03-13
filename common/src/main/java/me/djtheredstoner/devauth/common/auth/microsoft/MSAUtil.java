package me.djtheredstoner.devauth.common.auth.microsoft;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.djtheredstoner.devauth.common.auth.microsoft.token.Token;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MSAUtil {

    public static List<NameValuePair> buildNameValuePairs(Map<String, String> params) {
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public static String buildQuery(Map<String, String> params) {
        try {
            StringJoiner queryString = new StringJoiner("&");

            for (Map.Entry<String, String> param : params.entrySet()) {
                queryString.add(param.getKey() + "=" + URLEncoder.encode(param.getValue(), "UTF-8"));
            }

            return queryString.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> parseQuery(String query) {
        return URLEncodedUtils
            .parse(query, StandardCharsets.UTF_8)
            .stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    }

    public static boolean isValid(Token token) {
        return token != null && !token.isExpired();
    }
}
