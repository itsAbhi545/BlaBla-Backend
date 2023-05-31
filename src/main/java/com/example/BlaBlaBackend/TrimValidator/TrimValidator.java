package com.example.BlaBlaBackend.TrimValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Objects;
import java.util.stream.Stream;

public class TrimValidator implements ConstraintValidator<Trim, Object> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String[] fields;

    @Override
    public void initialize(Trim constraintAnnotation) {
        System.out.println("\u001B[33m" + "String = " + "\u001B[0m");
        fields = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        System.out.println("\u001B[33m" + "Stringhere = " + "\u001B[0m");
        for (String value2 : fields) {
            System.out.println("\u001B[33m" + "val = " + value2 + "\u001B[0m");
        }
        long notNull = Stream.of(fields)
                .map(field -> PARSER.parseExpression(field).getValue(value))
                .filter(Objects::nonNull)
                .count();
        return notNull == 0 || notNull == fields.length;
    }
}
