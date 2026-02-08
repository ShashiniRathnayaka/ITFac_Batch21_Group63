package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantGetByCategoryAPI;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

public class PlantGetByCategorySteps {

    private final PlantGetByCategoryAPI api = new PlantGetByCategoryAPI();
    private int lastStatus;
    private String lastBody;

    @When("a regular user requests GET to \\/api\\/plants\\/category\\/{int}")
    public void userRequestsGetByCategory(int categoryId) {
        api.getPlantsByCategory(categoryId);
        lastStatus = api.getStatusCode();
        lastBody = api.getResponseBody();
        System.out.println("DEBUG: Status=" + lastStatus + " Body=" + lastBody);
    }

    @Then("get-by-category response status should be {int}")
    public void getByCategoryResponseStatus(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("the response should contain plants for category id {int}")
    public void responseShouldContainPlantsForCategory(int categoryId) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONArray arr = new JSONArray(lastBody);
        Assertions.assertTrue(arr.length() > 0, "Expected at least one plant in response");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject plant = arr.getJSONObject(i);
            Assertions.assertTrue(plant.has("category"), "Plant entry missing 'category' object: " + plant.toString());
            JSONObject category = plant.getJSONObject("category");
            Assertions.assertEquals(categoryId, category.getInt("id"), "Plant category id mismatch for plant: " + plant.toString());
        }
    }
}
