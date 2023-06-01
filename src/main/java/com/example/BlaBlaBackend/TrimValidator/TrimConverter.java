package com.example.BlaBlaBackend.TrimValidator;

import com.fasterxml.jackson.databind.util.StdConverter;

public class TrimConverter extends StdConverter<String, String> {
    @Override
    public String convert(String value) {
        if (value == null){
            return null;
        }
        return value.trim();
    }
}

