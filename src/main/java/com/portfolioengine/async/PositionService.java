package com.portfolioengine.async;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.portfolioengine.domain.Ticker;

public class PositionService {

    private final ConcurrentHashMap<Ticker, Integer> quantities =
            new ConcurrentHashMap<>();

    public boolean sell(Ticker ticker, int quantity) {
        AtomicBoolean sold = new AtomicBoolean(false);
  
        quantities.compute(ticker, (key, available) -> {
            if (available == null) return null;
            else if (quantity <= 0 || available < quantity)  return available;
            else  {
                sold.set(true);
            }
            return available -quantity;});
        return  sold.get();

    }
}