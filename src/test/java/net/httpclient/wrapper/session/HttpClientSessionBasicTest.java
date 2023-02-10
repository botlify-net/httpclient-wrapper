package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientProxyConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpClientSessionBasicTest {

    @Test
    void constructWithoutProxyConfig() {
        HttpClientSessionBasic session = new HttpClientSessionBasic();
        assertNotNull(session);
    }

    @Test
    void constructWithProxyConfigHostPort() {

        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .build();

        HttpClientSessionBasic session = new HttpClientSessionBasic(config);
        assertNotNull(session);
    }

    @Test
    void constructWithProxyConfigHostPortUsernamePassword() {

        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .setUsernameAndPassword("user", "password")
                .build();

        HttpClientSessionBasic session = new HttpClientSessionBasic(config);
        assertNotNull(session);
    }

}