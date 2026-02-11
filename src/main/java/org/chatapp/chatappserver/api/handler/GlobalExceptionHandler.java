package org.chatapp.chatappserver.api.handler;

import org.chatapp.chatappserver.domain.dto.ApiValidationFailedResponse;
import org.chatapp.chatappserver.domain.dto.FieldErrorDto;
import org.chatapp.chatappserver.domain.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiValidationFailedResponse handleBusinessValidation(ValidationException ex) {
        return new ApiValidationFailedResponse(ex.getMessage(), ex.getErrors());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiValidationFailedResponse handleValidation(MethodArgumentNotValidException ex) {
        List<FieldErrorDto> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorDto(fe.getField(), fe.getDefaultMessage()))
                .toList();

        return new ApiValidationFailedResponse("Validation failed", errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> exception(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
