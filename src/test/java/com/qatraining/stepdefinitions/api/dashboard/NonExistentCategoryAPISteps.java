package com.qatraining.stepdefinitions.api.dashboard;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import io.cucumber.java.en.*;
import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

/**
 * Step definitions for API_DASH_010: Non-existent Category 404 Error
 * Tests API error handling for non-existent resource requests
 */
public class NonExistentCategoryAPISteps {

    private Response lastResponse;
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String API_BASE_URL = "http://localhost:8080/api";
    private static final Integer NON_EXISTENT_CATEGORY_ID = 999999;
    private Integer[] invalidIds = {999999, 888888, 777777};
    private int validResponseCount = 0;

    @When("the admin requests a non-existent category")
    public void adminRequestsNonExistentCategory() {
        lastResponse = given()
            .contentType("application/json")
            .spec(APIClient.getRequestSpec())
            .when()
            .get(API_BASE_URL + "/categories/" + NON_EXISTENT_CATEGORY_ID)
            .then()
            .extract()
            .response();
    }

    @When("the admin requests a non-existent plant")
    public void adminRequestsNonExistentPlant() {
        lastResponse = given()
            .contentType("application/json")
            .spec(APIClient.getRequestSpec())
            .when()
            .get(API_BASE_URL + "/plants/" + NON_EXISTENT_CATEGORY_ID)
            .then()
            .extract()
            .response();
    }

    @When("the admin requests multiple non-existent categories")
    public void adminRequestsMultipleNonExistentCategories() {
        validResponseCount = 0;
        for (Integer invalidId : invalidIds) {
            lastResponse = given()
                .contentType("application/json")
                .spec(APIClient.getRequestSpec())
                .when()
                .get(API_BASE_URL + "/categories/" + invalidId)
                .then()
                .extract()
                .response();

            if (lastResponse.getStatusCode() == 404) {
                validResponseCount++;
            }
        }
    }

    @Then("the response status should be 404")
    public void verifyResponseStatus404() {
        Assertions.assertEquals(404, lastResponse.getStatusCode(),
            "Expected 404 Not Found status code");
    }

    @Then("the error message should be returned")
    public void verifyErrorMessageReturned() {
        // Verify response contains content
        String responseBody = lastResponse.getBody().asString();
        Assertions.assertNotNull(responseBody, "Error response body should not be null");
        Assertions.assertFalse(responseBody.isEmpty(), "Error response body should not be empty");

        // Verify response is JSON
        Assertions.assertTrue(lastResponse.getContentType().contains("application/json"),
            "Error response should be in JSON format");
    }

    @Then("all responses should return 404 status")
    public void verifyAllResponsesAre404() {
        Assertions.assertEquals(invalidIds.length, validResponseCount,
            "All non-existent category requests should return 404");
    }
}
