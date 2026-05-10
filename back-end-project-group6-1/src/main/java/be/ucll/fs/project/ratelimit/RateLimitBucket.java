package be.ucll.fs.project.ratelimit;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;


public class RateLimitBucket {

    private final int maxRequests;
    private final long windowSeconds;

    private final AtomicInteger remaining;
    private volatile Instant windowStart;

    public RateLimitBucket(int maxRequests, long windowSeconds) {
        this.maxRequests   = maxRequests;
        this.windowSeconds = windowSeconds;
        this.remaining     = new AtomicInteger(maxRequests);
        this.windowStart   = Instant.now();
    }

    public synchronized boolean tryConsume() {
        Instant now = Instant.now();

        if (now.isAfter(windowStart.plusSeconds(windowSeconds))) {
            remaining.set(maxRequests);
            windowStart = now;
        }

        if (remaining.get() <= 0) {
            return false;
        }
        remaining.decrementAndGet();
        return true;
    }

    public long secondsUntilRefill() {
        long elapsed = Instant.now().getEpochSecond() - windowStart.getEpochSecond();
        long remaining = windowSeconds - elapsed;
        return Math.max(0, remaining);
    }
}
