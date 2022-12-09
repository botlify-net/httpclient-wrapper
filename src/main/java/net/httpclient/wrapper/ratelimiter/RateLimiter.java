package net.httpclient.wrapper.ratelimiter;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;

public class RateLimiter {

    @Getter @Setter @NotNull
    private Duration duration;

    @Getter @Setter @Nullable
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
    public synchronized void acquire() {
        try {
            if (lastAcquire == null) {
                lastAcquire = Instant.now();
                return;
            }
            Duration durationToSleep = getRemainingDuration();
            if (!durationToSleep.isZero() && !durationToSleep.isNegative())
                Thread.sleep(durationToSleep.toMillis());
            lastAcquire = Instant.now();
        } catch (InterruptedException e) {
            throw (new RuntimeException(e));
        }
    }

    /**
     * Reset the rate limiter.
     */
    public void reset() {
        lastAcquire = null;
    }

    /**
     * Get the time to wait before executing the next acquire.
     * @return The time to wait in Duration.
     */
    public @NotNull Duration getRemainingDuration() {
        if (lastAcquire == null) return (Duration.ZERO);
        if (duration.isZero()) return (Duration.ZERO);
        Instant now = Instant.now();
        Instant timeToExec = lastAcquire.plus(duration);
        if (timeToExec.isBefore(now)) return (Duration.ZERO);
        return (Duration.between(now, timeToExec));
    }

}
