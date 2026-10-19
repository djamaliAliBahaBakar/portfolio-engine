package com.portfolioengine.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class TickerFinderTest {
    @Test
    public void shouldReturnTickerWhenPresent() {
        List<String> tickers = List.of("AAPL", "MSFT", "NVDA");
        Optional<String>  result = TickerFinder.findTicker(tickers, "AAPL");
        assertEquals(Optional.of("AAPL"), result);

    }

    @Test
    public void shouldReturnEmptyWhenTickerNotFound() {
        List<String> tickers = List.of("AAPL", "MSFT", "NVDA");
        Optional<String>  result = TickerFinder.findTicker(tickers, "GOOG");
        assertTrue(result.isEmpty());
        
    }

    @Test
    public void shouldReturnEmptyForEmptyList() {
        List<String> tickers = List.of();
        Optional<String>  result = TickerFinder.findTicker(tickers, "AAPL");
        assertTrue(result.isEmpty());
        
    }
}