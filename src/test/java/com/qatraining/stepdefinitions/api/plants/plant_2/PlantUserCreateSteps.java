package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantUserCreateAPI;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantUserCreateSteps {

    private final PlantUserCreateAPI api = new PlantUserCreateAPI();
    private int lastStatus;
    private String lastBody;

    @When("a regular user sends a POST to \\/api\\/plants\\/category\\/{int} with body:")
    public void regularUserSendsPost(int categoryId, DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        Map<String, Object> body = new HashMap<>();
        if (!rows.isEmpty()) {
            Map<String, String> r = rows.get(0);
            Map<String, String> normalized = new HashMap<>();
            for (Map.Entry<String, String> e : r.entrySet()) {
                normalized.put(e.getKey().trim(), e.getValue());
            }
            if (normalized.containsKey("id")) body.put("id", Integer.parseInt(normalized.get("id")));
            if (normalized.containsKey("name")) body.put("name", normalized.get("name"));
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

    @Then("user creation response should have status {int}")
    public void userCreationResponseStatus(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("user creation response should contain forbidden error")
    public void userCreationResponseShouldContainForbiddenError() {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("error") || json.has("status"), "Response should contain 'error' or 'status' field");
        if (json.has("error")) {
            Assertions.assertEquals("Forbidden", json.getString("error"), "Expected error 'Forbidden'");
        } else if (json.has("status")) {
            Assertions.assertEquals(403, json.getInt("status"), "Expected status 403 in response body");
        }
    }
}
