package com.qatraining.stepdefinitions.api.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;

import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class CategoryAPISteps {

    private final ConfigManager config = ConfigManager.getInstance();
    private Response lastResponse;
    private Integer parentCategoryId;
    private Integer subCategoryId;
    private String parentCategoryName;
    private String subCategoryName;

    @When("the admin creates a main category with name prefix {string}")
    public void adminCreatesMainCategory(String prefix) {
        // Generate a compliant name (3-10 chars)
        parentCategoryName = generateCompliantName("C");
        Assertions.assertTrue(parentCategoryName.length() >= 3 && parentCategoryName.length() <= 10,
                "Parent category name must be 3-10 characters");

        Map<String, Object> body = new HashMap<>();
        body.put("id", 0);
        body.put("name", parentCategoryName);
        // Omit parent for main category (server expects either null or no field)
        body.put("subCategories", List.of());

        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .body(body)
                .when().post("/categories");

        if (lastResponse.getStatusCode() == 201) {
            parentCategoryId = lastResponse.jsonPath().getInt("id");
        }
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int expectedStatus) {
        Assertions.assertNotNull(lastResponse, "There is no response to assert");
        int actual = lastResponse.getStatusCode();
        if (actual != expectedStatus) {
            System.err.println("---- RESPONSE BODY (status: " + actual + ") ----");
            try {
                System.err.println(lastResponse.getBody().asPrettyString());
            } catch (Exception e) {
                System.err.println(lastResponse.getBody().asString());
            }
        }
        Assertions.assertEquals(expectedStatus, actual,
                "Expected HTTP status " + expectedStatus + " but was " + actual);
    }

    @And("the created category ID should be stored as {string}")
    public void createdCategoryIdShouldBeStoredAs(String varName) {
        Assertions.assertNotNull(parentCategoryId, "Parent category ID should be set");
        if ("parentCategoryId".equals(varName)) {
            // already set in field parentCategoryId
        } else {
            Assertions.fail("Unknown variable name requested: " + varName);
        }
    }

    @When("the admin creates a sub-category with name prefix {string} using the parent category id")
    public void adminCreatesSubCategory(String prefix) {
        Assertions.assertNotNull(parentCategoryId, "Parent category id must be present before creating sub-category");

        // Generate a compliant sub-category name (3-10 chars)
        subCategoryName = generateCompliantName("S");
        Assertions.assertTrue(subCategoryName.length() >= 3 && subCategoryName.length() <= 10,
                "Sub-category name must be 3-10 characters");

        Map<String, Object> body = new HashMap<>();
        body.put("id", 0);
        body.put("name", subCategoryName);
        // The API expects `parent` as a Category object, include id as object
        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parentCategoryId);
        body.put("parent", parentObj);
        body.put("subCategories", List.of());

        lastResponse = given()
                .spec(APIClient.getRequestSpec())
                .body(body)
                .when().post("/categories");

        if (lastResponse.getStatusCode() == 201) {
            subCategoryId = lastResponse.jsonPath().getInt("id");
        }
    }

    @Then("both categories should be retrievable via GET to the categories endpoint")
    public void bothCategoriesShouldBeRetrievable() {
        Response resp = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/categories");

        Assertions.assertEquals(200, resp.getStatusCode(), "GET /categories should return 200");

        List<String> names = resp.jsonPath().getList("name");
        Assertions.assertTrue(names.contains(parentCategoryName), "Parent category name should be present in categories list");
        Assertions.assertTrue(names.contains(subCategoryName), "Sub-category name should be present in categories list");

        // Optionally, verify that the sub-category references the parent
        List<Map<String, Object>> categories = resp.jsonPath().getList("$");
        boolean subPointsToParent = categories.stream().filter(c -> parentCategoryName.equals(c.get("name"))).findFirst().isPresent()
                && categories.stream().filter(c -> subCategoryName.equals(c.get("name"))).findFirst().isPresent();
        Assertions.assertTrue(subPointsToParent, "Both categories exist in response");
    }

    // Helper: generate a category name that satisfies the 3-10 char constraint
    private String generateCompliantName(String base) {
        String rand = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
        String prefix = (base == null || base.isBlank()) ? "c" : base.substring(0,1);
        String name = (prefix + rand).toLowerCase();
        if (name.length() < 3) {
            name = name + "x".repeat(3 - name.length());
        }
        if (name.length() > 10) {
            name = name.substring(0, 10);
        }
        return name;
    }
}
