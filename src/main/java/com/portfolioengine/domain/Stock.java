package com.portfolioengine.domain;

import java.util.concurrent.atomic.AtomicInteger;



public final class Stock {

    private final AtomicInteger available;

    public Stock(int initialStock) {
        this.available = new AtomicInteger(initialStock);
    }

    public boolean reserve(int quantity) {
        if (quantity <= 0) {
            return false;
        }
        while (true) { 
            int currentValue = available();
            if (currentValue < quantity) {
                return false;
            }
            int newStock = currentValue - quantity;
            if (this.available.compareAndSet(currentValue, newStock)) {
                return true;
            }

        }

    }

    public int available() {
        return available.get();
    }
}