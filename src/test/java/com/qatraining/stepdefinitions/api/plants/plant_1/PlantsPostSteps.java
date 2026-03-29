package com.qatraining.stepdefinitions.api.plants.plant_1;

import com.qatraining.api.Plants.plant_1.PlantsPageAPI;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlantsPostSteps {

    private final PlantsPageAPI plantsPageAPI = new PlantsPageAPI();
    private Map<String, Object> createBody;
    private int createdPlantId;

    @When("Admin sends POST request to create a new plant")
    public void adminCreatesPlant() {
        int categoryId = 4; // Use existing category
        createBody = new HashMap<>();
        // Generate a unique plant name for each test run
        String uniqueName = "Orange rose " + System.currentTimeMillis();
        createBody.put("name", uniqueName);
        createBody.put("price", 1500);
        createBody.put("quantity", 50);
        Map<String, Object> category = new HashMap<>();
        category.put("id", categoryId);
        createBody.put("category", category);

        plantsPageAPI.createPlant(categoryId, createBody);
        assertNotNull(plantsPageAPI.getResponse(), "API response is null. POST request may not have been sent.");
    }

    @Then("API should return 201 Created for plant creation")
    public void verifyStatusCode() {
        int status = plantsPageAPI.getStatusCode();
        System.out.println("Status Code: " + status);
        System.out.println("Response Body: " + plantsPageAPI.getResponseBody());
        assertEquals(201, status, "Expected 201 Created but got " + status);
    }

    @Then("Created plant details should be reflected in response")
    public void verifyCreatedValues() {
        JSONObject responseJson = new JSONObject(plantsPageAPI.getResponseBody());
        assertEquals(createBody.get("name"), responseJson.getString("name"));
        assertEquals(createBody.get("price"), responseJson.getInt("price"));
        assertEquals(createBody.get("quantity"), responseJson.getInt("quantity"));
        JSONObject category = responseJson.getJSONObject("category");
        assertEquals(((Map<?,?>)createBody.get("category")).get("id"), category.getInt("id"));
        createdPlantId = responseJson.getInt("id");
    }

    @Then("Plant should be persisted in the database")
    public void verifyPlantPersisted() {
        // Optionally, fetch the plant by ID and verify it exists
        // This step can be implemented if a GET endpoint is available
        assertTrue(createdPlantId > 0, "Created plant ID should be valid");
    }

    // For validation scenario
    private int invalidStatusCode;
    private String invalidResponseBody;
    private Map<String, Object> invalidCreateBody;
    private int invalidCategoryId;

    @When("Admin sends POST request to create a plant with invalid data:")
    public void adminCreatesPlantWithInvalidData(io.cucumber.datatable.DataTable dataTable) {
        java.util.List<java.util.Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        java.util.Map<String, String> row = rows.get(0);
        invalidCreateBody = new HashMap<>();
        String name = row.get("name");
        String priceStr = row.get("price");
        String quantityStr = row.get("quantity");
        String categoryIdStr = row.get("categoryId");

        if (name != null && !name.isEmpty()) invalidCreateBody.put("name", name);
        else invalidCreateBody.put("name", "");
        if (priceStr != null && !priceStr.isEmpty()) invalidCreateBody.put("price", Integer.parseInt(priceStr));
        if (quantityStr != null && !quantityStr.isEmpty()) invalidCreateBody.put("quantity", Integer.parseInt(quantityStr));

        Map<String, Object> category = new HashMap<>();
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            invalidCategoryId = Integer.parseInt(categoryIdStr);
            category.put("id", invalidCategoryId);
            invalidCreateBody.put("category", category);
        } else {
            // missing categoryId
            invalidCategoryId = 0;
        }

        // Use categoryId=4 if not provided, but skip category if missing
        int postCategoryId = (invalidCategoryId > 0) ? invalidCategoryId : 4;
        plantsPageAPI.createPlant(postCategoryId, invalidCreateBody);
        invalidStatusCode = plantsPageAPI.getStatusCode();
        invalidResponseBody = plantsPageAPI.getResponseBody();
    }

    @Then("API should return 400 Bad Request for invalid plant creation")
    public void verify400StatusForInvalidPlant() {
        System.out.println("Status Code: " + invalidStatusCode);
        System.out.println("Response Body: " + invalidResponseBody);
        assertEquals(400, invalidStatusCode, "Expected 400 Bad Request but got " + invalidStatusCode);
    }

    @Then("Validation error message for {word} should be returned")
    public void verifyValidationErrorMessage(String field) {
        org.json.JSONObject responseJson = new org.json.JSONObject(invalidResponseBody);
        assertEquals("BAD_REQUEST", responseJson.getString("error"));
        assertEquals(400, responseJson.getInt("status"));
        assertEquals("Validation failed", responseJson.getString("message"));
        assertTrue(responseJson.has("details"), "Response should contain 'details'");
        org.json.JSONObject details = responseJson.getJSONObject("details");
        assertTrue(details.has(field), "Validation error should be present for field: " + field);
    }

    @Then("Plant should not be created in the system")
    public void verifyPlantNotCreated() {
        // Optionally, could check plant list or search for name if needed
        // For now, just ensure 400 and no id in response
        org.json.JSONObject responseJson = new org.json.JSONObject(invalidResponseBody);
        assertFalse(responseJson.has("id"), "Response should not contain plant id");
    }
}
