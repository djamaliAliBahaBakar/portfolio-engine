package com.portfolioengine.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class PortfolioTest {

    @Test
    public void shouldReturnEmptyWhenPositionDoesNotExist() {
        Portfolio portfolio = new Portfolio();
        assertEquals(Optional.empty(), portfolio.findPosition(new Ticker("AAPL")));
    }

    @Test
    public void shouldRejectNullApply() {
        assertThrows(NullPointerException.class, ()-> new Portfolio().apply(null));
    }

    @Test
    public void shouldConsiderApplyBuyInEmptyPortfolio() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker = new Ticker("AAPL");
        Quantity quantity = new Quantity(new BigDecimal("10"));
        Price price = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));

        Buy buy = new Buy(ticker, quantity, price, LocalDate.now());
        portfolio.apply(buy);
        Position position = new Position(buy.ticker(), PositionQuantity.from(buy.quantity()));
       
        assertEquals(position, portfolio.findPosition(new Ticker("AAPL")).get());
    }

    @Test
    public void shouldConsiderApplyBuyInNonEmptyPortfolio() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker = new Ticker("AAPL");
        Quantity quantity = new Quantity(new BigDecimal("10"));
        Price price = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));

        Buy buy1 = new Buy(ticker, quantity, price, LocalDate.now());
        portfolio.apply(buy1);
        Quantity quantity2 = new Quantity(new BigDecimal("20"));
        Buy buy2 = new Buy(ticker, quantity2, price, LocalDate.now());
        
        portfolio.apply(buy2);
        Quantity quantity3 = new Quantity(buy1.quantity().quantity().add(buy2.quantity().quantity()));
        Position position = new Position(buy2.ticker(), PositionQuantity.from(quantity3));
       
        assertEquals(position, portfolio.findPosition(new Ticker("AAPL")).get());
    }
}



