package net.httpclient.wrapper.ratelimiter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;

public class RateLimiter {

    private Duration duration;
    private Instant lastAcquire;

    /*
     $      Constructor
     */

    /**
     * Create a rate limiter with a duration.
     * @param duration The duration between each acquire.
     */
    public RateLimiter(@NotNull Duration duration) {
        this.duration = duration;
    }

    /**
     * Sleep until the duration is passed.
     */
    public void acquire() {
        synchronized (this) {
            try {
                dangerousAcquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void dangerousAcquire() throws InterruptedException {
        synchronized (this) {
            if (lastAcquire == null) {
                lastAcquire = Instant.now();
                return;
            }
            Duration durationToSleep = getRemainingTime();
            if (durationToSleep.isZero()) Thread.sleep(durationToSleep.toMillis());
            lastAcquire = Instant.now();
        }
    }

    /**
     * Reset the rate limiter.
     */
    public void reset() {
        lastAcquire = null;
    }

    /**
     * Get the remaining time before the next acquire.
     * @return The remaining time in milliseconds.
     */
    public @NotNull Duration getRemainingTime() {
        if (lastAcquire == null) return Duration.ZERO;
        long lastRequest = lastAcquire.toEpochMilli();
        long now = Instant.now().toEpochMilli();
        long result = lastRequest + duration.toMillis() - now;
        return (result > 0) ? Duration.ofMillis(result) : Duration.ZERO;
    }

    /*
     $      Getters
     */

    public @NotNull Duration getDuration() {
        return duration;
    }

    public @Nullable Instant getLastAcquire() {
        return lastAcquire;
    }

    public void setDuration(@NotNull Duration duration) {
        this.duration = duration;
    }

}
