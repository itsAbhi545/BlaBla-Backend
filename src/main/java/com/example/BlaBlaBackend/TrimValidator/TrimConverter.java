package com.example.BlaBlaBackend.TrimValidator;

import com.fasterxml.jackson.databind.util.StdConverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class TrimConverter extends StdConverter<String, String> implements Converter<String, String> {
    @Override
    public String convert(String value) {
        System.out.println("\u001B[33m" + "control here = " + value + "\u001B[0m");
        if (value == null){
            return null;
        }
        return value.trim();
    }


}

