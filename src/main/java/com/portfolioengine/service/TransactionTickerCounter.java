package com.portfolioengine.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionTickerCounter {
    public static Map<String, Integer> countByTicker(
        List<String> tickers) {
            Map<String, Integer> transactionByTicker = new HashMap<>();
            for (String ticker : tickers) {

                transactionByTicker.merge(ticker, 1, Integer::sum);
            }
            return transactionByTicker;
            
        }

}