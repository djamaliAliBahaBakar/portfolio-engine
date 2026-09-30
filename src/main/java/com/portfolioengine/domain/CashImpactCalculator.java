package com.portfolioengine.domain;

import java.util.Objects;

public class CashImpactCalculator {

    public Money calculate(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction");

        return switch (transaction) {
            case Buy buy ->
                buy.price().multiply(buy.quantity()).negate();

            case Sell sell ->
                sell.price().multiply(sell.quantity());

            case Dividend dividend ->
                dividend.money();

            case Fee fee ->
                fee.money().negate();
        };
    }

}