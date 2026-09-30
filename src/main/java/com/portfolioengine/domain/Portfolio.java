package com.portfolioengine.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public final class Portfolio {

    private final Map<Ticker, Position> positions;

    public Portfolio() {
        positions = new HashMap<>();
    }

    public Optional<Position> findPosition(Ticker ticker) {
        return Optional.ofNullable(positions.get(ticker));
    }
}