package me.djtheredstoner.devauth.common.util.request;

import me.djtheredstoner.devauth.common.util.Util;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This is super cursed but it works
 * @param <B> request body type
 * @param <R> response type
 */
public class HttpBuilder<B, R> {

    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private final List<BiFunction<B, HttpPost, B>> requestHandlers = new ArrayList<BiFunction<B, HttpPost, B>>();
    private B bodyData;
    private Function<B, HttpEntity> bodyCreator;
    private final List<BiConsumer<HttpResponse, String>> responseHandlers = new ArrayList<>();

    public HttpBuilder(String url) {
        this.url = url;
    }

    public HttpBuilder<B, R> header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpBuilder<B, R> requestHandler(BiFunction<B, HttpPost, B> handler) {
        requestHandlers.add(handler);
        return this;
    }

    public HttpBuilder<B, R> body(Function<B, HttpEntity> function, B bodyData) {
        this.bodyData = bodyData;
        this.bodyCreator = function;
        return this;
    }

    public HttpBuilder<B, R> responseHandler(BiConsumer<HttpResponse, String> handler) {
        responseHandlers.add(handler);
        return this;
    }

    public HttpRes<R> execute() {
        try {
            HttpPost req = new HttpPost(url);
            req.setHeaders(createHeaders());

            B body = bodyData;
            for (BiFunction<B, HttpPost, B> handler : requestHandlers) {
                body = handler.apply(body, req);
            }

            HttpEntity entity = bodyCreator.apply(bodyData);
            req.setEntity(entity);

            HttpResponse res = Util.client.execute(req);
            String resBody = null;
            if (res.getEntity().getContentType() != null) {
                resBody = EntityUtils.toString(res.getEntity());
            }

            for (BiConsumer<HttpResponse, String> handler : responseHandlers) {
                handler.accept(res, resBody);
            }

            return new HttpRes<>(res, resBody);
        } catch (Exception e) {
            throw new RuntimeException("Error POSTing url: " + url, e);
        }
    }

    private Header[] createHeaders() {
        Header[] h = new Header[headers.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            h[i++] = new BasicHeader(entry.getKey(), entry.getValue());
        }
        return h;
    }

}
