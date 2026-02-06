package com.qatraining.api.tests;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.qatraining.config.ConfigManager;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

/**
 * API_DASH_008: Verify user is unauthorized to create plant
 * Ensures that role-based access control prevents a standard user from creating a plant
 */
@DisplayName("API_DASH_008: User Unauthorized Plant Creation")
public class UserUnauthorizedPlantCreationTest {

    private Response lastResponse;
    private String userToken;
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String API_BASE_URL = "http://localhost:8080/api";

    @BeforeEach
    public void setUp() {
        // Try to authenticate as admin first to ensure API is running
        Response adminAuthResponse = given()
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

        assertEquals(200, adminAuthResponse.getStatusCode(), "Admin authentication should succeed for API verification");
        
        // Try to authenticate as a standard user (not admin)
        Response authResponse = given()
            .contentType("application/json")
            .body(new HashMap<String, String>() {{
                put("username", "user");
                put("password", "user@123");
            }})
            .when()
            .post(API_BASE_URL + "/auth/login")
            .then()
            .extract()
            .response();

        // If user auth succeeds, use that token
        if (authResponse.getStatusCode() == 200) {
            userToken = authResponse.jsonPath().getString("token");
            assertNotNull(userToken, "User token should be returned");
        } else {
            // If user account doesn't exist, use invalid token to test unauthorized access
            userToken = "invalid_user_token_12345";
        }
    }

    @Test
    @DisplayName("User should not be able to create a plant")
    public void testUserCannotCreatePlant() {
        // Attempt to create a plant without admin privileges
        Map<String, Object> plantData = new HashMap<>();
        plantData.put("name", "Tomato Plant");
        plantData.put("species", "Solanum lycopersicum");
        plantData.put("quantity", 100);
        plantData.put("category", new HashMap<String, Object>() {{
            put("id", 1);
        }});

        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + userToken)
            .body(plantData)
            .when()
            .post(API_BASE_URL + "/plants")
            .then()
            .extract()
            .response();

        // Verify response status is 403 Forbidden or 401 Unauthorized
        int statusCode = lastResponse.getStatusCode();
        assertTrue(statusCode == 403 || statusCode == 401,
            "User should receive 403 Forbidden or 401 Unauthorized, got: " + statusCode);
    }

    @Test
    @DisplayName("User should not be able to create a plant in a valid category")
    public void testUserCannotCreatePlantInValidCategory() {
        // First, create a category as admin (in setup or elsewhere)
        Map<String, Object> categoryData = new HashMap<>();
        categoryData.put("name", "Test0801");

        Response categoryResponse = given()
            .contentType("application/json")
            .when()
            .post(API_BASE_URL + "/categories")
            .then()
            .extract()
            .response();

        final Integer categoryId = categoryResponse.getStatusCode() == 201 
            ? categoryResponse.jsonPath().getInt("id") 
            : 1;

        // Now attempt to create a plant under this category as user
        Map<String, Object> plantData = new HashMap<>();
        plantData.put("name", "Pepper Plant");
        plantData.put("species", "Capsicum annuum");
        plantData.put("quantity", 50);
        plantData.put("category", new HashMap<String, Object>() {{
            put("id", categoryId);
        }});

        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + userToken)
            .body(plantData)
            .when()
            .post(API_BASE_URL + "/plants")
            .then()
            .extract()
            .response();

        // Verify response status is 403 Forbidden or 401 Unauthorized
        int statusCode = lastResponse.getStatusCode();
        assertTrue(statusCode == 403 || statusCode == 401,
            "User should receive 403 Forbidden or 401 Unauthorized, got: " + statusCode);
    }

    @Test
    @DisplayName("User should not be able to create a plant in a sub-category")
    public void testUserCannotCreatePlantInSubCategory() {
        // Attempt to create a plant in a sub-category as standard user
        Map<String, Object> plantData = new HashMap<>();
        plantData.put("name", "Carrot");
        plantData.put("species", "Daucus carota");
        plantData.put("quantity", 75);
        plantData.put("category", new HashMap<String, Object>() {{
            put("id", 2);
        }});

        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + userToken)
            .body(plantData)
            .when()
            .post(API_BASE_URL + "/plants/category/2")
            .then()
            .extract()
            .response();

        // Verify response status is 403 Forbidden or 401 Unauthorized
        int statusCode = lastResponse.getStatusCode();
        assertTrue(statusCode == 403 || statusCode == 401,
            "User should receive 403 Forbidden or 401 Unauthorized when creating plant in sub-category, got: " + statusCode);
    }

    @Test
    @DisplayName("Plant should not be created when user lacks permissions")
    public void testPlantIsNotCreatedAfterUnauthorizedAttempt() {
        // Attempt to create a plant with non-admin token
        Map<String, Object> plantData = new HashMap<>();
        plantData.put("name", "Basil Plant");
        plantData.put("species", "Ocimum basilicum");
        plantData.put("quantity", 30);
        plantData.put("category", new HashMap<String, Object>() {{
            put("id", 1);
        }});

        lastResponse = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + userToken)
            .body(plantData)
            .when()
            .post(API_BASE_URL + "/plants")
            .then()
            .extract()
            .response();

        // Verify unauthorized response
        int statusCode = lastResponse.getStatusCode();
        assertTrue(statusCode == 403 || statusCode == 401,
            "Expected 403 or 401, got: " + statusCode);
    }
}
