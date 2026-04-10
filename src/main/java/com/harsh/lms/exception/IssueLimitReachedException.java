package com.harsh.lms.exception;

public class IssueLimitReachedException extends RuntimeException {
    public IssueLimitReachedException() {
        super("Limit reached! You have already issued 3 book");
    }
}
