package net.httpclient.wrapper.ratelimiter;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    @Test
    public void constructTest() {
        assertThrows(IllegalArgumentException.class, () -> new RateLimiter(null));
        assertDoesNotThrow(() -> new RateLimiter(Duration.ofSeconds(1)));
    }

    @Test
    public void acquireTest() {
        RateLimiter rateLimiter = new RateLimiter(Duration.ofSeconds(1));
        assertDoesNotThrow(() -> rateLimiter.acquire());
        assertDoesNotThrow(() -> rateLimiter.acquire());
    }

    @Test
    public void getRemainingTimeTest() throws InterruptedException {
        int durationInSeconds = 3;
        RateLimiter rateLimiter = new RateLimiter(Duration.ofSeconds(durationInSeconds));
        rateLimiter.acquire();
        assertEquals(durationInSeconds, rateLimiter.getRemainingTime().toSeconds());
        rateLimiter.reset();
        assertEquals(Duration.ZERO, rateLimiter.getRemainingTime());
    }

}