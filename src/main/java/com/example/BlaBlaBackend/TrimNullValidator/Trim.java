package com.example.BlaBlaBackend.TrimNullValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TrimValidator.class)
public @interface Trim {
    String[] value() default {};
    String[] exclude() default {};

    String message() default "";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
