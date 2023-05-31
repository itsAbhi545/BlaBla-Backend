package com.example.BlaBlaBackend.customJsonDeserializer;

import com.fasterxml.jackson.databind.util.StdConverter;

public class TrimConverter extends StdConverter<String, String> {
    @Override
    public String convert(String value) {
        System.out.println("value print = " + value);
        if (value == null){
            return null;
        }
        return value.trim();
    }
}
