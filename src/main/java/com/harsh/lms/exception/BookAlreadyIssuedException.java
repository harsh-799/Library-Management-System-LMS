package com.harsh.lms.exception;

public class BookAlreadyIssuedException extends RuntimeException {
    public BookAlreadyIssuedException() {
        super("You have already issued this book.");
    }
}
