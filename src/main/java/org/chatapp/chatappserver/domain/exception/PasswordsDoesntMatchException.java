package org.chatapp.chatappserver.domain.exception;

public class PasswordsDoesntMatchException extends RuntimeException {
    public PasswordsDoesntMatchException(String message) {
        super(message);
    }
}
