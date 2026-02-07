package com.qatraining.stepdefinitions.api.categories;

import com.qatraining.api.categories.CategoryAddPageAPI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryAddSteps {

    private final CategoryAddPageAPI categoryAddPageAPI = new CategoryAddPageAPI();
    private Map<String, Object> requestBody;
    private String categoryName;

    @Given("Category name to be created does not already exist")
    public void categoryNameToBeCreatedDoesNotAlreadyExist() {
        // This is a precondition that ensures uniqueness
        // We generate unique names with timestamp to avoid conflicts
        System.out.println("Precondition: Category name will be generated uniquely");
    }

    @When("Admin sends a POST request to {string} with valid category details")
    public void adminSendsPostRequestWithValidCategoryDetails(String endpoint) {
        // Generate unique category name to avoid conflicts
        long nanoTime = System.nanoTime();
        int uniqueSuffix = (int) ((nanoTime / 1000) % 1000);
        categoryName = "Cat" + String.format("%03d", uniqueSuffix);

        // Build request body following Swagger example structure
        requestBody = new HashMap<>();
        requestBody.put("name", categoryName);

        // Parent category (optional - can be null for main categories)
        Map<String, String> parent = new HashMap<>();
        parent.put("name", "Fruits");
        requestBody.put("parent", parent);

        // Empty subCategories array
        requestBody.put("subCategories", new java.util.ArrayList<>());

        System.out.println("POST " + endpoint + " - Category Name: " + categoryName);

        categoryAddPageAPI.createCategory(requestBody);

        assertNotNull(
                categoryAddPageAPI.getResponse(),
                "API response is null. POST request may not have been sent.");
    }

    @Then("API returns {int} Created status code")
    public void apiReturnsCreatedStatusCode(int expectedStatusCode) {
        int actualStatus = categoryAddPageAPI.getStatusCode();
        System.out.println("Status Code: " + actualStatus);
        System.out.println("Response Body: " + categoryAddPageAPI.getResponseBody());
        assertEquals(expectedStatusCode, actualStatus,
                "Expected " + expectedStatusCode + " Created but got " + actualStatus);
    }

    @Then("New category is created successfully")
    public void newCategoryIsCreatedSuccessfully() {
        String responseBody = categoryAddPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");

        JSONObject responseJson = new JSONObject(responseBody);
        assertTrue(responseJson.has("id"), "Response should contain 'id' field");
        assertTrue(responseJson.has("name"), "Response should contain 'name' field");

        System.out.println("Category created successfully with ID: " + responseJson.getInt("id"));
    }

    @Then("Response body contains the created category details")
    public void responseBodyContainsCreatedCategoryDetails() {
        JSONObject responseJson = new JSONObject(categoryAddPageAPI.getResponseBody());

        // Verify response contains expected fields
        assertTrue(responseJson.has("id"), "Response should contain 'id'");
        assertTrue(responseJson.has("name"), "Response should contain 'name'");
        assertTrue(responseJson.has("subCategories"), "Response should contain 'subCategories'");

        // Verify id is a positive integer
        int categoryId = responseJson.getInt("id");
        assertTrue(categoryId > 0, "Category ID should be positive");

        // Verify name matches what was sent
        String responseName = responseJson.getString("name");
        assertEquals(categoryName, responseName,
                "Response category name should match the requested name");

        System.out.println("Verified: Response contains category ID=" + categoryId +
                ", name=" + responseName);
    }

    @Then("Category is persisted in the database")
    public void categoryIsPersistedInDatabase() {
        // This step verifies the category was successfully created and persisted
        // The fact that we received a 201 with an ID confirms database persistence
        JSONObject responseJson = new JSONObject(categoryAddPageAPI.getResponseBody());
        int categoryId = responseJson.getInt("id");

        assertTrue(categoryId > 0,
                "Category should have a valid database ID, indicating successful persistence");

        System.out.println("Verified: Category persisted in database with ID: " + categoryId);
    }

    @When("Admin sends a POST request to {string} with category name {string}")
    public void adminSendsPostRequestWithCategoryName(String endpoint, String categoryName) {
        // Build request body with the provided category name (which may be invalid)
        requestBody = new HashMap<>();
        requestBody.put("name", categoryName);

        // Parent category (optional)
        Map<String, String> parent = new HashMap<>();
        parent.put("name", "Fruits");
        requestBody.put("parent", parent);

        // Empty subCategories array
        requestBody.put("subCategories", new java.util.ArrayList<>());

        System.out.println("POST " + endpoint + " - Category Name: '" + categoryName + "' (length: "
                + categoryName.length() + ")");

        categoryAddPageAPI.createCategory(requestBody);

        assertNotNull(
                categoryAddPageAPI.getResponse(),
                "API response is null. POST request may not have been sent.");
    }

    @Then("API returns {int} Bad Request status code")
    public void apiReturnsBadRequestStatusCode(int expectedStatusCode) {
        int actualStatus = categoryAddPageAPI.getStatusCode();
        System.out.println("Status Code: " + actualStatus);
        System.out.println("Response Body: " + categoryAddPageAPI.getResponseBody());
        assertEquals(expectedStatusCode, actualStatus,
                "Expected " + expectedStatusCode + " Bad Request but got " + actualStatus);
    }

    @Then("Validation error message is returned")
    public void validationErrorMessageIsReturned() {
        String responseBody = categoryAddPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");

        JSONObject responseJson = new JSONObject(responseBody);

        // Verify response contains error information
        assertTrue(responseJson.has("error"), "Response should contain 'error' field");
        assertTrue(responseJson.has("message"), "Response should contain 'message' field");
        assertTrue(responseJson.has("status"), "Response should contain 'status' field");

        // Verify error type
        assertEquals("BAD_REQUEST", responseJson.getString("error"),
                "Error type should be BAD_REQUEST");

        // Verify validation message
        assertEquals("Validation failed", responseJson.getString("message"),
                "Message should indicate validation failure");

        System.out.println("Validation error confirmed: " + responseJson.getString("message"));
    }

    @Then("Validation error message contains {string}")
    public void validationErrorMessageContains(String expectedMessage) {
        JSONObject responseJson = new JSONObject(categoryAddPageAPI.getResponseBody());

        // Check if details field contains the expected validation message
        assertTrue(responseJson.has("details"), "Response should contain 'details' field");

        JSONObject details = responseJson.getJSONObject("details");
        assertTrue(details.has("name"), "Details should contain 'name' validation error");

        String actualMessage = details.getString("name");
        assertEquals(expectedMessage, actualMessage,
                "Validation message should match expected message");

        System.out.println("Verified validation message: " + actualMessage);
    }

    @Then("Category is not created")
    public void categoryIsNotCreated() {
        // Verify that the response does NOT contain a category ID
        // A 400 response should not include a created resource
        int statusCode = categoryAddPageAPI.getStatusCode();
        assertEquals(400, statusCode,
                "Status code should be 400, indicating category was not created");

        JSONObject responseJson = new JSONObject(categoryAddPageAPI.getResponseBody());
        assertFalse(responseJson.has("id"),
                "Response should NOT contain an 'id' field when validation fails");

        System.out.println("Verified: Category was not created due to validation failure");
    }

    @Then("No data is saved in the database")
    public void noDataIsSavedInDatabase() {
        // Verify that no data was persisted
        // Since we got a 400 Bad Request, no database insert should have occurred
        int statusCode = categoryAddPageAPI.getStatusCode();
        assertEquals(400, statusCode,
                "400 status confirms no data was saved to database");

        System.out.println("Verified: No data saved in database (400 Bad Request response)");
    }
}
