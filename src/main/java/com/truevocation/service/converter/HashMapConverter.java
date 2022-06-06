package com.truevocation.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;


@Converter
public class HashMapConverter implements AttributeConverter<Map<String, String>, String> {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> location) {

        String dbLocation = null;
        try {
            dbLocation = objectMapper.writeValueAsString(location);
        } catch (final JsonProcessingException e) {

        }

        return dbLocation;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbLocation) {
        if (dbLocation == null) {
            return null;
        }
        Map<String, String> location = null;
        try {
            location = objectMapper.readValue(dbLocation, Map.class);
        } catch (final IOException e) {

        }

        return location;
    }

}
