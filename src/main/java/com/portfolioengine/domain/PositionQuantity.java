package com.portfolioengine.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record PositionQuantity(BigDecimal quantity) {
    public PositionQuantity {
        Objects.requireNonNull(quantity, "quantity");
        quantity = quantity.stripTrailingZeros();
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Positive quantity is allowed");
        }
    }

    public PositionQuantity add(Quantity newQuantity) {
        Objects.requireNonNull(newQuantity, "quantity");
        return new PositionQuantity(quantity.add(newQuantity.quantity()));
    }

    public PositionQuantity subtract(Quantity newQuantity) {
        Objects.requireNonNull(newQuantity, "quantity");
        return new PositionQuantity(quantity.subtract(newQuantity.quantity()));
    }
}