package com.portfolioengine.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PortfolioTest {

    @Test
    public void shouldReturnEmptyWhenPositionDoesNotExist() {
        Portfolio portfolio = new Portfolio();
        assertEquals(Optional.empty(), portfolio.findPosition(new Ticker("AAPL")));
    }

    @Test
    public void shouldRejectBuyNullApply() {
        Buy buy = null;
        assertThrows(NullPointerException.class, ()-> new Portfolio().apply(buy));
    }

    @Test
    public void shouldRejecSelltNullApply() {
        Sell sell = null;
        assertThrows(NullPointerException.class, ()-> new Portfolio().apply(sell));
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

    @Test
    public void shouldDecreasePositionWhenSellingPartially() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker = new Ticker("AAPL");
        Quantity quantity = new Quantity(new BigDecimal("10"));
        Price price = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));

        Buy buy = new Buy(ticker, quantity, price, LocalDate.now());
        portfolio.apply(buy);

        Quantity quantitySell = new Quantity(new BigDecimal("7"));
        Sell sell = new Sell(ticker, quantitySell, price, LocalDate.now());
        portfolio.apply(sell);

        Position position = portfolio.findPosition(ticker).get();
        assertEquals(new BigDecimal("3"), position.quantity().quantity());

    }

    @Test
    public void shouldRemovePositionWhenSellingEntirePosition() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker = new Ticker("AAPL");
        Quantity quantity = new Quantity(new BigDecimal("10"));
        Price price = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));

        Buy buy = new Buy(ticker, quantity, price, LocalDate.now());
        portfolio.apply(buy);

        Quantity quantitySell = new Quantity(new BigDecimal("10"));
        Sell sell = new Sell(ticker, quantitySell, price, LocalDate.now());
        portfolio.apply(sell);
        Optional<Position> position = portfolio.findPosition(ticker);
        assertTrue(position.isEmpty());
    }

    @Test
    public void shouldRejectSellGreaterThanPosition() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker = new Ticker("AAPL");
        Quantity quantity = new Quantity(new BigDecimal("10"));
        Price price = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));

        Buy buy = new Buy(ticker, quantity, price, LocalDate.now());
        portfolio.apply(buy);

        Quantity quantitySell = new Quantity(new BigDecimal("20"));
        Sell sell = new Sell(ticker, quantitySell, price, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> portfolio.apply(sell));
        Position position = portfolio.findPosition(ticker).get();
        assertEquals(new PositionQuantity(new BigDecimal("10")), position.quantity());
    }

    @Test
    public void shouldRejectSellWhenPositionDoesNotExist() {
        Portfolio portfolio = new Portfolio();
        Quantity quantitySell = new Quantity(new BigDecimal("20"));
        Sell sell = new Sell(new  Ticker("GOOG"), quantitySell, new Price(new BigDecimal("2"), Currency.getInstance("EUR")), LocalDate.now());
        assertThrows(IllegalArgumentException.class, ()-> portfolio.apply(sell));
    }
}



