package com.portfolioengine.domain;

import java.time.LocalDate;
import java.util.Objects;

public record Fee(Money money, LocalDate date) implements Transaction {
    public Fee {
        Objects.requireNonNull(money, "money");
        Objects.requireNonNull(date, "date");
    }
}