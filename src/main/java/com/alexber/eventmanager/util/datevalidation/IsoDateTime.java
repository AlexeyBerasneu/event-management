package com.alexber.eventmanager.util.datevalidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsoDateTimeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsoDateTime {

    String message() default "Date must be in ISO-8601 format (yyyy-MM-dd'T'HH:mm:ss+00:00)";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
