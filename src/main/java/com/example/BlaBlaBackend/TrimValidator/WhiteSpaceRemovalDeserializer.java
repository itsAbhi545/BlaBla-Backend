package com.example.BlaBlaBackend.TrimValidator;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class WhiteSpaceRemovalDeserializer extends JsonDeserializer<String> {
//    @Override
//    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
//        return p.currentToken().asString().trim();
//    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) {
        // This is where you can deserialize your value the way you want.
        // Don't know if the following expression is correct, this is just an idea.
        return jp.getCurrentToken().asString().trim();
    }
}
