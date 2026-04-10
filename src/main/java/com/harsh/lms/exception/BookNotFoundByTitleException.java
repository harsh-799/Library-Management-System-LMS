package com.harsh.lms.exception;

public class BookNotFoundByTitleException extends RuntimeException {
    public BookNotFoundByTitleException(String message) {
        super(message);
    }
}
