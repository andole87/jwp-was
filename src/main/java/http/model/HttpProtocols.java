package http.model;

import http.supoort.IllegalHttpRequestException;

import java.util.Arrays;

public enum HttpProtocols {
    DEFAULT("HTTP/1.1"),
    ADVANCED("HTTP/2.0");

    private String protocol;

    HttpProtocols(String protocol) {
        this.protocol = protocol;
    }

    public static HttpProtocols of(String protocol) {
        return Arrays.stream(HttpProtocols.values())
                .filter(value -> value.getProtocol().equals(protocol))
                .findAny()
                .orElseThrow(IllegalHttpRequestException::new);

    }

    public String getProtocol() {
        return this.protocol;
    }
}