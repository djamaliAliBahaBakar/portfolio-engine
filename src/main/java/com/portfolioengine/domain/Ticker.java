package  com.portfolioengine.domain;

import java.util.Locale;
import java.util.Objects;

public record Ticker(String value) {
    public Ticker {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Ticker value is empty");
        }
        value = value.trim().toUpperCase(Locale.ROOT);
    }
}