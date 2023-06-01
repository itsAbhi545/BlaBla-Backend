package com.example.BlaBlaBackend.TrimValidator;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@JacksonAnnotationsInside
@JsonSerialize(converter = TrimConverter.class)
@JsonDeserialize(converter = TrimConverter.class)
public @interface MultipleTrim {
    Trim[] value() default {};
}
