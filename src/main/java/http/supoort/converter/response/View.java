package http.supoort.converter.response;

import http.model.response.ServletResponse;

import java.io.DataOutputStream;

public interface View {
    void render(ServletResponse response, DataOutputStream dataOutputStream);
}
