package http.supoort;

import http.model.ServletRequest;
import http.session.SessionManager;

import java.io.InputStream;

public class HttpRequestFactory {
    private SessionManager sessionManager;

    public HttpRequestFactory(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public ServletRequest getRequest(InputStream inputStream) {
        ServletRequest servletRequest = HttpRequestParser.parse(inputStream);

        servletRequest.bindSession(sessionManager.getSession());
        return servletRequest;
    }
}
