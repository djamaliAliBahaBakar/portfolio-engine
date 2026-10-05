package com.portfolioengine.domain;

import java.time.LocalDate;
import java.util.Objects;

public record Dividend(Ticker ticker, Money money, LocalDate date) implements TransactionAssociatedToTicker {
    public Dividend {
        Objects.requireNonNull(ticker, "ticker");
        Objects.requireNonNull(money, "money");
        Objects.requireNonNull(date, "date");
    }
}