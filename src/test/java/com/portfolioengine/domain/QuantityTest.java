package com.portfolioengine.domain;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;


class QuantityTest {

    @Test
    void shouldRejectQuantityToNull() {
        assertThrows(NullPointerException.class, () -> new Quantity(null));
    }

    @Test
    void shouldRejectQuantityToZero() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new Quantity(new BigDecimal("0.00")));
    }

    @Test 
    void shouldRejectNegativeQuantity() {
         assertThrows(IllegalArgumentException.class, () -> new Quantity(BigDecimal.valueOf(-5)));
    
    }

    @Test 
    void shouldConsiderEquivalentQuantitiesEqual() {
        Quantity quantity1 = new Quantity(BigDecimal.valueOf(5.0));
        Quantity quantity2 = new Quantity(new BigDecimal("5.00"));
        assertEquals(quantity1, quantity2);
    }


}