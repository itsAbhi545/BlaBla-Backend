package com.example.BlaBlaBackend.trimming;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Payload;

import java.lang.annotation.*;

//@Retention(RetentionPolicy.RUNTIME)
//@Target(value = {ElementType.FIELD})
@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(converter = TrimConverter2.class)
@JsonDeserialize(converter = TrimConverter2.class)
public @interface Trim2 {
    String[] value();

    String message() default "{AllOrNone.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}