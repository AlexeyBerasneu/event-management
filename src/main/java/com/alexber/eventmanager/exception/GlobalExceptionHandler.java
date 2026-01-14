package com.alexber.eventmanager.exception;

import com.alexber.eventmanager.exception.customexception.AmountRegistrationException;
import com.alexber.eventmanager.exception.customexception.NotAvailableEventException;
import com.alexber.eventmanager.exception.customexception.StatusEventException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class,})
    public ResponseEntity<ServerErrorDto> handleValidationException(Exception ex) {
        logger.warn("Got validation exception", ex);
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
        logger.warn("Got method parameter validation exception", ex);

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
        logger.error("Got not found exception", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ServerErrorDto(
                        "Not found",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(NotAvailableEventException.class)
    public ResponseEntity<ServerErrorDto> handleDeleteEventException(NotAvailableEventException ex) {
        logger.warn("Got delete event exception", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ServerErrorDto(
                        "Error deleting event",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(StatusEventException.class)
    public ResponseEntity<ServerErrorDto> handleStatusEventException(StatusEventException ex) {
        logger.error("Got status event exception", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ServerErrorDto(
                        "Error status event",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ServerErrorDto> handleIllegalStateException(IllegalStateException ex) {
        logger.error("Got amount of occupied event exception", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ServerErrorDto(
                        "Error amount of available places for event",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ServerErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("Got access denied exception", ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ServerErrorDto(
                        "Forbidden",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(AmountRegistrationException.class)
    public ResponseEntity<ServerErrorDto> handleAmountRegistrationException(AmountRegistrationException ex) {
        logger.error("Got registration exception", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ServerErrorDto(
                        "Registration Error",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ServerErrorDto> ConstraintViolationException(ConstraintViolationException ex) {
        logger.error("Got constraint validation exception", ex);
        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> String.format(
                        "%s: %s",
                        v.getPropertyPath(),
                        v.getMessage()))
                .collect(Collectors.joining("\n"));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDto(
                        "Constraint validation exception",
                        message,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenericException(Exception ex) {
        logger.error("Got global exception", ex);
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
