package com.portfolioengine.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TickerStreamProcessorTest {
    @Test
    public void shouldConsiderMultipleTickersWithDoublons() {
        List<String> tickers = List.of(
        "AAPL",
        "MSFT",
        "AAPL",
        "NVDA",
        "AMZN",
        "ADBE",
        "ADBE"
        ) ;

        List<String> distinctTickers = TickerStreamProcessor.findDistinctSortedTickersStartingWithA(tickers);
        assertEquals(List.of("AAPL","ADBE",  "AMZN"), distinctTickers);

    }

    @Test
    public void shouldConsiderEmptyTickers() {
        List<String> tickers = List.of() ;

        List<String> distinctTickers = TickerStreamProcessor.findDistinctSortedTickersStartingWithA(tickers);
        assertTrue(distinctTickers.isEmpty());

    }

        @Test
    public void shouldTickerNonStartedWithA() {
        List<String> tickers = List.of( "MSFT","NVDA");


        List<String> distinctTickers = TickerStreamProcessor.findDistinctSortedTickersStartingWithA(tickers);
        assertTrue(distinctTickers.isEmpty());

    }


}