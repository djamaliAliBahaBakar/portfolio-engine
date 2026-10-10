package com.portfolioengine.domain;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class ClientRequestCounter {

    private final Map<String, Integer> counters =
            new ConcurrentHashMap<>();

    public void increment(String clientId) {
        counters.merge(clientId, 1, (oldValue, newValue) -> oldValue+ newValue);
    }

    public int count(String clientId) {
        return counters.getOrDefault(clientId, 0);
    }
}