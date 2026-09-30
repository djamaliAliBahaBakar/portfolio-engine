package com.portfolioengine.domain;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void shouldRejectNullTicker() {
        assertThrows(NullPointerException.class, () -> new Position(null, new PositionQuantity(BigDecimal.TEN)));
    }

    @Test
    public void shouldRejectNullquantity() {
        assertThrows(NullPointerException.class, () -> new Position(new Ticker("AAPL"), null));
    }

    @Test
    void shouldCreateValidPosition() {
        Position position = new Position(
            new Ticker("AAPL"),
            new PositionQuantity(BigDecimal.TEN)
        );

        assertEquals(new Ticker("AAPL"), position.ticker());
        assertEquals(new PositionQuantity(BigDecimal.TEN), position.quantity());
    }
}