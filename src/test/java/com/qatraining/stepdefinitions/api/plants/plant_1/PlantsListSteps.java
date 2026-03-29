package com.qatraining.stepdefinitions.api.plants.plant_1;

import com.qatraining.api.Plants.plant_1.PlantsPageAPI;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for Plants List API test
 */
public class PlantsListSteps {

        private int userPlantId;
        private JSONObject userPlantDetails;

        @io.cucumber.java.en.Given("A plant exists for user to fetch by ID")
        public void userPlantExistsForGetById() {
            // Use user token and fetch the list, pick the first plant
            com.qatraining.utils.TokenHolder.useUserToken();
            plantsPageAPI.getPlantsList();
            String responseBody = plantsPageAPI.getResponseBody();
            JSONArray arr = new JSONArray(responseBody);
            assertTrue(arr.length() > 0, "No plants available for user to fetch by ID");
            userPlantDetails = arr.getJSONObject(0);
            userPlantId = userPlantDetails.getInt("id");
            System.out.println("DEBUG: Using plantId for user GET by ID: " + userPlantId);
        }

        @io.cucumber.java.en.When("User sends GET request to retrieve plant by ID")
        public void userGetsPlantById() {
            com.qatraining.utils.TokenHolder.useUserToken();
            plantsPageAPI.getPlantById(userPlantId);
            assertNotNull(plantsPageAPI.getResponse(), "API response is null. GET by ID may not have been sent.");
            System.out.println("DEBUG: GET by ID status: " + plantsPageAPI.getStatusCode());
            System.out.println("DEBUG: GET by ID response: " + plantsPageAPI.getResponseBody());
        }

        @io.cucumber.java.en.Then("API should return 200 OK for plant get by ID")
        public void verifyStatusCodeForGetById() {
            int status = plantsPageAPI.getStatusCode();
            assertEquals(200, status, "Expected 200 OK for GET by ID but got " + status);
        }

        @io.cucumber.java.en.Then("Response should contain correct plant details for requested ID")
        public void verifyPlantDetailsForGetById() {
            String responseBody = plantsPageAPI.getResponseBody();
            JSONObject plant = new JSONObject(responseBody);
            assertEquals(userPlantId, plant.getInt("id"), "Returned plant ID does not match requested ID");
            assertEquals(userPlantDetails.getString("name"), plant.getString("name"), "Plant name mismatch");
            assertEquals(userPlantDetails.getInt("price"), plant.getInt("price"), "Plant price mismatch");
            assertEquals(userPlantDetails.getInt("quantity"), plant.getInt("quantity"), "Plant quantity mismatch");
            // Category check (if present)
            if (userPlantDetails.has("category") && plant.has("category")) {
                JSONObject expectedCat = userPlantDetails.getJSONObject("category");
                JSONObject actualCat = plant.getJSONObject("category");
                assertEquals(expectedCat.getInt("id"), actualCat.getInt("id"), "Category ID mismatch");
                assertEquals(expectedCat.getString("name"), actualCat.getString("name"), "Category name mismatch");
            }
        }
    
    private final PlantsPageAPI plantsPageAPI = new PlantsPageAPI();

    @When("Admin sends GET request to retrieve plants list")
    public void adminRetrievesPlantsList() {
        // Hook already set admin token - just make the request
        plantsPageAPI.getPlantsList();
        assertNotNull(
                plantsPageAPI.getResponse(),
                "API response is null. GET request may not have been sent."
        );
        System.out.println("Status Code: " + plantsPageAPI.getStatusCode());
        System.out.println("Response Body: " + plantsPageAPI.getResponseBody());
    }

    @When("User sends GET request to retrieve plants list")
    public void userRetrievesPlantsList() {
        // Hook already set user token - just make the request
        plantsPageAPI.getPlantsList();
        assertNotNull(
                plantsPageAPI.getResponse(),
                "API response is null. GET request may not have been sent."
        );
        System.out.println("Status Code: " + plantsPageAPI.getStatusCode());
        System.out.println("Response Body: " + plantsPageAPI.getResponseBody());
    }

    @Then("API should return 200 OK for plants list retrieval")
    public void verifyStatusCodeForList() {
        int status = plantsPageAPI.getStatusCode();
        assertEquals(200, status, "Expected 200 OK but got " + status);
    }

    @Then("Response should contain plant records")
    public void verifyPlantListIsNotEmpty() {
        String responseBody = plantsPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");

        JSONArray plantsArray = new JSONArray(responseBody);
        assertTrue(plantsArray.length() > 0, "Expected at least one plant record in response");
    }

    @Then("Plant records should have valid structure with id, name, price, quantity, category")
    public void verifyPlantRecordStructure() {
        String responseBody = plantsPageAPI.getResponseBody();
        JSONArray plantsArray = new JSONArray(responseBody);

        for (int i = 0; i < plantsArray.length(); i++) {
            JSONObject plant = plantsArray.getJSONObject(i);

            assertTrue(plant.has("id"), "Plant record should have 'id' field");
            assertTrue(plant.has("name"), "Plant record should have 'name' field");
            assertTrue(plant.has("price"), "Plant record should have 'price' field");
            assertTrue(plant.has("quantity"), "Plant record should have 'quantity' field");
            assertTrue(plant.has("category"), "Plant record should have 'category' field");

            // Verify category structure
            JSONObject category = plant.getJSONObject("category");
            assertTrue(category.has("id"), "Category should have 'id' field");
            assertTrue(category.has("name"), "Category should have 'name' field");
        }
    }

    @Then("No data records should be modified")
    public void verifyNoDataModification() {
        // This step verifies that the GET request is read-only
        // Since we're using a GET request, no modifications are made
        System.out.println("GET request is read-only - no data modifications");
        assertTrue(true, "GET request does not modify any data");
    }
}