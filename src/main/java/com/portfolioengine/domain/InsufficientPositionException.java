package com.portfolioengine.domain;

import java.math.BigDecimal;

public class InsufficientPositionException extends  RuntimeException {
    private final Ticker ticker;
    private final BigDecimal requested;
    private final BigDecimal available;

    public InsufficientPositionException(final Ticker ticker, final BigDecimal requested, final BigDecimal available) {
        super("Sell quantity " + requested + " greater than current position quantity :"+ available);
        this.ticker = ticker;
        this.requested = requested;
        this.available = available;
    }

    public Ticker getTicker() {
        return this.ticker;
    }

    public BigDecimal getRequested() {
        return this.requested;
    }

    public BigDecimal getAvailable() {
        return  this.available;
    }


    
}