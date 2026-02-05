package com.qatraining.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;


// JSON utility for reading and parsing test data
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

 
    // Read JSON file and return as JsonNode
    public static JsonNode readJsonFile(String filePath) {
        try {
            return objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    
    // Convert object to JSON string
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

  
    // Convert JSON string to object
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
}
