package com.alexber.eventmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(
            {MethodArgumentNotValidException.class,
                    IllegalArgumentException.class,})
    public ResponseEntity<ServerErrorDto> handleValidationException(Exception ex) {
        log.error("Got validation exception", ex);
        String detailMessage = ex instanceof MethodArgumentNotValidException ?
                constructMethodArgumentNotValidate((MethodArgumentNotValidException) ex) :
                ex.getMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDto(
                        "Validation Error",
                        detailMessage,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ServerErrorDto> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex
    ) {
        log.error("Got method parameter validation exception", ex);

        String detailMessage = ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDto(
                        "Validation request parameter Error",
                        detailMessage,
                        LocalDateTime.now()
                ));
    }



    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleNotFoundException(EntityNotFoundException ex) {
        log.error("Got not found exception", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ServerErrorDto(
                        "Not found",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenericException(Exception ex) {
        log.error("Got global exception", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ServerErrorDto(
                        "Server Error",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    private static String constructMethodArgumentNotValidate(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
