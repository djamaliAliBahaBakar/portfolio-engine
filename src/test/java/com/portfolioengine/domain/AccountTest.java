package com.portfolioengine.domain;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    void shouldTransferConcurrentlyWithoutDeadlock() throws Exception {
        Account accountA = new Account(1, 1000);
        Account accountB = new Account(2, 1000);

        // À compléter
        ExecutorService executors =Executors.newFixedThreadPool(2);
        try {
            
            CountDownLatch ready = new CountDownLatch(2);
            CountDownLatch start = new CountDownLatch(1);

            Callable<Void> task1 = () -> {
                ready.countDown();
                start.await();
                accountA.transferTo(accountB, 300);
                return null;
            };

            Callable<Void> task2 = () -> {
                ready.countDown();
                start.await();
                accountB.transferTo(accountA, 200);
                return null;
            };

            Future<Void> f1 = executors.submit(task1);
            Future<Void> f2 = executors.submit(task2);

            assertTrue(ready.await(2, TimeUnit.SECONDS));
            start.countDown();
       
            

            f1.get(2, TimeUnit.SECONDS);
            f2.get(2, TimeUnit.SECONDS);

            assertEquals(900, accountA.balance());
            assertEquals(1100, accountB.balance());
        } finally {
            executors.shutdownNow();
        }

        

    }


}



