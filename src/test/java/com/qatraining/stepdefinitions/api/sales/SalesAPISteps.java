package com.qatraining.stepdefinitions.api.sales;

import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SalesAPISteps {

    private final ConfigManager config = ConfigManager.getInstance();
    private Response lastResponse;
    private Integer plantId;
    private Integer saleId;
    private String plantName;

    @When("the admin creates a sale for a plant with quantity {int}")
    public void adminCreatesSaleForPlantWithQuantity(int qty) {
        // Create parent category
        String parentName = RandomStringUtils.randomAlphabetic(5).toLowerCase();
        Map<String, Object> parentBody = new HashMap<>();
        parentBody.put("id", 0);
        parentBody.put("name", parentName);
        parentBody.put("subCategories", List.of());

        Response parentResp = given()
                .spec(APIClient.getRequestSpec())
                .body(parentBody)
                .when().post("/categories");

        Assertions.assertEquals(201, parentResp.getStatusCode(), "Parent category creation should return 201");
        Integer parentId = parentResp.jsonPath().getInt("id");
        Assertions.assertNotNull(parentId, "Parent id should be returned");

        // Create sub-category
        String subName = RandomStringUtils.randomAlphabetic(5).toLowerCase();
        Map<String, Object> subBody = new HashMap<>();
        subBody.put("id", 0);
        subBody.put("name", subName);
        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parentId);
        subBody.put("parent", parentObj);
        subBody.put("subCategories", List.of());

        Response subResp = given()
                .spec(APIClient.getRequestSpec())
                .body(subBody)
                .when().post("/categories");

        Assertions.assertEquals(201, subResp.getStatusCode(), "Sub-category creation should return 201");
        Integer subCategoryId = subResp.jsonPath().getInt("id");
        Assertions.assertNotNull(subCategoryId, "Sub-category id should be returned");

        // Create plant under sub-category
        plantName = "plant_" + RandomStringUtils.randomAlphanumeric(6).toLowerCase();
        Map<String, Object> plantBody = new HashMap<>();
        plantBody.put("id", 0);
        plantBody.put("name", plantName);
        plantBody.put("description", "sale test plant");
        plantBody.put("price", 5.0);
        plantBody.put("quantity", 10);

        Response plantResp = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("categoryId", subCategoryId)
                .body(plantBody)
                .when().post("/plants/category/{categoryId}");

        Assertions.assertEquals(201, plantResp.getStatusCode(), "Plant creation should return 201");
        plantId = plantResp.jsonPath().getInt("id");
        Assertions.assertNotNull(plantId, "Plant id should be returned");

        // Create sale via POST /api/sales/plant/{plantId}?quantity={qty}
        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .queryParam("quantity", qty)
                .pathParam("plantId", plantId)
                .when().post("/sales/plant/{plantId}");

        if (lastResponse.getStatusCode() == 201) {
            saleId = lastResponse.jsonPath().getInt("id");
        }
    }

    @Then("the sale creation response should be {int}")
    public void saleCreationResponseShouldBe(int expectedStatus) {
        Assertions.assertNotNull(lastResponse, "There is no response to assert");
        int actual = lastResponse.getStatusCode();
        if (actual != expectedStatus) {
            System.err.println("---- SALE CREATION RESPONSE (status: " + actual + ") ----");
            try { System.err.println(lastResponse.getBody().asPrettyString()); } catch (Exception e) { System.err.println(lastResponse.getBody().asString()); }
        }
        Assertions.assertEquals(expectedStatus, actual, "Expected HTTP status " + expectedStatus + " but was " + actual);
    }

    @Then("the created sale should be present in the sales list")
    public void createdSaleShouldBePresentInSales() {
        Assertions.assertNotNull(saleId, "Sale id must be present to verify");

        Response resp = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/sales");

        Assertions.assertEquals(200, resp.getStatusCode(), "GET /sales should return 200");
        List<Map<String, Object>> list = resp.jsonPath().getList("$");
        Assertions.assertTrue(list.size() > 0, "Sales list should not be empty after creating a sale");

        boolean found = list.stream().anyMatch(s -> {
            Object id = ((Map<?, ?>) s).get("id");
            if (id instanceof Number) return ((Number) id).intValue() == saleId;
            if (id instanceof String) return Integer.parseInt((String) id) == saleId;
            return false;
        });
        Assertions.assertTrue(found, "Created sale should be present in /sales list");
    }

    // New: request all sales
    private Response salesListResponse;

    @When("the admin requests the sales list")
    public void adminRequestsSalesList() {
        salesListResponse = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/sales");
    }

    @Then("the sales endpoint returns {int} and JSON array")
    public void salesEndpointReturnsStatusAndJsonArray(int expectedStatus) {
        Assertions.assertNotNull(salesListResponse, "No response for /sales");
        int status = salesListResponse.getStatusCode();
        if (status != expectedStatus) {
            System.err.println("---- /sales RESPONSE (status: " + status + ") ----");
            try { System.err.println(salesListResponse.getBody().asPrettyString()); } catch (Exception e) { System.err.println(salesListResponse.getBody().asString()); }
        }
        Assertions.assertEquals(expectedStatus, status, "Expected HTTP status " + expectedStatus + " but was " + status);
        Assertions.assertTrue(salesListResponse.getContentType().contains("application/json"), "Expected JSON content type");
        List<Map<String, Object>> list = salesListResponse.jsonPath().getList("$");
        Assertions.assertNotNull(list, "Sales response should be a JSON array");
    }
}
