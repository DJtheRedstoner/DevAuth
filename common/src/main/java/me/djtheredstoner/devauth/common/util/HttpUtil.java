package me.djtheredstoner.devauth.common.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.djtheredstoner.devauth.common.auth.microsoft.Constants;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpUtil {
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    public static HttpClient.Builder newHttpClientBuilder()
    {
        return HttpClient.newBuilder()
                .connectTimeout(DEFAULT_TIMEOUT);
    }

    public static HttpRequest.Builder newHttpClientRequest(final URI uri)
    {
        return HttpRequest.newBuilder(uri)
                .timeout(DEFAULT_TIMEOUT)
                .setHeader("User-Agent", Constants.USER_AGENT);
    }

    public static String buildQuery(Map<String, String> params) {

        return params.entrySet()
                .stream()
                .map(e -> Stream.of(e.getKey(), e.getValue())
                        .map(s -> URLEncoder.encode(s, StandardCharsets.UTF_8))
                        .collect(Collectors.joining("=")))
                .collect(Collectors.joining("&"));
    }

    public static Map<String, String> parseQuery(final String query) {
        if(query == null || query.isEmpty()) {
            return Map.of();
        }

        return Arrays.stream(query.split("&"))
                .map(kv -> Arrays.stream(kv.split("="))
                        .limit(2)
                        .map(s -> URLDecoder.decode(s, StandardCharsets.UTF_8))
                        .toList())
                .collect(Collectors.toMap(
                        List::getFirst,
                        l -> l.size() > 1 ? l.getLast() : ""));

    }

    public static JsonObject jsonPost(URI uri, JsonObject body) {
        return jsonPost(
                uri,
                HttpRequest.BodyPublishers.ofString(Util.GSON.toJson(body)),
                Map.of());
    }

    public static JsonObject jsonPost(
            URI uri,
            HttpRequest.BodyPublisher bodyPublisher,
            Map<String, String> additionalHeaders) {
        try(HttpClient httpClient = newHttpClientBuilder().build()) {
            HttpRequest.Builder builder = newHttpClientRequest(uri)
                    .setHeader("Accept", "application/json")
                    .POST(bodyPublisher);

            additionalHeaders.forEach(builder::setHeader);

            HttpResponse<String> response = httpClient.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            checkStatus(response);

            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to execute request to " + uri, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Got interrupted", e);
        }
    }

    public static HttpResponse<String> checkStatus(HttpResponse<String> res) {
        if (res.statusCode() != 200) {
            throw new RuntimeException(
                    "Received bad status " + res.statusCode() +
                            " body: " + res.body()
            );
        }
        return res;
    }
}
