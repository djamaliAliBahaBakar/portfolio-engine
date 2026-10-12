package com.portfolioengine.async;

import java.util.List;


public interface RecommendationService {
    public List<Recommendation> load(String customerId);
}