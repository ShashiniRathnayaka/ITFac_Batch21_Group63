package com.qatraining.stepdefinitions.api.plants.plant_1;

import com.qatraining.api.Plants.plant_1.PlantsPageAPI;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlantUpdateSteps {

    private final PlantsPageAPI plantsPageAPI = new PlantsPageAPI();
    private int testPlantId;
    private Map<String, Object> updateBody;
    private int userAttemptPlantId;
    private Map<String, Object> userUpdateBody;
    private JSONObject originalPlantData;

    @When("Admin sends PUT request to update plant details")
    public void adminUpdatesPlant() {
        // Ensure a plant exists and pick its id
        plantsPageAPI.getPlantsList();
        String response = plantsPageAPI.getResponseBody();
        org.json.JSONArray arr = new org.json.JSONArray(response);
        assertTrue(arr.length() > 0, "No plants available to update");
        testPlantId = arr.getJSONObject(0).getInt("id");

        updateBody = new HashMap<>();
        updateBody.put("id", testPlantId);
        updateBody.put("name", "tulip123");
        updateBody.put("price", 9);
        updateBody.put("quantity", 10);
        updateBody.put("categoryId", 4);

        plantsPageAPI.updatePlant(testPlantId, updateBody);

        assertNotNull(
                plantsPageAPI.getResponse(),
                "API response is null. PUT request may not have been sent.");
    }

    @Then("API should return 200 OK for plant update")
    public void verifyStatusCode() {
        int status = plantsPageAPI.getStatusCode();
        System.out.println("Status Code: " + status);
        System.out.println("Response Body: " + plantsPageAPI.getResponseBody());
        assertEquals(200, status, "Expected 200 OK but got " + status);
    }

    @Then("Updated plant details should be reflected in response")
    public void verifyUpdatedValues() {
        JSONObject responseJson = new JSONObject(plantsPageAPI.getResponseBody());
        assertEquals(updateBody.get("name"), responseJson.getString("name"));
        assertEquals(updateBody.get("price"), responseJson.getInt("price"));
        assertEquals(updateBody.get("quantity"), responseJson.getInt("quantity"));
        JSONObject category = responseJson.getJSONObject("category");
        assertEquals(updateBody.get("categoryId"), category.getInt("id"));
    }

    @io.cucumber.java.en.Given("A plant exists for user to attempt update")
    public void userPlantExistsForUpdate() {
        // Fetch user token and get plants list
        com.qatraining.utils.TokenHolder.useUserToken();
        plantsPageAPI.getPlantsList();
        String response = plantsPageAPI.getResponseBody();
        org.json.JSONArray arr = new org.json.JSONArray(response);
        assertTrue(arr.length() > 0, "No plants available for user update attempt");
        originalPlantData = arr.getJSONObject(0);
        userAttemptPlantId = originalPlantData.getInt("id");
        System.out.println("DEBUG: User attempting to update plantId: " + userAttemptPlantId);
        System.out.println("DEBUG: Original plant data: " + originalPlantData.toString());
    }

    @io.cucumber.java.en.When("User sends PUT request to update plant details")
    public void userAttemptsUpdatePlant() {
        com.qatraining.utils.TokenHolder.useUserToken();
        userUpdateBody = new HashMap<>();
        userUpdateBody.put("id", userAttemptPlantId);
        userUpdateBody.put("name", "UnauthorizedUpdate");
        userUpdateBody.put("price", 9999);
        userUpdateBody.put("quantity", 999);
        userUpdateBody.put("categoryId", 4);

        plantsPageAPI.updatePlant(userAttemptPlantId, userUpdateBody);
        assertNotNull(plantsPageAPI.getResponse(), "API response is null. PUT request may not have been sent.");
        System.out.println("DEBUG: User update attempt status: " + plantsPageAPI.getStatusCode());
    }

    @Then("API should return 403 Forbidden for plant update")
    public void verifyForbiddenStatus() {
        int status = plantsPageAPI.getStatusCode();
        System.out.println("DEBUG: Update forbidden response code: " + status);
        System.out.println("DEBUG: Update forbidden response body: " + plantsPageAPI.getResponseBody());
        assertTrue(status == 403 || status == 401, "Expected 403 Forbidden or 401 Unauthorized but got " + status);
    }

    @Then("Plant data should not be modified after failed update attempt")
    public void verifyPlantNotModified() {
        com.qatraining.utils.TokenHolder.useUserToken();
        plantsPageAPI.getPlantById(userAttemptPlantId);
        JSONObject currentPlantData = new JSONObject(plantsPageAPI.getResponseBody());
        
        // Verify original name and price were not changed
        assertEquals(originalPlantData.getString("name"), currentPlantData.getString("name"), 
            "Plant name was modified despite unauthorized update attempt");
        assertEquals(originalPlantData.getInt("price"), currentPlantData.getInt("price"), 
            "Plant price was modified despite unauthorized update attempt");
        System.out.println("DEBUG: Plant data verification passed - not modified");
    }
    @io.cucumber.java.en.Given("An admin is authenticated and a valid plant exists")
    public void adminAuthenticatedAndPlantExists() {
        com.qatraining.utils.TokenHolder.useAdminToken();
        plantsPageAPI.getPlantsList();
        String response = plantsPageAPI.getResponseBody();
        org.json.JSONArray arr = new org.json.JSONArray(response);
        assertTrue(arr.length() > 0, "No plants available to update");
        testPlantId = arr.getJSONObject(0).getInt("id");
        originalPlantData = arr.getJSONObject(0);
    }

    @io.cucumber.java.en.When("Admin sends PUT request to update plant category name with existing sub category name")
    public void adminUpdatesPlantCategoryName() {
        // Use existing plant details, only change category name
        Map<String, Object> category = new HashMap<>();
        category.put("id", originalPlantData.getJSONObject("category").getInt("id"));
        category.put("name", "komarika"); // Use a valid sub-category name if possible

        updateBody = new HashMap<>();
        updateBody.put("id", testPlantId);
        updateBody.put("name", originalPlantData.getString("name"));
        updateBody.put("price", originalPlantData.getInt("price"));
        updateBody.put("quantity", originalPlantData.getInt("quantity"));
        updateBody.put("category", category);

        plantsPageAPI.updatePlant(testPlantId, updateBody);
        assertNotNull(plantsPageAPI.getResponse(), "API response is null. PUT request may not have been sent.");
    }

    @io.cucumber.java.en.Then("API should return 200 OK for plant category update")
    public void verifyCategoryUpdateStatusCode() {
        int status = plantsPageAPI.getStatusCode();
        System.out.println("Status Code: " + status);
        System.out.println("Response Body: " + plantsPageAPI.getResponseBody());
        assertEquals(200, status, "Expected 200 OK but got " + status);
    }

    @io.cucumber.java.en.Then("Updated plant category name should be reflected in response")
    public void verifyUpdatedCategoryName() {
        JSONObject responseJson = new JSONObject(plantsPageAPI.getResponseBody());
        JSONObject category = responseJson.getJSONObject("category");
        assertEquals("komarika", category.getString("name"), "Category name was not updated as expected");
    }
}