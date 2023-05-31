package com.example.BlaBlaBackend.TrimValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TrimValidator.class)
public @interface Trim {
    String[] value();

    String message() default "{Trim.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
