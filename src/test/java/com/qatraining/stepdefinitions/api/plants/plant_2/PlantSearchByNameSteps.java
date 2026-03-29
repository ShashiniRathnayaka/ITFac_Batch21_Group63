package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantSearchByNameAPI;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class PlantSearchByNameSteps {

    private final PlantSearchByNameAPI api = new PlantSearchByNameAPI();
    private int lastStatus;
    private String lastBody;
    private String searchName;

    @When("a user searches for plants with name filter:")
    public void userSearchesPlantsWithNameFilter(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        String name = "";
        int page = 0;
        int size = 10;
        String sort = "";
        
        if (!rows.isEmpty()) {
            Map<String, String> r = rows.get(0);
            if (r.containsKey("name")) {
                name = r.get("name");
                searchName = name;
            }
            if (r.containsKey("page")) {
                page = Integer.parseInt(r.get("page"));
            }
            if (r.containsKey("size")) {
                size = Integer.parseInt(r.get("size"));
            }
            if (r.containsKey("sort")) {
                sort = r.get("sort");
            }
        }
        
        api.searchPlantsByName(name, page, size, sort);
        lastStatus = api.getStatusCode();
        lastBody = api.getResponseBody();
        System.out.println("DEBUG: Status=" + lastStatus + " Body=" + lastBody);
    }

    @Then("search response status should be {int}")
    public void searchResponseStatus(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("the response should contain plants matching search criteria")
    public void responseShouldContainPlantsMatchingCriteria() {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("content"), "Response should contain 'content' field");
        JSONArray content = json.getJSONArray("content");
        Assertions.assertTrue(content.length() > 0, "Content array should not be empty");
    }

    @Then("all returned plants should have name containing {string}")
    public void allReturnedPlantsShouldHaveNameContaining(String searchTerm) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        JSONArray content = json.getJSONArray("content");
        
        for (int i = 0; i < content.length(); i++) {
            JSONObject plant = content.getJSONObject(i);
            Assertions.assertTrue(plant.has("name"), "Plant should have 'name' field");
            String plantName = plant.getString("name");
            Assertions.assertTrue(plantName.toLowerCase().contains(searchTerm.toLowerCase()), 
                    "Plant name '" + plantName + "' should contain '" + searchTerm + "'");
        }
    }

    @Then("search results should be sorted by price descending")
    public void searchResultsShouldBeSortedByPriceDescending() {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        JSONArray content = json.getJSONArray("content");
        
        if (content.length() > 1) {
            double previousPrice = Double.MAX_VALUE;
            for (int i = 0; i < content.length(); i++) {
                JSONObject plant = content.getJSONObject(i);
                Assertions.assertTrue(plant.has("price"), "Plant should have 'price' field");
                double currentPrice = plant.getDouble("price");
                Assertions.assertTrue(currentPrice <= previousPrice,
                        "Plants should be sorted by price descending. Found " + currentPrice + " after " + previousPrice);
                previousPrice = currentPrice;
            }
        }
    }
}
