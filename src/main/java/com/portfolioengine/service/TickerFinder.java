package com.portfolioengine.service;

import java.util.List;
import java.util.Optional;

public class TickerFinder {

    public static Optional<String> findTicker(
            List<String> tickers,
            String searchedTicker) {
                return tickers.stream().filter(ticker -> ticker.equals(searchedTicker)).findFirst();
            
    }
}