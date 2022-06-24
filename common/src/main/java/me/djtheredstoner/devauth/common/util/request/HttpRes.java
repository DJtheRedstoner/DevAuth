package me.djtheredstoner.devauth.common.util.request;

import org.apache.http.HttpResponse;

import java.util.function.BiFunction;

public class HttpRes<T> {

    private final HttpResponse response;
    private final String body;

    public HttpRes(HttpResponse response, String body) {
        this.response = response;
        this.body = body;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public T into(BiFunction<HttpResponse, String, T> function) {
        return function.apply(response, body);
    }
}
