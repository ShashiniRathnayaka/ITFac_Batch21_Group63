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
 * Step Definitions for API_DASH_005: Plant Category Reference
 * Verifies that plants correctly reference their parent categories
 */
public class PlantCategoryReferenceSteps {

    private final ConfigManager config = ConfigManager.getInstance();
    private Response lastResponse;
    private Integer parentCategoryId;
    private Integer subCategoryId;
    private Integer plantId;
    private String plantName;
    private String parentCategoryName;
    private String subCategoryName;

    @Given("a parent category exists")
    public void createParentCategory() {
        // Category name must be 3-10 characters (per SRS 5.2)
        parentCategoryName = "Plants" + RandomStringUtils.randomNumeric(2);

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

    @And("a sub-category exists under the parent category")
    public void createSubCategoryUnderParent() {
        Assertions.assertNotNull(parentCategoryId, "Parent category must be created first");

        // Category name must be 3-10 characters (per SRS 5.2)
        subCategoryName = "Flowers" + RandomStringUtils.randomNumeric(2);

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

    @When("the admin creates a plant under that sub-category for category reference")
    public void createPlantUnderSubCategory() {
        Assertions.assertNotNull(subCategoryId, "Sub-category must exist before creating a plant");

        plantName = "Rose" + RandomStringUtils.randomNumeric(3);
        double plantPrice = 150.99;
        int plantQuantity = 50;

        Map<String, Object> plantBody = new HashMap<>();
        plantBody.put("name", plantName);
        plantBody.put("price", plantPrice);
        plantBody.put("quantity", plantQuantity);

        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("categoryId", subCategoryId)
                .body(plantBody)
                .when()
                .post("/plants/category/{categoryId}");

        if (lastResponse.getStatusCode() == 201) {
            plantId = lastResponse.jsonPath().getInt("id");
        }
    }

    @Then("the plant is created successfully with status {int}")
    public void verifyPlantCreationStatus(int expectedStatus) {
        Assertions.assertNotNull(lastResponse, "Response should exist");
        Assertions.assertEquals(expectedStatus, lastResponse.getStatusCode(),
                "Expected status " + expectedStatus + " but got " + lastResponse.getStatusCode());
    }

    @When("the admin retrieves the plant by ID")
    public void retrievePlantById() {
        Assertions.assertNotNull(plantId, "Plant ID must exist");

        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .when()
                .get("/plants/{plantId}", plantId);
    }

    @Then("the plant response returns status {int}")
    public void verifyPlantResponseStatus(int expectedStatus) {
        Assertions.assertNotNull(lastResponse, "Response should exist");
        Assertions.assertEquals(expectedStatus, lastResponse.getStatusCode(),
                "Expected status " + expectedStatus + " but got " + lastResponse.getStatusCode());
    }

    @And("the plant response contains correct category reference")
    public void verifyPlantCategoryReference() {
        Assertions.assertNotNull(lastResponse, "Response should exist");

        Integer categoryId = lastResponse.jsonPath().getInt("category.id");
        String categoryName = lastResponse.jsonPath().getString("category.name");

        Assertions.assertNotNull(categoryId, "Category ID should be present in response");
        Assertions.assertEquals(subCategoryId, categoryId,
                "Plant should reference correct sub-category");
        Assertions.assertEquals(subCategoryName, categoryName,
                "Category name should match");
    }

    @And("the category reference includes parent information")
    public void verifyParentCategoryReference() {
        Assertions.assertNotNull(lastResponse, "Response should exist");

        try {
            Integer parentId = lastResponse.jsonPath().getInt("category.parentId");
            Assertions.assertNotNull(parentId, "Parent category ID should be included");
            Assertions.assertEquals(parentCategoryId, parentId,
                    "Parent ID should match");
        } catch (Exception e) {
            // Check for parent object reference
            Object parentRef = lastResponse.jsonPath().get("category.parent");
            Assertions.assertNotNull(parentRef,
                    "Parent category reference should be included in response");
        }
    }
}
