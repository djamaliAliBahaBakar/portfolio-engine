package com.portfolioengine.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public final class Portfolio {

    private final Map<Ticker, Position> positions;
    private final List<Transaction> transactions;

    public Portfolio() {
        positions = new HashMap<>();
        transactions = new ArrayList<>();
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
        transactions.add(buy);
        positions.put(buy.ticker(), position);


    }

    public void apply(Sell sell) {
        Objects.requireNonNull(sell);

        Position currentPosition = findPosition(sell.ticker()).orElseThrow( 
            () -> new PositionNotFoundException(sell.ticker()));


        if (sell.quantity().quantity().compareTo(currentPosition.quantity().quantity()) > 0) {
            throw new InsufficientPositionException("Sell quantity " + sell.quantity() + " greater than current position quanity :"+ currentPosition.quantity().quantity());
        }
        
        Position position = new Position(
            currentPosition.ticker(),
            currentPosition.quantity().subtract(sell.quantity())
        );
        
        if (position.quantity().isZero()) {
            positions.remove(currentPosition.ticker());
        } else {
            positions.put(sell.ticker(), position);
        }
        transactions.add(sell);
    }

    public List<Ticker> tickers() {
        return positions.values()
            .stream().map(position -> position.ticker())
            .sorted(Comparator.comparing(Ticker::value))
            .toList();
    }

    public Map<Ticker, List<TransactionAssociatedToTicker>> transactionsByTicker() {
        return transactions.stream()
            .filter(transaction -> transaction instanceof TransactionAssociatedToTicker)
            .map( t -> (TransactionAssociatedToTicker)t)
            .collect(Collectors.groupingBy(TransactionAssociatedToTicker::ticker));

    }
   
}