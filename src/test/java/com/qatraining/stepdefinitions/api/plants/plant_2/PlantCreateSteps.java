package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantCreateAPI;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantCreateSteps {

    private final PlantCreateAPI api = new PlantCreateAPI();
    private int lastStatus;
    private String lastBody;

    @When("the admin sends a POST to \\/api\\/plants\\/category\\/{int} with body:")
    public void adminSendsPost(int categoryId, DataTable table) {
        // DataTable has header row and one data row
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        Map<String, Object> body = new HashMap<>();
        if (!rows.isEmpty()) {
            Map<String, String> r = rows.get(0);
            // normalize keys to be defensive against casing/underscores
            Map<String, String> normalized = new HashMap<>();
            for (Map.Entry<String, String> e : r.entrySet()) {
                normalized.put(e.getKey().trim(), e.getValue());
            }
            // copy known fields and replace {time} placeholder with actual timestamp
            if (normalized.containsKey("id")) body.put("id", Integer.parseInt(normalized.get("id")));
            if (normalized.containsKey("name")) {
                String name = normalized.get("name").replace("{time}", String.valueOf(System.currentTimeMillis()));
                body.put("name", name);
            }
            if (normalized.containsKey("price")) body.put("price", Integer.parseInt(normalized.get("price")));
            if (normalized.containsKey("quantity")) body.put("quantity", Integer.parseInt(normalized.get("quantity")));
            if (normalized.containsKey("category_Id")) body.put("category_Id", Integer.parseInt(normalized.get("category_Id")));
        }

        try {
            api.createPlant(categoryId, body);
            lastStatus = api.getStatusCode();
            lastBody = api.getResponseBody();
            System.out.println("DEBUG: Status=" + lastStatus + " Body=" + lastBody);
        } catch (Exception e) {
            lastStatus = -1;
            lastBody = e.getMessage();
            System.out.println("DEBUG: Exception while creating plant: " + e.getMessage());
        }
    }

    @Then("the response status code should be {int}")
    public void responseStatusShouldBe(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("the response should contain the created plant with name containing {string} and category id {int}")
    public void responseShouldContainCreatedPlant(String namePrefix, int categoryId) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        String createdName = json.getString("name");
        Assertions.assertTrue(createdName.contains(namePrefix), 
                "Created plant name should contain '" + namePrefix + "', got: " + createdName);
        JSONObject category = json.getJSONObject("category");
        Assertions.assertEquals(categoryId, category.getInt("id"), "Created plant category id mismatch");
    }
}
