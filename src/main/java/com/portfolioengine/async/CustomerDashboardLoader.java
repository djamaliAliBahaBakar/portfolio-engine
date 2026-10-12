package com.portfolioengine.async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;




public class CustomerDashboardLoader {
    private final  CustomerService customerService;
    private final  RecommendationService recommendationService;

    public CustomerDashboardLoader(CustomerService customerService, RecommendationService recommendationService) {
        this.customerService = customerService;
        this.recommendationService = recommendationService;
    }

    public CompletableFuture<CustomerDashboard> loadDashboard(
        String customerId,
        Executor executor) {

        CompletableFuture<Customer> customerFuture = CompletableFuture.supplyAsync(
            () -> customerService.load(customerId),
            executor
        );

        CompletableFuture<List<Recommendation>> recommendationFuture = CompletableFuture.supplyAsync(
            () -> recommendationService.load(customerId), 
            executor).exceptionally(ex -> List.of());


        CompletableFuture<CustomerDashboard> dashboardFuture = customerFuture.thenCombine(
            recommendationFuture, (customer, recommendation) -> new CustomerDashboard(customer, recommendation) );
        return dashboardFuture;

       }



}