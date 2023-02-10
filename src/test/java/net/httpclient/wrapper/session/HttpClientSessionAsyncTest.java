package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientProxyConfig;
import org.junit.jupiter.api.Test;

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

}