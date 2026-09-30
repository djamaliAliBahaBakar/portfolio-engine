package  com.portfolioengine.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Price(BigDecimal amount, Currency currency) {
    public Price {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        amount = amount.stripTrailingZeros();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity, "quantity");
        return new Money( quantity.quantity().multiply(amount), currency);
    }
}