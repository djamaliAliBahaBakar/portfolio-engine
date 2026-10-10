package com.portfolioengine.domain;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ClientRequestCounterTest {
    @Test
    public void shouldCountAllConcurrentRequestsWithoutLostUpdates() throws Exception{
        ClientRequestCounter request = new ClientRequestCounter();
        ExecutorService executors =Executors.newFixedThreadPool(2);

        try {
            

            Callable<Void> task1 = () -> {
                    for (int i = 0; i < 1000; i++) {
                        request.increment("client-A");
                    }
                    return null;
                };

            Callable<Void> task2 = () -> {
                    for (int i = 0; i < 1000; i++) {
                        request.increment("client-A");
                    }
                    return null;
                };


            Future<Void> f1 = executors.submit(task1);
            Future<Void> f2 = executors.submit(task2);

            f1.get(2, TimeUnit.SECONDS);
            f2.get(2, TimeUnit.SECONDS);

            assertEquals(2000, request.count("client-A"));
        } finally {
            executors.shutdownNow();
        }
    }
}