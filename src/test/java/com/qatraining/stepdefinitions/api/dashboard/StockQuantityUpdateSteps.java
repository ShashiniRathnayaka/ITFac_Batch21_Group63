package com.qatraining.stepdefinitions.api.dashboard;

import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Step Definitions for API_DASH_006: Stock Quantity Update After Sale
 * Verifies that plant stock is correctly reduced after recording sales
 */
public class StockQuantityUpdateSteps {

    private final ConfigManager config = ConfigManager.getInstance();
    private Response lastResponse;
    private Integer parentCategoryId;
    private Integer subCategoryId;
    private Integer plantId;
    private String plantName;
    private int currentStock;
    private int saleQuantity;

    @And("^the admin creates a plant with (\\d+) units of stock$")
    public void createPlantWithStock(int stockQuantity) {
        if (parentCategoryId == null) {
            createParentCategory();
        }
        if (subCategoryId == null) {
            createSubCategoryUnderParent();
        }

        plantName = "Rose" + RandomStringUtils.randomNumeric(3);
        double plantPrice = 150.99;

        Map<String, Object> plantBody = new HashMap<>();
        plantBody.put("name", plantName);
        plantBody.put("price", plantPrice);
        plantBody.put("quantity", stockQuantity);

        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("categoryId", subCategoryId)
                .body(plantBody)
                .when()
                .post("/plants/category/{categoryId}");

        Assertions.assertEquals(201, lastResponse.getStatusCode(),
                "Plant creation should return 201");

        plantId = lastResponse.jsonPath().getInt("id");
        currentStock = stockQuantity;
    }

    @And("the initial stock quantity is verified as {int}")
    public void verifyInitialStock(int expectedStock) {
        Assertions.assertNotNull(plantId, "Plant must be created first");

        Response getResponse = given()
                .spec(APIClient.getRequestSpec())
                .when()
                .get("/plants/{plantId}", plantId);

        Assertions.assertEquals(200, getResponse.getStatusCode(),
                "GET plant should return 200");

        Integer stock = getResponse.jsonPath().getInt("quantity");
        Assertions.assertNotNull(stock, "Stock quantity should be present");
        Assertions.assertEquals(expectedStock, (int) stock,
                "Stock should be " + expectedStock);

        currentStock = stock;
    }

    @When("^the admin records a sale with quantity (\\d+)$")
    public void recordSale(int quantity) {
        Assertions.assertNotNull(plantId, "Plant must be created first");

        saleQuantity = quantity;

        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .queryParam("quantity", quantity)
                .when()
                .post("/sales/plant/{plantId}", plantId);
    }

    @When("^the admin records another sale with quantity (\\d+)$")
    public void recordAnotherSale(int quantity) {
        recordSale(quantity);
    }

    @Then("the sale is recorded successfully with status {int}")
    public void verifySaleRecordedSuccess(int expectedStatus) {
        Assertions.assertNotNull(lastResponse, "Response should exist");
        Assertions.assertEquals(expectedStatus, lastResponse.getStatusCode(),
                "Expected status " + expectedStatus + " but got " + lastResponse.getStatusCode());
    }

    @And("^the stock quantity is reduced to (\\d+)$")
    public void verifyStockReduction(int expectedStock) {
        Assertions.assertNotNull(plantId, "Plant must be created first");

        Response getResponse = given()
                .spec(APIClient.getRequestSpec())
                .when()
                .get("/plants/{plantId}", plantId);

        Assertions.assertEquals(200, getResponse.getStatusCode(),
                "GET plant should return 200");

        Integer actualStock = getResponse.jsonPath().getInt("quantity");
        Assertions.assertNotNull(actualStock, "Stock quantity should be present");
        Assertions.assertEquals(expectedStock, (int) actualStock,
                "Stock should be " + expectedStock + " but was " + actualStock);

        currentStock = actualStock;
    }

    @And("^the reduction amount is exactly (\\d+) units$")
    public void verifyReductionAmount(int expectedReduction) {
        Assertions.assertNotNull(lastResponse, "Sale response should exist");

        // Verify sale quantity in response
        Integer quantity = lastResponse.jsonPath().getInt("quantity");
        Assertions.assertNotNull(quantity, "Sale quantity should be present");
        Assertions.assertEquals(expectedReduction, (int) quantity,
                "Sale quantity should be " + expectedReduction);
    }

    // Helper methods
    private void createParentCategory() {
        String parentCategoryName = "Plants" + RandomStringUtils.randomNumeric(2);

        Map<String, Object> parentBody = new HashMap<>();
        parentBody.put("name", parentCategoryName);

        Response parentResp = given()
                .spec(APIClient.getRequestSpec())
                .body(parentBody)
                .when()
                .post("/categories");

        Assertions.assertEquals(201, parentResp.getStatusCode(),
                "Parent category creation should return 201");

        parentCategoryId = parentResp.jsonPath().getInt("id");
        Assertions.assertNotNull(parentCategoryId, "Parent category ID should be returned");
    }

    private void createSubCategoryUnderParent() {
        Assertions.assertNotNull(parentCategoryId, "Parent category must be created first");

        String subCategoryName = "Flowers" + RandomStringUtils.randomNumeric(2);

        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parentCategoryId);

        Map<String, Object> subBody = new HashMap<>();
        subBody.put("name", subCategoryName);
        subBody.put("parent", parentObj);

        Response subResp = given()
                .spec(APIClient.getRequestSpec())
                .body(subBody)
                .when()
                .post("/categories");

        Assertions.assertEquals(201, subResp.getStatusCode(),
                "Sub-category creation should return 201");

        subCategoryId = subResp.jsonPath().getInt("id");
        Assertions.assertNotNull(subCategoryId, "Sub-category ID should be returned");
    }
}
