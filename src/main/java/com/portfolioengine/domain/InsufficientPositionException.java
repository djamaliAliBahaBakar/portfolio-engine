package com.portfolioengine.domain;


public class InsufficientPositionException extends  RuntimeException {
    public InsufficientPositionException(final String message) {
        super(message);
    }
}