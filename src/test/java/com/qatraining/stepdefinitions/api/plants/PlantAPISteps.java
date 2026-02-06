package com.qatraining.stepdefinitions.api.plants;

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
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PlantAPISteps {

    private final ConfigManager config = ConfigManager.getInstance();
    private Response lastResponse;
    private Integer subCategoryId;
    private Integer plantId;
    private String plantName;


    @And("a sub-category exists")
    public void aSubCategoryExists() {
        // Create a parent category
        String parentName = generateCompliantName("p");
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

        // Create a sub-category that references the parent (send parent object)
        String subName = generateCompliantName("s");
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
        subCategoryId = subResp.jsonPath().getInt("id");
        Assertions.assertNotNull(subCategoryId, "Sub-category id should be returned");
    }

    @When("the admin creates a plant under that sub-category with valid data")
    public void adminCreatesPlantUnderSubCategory() {
        Assertions.assertNotNull(subCategoryId, "Sub-category must exist before creating a plant");

        plantName = "plant_" + RandomStringUtils.randomAlphanumeric(6).toLowerCase();

        Map<String, Object> body = new HashMap<>();
        body.put("id", 0);
        body.put("name", plantName);
        body.put("description", "API test plant");
        body.put("price", 9.99);
        // API expects 'quantity' field (validation message uses 'quantity')
        body.put("quantity", 10);

        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("subCategoryId", subCategoryId)
                .body(body)
                .when().post("/plants/category/{subCategoryId}");

        if (lastResponse.getStatusCode() == 201) {
            plantId = lastResponse.jsonPath().getInt("id");
        }
    }

    @Then("the plant creation response status should be {int}")
    public void plantCreationResponseStatusShouldBe(int expectedStatus) {
        Assertions.assertNotNull(lastResponse, "There is no response to assert for plant creation");
        int actual = lastResponse.getStatusCode();
        if (actual != expectedStatus) {
            System.err.println("---- PLANT CREATION RESPONSE BODY (status: " + actual + ") ----");
            try {
                System.err.println(lastResponse.getBody().asPrettyString());
            } catch (Exception e) {
                System.err.println(lastResponse.getBody().asString());
            }
        }
        Assertions.assertEquals(expectedStatus, actual,
                "Expected HTTP status " + expectedStatus + " but was " + actual);
    }

    @Then("the created plant should be linked to the sub-category")
    public void createdPlantShouldBeLinked() {
        Assertions.assertNotNull(plantId, "Plant id must be present to verify linkage");

        // Try to GET the plant by id
        Response resp = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/plants/" + plantId);

        if (resp.getStatusCode() != 200) {
            // fallback to GET /plants and find by id
            resp = given()
                    .spec(APIClient.getRequestSpec())
                    .when().get("/plants");
            Assertions.assertEquals(200, resp.getStatusCode(), "GET /plants should return 200");
            List<Map<String, Object>> plants = resp.jsonPath().getList("$");
            Map<String, Object> found = plants.stream()
                    .filter(p -> plantId.equals(((Number)p.get("id")).intValue()))
                    .findFirst().orElse(null);
            Assertions.assertNotNull(found, "Created plant should be present in /plants list");
            Object categoryObj = found.get("category");
            Integer catId = extractCategoryId(categoryObj, found);
            Assertions.assertEquals(subCategoryId, catId, "Plant should reference the created sub-category");
            return;
        }

        // If GET /plants/{id} returned 200, check category info in response
        Assertions.assertEquals(200, resp.getStatusCode(), "GET /plants/{id} should return 200");
        Integer catId = null;
        try {
            if (resp.jsonPath().get("category.id") != null) {
                catId = resp.jsonPath().getInt("category.id");
            } else if (resp.jsonPath().get("categoryId") != null) {
                catId = resp.jsonPath().getInt("categoryId");
            }
        } catch (Exception e) {
            // ignore and fallback
        }

        Assertions.assertNotNull(catId, "Response should contain category id");
        Assertions.assertEquals(subCategoryId, catId, "Plant should be linked to the sub-category");
    }

    private Integer extractCategoryId(Object categoryObj, Map<String, Object> context) {
        if (categoryObj == null) return null;
        if (categoryObj instanceof Map) {
            Object id = ((Map<?, ?>) categoryObj).get("id");
            if (id instanceof Number) return ((Number) id).intValue();
            if (id instanceof String) return Integer.valueOf((String) id);
        }
        // Sometimes the response uses parentId or categoryId on the plant object
        if (context.containsKey("categoryId")) {
            Object id = context.get("categoryId");
            if (id instanceof Number) return ((Number) id).intValue();
            if (id instanceof String) return Integer.valueOf((String) id);
        }
        return null;
    }

    // Helper: generate a name that satisfies constraints (3-25 chars)
    private String generateCompliantName(String base) {
        String rand = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
        String prefix = (base == null || base.isBlank()) ? "c" : base.substring(0,1);
        String name = (prefix + rand).toLowerCase();
        if (name.length() < 3) name = name + "x".repeat(3 - name.length());
        if (name.length() > 25) name = name.substring(0, 25);
        return name;
    }
}
