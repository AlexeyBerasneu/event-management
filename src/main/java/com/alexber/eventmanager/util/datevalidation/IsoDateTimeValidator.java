package com.alexber.eventmanager.util.datevalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class IsoDateTimeValidator implements ConstraintValidator<IsoDateTime, String> {

    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        final OffsetDateTime dateTime;
        try {
            dateTime = OffsetDateTime.parse(value, PATTERN);
        } catch (DateTimeParseException e) {
            return false;
        }
        if (dateTime.isBefore(OffsetDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Date must be in the future").addConstraintViolation();
            return false;
        }
        return true;
    }
}
