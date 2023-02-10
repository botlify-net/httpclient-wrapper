package net.httpclient.wrapper.session;

import net.httpclient.wrapper.HttpClientProxyConfig;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientSessionRateLimitedTest {

    @Test
    void constructWithoutProxyConfig() {
        Duration duration = Duration.ofMillis(100);
        HttpClientSessionRateLimited session = new HttpClientSessionRateLimited(duration);
        assertNotNull(session);
    }

    @Test
    void constructWithProxyConfigHostPort() {

        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .build();

        Duration duration = Duration.ofMillis(100);
        HttpClientSessionRateLimited session = new HttpClientSessionRateLimited(duration, config);
        assertNotNull(session);
    }

    @Test
    void constructWithProxyConfigHostPortUsernamePassword() {

        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .setUsernameAndPassword("user", "password")
                .build();

        Duration duration = Duration.ofMillis(100);
        HttpClientSessionRateLimited session = new HttpClientSessionRateLimited(duration, config);
        assertNotNull(session);
    }

}