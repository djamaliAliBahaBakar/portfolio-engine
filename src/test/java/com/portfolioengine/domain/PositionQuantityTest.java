package com.portfolioengine.domain;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class PositionQuantityTest {
    @Test
    public void shouldRejectNullQuantity() {
        assertThrows(NullPointerException.class, ()-> new PositionQuantity(null));
    }

    @Test
    public void shouldRejectNegativeQuantity() {
         assertThrows(IllegalArgumentException.class, ()-> new PositionQuantity(new BigDecimal("-3.4")));
    
    }

    @Test
    public void shouldConsiderSameQuantity() {
        PositionQuantity pos1 = new PositionQuantity(new BigDecimal("5"));
        PositionQuantity pos2 = new PositionQuantity(new BigDecimal("5.00"));
        assertEquals(pos1, pos2);
    }

    @Test
    public void shouldAcceptZeroQuantity() {
        assertDoesNotThrow(() -> new PositionQuantity(new BigDecimal("0")));
    }

    @Test
    public void shouldConsiderAddPosition() {
        PositionQuantity pos = new PositionQuantity(new BigDecimal("5.21"));
        Quantity quantity = new Quantity(new BigDecimal("0.21"));
        assertEquals(new PositionQuantity(new BigDecimal("5.42")), pos.add(quantity));
    }

    @Test
    public void shouldConsiderSubtractPosition() {
        PositionQuantity pos = new PositionQuantity(new BigDecimal("5.21"));
        Quantity quantity = new Quantity(new BigDecimal("0.21"));
        assertEquals(new PositionQuantity(new BigDecimal("5")), pos.subtract(quantity));
    }

    @Test
    public void shouldRejectNegativePosition() {
        PositionQuantity pos = new PositionQuantity(new BigDecimal("5.21"));
        Quantity quantity = new Quantity(new BigDecimal("10.21"));
        assertThrows(IllegalArgumentException.class, () -> pos.subtract(quantity));
    }

    @Test
    public void shouldRejectNullQuantityWhenAdding() {
        PositionQuantity pos = new PositionQuantity(new BigDecimal("5.21"));
        assertThrows(NullPointerException.class, () -> pos.add(null));
    }

    @Test
    public void shouldRejectNullQuantityWhenSubtracting() {
        PositionQuantity pos = new PositionQuantity(new BigDecimal("5.21"));
        assertThrows(NullPointerException.class, () -> pos.subtract(null));
    }

    @Test
    public void shouldConsiderZeroPosition() {
        PositionQuantity pos = new PositionQuantity(new BigDecimal("5.21"));
        Quantity quantity = new Quantity(new BigDecimal("5.21"));
        assertDoesNotThrow( () -> pos.subtract(quantity));
    }

    @Test
    public void shouldConsiderPositionQuantityFromQuantity() {
        PositionQuantity positionQuantity = PositionQuantity.from(new Quantity(new BigDecimal("3")));
        assertEquals(new BigDecimal("3"), positionQuantity.quantity());
    }

    @Test
    public void shouldRejectNullquantityFromMethod() {
        assertThrows(NullPointerException.class, ()-> PositionQuantity.from(null));
    }

}