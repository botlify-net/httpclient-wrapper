package net.httpclient.wrapper;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientProxyConfigTest {

    @Test
    void testBuildOnlyHostAndPort() {
        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .build();
        assertEquals("localhost", config.getHost());
        assertEquals(8080, config.getPort());
    }

    @Test
    void testBuildOnlyHostAndPortWithScheme() {
        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .setUsernameAndPassword("user", "password")
                .build();
        assertEquals("localhost", config.getHost());
        assertEquals(8080, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("password", config.getPassword());
    }

    @Test
    void testHasValidCredentials() {
        HttpClientProxyConfig config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .setUsernameAndPassword("user", "password")
                .build();
        assertTrue(config.hasValidCredentials());

        config = new HttpClientProxyConfig.Builder()
                .setHostAndPort("localhost", 8080)
                .build();
        assertFalse(config.hasValidCredentials());
    }

}