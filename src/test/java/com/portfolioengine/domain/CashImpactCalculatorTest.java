package com.portfolioengine.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CashImpactCalculatorTest {
    @Test
    void shouldRejectNullTransaction() {
        assertThrows(NullPointerException.class, () -> new CashImpactCalculator().calculate(null));
    }

    @Test
    void shouldReturnNegativeCashImpactForBuy() {
        Ticker ticker = new Ticker("AAPL");
        Quantity quantity = new Quantity(new BigDecimal("10"));
        Price price = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));

        Buy buy = new Buy(ticker, quantity, price, LocalDate.now());
        Money money = new CashImpactCalculator().calculate(buy);
        assertEquals(new Money(new BigDecimal("-20"), Currency.getInstance("EUR")), money) ;

    }


        @Test
    void shouldReturnPositiveCashImpactForSell() {
        Ticker ticker = new Ticker("AAPL");
        Quantity quantity = new Quantity(new BigDecimal("10"));
        Price price = new Price(new BigDecimal("2"), Currency.getInstance("EUR"));

        Sell sell = new Sell(ticker, quantity, price, LocalDate.now());
        Money money = new CashImpactCalculator().calculate(sell);
        assertEquals(new Money(new BigDecimal("20"), Currency.getInstance("EUR")), money) ;

    }

    @Test
    void shouldReturnPositiveCashImpactForDividend() {
        Ticker ticker = new Ticker("AAPL");
        Money money = new Money(new BigDecimal("2"), Currency.getInstance("EUR"));

        Dividend dividend = new Dividend(ticker, money, LocalDate.now());
        Money resMoney = new CashImpactCalculator().calculate(dividend);
        assertEquals(new Money(new BigDecimal("2"), Currency.getInstance("EUR")), resMoney) ;

    }

      @Test
    void shouldReturnNegativeCashImpactForFee() {

        Money money = new Money(new BigDecimal("20"), Currency.getInstance("EUR"));

        Fee fee = new Fee( money, LocalDate.now());
        Money resMoney = new CashImpactCalculator().calculate(fee);
        assertEquals(new Money(new BigDecimal("-20"), Currency.getInstance("EUR")), resMoney) ;

    }

}