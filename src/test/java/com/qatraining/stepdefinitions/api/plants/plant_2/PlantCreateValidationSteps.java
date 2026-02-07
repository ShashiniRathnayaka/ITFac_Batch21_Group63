package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantCreateValidationAPI;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantCreateValidationSteps {

    private final PlantCreateValidationAPI api = new PlantCreateValidationAPI();
    private int lastStatus;
    private String lastBody;

    @When("user sends POST to \\/api\\/plants\\/category\\/{int} with incomplete data:")
    public void userSendsPostWithIncompleteData(int categoryId, DataTable table) {
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
            // Note: intentionally omit price to test validation
            if (normalized.containsKey("id")) body.put("id", Integer.parseInt(normalized.get("id")));
            if (normalized.containsKey("name")) {
                String name = normalized.get("name").replace("{time}", String.valueOf(System.currentTimeMillis()));
                body.put("name", name);
            }
            if (normalized.containsKey("quantity")) body.put("quantity", Integer.parseInt(normalized.get("quantity")));
            if (normalized.containsKey("category_Id")) body.put("category_Id", Integer.parseInt(normalized.get("category_Id")));
            // price is intentionally NOT included to trigger validation error
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

    @Then("the response should contain validation error for field {string}")
    public void responseShouldContainValidationErrorForField(String fieldName) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        
        // Check if response has "details" field with validation errors
        Assertions.assertTrue(json.has("details"), 
                "Response should contain 'details' field with validation errors");
        
        JSONObject details = json.getJSONObject("details");
        Assertions.assertTrue(details.has(fieldName), 
                "Response details should contain validation error for field '" + fieldName + "'. Details: " + details.toString());
        
        System.out.println("DEBUG: Validation error for '" + fieldName + "': " + details.getString(fieldName));
    }

    @Then("validation response should have status {int}")
    public void validationResponseStatus(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("validation response should contain error message {string}")
    public void validationResponseErrorMessage(String expectedMessage) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        
        Assertions.assertTrue(json.has("message"), "Response should contain 'message' field");
        String actualMessage = json.getString("message");
        Assertions.assertEquals(expectedMessage, actualMessage, 
                "Error message mismatch. Expected: '" + expectedMessage + "', got: '" + actualMessage + "'");
        
        System.out.println("DEBUG: Error message: " + actualMessage);
    }
}
