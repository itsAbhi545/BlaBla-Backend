package com.example.BlaBlaBackend.customAnnotation;

import org.springframework.core.convert.converter.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.stereotype.Component;

@Component
public class TrimConverter extends StdConverter<String, String> implements Converter<String,String> {
    @Override
    public String convert(String value) {
       // System.out.println("value print = " + value);
        if (value == null){
            return null;
        }
        return value.trim();
    }
}
