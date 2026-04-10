package com.harsh.lms.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException() {
        super("All books are issued");
    }
}
