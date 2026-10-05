package com.portfolioengine.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;
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
        assertThrows(InsufficientPositionException.class, () -> portfolio.apply(sell));
        Position position = portfolio.findPosition(ticker).get();
        assertEquals(new PositionQuantity(new BigDecimal("10")), position.quantity());
    }

    @Test
    public void shouldRejectSellWhenPositionDoesNotExist() {
        Portfolio portfolio = new Portfolio();
        Quantity quantitySell = new Quantity(new BigDecimal("20"));
        Sell sell = new Sell(new  Ticker("GOOG"), quantitySell, new Price(new BigDecimal("2"), Currency.getInstance("EUR")), LocalDate.now());
        assertThrows(PositionNotFoundException.class, ()-> portfolio.apply(sell));
    }

    @Test
    public void shouldConsiderEmptyPortfolioWhenGetTikers() {
        Portfolio portfolio = new Portfolio();
        List<Ticker> tickers = portfolio.tickers();
        assertEquals(0, tickers.size());
    }

    @Test
    public void shouldConsiderListTickerForPortfolio() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("30"));
        Price price2 = new Price(new BigDecimal("4"), Currency.getInstance("USD"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        Ticker ticker3 = new Ticker("GOOG");
        Quantity quantity3 = new Quantity(new BigDecimal("20"));
        Price price3 = new Price(new BigDecimal("3"), Currency.getInstance("EUR"));
        Buy buy3 = new Buy(ticker3, quantity3, price3, LocalDate.now());
        portfolio.apply(buy3);

        List<Ticker> tickers = portfolio.tickers();
        assertEquals(List.of(ticker1, ticker3, ticker2), tickers);
    }

    @Test
    public void shouldConsiderTransactionsByTickerForValidBuyTransaction() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("30"));
        Price price2 = new Price(new BigDecimal("4"), Currency.getInstance("EUR"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        Ticker ticker3 = new Ticker("AAPL");
        Quantity quantity3 = new Quantity(new BigDecimal("20"));
        Price price3 = new Price(new BigDecimal("3"), Currency.getInstance("EUR"));
        Buy buy3 = new Buy(ticker3, quantity3, price3, LocalDate.now());
        portfolio.apply(buy3);

        Map<Ticker, List<TransactionAssociatedToTicker>> transactionsByTicker = portfolio.transactionsByTicker();
        assertEquals(List.of(buy1, buy3), transactionsByTicker.get(ticker1));
        assertEquals(List.of(buy2), transactionsByTicker.get(ticker2));
    }

    @Test
    public void shouldConsiderTransactionsByTickerForValidSellTransaction() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("30"));
        Price price2 = new Price(new BigDecimal("4"), Currency.getInstance("EUR"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        Ticker ticker3 = new Ticker("AAPL");
        Quantity quantity3 = new Quantity(new BigDecimal("7"));
        Price price3 = new Price(new BigDecimal("3"), Currency.getInstance("EUR"));
        Sell sell = new Sell(ticker3, quantity3, price3, LocalDate.now());
        portfolio.apply(sell);

        Map<Ticker, List<TransactionAssociatedToTicker>> transactionsByTicker = portfolio.transactionsByTicker();
        assertEquals(List.of(buy1, sell), transactionsByTicker.get(ticker1));
        assertEquals(List.of(buy2), transactionsByTicker.get(ticker2));
    }

    @Test
    public void shouldConsiderTransactionsByTickerForInvalidSellTransaction() {
        Portfolio portfolio = new Portfolio();
        Ticker ticker1 = new Ticker("AAPL");
        Quantity quantity1 = new Quantity(new BigDecimal("10"));
        Price price1 = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));
        Buy buy1 = new Buy(ticker1, quantity1, price1, LocalDate.now());
        portfolio.apply(buy1);

        Ticker ticker2 = new Ticker("MSFT");
        Quantity quantity2 = new Quantity(new BigDecimal("30"));
        Price price2 = new Price(new BigDecimal("4"), Currency.getInstance("EUR"));
        Buy buy2 = new Buy(ticker2, quantity2, price2, LocalDate.now());
        portfolio.apply(buy2);

        Ticker ticker3 = new Ticker("AAPL");
        Quantity quantity3 = new Quantity(new BigDecimal("20"));
        Price price3 = new Price(new BigDecimal("3"), Currency.getInstance("EUR"));
        Sell sell = new Sell(ticker3, quantity3, price3, LocalDate.now());
        assertThrows(InsufficientPositionException.class, () -> portfolio.apply(sell));

        Map<Ticker, List<TransactionAssociatedToTicker>> transactionsByTicker = portfolio.transactionsByTicker();
        assertEquals(List.of(buy1), transactionsByTicker.get(ticker1));
        assertEquals(List.of(buy2), transactionsByTicker.get(ticker2));
    }

    
}


