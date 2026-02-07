package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantPriceValidationAPI;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantPriceValidationSteps {

    private final PlantPriceValidationAPI api = new PlantPriceValidationAPI();
    private int lastStatus;
    private String lastBody;

    @When("user sends POST to \\/api\\/plants\\/category\\/{int} with invalid-price data:")
    public void userSendsPostWithInvalidPrice(int categoryId, DataTable table) {
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

    @Then("price validation response should have status {int}")
    public void priceValidationResponseStatus(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("the response should contain validation error for price")
    public void responseShouldContainValidationErrorForPrice() {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("details"), "Response should contain 'details' field");
        JSONObject details = json.getJSONObject("details");
        Assertions.assertTrue(details.has("price"), "Details should contain 'price' validation error");
        String msg = details.getString("price");
        Assertions.assertEquals("Price must be greater than 0", msg, "Unexpected price validation message: " + msg);
    }

    @Then("price validation response should contain error message {string}")
    public void priceValidationResponseShouldContainErrorMessage(String expectedMessage) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("message"), "Response should contain 'message' field");
        Assertions.assertEquals(expectedMessage, json.getString("message"), "Error message mismatch");
    }
}
