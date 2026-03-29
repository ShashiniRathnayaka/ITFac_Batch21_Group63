package com.qatraining.stepdefinitions.api.dashboard;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;

import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

/**
 * Step definitions for API_DASH_009: Unsupported HTTP Method Handling
 * Tests API response to unsupported HTTP methods
 */
public class UnsupportedHTTPMethodSteps {

    private Response lastResponse;
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String API_BASE_URL = ConfigManager.getInstance().getApiBaseUrl();
    private String currentMethod;
    private String currentEndpoint;

    @When("^the admin sends a PUT request to /api/categories$")
    public void sendPutRequestToCategories() {
        currentMethod = "PUT";
        currentEndpoint = "/api/categories";
        
        lastResponse = given()
            .spec(APIClient.getRequestSpec())
            .body(new HashMap<String, Object>() {{
                put("name", "UpdatedCategory");
                put("id", 1);
            }})
            .when()
            .put(API_BASE_URL + "/categories")
            .then()
            .extract()
            .response();
    }

    @When("^the admin sends a DELETE request to /api/categories$")
    public void sendDeleteRequestToCategories() {
        currentMethod = "DELETE";
        currentEndpoint = "/api/categories";
        
        lastResponse = given()
            .spec(APIClient.getRequestSpec())
            .when()
            .delete(API_BASE_URL + "/categories")
            .then()
            .extract()
            .response();
    }

    @When("^the admin sends a PATCH request to /api/plants$")
    public void sendPatchRequestToPlants() {
        currentMethod = "PATCH";
        currentEndpoint = "/api/plants";
        
        lastResponse = given()
            .spec(APIClient.getRequestSpec())
            .body(new HashMap<String, Object>() {{
                put("quantity", 50);
            }})
            .when()
            .patch(API_BASE_URL + "/plants")
            .then()
            .extract()
            .response();
    }

    @When("^the admin sends an OPTIONS request to /api/sales$")
    public void sendOptionsRequestToSales() {
        currentMethod = "OPTIONS";
        currentEndpoint = "/api/sales";
        
        lastResponse = given()
            .spec(APIClient.getRequestSpec())
            .when()
            .options(API_BASE_URL + "/sales")
            .then()
            .extract()
            .response();
    }

    @Then("the response status should be 405 Method Not Allowed")
    public void verifyMethodNotAllowedResponse() {
        int statusCode = lastResponse.getStatusCode();
        Assertions.assertEquals(405, statusCode,
            currentMethod + " request to " + currentEndpoint + " should return 405, but got: " + statusCode);
    }

    @Then("^the response status should indicate method handling \\(200 or 405\\)$")
    public void verifyMethodHandlingResponse() {
        int statusCode = lastResponse.getStatusCode();
        Assertions.assertTrue(statusCode == 405 || statusCode == 200,
            currentMethod + " request to " + currentEndpoint + " should return 200 or 405, but got: " + statusCode);
    }
}
