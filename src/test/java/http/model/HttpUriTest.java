package http.model;

import http.exceptions.IllegalHttpRequestException;
import http.model.request.HttpUri;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HttpUriTest {
    HttpUri httpUri;

    @Test
    void HTTP_URI_가_아닌경우() {
        assertThatThrownBy(() -> new HttpUri("something")).isInstanceOf(IllegalHttpRequestException.class);
    }

    @Test
    void HTTP_URI_가_맞는경우() {
        assertDoesNotThrow(() -> new HttpUri("/index.html"));
    }
}