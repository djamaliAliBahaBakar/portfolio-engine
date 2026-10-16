package com.portfolioengine.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class SemaphoreTest {

    @Test
    void shouldLimitConcurrentTasksToTen() throws Exception {
        Semaphore semaphore = new Semaphore(10);
        long start = System.nanoTime();

        try (ExecutorService executor =
                Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 100; i++) {
                executor.submit(() -> {
                    // TODO 1 : acquérir un permis
                    semaphore.acquire();

                    try {
                        Thread.sleep(500);
                    } finally {
                        // TODO 2 : libérer le permis
                        semaphore.release();
                    }

                    return null;
                });
            }
        }
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(
            System.nanoTime() - start
        );

        System.out.println("Duration: " + elapsedMs + " ms");
    }
}