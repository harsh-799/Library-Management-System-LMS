package com.harsh.lms.exception;

public class BookNotFoundByAuthorException extends RuntimeException {
    public BookNotFoundByAuthorException(String message) {
        super(message);
    }
}
