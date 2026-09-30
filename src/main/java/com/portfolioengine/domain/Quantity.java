package com.portfolioengine.domain;

import java.math.BigDecimal;
import java.util.Objects;


public record Quantity(BigDecimal quantity) {
    public Quantity {
        Objects.requireNonNull(quantity, "quantity");
        quantity = quantity.stripTrailingZeros();
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity is equals to 0");
        }
    }
}
