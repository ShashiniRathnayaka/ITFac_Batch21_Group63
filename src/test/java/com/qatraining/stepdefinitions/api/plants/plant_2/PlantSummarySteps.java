package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantSummaryAPI;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

public class PlantSummarySteps {

    private final PlantSummaryAPI api = new PlantSummaryAPI();
    private int lastStatus;
    private String lastBody;

    @When("a user requests GET to \\/api\\/plants\\/summary")
    public void userRequestsSummary() {
        api.getPlantSummary();
        lastStatus = api.getStatusCode();
        lastBody = api.getResponseBody();
        System.out.println("DEBUG: Status=" + lastStatus + " Body=" + lastBody);
    }

    @Then("summary response status should be {int}")
    public void summaryResponseStatus(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("the response should contain plant summary information")
    public void responseShouldContainPlantSummary() {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        
        Assertions.assertTrue(json.has("totalPlants"), "Response should contain 'totalPlants' field");
        Assertions.assertTrue(json.has("lowStockPlants"), "Response should contain 'lowStockPlants' field");
        
        int totalPlants = json.getInt("totalPlants");
        int lowStockPlants = json.getInt("lowStockPlants");
        
        Assertions.assertTrue(totalPlants >= 0, "totalPlants should be >= 0, got: " + totalPlants);
        Assertions.assertTrue(lowStockPlants >= 0, "lowStockPlants should be >= 0, got: " + lowStockPlants);
        
        System.out.println("DEBUG: Summary - Total Plants: " + totalPlants + ", Low Stock: " + lowStockPlants);
    }
}
