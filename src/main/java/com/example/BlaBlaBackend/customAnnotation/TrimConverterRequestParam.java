package com.example.BlaBlaBackend.customAnnotation;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
public class TrimConverterRequestParam implements org.springframework.core.convert.converter.Converter<String,String> {
    @Override
    public String convert(String source) {
        System.out.println(source+"/////");
        return source.trim();
    }
}
