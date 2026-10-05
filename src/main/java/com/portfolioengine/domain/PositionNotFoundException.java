package com.portfolioengine.domain;


public class PositionNotFoundException extends RuntimeException {
    public PositionNotFoundException(final Ticker ticker) {
        super("Ticker "+ ticker.value() + " Not found in Position");
    }
}