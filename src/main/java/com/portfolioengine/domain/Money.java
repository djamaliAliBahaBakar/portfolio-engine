package com.portfolioengine.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;



public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        amount = amount.stripTrailingZeros();
    }


    public Money add(Money money) {
        Objects.requireNonNull(money);
        if (!currency.equals(money.currency())) {
            throw new IllegalArgumentException("Currencies are different");
        }

        
        return new Money(amount.add(money.amount()), currency);
    }

    public Money negate() {
        return new Money(amount.negate(), currency);
    }
}
