package com.harsh.lms.exception;

public class LibraryHasNoMembersException extends RuntimeException {
    public LibraryHasNoMembersException() {
        super("Library has no members");
    }
}
