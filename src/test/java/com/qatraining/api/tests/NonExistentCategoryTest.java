package com.qatraining.api.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * API_DASH_010: Non-existent Category Returns 404 Error
 * Ensures the API returns an appropriate error when accessing a non-existent category
 */
@DisplayName("API_DASH_010: Non-existent Category 404 Error")
public class NonExistentCategoryTest {

    private Response lastResponse;
    private String adminToken;
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String API_BASE_URL = "http://localhost:8080/api";

    @BeforeEach
    public void setUp() {
        // Authenticate as admin
        Response authResponse = given()
            .contentType("application/json")
            .body(new HashMap<String, String>() {{
                put("username", "admin");
                put("password", "admin123");
            }})
            .when()
            .post(API_BASE_URL + "/auth/login")
            .then()
            .extract()
            .response();

        assertEquals(200, authResponse.getStatusCode(), "Admin authentication should succeed");
        adminToken = authResponse.jsonPath().getString("token");
        assertNotNull(adminToken, "Admin token should be returned");
    }

    @Test
    @DisplayName("GET non-existent category by ID should return 404 Not Found")
    public void testGetNonExistentCategoryReturns404() {
        // Attempt to retrieve a non-existent category (ID: 999999)
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/categories/999999")
            .then()
            .extract()
            .response();

        // Verify response status is 404 Not Found
        assertEquals(404, lastResponse.getStatusCode(),
            "GET request for non-existent category should return 404 Not Found, got: " + lastResponse.getStatusCode());
    }

    @Test
    @DisplayName("404 response should contain error message")
    public void testNonExistentCategoryReturnsErrorMessage() {
        // Attempt to retrieve a non-existent category
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/categories/999999")
            .then()
            .extract()
            .response();

        // Verify response status is 404
        assertEquals(404, lastResponse.getStatusCode(),
            "Expected 404 Not Found");

        // Verify error message is returned in response
        String responseBody = lastResponse.getBody().asString();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.isEmpty(), "Response body should contain error information");

        // Check for common error message indicators
        boolean hasErrorMessage = responseBody.toLowerCase().contains("not found") ||
                                  responseBody.toLowerCase().contains("error") ||
                                  responseBody.toLowerCase().contains("category");
        assertTrue(hasErrorMessage,
            "Response should contain error/not found indicator, got: " + responseBody);
    }

    @Test
    @DisplayName("GET non-existent plant by ID should return 404 Not Found")
    public void testGetNonExistentPlantReturns404() {
        // Attempt to retrieve a non-existent plant (ID: 999999)
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/plants/999999")
            .then()
            .extract()
            .response();

        // Verify response status is 404 Not Found
        assertEquals(404, lastResponse.getStatusCode(),
            "GET request for non-existent plant should return 404 Not Found, got: " + lastResponse.getStatusCode());
    }

    @Test
    @DisplayName("Multiple non-existent resource requests should all return 404")
    public void testMultipleNonExistentResourcesReturn404() {
        // Test non-existent category
        Response categoryResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/categories/999999")
            .then()
            .extract()
            .response();

        assertEquals(404, categoryResponse.getStatusCode(),
            "Non-existent category should return 404");

        // Test non-existent plant
        Response plantResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/plants/999999")
            .then()
            .extract()
            .response();

        assertEquals(404, plantResponse.getStatusCode(),
            "Non-existent plant should return 404");

        // Test non-existent sale
        Response saleResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/sales/999999")
            .then()
            .extract()
            .response();

        assertEquals(404, saleResponse.getStatusCode(),
            "Non-existent sale should return 404");
    }

    @Test
    @DisplayName("Invalid category ID format should return 404 or 400")
    public void testInvalidCategoryIdFormatReturns4xx() {
        // Attempt to retrieve category with invalid ID format
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/categories/invalid-id")
            .then()
            .extract()
            .response();

        int statusCode = lastResponse.getStatusCode();
        assertTrue(statusCode == 404 || statusCode == 400,
            "Invalid category ID should return 400 Bad Request or 404 Not Found, got: " + statusCode);
    }
}
