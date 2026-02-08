package com.qatraining.stepdefinitions.api.plants.plant_2;

import com.qatraining.api.Plants.plant_2.PlantGetPagedAndSortedAPI;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class PlantGetPagedAndSortedSteps {

    private final PlantGetPagedAndSortedAPI api = new PlantGetPagedAndSortedAPI();
    private int lastStatus;
    private String lastBody;

    @When("a user requests GET to \\/api\\/plants\\/paged with pagination and multi-sort:")
    public void userRequestsPagedPlantsWithMultiSort(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        int page = 0;
        int size = 10;
        java.util.List<String> sortList = new java.util.ArrayList<>();
        
        if (!rows.isEmpty()) {
            Map<String, String> r = rows.get(0);
            if (r.containsKey("page")) {
                page = Integer.parseInt(r.get("page"));
            }
            if (r.containsKey("size")) {
                size = Integer.parseInt(r.get("size"));
            }
            // Collect all sort parameters (sort1, sort2, etc.)
            for (Map.Entry<String, String> entry : r.entrySet()) {
                if (entry.getKey().startsWith("sort") && entry.getValue() != null && !entry.getValue().isEmpty()) {
                    sortList.add(entry.getValue());
                }
            }
        }
        
        api.getPlantsWithPagination(page, size, sortList.toArray(new String[0]));
        lastStatus = api.getStatusCode();
        lastBody = api.getResponseBody();
        System.out.println("DEBUG: Status=" + lastStatus + " Body=" + lastBody);
    }

    @Then("paginated response status should be {int}")
    public void paginatedResponseStatus(int expected) {
        Assertions.assertEquals(expected, lastStatus, "Unexpected status code. Response body: " + lastBody);
    }

    @Then("the response should contain paginated plant content")
    public void responseShouldContainPaginatedContent() {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("content"), "Response should contain 'content' field");
        JSONArray content = json.getJSONArray("content");
        Assertions.assertTrue(content.length() > 0, "Content array should not be empty");
        
        // Verify first plant in content has required fields
        JSONObject firstPlant = content.getJSONObject(0);
        Assertions.assertTrue(firstPlant.has("id"), "Plant should have 'id' field");
        Assertions.assertTrue(firstPlant.has("name"), "Plant should have 'name' field");
        Assertions.assertTrue(firstPlant.has("price"), "Plant should have 'price' field");
        Assertions.assertTrue(firstPlant.has("category"), "Plant should have 'category' field");
    }

    @Then("the response should indicate total pages greater than {int}")
    public void responseShouldIndicateTotalPages(int minPages) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("totalPages"), "Response should contain 'totalPages' field");
        int totalPages = json.getInt("totalPages");
        Assertions.assertTrue(totalPages > minPages, 
                "Expected totalPages > " + minPages + ", but got " + totalPages);
    }

    @Then("the response page number should be {int}")
    public void responsePageNumberShouldBe(int expectedPage) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("number"), "Response should contain 'number' field");
        int actualPage = json.getInt("number");
        Assertions.assertEquals(expectedPage, actualPage, "Expected page number " + expectedPage + ", got " + actualPage);
    }

    @Then("the response should contain exactly {int} plants")
    public void responseShouldContainExactlyPlants(int expectedCount) {
        Assertions.assertNotNull(lastBody, "Response body is null");
        JSONObject json = new JSONObject(lastBody);
        Assertions.assertTrue(json.has("numberOfElements"), "Response should contain 'numberOfElements' field");
        JSONArray content = json.getJSONArray("content");
        Assertions.assertEquals(expectedCount, content.length(), 
                "Expected " + expectedCount + " plants, got " + content.length());
    }}