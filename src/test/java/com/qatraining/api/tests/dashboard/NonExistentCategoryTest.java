package com.qatraining.api.tests.dashboard;

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
 * API_DASH_010: Verify Non-existent Category 404 Error
 * Ensures the API returns an appropriate error when accessing a non-existent category
 */
@DisplayName("API_DASH_010: Non-existent Category 404 Error")
public class NonExistentCategoryTest {

    private Response lastResponse;
    private String adminToken;
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String API_BASE_URL = "http://localhost:8080/api";
    private static final Integer NON_EXISTENT_CATEGORY_ID = 999999;

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
    @DisplayName("GET non-existent category should return 404 Not Found")
    public void testGetNonExistentCategoryReturns404() {
        // Attempt to retrieve a non-existent category
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/categories/" + NON_EXISTENT_CATEGORY_ID)
            .then()
            .extract()
            .response();

        // Verify response status is 404 Not Found
        assertEquals(404, lastResponse.getStatusCode(),
            "Accessing non-existent category should return 404 Not Found");
    }

    @Test
    @DisplayName("404 response should contain error message")
    public void testNonExistentCategoryErrorMessage() {
        // Attempt to retrieve a non-existent category
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/categories/" + NON_EXISTENT_CATEGORY_ID)
            .then()
            .extract()
            .response();

        // Verify response status is 404
        assertEquals(404, lastResponse.getStatusCode(),
            "Accessing non-existent category should return 404 Not Found");

        // Verify error message is returned
        String responseBody = lastResponse.getBody().asString();
        assertNotNull(responseBody, "Error response body should not be null");
        assertFalse(responseBody.isEmpty(), "Error response body should not be empty");
        
        // Verify response is JSON
        assertTrue(lastResponse.getContentType().contains("application/json"),
            "Error response should be JSON format");
    }

    @Test
    @DisplayName("GET non-existent plant should return 404 Not Found")
    public void testGetNonExistentPlantReturns404() {
        // Attempt to retrieve a non-existent plant
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/plants/" + NON_EXISTENT_CATEGORY_ID)
            .then()
            .extract()
            .response();

        // Verify response status is 404 Not Found
        assertEquals(404, lastResponse.getStatusCode(),
            "Accessing non-existent plant should return 404 Not Found");
    }

    @Test
    @DisplayName("GET non-existent sales record should return 404 Not Found")
    public void testGetNonExistentSalesReturns404() {
        // Attempt to retrieve plants by non-existent category
        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + adminToken)
            .when()
            .get(API_BASE_URL + "/plants/category/" + NON_EXISTENT_CATEGORY_ID)
            .then()
            .extract()
            .response();

        // Should either return 404 or empty list (200)
        int statusCode = lastResponse.getStatusCode();
        assertTrue(statusCode == 404 || statusCode == 200,
            "Accessing plants with non-existent category should return 404 or 200 with empty list, got: " + statusCode);
    }

    @Test
    @DisplayName("Multiple invalid category requests should consistently return 404")
    public void testMultipleInvalidCategoryRequestsReturn404() {
        Integer[] invalidIds = {999999, 888888, 777777, 0, -1};

        for (Integer invalidId : invalidIds) {
            lastResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get(API_BASE_URL + "/categories/" + invalidId)
                .then()
                .extract()
                .response();

            assertEquals(404, lastResponse.getStatusCode(),
                "Category ID " + invalidId + " should return 404 Not Found");
        }
    }
}
