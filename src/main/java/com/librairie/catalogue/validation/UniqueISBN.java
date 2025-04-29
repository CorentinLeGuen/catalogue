package com.librairie.catalogue.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueISBNValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueISBN {
    String message() default "ISBN already used";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}