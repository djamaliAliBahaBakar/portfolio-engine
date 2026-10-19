package com.portfolioengine.service;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


public class TransactionTickerCounterTest {
    @Test
    public void shouldCountTransactionsByTicker() {
        List<String> tickers = List.of(
            "AAPL",
            "MSFT",
            "AAPL",
            "NVDA",
            "MSFT",
            "AAPL"
        );
        
        Map<String, Integer> transactionByTicker = TransactionTickerCounter.countByTicker(tickers);
        
        assertEquals(Map.of(
            "AAPL", 3,
            "MSFT", 2,
            "NVDA", 1
        ), transactionByTicker);
        
    }

        @Test
    public void shouldCountRepeatedTicker() {
        List<String> tickers = List.of(
            "AAPL",
            "AAPL",
            "AAPL",
            "AAPL",
            "AAPL",
            "AAPL"
        );
        
        Map<String, Integer> transactionByTicker = TransactionTickerCounter.countByTicker(tickers);
        
        assertEquals(Map.of(
            "AAPL", 6
        ), transactionByTicker);
        
    }

    @Test
    public void shouldReturnEmptyMapForEmptyInput() {
        List<String> tickers = List.of();
        
        Map<String, Integer> transactionByTicker = TransactionTickerCounter.countByTicker(tickers);
        
        assertEquals(Map.of(
           
        ), transactionByTicker);
        
    }

    @Test
    public void shouldCountDistinctTickers() {
        List<String> tickers = List.of(
            "AAPL",
            "MSFT",
            "NVDA",
            "GOOG"
        );
        
        Map<String, Integer> transactionByTicker = TransactionTickerCounter.countByTicker(tickers);
        
        assertEquals(Map.of(
            "AAPL", 1,
            "MSFT", 1,
            "NVDA", 1,
            "GOOG",1
        ), transactionByTicker);
        
    }
}