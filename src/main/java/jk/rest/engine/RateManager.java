package jk.rest.engine;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class tracks rate limits within Kraken API.
 * Assumes an Intermediate account setup.
 */
public class RateManager {

    private final int MAX_QUERIES = 20; // max counter

    private LocalDateTime start;
    private AtomicInteger publicApiRequests;
    private AtomicInteger privateApiRequests;

    private ScheduledThreadPoolExecutor executor;

    public RateManager () {
        this.start = LocalDateTime.now();
        this.privateApiRequests = new AtomicInteger();
        this.publicApiRequests = new AtomicInteger();
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("run!");
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void test() {
        System.out.println("test!");
    }

    public RateManager addPublicRequest () {
        publicApiRequests.incrementAndGet();
        return this;
    }

    public RateManager addPrivateRequest () {
        privateApiRequests.incrementAndGet();
        return this;
    }
}
