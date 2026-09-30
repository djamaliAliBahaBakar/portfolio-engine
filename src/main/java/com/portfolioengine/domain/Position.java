package com.portfolioengine.domain;
import java.util.Objects;


public record Position(Ticker ticker, PositionQuantity quantity) {
    public Position {
        Objects.requireNonNull(ticker, "ticker");
        Objects.requireNonNull(quantity, "quantity");

        
    }
}