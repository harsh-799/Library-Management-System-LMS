package com.harsh.lms.exception;

public class InvalidBookException extends RuntimeException {
    public InvalidBookException() {
        super("Invalid book ID");
    }
}
