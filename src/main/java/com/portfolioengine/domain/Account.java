package com.portfolioengine.domain;

public final class Account {

    private final long id;
    private int balance;

    public Account(long id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public long id() {
        return id;
    }

    // À compléter
    public void transferTo(Account destination, int amount) {
        long id1 = this.id;
        long id2 = destination.id;
        Account first;
        Account second;

        if (id1 < id2) {
            first = this;
            second = destination;
        } else {
            first = destination;
            second = this;
        }



        synchronized (first) {
            synchronized (second) {
                if (balance < amount) {
                throw new IllegalStateException(
                    "Insufficient funds");
            }

                destination.credit(amount);
                balance -= amount;
            }
        }

    }

     public synchronized void credit(int amount) {
        balance += amount;
    }

    public synchronized int balance() {
        return balance;
    }
}


