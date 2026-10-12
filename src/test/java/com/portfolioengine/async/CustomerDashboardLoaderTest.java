package com.portfolioengine.async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;


public class CustomerDashboardLoaderTest {
    @Test
    public void shouldConsiderNominalCustomerDashboardCreation() throws ExecutionException , InterruptedException , TimeoutException  {
        CustomerService customerService = customerId -> new Customer(customerId, "Alice");
        RecommendationService recommendationService = customerId -> List.of(new Recommendation("P1"));
        CustomerDashboardLoader dashboardLoader = new CustomerDashboardLoader(customerService, recommendationService);

        ExecutorService executor =  Executors.newFixedThreadPool(2);

        try {
            CompletableFuture<CustomerDashboard> customerDashboardFuture = dashboardLoader.loadDashboard("C1", executor);
            CustomerDashboard  c1 = customerDashboardFuture.get(2, TimeUnit.SECONDS);
            assertEquals("C1", c1.customer().id());
            assertEquals(List.of(new Recommendation("P1")), c1.recommandations());

        }  finally {
            executor.shutdownNow();
        }
    }

    @Test
    void shouldReturnEmptyRecommendationsWhenServiceFails() throws ExecutionException , InterruptedException , TimeoutException  {
        CustomerService customerService = customerId -> new Customer(customerId, "Alice");
        RecommendationService recommendationService = customerId -> { throw new IllegalArgumentException();};
        CustomerDashboardLoader dashboardLoader = new CustomerDashboardLoader(customerService, recommendationService);

        ExecutorService executor =  Executors.newFixedThreadPool(2);

        try {
            CompletableFuture<CustomerDashboard> customerDashboardFuture = dashboardLoader.loadDashboard("C1", executor);
            CustomerDashboard  c1 = customerDashboardFuture.get(2, TimeUnit.SECONDS);
            assertEquals("C1", c1.customer().id());
            assertEquals(List.of(), c1.recommandations());

        }  finally {
            executor.shutdownNow();
        }
    }

    @Test
    void shouldFailWhenCustomerServiceFails() throws ExecutionException , InterruptedException , TimeoutException  {
        CustomerService customerService = customerId -> {throw new IllegalStateException();};
        RecommendationService recommendationService = customerId -> List.of(new Recommendation("P1"));
        CustomerDashboardLoader dashboardLoader = new CustomerDashboardLoader(customerService, recommendationService);
         ExecutorService executor =  Executors.newFixedThreadPool(2);

        try {
            CompletableFuture<CustomerDashboard> customerDashboardFuture = dashboardLoader.loadDashboard("C1", executor);
            
            ExecutionException exception = assertThrows(ExecutionException.class, () -> customerDashboardFuture.get(2, TimeUnit.SECONDS));

            assertInstanceOf(IllegalStateException.class, exception.getCause());
        }  finally {
            executor.shutdownNow();
        }
    }
}