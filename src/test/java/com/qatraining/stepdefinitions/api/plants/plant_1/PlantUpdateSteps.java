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
    private final int testPlantId = 1;

    private Map<String, Object> updateBody;

    @When("Admin sends PUT request to update plant details")
    public void adminUpdatesPlant() {

        updateBody = new HashMap<>();
        updateBody.put("id", testPlantId);
        updateBody.put("name", "tulip123");
        updateBody.put("price", 9);
        updateBody.put("quantity", 10);
        updateBody.put("categoryId", 2);

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
}
