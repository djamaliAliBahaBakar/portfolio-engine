package com.portfolioengine.domain;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class PriceTest {
    @Test
    void shouldRejectNullAmount() {
        assertThrows(NullPointerException.class, () -> new Price(null, Currency.getInstance("EUR")));
    }

    @Test 
    void shouldRejectNullCurrency() {
         assertThrows(NullPointerException.class, () -> new Price(BigDecimal.TWO, null));
    
    }

    @Test
    void shouldRejectZeroAmount() {
         assertThrows(IllegalArgumentException.class, () -> new Price(BigDecimal.ZERO, Currency.getInstance("EUR")));
    
    }

        @Test
    void shouldRejectNegativeAmount() {
         assertThrows(IllegalArgumentException.class, () -> new Price(BigDecimal.valueOf(-4), Currency.getInstance("EUR")));
    
    }

     @Test 
    void shouldConsiderEquivalentPricesEqual() {
        Price price1 = new Price(new BigDecimal("5.00"), Currency.getInstance("EUR"));
        Price price2 = new Price(new BigDecimal("5"), Currency.getInstance("EUR"));
        assertEquals(price1, price2 );

    }

    @Test
    void shouldRejectMultiplyNullQuantity() {
        assertThrows(NullPointerException.class, () -> new Price(BigDecimal.TEN, Currency.getInstance("EUR")).multiply(null));
    }

    @Test
    void shouldMultiplyPriceByQuantity() {
        Price price = new Price(BigDecimal.valueOf(12.5), Currency.getInstance("USD"));
        Quantity quantity = new Quantity(BigDecimal.valueOf(2.5));
        assertEquals(new Money(BigDecimal.valueOf(31.25), Currency.getInstance("USD")), price.multiply(quantity));
    }

}



