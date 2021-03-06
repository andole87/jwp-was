package http.controller;

import http.model.request.ServletRequest;
import http.model.response.ServletResponse;
import http.supoort.RequestMapping;

public class FileResourceController extends AbstractController {

    public FileResourceController(RequestMapping mapping) {
        super(mapping);
    }

    public FileResourceController(RequestMapping... mappings) {
        super(mappings);
    }

    @Override
    public void handle(ServletRequest servletRequest, ServletResponse servletResponse) {
        String resource = servletRequest.getHttpUri().getResourceLocation();

        servletResponse.ok(resource);
    }
}
