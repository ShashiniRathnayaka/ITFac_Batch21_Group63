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
 * Tests error handling for non-existent resources
 */
public class NonExistentCategorySteps {

    private Response lastResponse;
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String API_BASE_URL = ConfigManager.getInstance().getApiBaseUrl();

    @When("the admin requests a non-existent category with ID {int}")
    public void adminRequestsNonExistentCategory(int categoryId) {
        lastResponse = given()
            .contentType("application/json")
            .spec(APIClient.getRequestSpec())
            .when()
            .get("/categories/" + categoryId)
            .then()
            .extract()
            .response();
    }

    @When("the admin requests a non-existent plant with ID {int}")
    public void adminRequestsNonExistentPlant(int plantId) {
        lastResponse = given()
            .contentType("application/json")
            .spec(APIClient.getRequestSpec())
            .when()
            .get("/plants/" + plantId)
            .then()
            .extract()
            .response();
    }

    @When("the admin requests non-existent resources")
    public void adminRequestsMultipleNonExistentResources() {
        // Request non-existent category
        Response categoryResponse = given()
            .spec(APIClient.getRequestSpec())
            .when()
            .get("/categories/999999")
            .then()
            .extract()
            .response();

        Assertions.assertEquals(404, categoryResponse.getStatusCode(),
            "Non-existent category should return 404");

        // Request non-existent plant
        Response plantResponse = given()
            .spec(APIClient.getRequestSpec())
            .when()
            .get("/plants/999999")
            .then()
            .extract()
            .response();

        Assertions.assertEquals(404, plantResponse.getStatusCode(),
            "Non-existent plant should return 404");

        // Request non-existent sale
        lastResponse = given()
            .spec(APIClient.getRequestSpec())
            .when()
            .get("/sales/999999")
            .then()
            .extract()
            .response();
    }

    @When("the admin requests a category with invalid ID format")
    public void adminRequestsCategoryWithInvalidIdFormat() {
        lastResponse = given()
            .contentType("application/json")
            .spec(APIClient.getRequestSpec())
            .when()
            .get("/categories/invalid-id")
            .then()
            .extract()
            .response();
    }

    @Then("the response status should be 404 Not Found")
    public void verifyStatus404() {
        int statusCode = lastResponse.getStatusCode();
        Assertions.assertEquals(404, statusCode,
            "Expected 404 Not Found, but got: " + statusCode);
    }

    @Then("an error message should be returned")
    public void verifyErrorMessageReturned() {
        String responseBody = lastResponse.getBody().asString();
        Assertions.assertNotNull(responseBody, "Response body should not be null");
        Assertions.assertFalse(responseBody.isEmpty(), "Response body should contain error information");

        // Check for error indicators
        boolean hasErrorMessage = responseBody.toLowerCase().contains("not found") ||
                                  responseBody.toLowerCase().contains("error") ||
                                  responseBody.toLowerCase().contains("category");
        Assertions.assertTrue(hasErrorMessage,
            "Response should contain error/not found indicator, got: " + responseBody);
    }

    @Then("all requests should return 404 Not Found")
    public void verifyAllRequests404() {
        int statusCode = lastResponse.getStatusCode();
        Assertions.assertEquals(404, statusCode,
            "All non-existent resource requests should return 404, but got: " + statusCode);
    }

    @Then("the response status should be 400 or 404")
    public void verifyStatus400Or404() {
        int statusCode = lastResponse.getStatusCode();
        Assertions.assertTrue(statusCode == 400 || statusCode == 404,
            "Expected 400 Bad Request or 404 Not Found, but got: " + statusCode);
    }
}
