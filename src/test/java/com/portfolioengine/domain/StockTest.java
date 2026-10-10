package com.portfolioengine.domain;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class StockTest {
    @Test
    public void shouldAllowOnlyOneConcurrentReservationWhenStockIsInsufficientForBoth() throws Exception {
        Stock stock = new Stock(10);

         ExecutorService executors = Executors.newFixedThreadPool(2);

         try {
            CountDownLatch ready = new CountDownLatch(2);
            CountDownLatch start = new CountDownLatch(1);
            Callable<Boolean> task1 = () -> {
                ready.countDown();
                start.await();
                return stock.reserve(7);
            };

            Callable<Boolean> task2 = () -> {
                ready.countDown();
                start.await();
                return stock.reserve(6);
            };
            Future<Boolean> f1 = executors.submit(task1);
            Future<Boolean> f2 = executors.submit(task2);
            assertTrue(ready.await(2, TimeUnit.SECONDS));
            start.countDown();

            boolean res1  = f1.get(2, TimeUnit.SECONDS);
            boolean res2 = f2.get(2, TimeUnit.SECONDS);
            assertTrue(res1 ^ res2);
            assertTrue(stock.available() == 3 || stock.available() == 4);



             
         } finally {
            executors.shutdownNow();
         }
         

    }
}