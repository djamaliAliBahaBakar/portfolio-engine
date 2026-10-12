package com.portfolioengine.async;

import java.util.List;


public record CustomerDashboard(Customer customer, List<Recommendation> recommandations) {
    
}