package com.qatraining.stepdefinitions.api.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import com.qatraining.api.APIClient;

import io.cucumber.java.en.Given;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

/**
 * Step Definitions for API Authentication
 * Handles user authentication for API tests
 */
public class AuthenticationAPISteps {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String USER_USERNAME = "testuser";
    private static final String USER_PASSWORD = "test123";

    @Given("the admin is authenticated")
    public void authenticateAsAdmin() {
        Map<String, String> loginBody = new HashMap<>();
        loginBody.put("username", ADMIN_USERNAME);
        loginBody.put("password", ADMIN_PASSWORD);

        Response response = given()
                .spec(APIClient.getRequestSpec())
                .body(loginBody)
                .when()
                .post("/auth/login");

        Assertions.assertEquals(200, response.getStatusCode(),
                "Admin authentication should return 200. Response: " + response.getBody().asString());

        String token = response.jsonPath().getString("token");
        Assertions.assertNotNull(token, "Authentication token should not be null");

        APIClient.setAuthToken(token);
    }

    @Given("the user is authenticated")
    public void authenticateAsUser() {
        Map<String, String> loginBody = new HashMap<>();
        loginBody.put("username", USER_USERNAME);
        loginBody.put("password", USER_PASSWORD);

        Response response = given()
                .spec(APIClient.getRequestSpec())
                .body(loginBody)
                .when()
                .post("/auth/login");

        Assertions.assertEquals(200, response.getStatusCode(),
                "User authentication should return 200. Response: " + response.getBody().asString());

        String token = response.jsonPath().getString("token");
        Assertions.assertNotNull(token, "Authentication token should not be null");

        APIClient.setAuthToken(token);
    }
}
