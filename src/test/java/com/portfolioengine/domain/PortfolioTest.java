package com.portfolioengine.domain;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PortfolioTest {

    @Test
    public void shouldReturnEmptyWhenPositionDoesNotExist() {
        Portfolio portfolio = new Portfolio();
        assertEquals(Optional.empty(), portfolio.findPosition(new Ticker("AAPL")));
    }
}



