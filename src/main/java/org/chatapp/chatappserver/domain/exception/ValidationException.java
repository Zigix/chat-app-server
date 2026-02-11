package org.chatapp.chatappserver.domain.exception;

import org.chatapp.chatappserver.domain.dto.FieldErrorDto;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<FieldErrorDto> errors;

    public ValidationException(String message, List<FieldErrorDto> errors) {
        super(message);
        this.errors = errors;
    }

    public List<FieldErrorDto> getErrors() {
        return errors;
    }
}
