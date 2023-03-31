package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientProxyConfig;
import net.httpclient.wrapper.exception.HttpClientException;
import net.httpclient.wrapper.exception.HttpServerException;
import net.httpclient.wrapper.response.RequestResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientSessionAsyncTest {

    @Test
    void constructWithoutProxyConfig() {
        HttpClientSessionAsync session = new HttpClientSessionAsync(1);
        assertNotNull(session);
    }

    @Test
    void constructWithProxyConfigHostPort() {

        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .build();

        HttpClientSessionAsync session = new HttpClientSessionAsync(config, 1);
        assertNotNull(session);
    }

    @Test
    void constructWithProxyConfigHostPortUsernamePassword() {

        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .setUsernameAndPassword("user", "password")
                .build();

        HttpClientSessionAsync session = new HttpClientSessionAsync(config, 1);
        assertNotNull(session);
    }

    @Test
    void testWithOneSession() throws HttpClientException, IOException, HttpServerException {
        final HttpClientSessionAsync session = new HttpClientSessionAsync(1);
        assertNotNull(session);

        System.out.println("Testing with one session, sending get request to https://httpbin.org/get");
        // Send get request
        RequestResponse requestResponse = session.sendGet("https://httpbin.org/get");
        assertNotNull(requestResponse);
        assertEquals(200, requestResponse.getStatusCode());
        assertNotNull(requestResponse.toJSONObject());
    }

}