package com.portfolioengine.domain;

import java.time.LocalDate;
import java.util.Objects;

public record Buy(Ticker ticker, Quantity quantity, Price price , LocalDate date) implements Transaction {
    public Buy {
        Objects.requireNonNull(ticker, "ticker");
        Objects.requireNonNull(quantity, "quantity");
        Objects.requireNonNull(price, "price");
        Objects.requireNonNull(date, "date");
    } 
}