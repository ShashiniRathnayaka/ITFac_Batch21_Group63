package com.qatraining.api;

import com.qatraining.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * API Client for REST API testing
 */
public class APIClient {

    private static ThreadLocal<String> authToken = new ThreadLocal<>();
    private static ConfigManager config = ConfigManager.getInstance();

    static {
        RestAssured.baseURI = config.getApiBaseUrl();
    }

    /**
     * Get base request specification
     */
    public static RequestSpecification getRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setContentType(ContentType.JSON);
        builder.setAccept(ContentType.JSON);
        builder.addFilter(new AllureRestAssured());

        if (authToken.get() != null) {
            builder.addHeader("Authorization", "Bearer " + authToken.get());
        }

        return builder.build();
    }

    /**
     * Set authentication token
     */
    public static void setAuthToken(String token) {
        authToken.set(token);
    }

    /**
     * Get authentication token
     */
    public static String getAuthToken() {
        return authToken.get();
    }

    /**
     * Clear authentication token
     */
    public static void clearAuthToken() {
        authToken.remove();
    }

    /**
     * Get request specification with token
     */
    public static RequestSpecification getAuthenticatedRequestSpec() {
        if (authToken.get() == null) {
            throw new RuntimeException("Authentication token not set. Please login first.");
        }
        return getRequestSpec();
    }
}
