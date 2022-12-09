package net.httpclient.wrapper.ratelimiter;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    @Test
    public void acquireTest() {
        int timeSec = 3;
        RateLimiter rateLimiter = new RateLimiter(Duration.ofSeconds(timeSec));
        System.out.println("Start acquire test: " + Instant.now());
        assertDoesNotThrow(rateLimiter::acquire);
        System.out.println("Waiting " + timeSec + " second: " + Instant.now());
        assertDoesNotThrow(rateLimiter::acquire);
        System.out.println("Waiting " + timeSec + " second: " + Instant.now());
        assertDoesNotThrow(rateLimiter::acquire);
        System.out.println("End of test: " + Instant.now());
    }

    @Test
    public void getRemainingTimeTest() {
        int durationInSeconds = 3;
        RateLimiter rateLimiter = new RateLimiter(Duration.ofSeconds(durationInSeconds));
        rateLimiter.acquire();
        assertEquals(durationInSeconds - 1, rateLimiter.getRemainingDuration().toSeconds());
        rateLimiter.reset();
        assertEquals(Duration.ZERO, rateLimiter.getRemainingDuration());
    }

    @Test
    public void testChangeDuration() {
        int durationInSeconds = 1;
        RateLimiter rateLimiter = new RateLimiter(Duration.ofSeconds(durationInSeconds));
        rateLimiter.acquire();
        rateLimiter.setDuration(Duration.ofSeconds(2));
        assertEquals(2, rateLimiter.getDuration().toSeconds());
    }

}