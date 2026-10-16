package com.portfolioengine.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

class ThreadComparisonTest {

    @Test
    void shouldRunTasksWithPlatformThreads() throws Exception {

        long start = System.nanoTime();

        try (ExecutorService executor =
                Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 5; i++) {
                executor.submit(() -> {
                    Thread.sleep(500);
                    return null;
                });
            }
        }
    }

}