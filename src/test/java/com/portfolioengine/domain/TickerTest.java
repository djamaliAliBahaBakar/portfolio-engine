package com.portfolioengine.domain;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;





class TickerTest {

    @Test
    void shouldNormalizeTicker() {
        Ticker ticker = new Ticker(" aapl ");
        assertEquals("AAPL", ticker.value());

    }

    @Test
    void shouldRejectNullValue()  {
        assertThrows(NullPointerException.class, () -> new Ticker(null));

    }


    @Test
    void shouldRejectemptyValue()  {
        assertThrows(IllegalArgumentException.class, () -> new Ticker(" "));

    }


}
