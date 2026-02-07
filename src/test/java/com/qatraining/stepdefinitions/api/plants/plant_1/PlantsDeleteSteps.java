package com.qatraining.stepdefinitions.api.plants.plant_1;

import com.qatraining.api.Plants.plant_1.PlantsPageAPI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
// ...existing code...

import static org.junit.jupiter.api.Assertions.*;

public class PlantsDeleteSteps {
    private final PlantsPageAPI plantsPageAPI = new PlantsPageAPI();
    private int plantIdToDelete;
    private int deleteStatus;
    private int userAttemptPlantId;
    private int userDeleteStatus;
    private org.json.JSONObject originalPlantData;

    @Given("An existing plant is available for deletion")
    public void ensurePlantExists() {
        // Use a plant from the list (not hardcoded if possible)
        plantsPageAPI.getPlantsList();
        String response = plantsPageAPI.getResponseBody();
        // Pick the first plant in the list
        org.json.JSONArray arr = new org.json.JSONArray(response);
        assertTrue(arr.length() > 0, "No plants available to delete");
        plantIdToDelete = arr.getJSONObject(0).getInt("id");
    }

    @When("Admin sends DELETE request to delete the plant")
    public void adminDeletesPlant() {
        plantsPageAPI.deletePlant(plantIdToDelete);
        deleteStatus = plantsPageAPI.getStatusCode();
    }

    @Then("API should return 200 or 204 for plant deletion")
    public void verifyDeleteStatus() {
        assertTrue(deleteStatus == 200 || deleteStatus == 204, "Expected 200 or 204 but got " + deleteStatus);
    }

    @Then("Plant should not be found in the system")
    public void verifyPlantNotFound() {
        plantsPageAPI.getPlantById(plantIdToDelete);
        int status = plantsPageAPI.getStatusCode();
        assertEquals(404, status, "Expected 404 Not Found after deletion, but got " + status);
    }

    @Given("A plant exists for user to attempt delete")
    public void userPlantExistsForDelete() {
        com.qatraining.utils.TokenHolder.useUserToken();
        plantsPageAPI.getPlantsList();
        String response = plantsPageAPI.getResponseBody();
        org.json.JSONArray arr = new org.json.JSONArray(response);
        assertTrue(arr.length() > 0, "No plants available for user delete attempt");
        originalPlantData = arr.getJSONObject(0);
        userAttemptPlantId = originalPlantData.getInt("id");
        System.out.println("DEBUG: User attempting to delete plantId: " + userAttemptPlantId);
        System.out.println("DEBUG: Original plant data: " + originalPlantData.toString());
    }

    @When("User sends DELETE request to delete the plant")
    public void userAttemptsDeletePlant() {
        com.qatraining.utils.TokenHolder.useUserToken();
        plantsPageAPI.deletePlant(userAttemptPlantId);
        userDeleteStatus = plantsPageAPI.getStatusCode();
        System.out.println("DEBUG: User delete attempt status: " + userDeleteStatus);
        System.out.println("DEBUG: User delete response: " + plantsPageAPI.getResponseBody());
    }

    @Then("API should return 403 or 401 for delete attempt")
    public void verifyDeleteForbiddenStatus() {
        assertTrue(userDeleteStatus == 403 || userDeleteStatus == 401, 
            "Expected 403 Forbidden or 401 Unauthorized but got " + userDeleteStatus);
    }

    @Then("Plant should still exist after failed delete attempt")
    public void verifyPlantStillExists() {
        com.qatraining.utils.TokenHolder.useUserToken();
        plantsPageAPI.getPlantById(userAttemptPlantId);
        int status = plantsPageAPI.getStatusCode();
        assertEquals(200, status, "Expected 200 OK when fetching plant after failed delete, but got " + status);
        
        org.json.JSONObject currentPlantData = new org.json.JSONObject(plantsPageAPI.getResponseBody());
        assertEquals(originalPlantData.getInt("id"), currentPlantData.getInt("id"), "Plant ID changed unexpectedly");
        assertEquals(originalPlantData.getString("name"), currentPlantData.getString("name"), 
            "Plant was deleted despite unauthorized delete attempt");
        System.out.println("DEBUG: Plant verification passed - still exists after failed delete");
    }
}
