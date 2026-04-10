package com.harsh.lms.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("No member found with that ID.");
    }
}
