package com.harsh.lms.exception;

public class InvalidBookStockException extends RuntimeException {
    public InvalidBookStockException(String message) {
        super(message);
    }
}
