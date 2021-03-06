package http.supoort.converter.request;

import http.model.request.HttpMethod;
import http.model.request.ServletRequest;

import java.io.BufferedReader;

public class PostRequestConverter extends AbstractHttpRequestConverter {
    @Override
    public ServletRequest convert(String uri, String protocol, BufferedReader bufferedReader) {
        return convertWithBody(HttpMethod.POST, uri, protocol, bufferedReader);
    }
}
