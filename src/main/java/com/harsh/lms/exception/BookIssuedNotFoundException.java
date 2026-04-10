package com.harsh.lms.exception;

public class BookIssuedNotFoundException extends RuntimeException{
    public BookIssuedNotFoundException() {
        super("No book has been issued yet");
    }
}
