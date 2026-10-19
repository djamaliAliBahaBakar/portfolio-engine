package com.portfolioengine.service;

import java.util.List;

public class TickerStreamProcessor {
    public static List<String> findDistinctSortedTickersStartingWithA(
        List<String> tickers) {
            return tickers.stream().filter(ticker->ticker.startsWith("A")).distinct().sorted().toList();

    
    }
}