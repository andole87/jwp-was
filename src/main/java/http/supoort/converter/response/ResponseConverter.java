package http.supoort.converter.response;

import http.exceptions.FailToConvertMessageException;
import http.model.response.ContentType;
import http.model.response.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class ResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseConverter.class);
    private static final String TEMPLATE_PATH = "./templates";
    private static final String STATIC_PATH = "./static";
    private static final String SPACE = " ";
    private static final String HEAD_SEPARATOR = ":" + SPACE;
    private static final String LINE_BREAK = "\r\n";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String EXTENSION_SEPARATOR = ".";
    private final View view;

    public ResponseConverter(View view) {
        this.view = view;
    }

    public void convert(ServletResponse response) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(response.getOutputStream());
            responseStatus(response, dataOutputStream);
            responseHeader(response, dataOutputStream);
            responseBody(response, dataOutputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new FailToConvertMessageException(e.getMessage());
        }
    }

    private void responseStatus(ServletResponse response, DataOutputStream dos) throws IOException {
        dos.writeBytes(response.getProtocols().getProtocol() + SPACE);
        dos.writeBytes(response.getHttpStatus().getMessage() + LINE_BREAK);
    }


    private void responseHeader(ServletResponse response, DataOutputStream dos) throws IOException {
        for (Map.Entry<String, String> entry : response.getHttpHeaders().entrySet()) {
            dos.writeBytes(entry.getKey() + HEAD_SEPARATOR + entry.getValue() + LINE_BREAK);
        }
    }

    private void responseBody(ServletResponse response, DataOutputStream dos) throws IOException {
        if (!response.hasResource()) {
            return;
        }

        if (response.hasModel()) {
            view.render(response, dos);
            return;
        }

        String resource = response.getView();
        ContentType contentType = getExtension(resource);
        byte[] body = getFileResource(contentType, resource);
        writeBody(dos, contentType, body);
    }

    private ContentType getExtension(String resource) {
        return ContentType.of(resource.substring(resource.lastIndexOf(EXTENSION_SEPARATOR) + 1));
    }

    private byte[] getFileResource(ContentType contentType, String resource) {
        if (contentType.isHTML()) {
            return FileIoUtils.loadFileFromClasspath(TEMPLATE_PATH + resource);
        }
        return FileIoUtils.loadFileFromClasspath(STATIC_PATH + resource);
    }

    private void writeBody(DataOutputStream dos, ContentType contentType, byte[] body) throws IOException {
        dos.writeBytes(CONTENT_TYPE + HEAD_SEPARATOR + contentType.getMimeType() + LINE_BREAK);
        dos.writeBytes(CONTENT_LENGTH + HEAD_SEPARATOR + body.length + LINE_BREAK);
        dos.writeBytes(LINE_BREAK);
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
