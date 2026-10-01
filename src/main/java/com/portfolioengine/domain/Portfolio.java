package com.portfolioengine.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public final class Portfolio {

    private final Map<Ticker, Position> positions;

    public Portfolio() {
        positions = new HashMap<>();
    }

    public Optional<Position> findPosition(Ticker ticker) {
        return Optional.ofNullable(positions.get(ticker));
    }
    public void apply(Buy buy) {
        Objects.requireNonNull(buy);
        Optional<Position> pos = findPosition(buy.ticker());
        Position position;
        if (pos.isPresent()) {
            Position currentPosition = pos.get();

            position = new Position(
                currentPosition.ticker(),
                currentPosition.quantity().add(buy.quantity())
            );
            
        } else {
            position = new Position(buy.ticker(), PositionQuantity.from(buy.quantity()));
       }
        positions.put(buy.ticker(), position);


    }

    public void apply(Sell sell) {
        Objects.requireNonNull(sell);
        Position currentPosition = findPosition(sell.ticker()).orElseThrow( 
            () -> new IllegalArgumentException("Position with ticker" + sell.ticker().value() + " not found"));


        Position position = new Position(
            currentPosition.ticker(),
            currentPosition.quantity().subtract(sell.quantity())
        );
        
        if (position.quantity().isZero()) {
            positions.remove(currentPosition.ticker());
        } else {
            positions.put(sell.ticker(), position);
        }
    }
   
}