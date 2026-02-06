package com.qatraining.stepdefinitions.api.dashboard;

import com.qatraining.api.APIClient;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class DashboardAPISteps {

    private Response categoriesResp;
    private Response plantsResp;
    private Response salesResp;

    @When("the admin requests dashboard summary data")
    public void adminRequestsDashboardSummary() {
        categoriesResp = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/categories");

        plantsResp = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/plants");

        salesResp = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/sales");
    }

    @Then("GET to the categories endpoint returns 200 and non-empty list")
    public void categoriesShouldReturn200AndNonEmpty() {
        Assertions.assertNotNull(categoriesResp, "No response for /categories");
        int status = categoriesResp.getStatusCode();
        if (status != 200) {
            System.err.println("---- /categories RESPONSE (status: " + status + ") ----");
            System.err.println(categoriesResp.getBody().asPrettyString());
        }
        Assertions.assertEquals(200, status, "Expected 200 for /categories");
        Assertions.assertTrue(categoriesResp.getContentType().contains("application/json"), "Expected JSON content type");

        List<Map<String, Object>> list = categoriesResp.jsonPath().getList("$");
        Assertions.assertNotNull(list, "Categories response should be a JSON array");
        Assertions.assertTrue(list.size() > 0, "Categories list should not be empty for dashboard");
        // Verify each item has an id
        for (Map<String, Object> c : list) {
            Assertions.assertNotNull(c.get("id"), "Each category should have an id");
        }
    }

    @Then("GET to the plants endpoint returns 200 and non-empty list")
    public void plantsShouldReturn200AndNonEmpty() {
        Assertions.assertNotNull(plantsResp, "No response for /plants");
        int status = plantsResp.getStatusCode();
        if (status != 200) {
            System.err.println("---- /plants RESPONSE (status: " + status + ") ----");
            System.err.println(plantsResp.getBody().asPrettyString());
        }
        Assertions.assertEquals(200, status, "Expected 200 for /plants");
        Assertions.assertTrue(plantsResp.getContentType().contains("application/json"), "Expected JSON content type");

        List<Map<String, Object>> list = plantsResp.jsonPath().getList("$");
        Assertions.assertNotNull(list, "Plants response should be a JSON array");
        Assertions.assertTrue(list.size() > 0, "Plants list should not be empty for dashboard");
        for (Map<String, Object> p : list) {
            Assertions.assertNotNull(p.get("id"), "Each plant should have an id");
        }
    }

    @Then("GET to the sales endpoint returns 200 and non-empty list")
    public void salesShouldReturn200AndNonEmpty() {
        Assertions.assertNotNull(salesResp, "No response for /sales");
        int status = salesResp.getStatusCode();
        if (status != 200) {
            System.err.println("---- /sales RESPONSE (status: " + status + ") ----");
            System.err.println(salesResp.getBody().asPrettyString());
        }
        Assertions.assertEquals(200, status, "Expected 200 for /sales");
        Assertions.assertTrue(salesResp.getContentType().contains("application/json"), "Expected JSON content type");

        List<Map<String, Object>> list = salesResp.jsonPath().getList("$");
        Assertions.assertNotNull(list, "Sales response should be a JSON array");
        if (list.size() == 0) {
            System.err.println("---- /sales RESPONSE BODY (empty list) ----");
            System.err.println(salesResp.getBody().asPrettyString());
        }
        Assertions.assertTrue(list.size() > 0, "Sales list should not be empty for dashboard");
        for (Map<String, Object> s : list) {
            Assertions.assertNotNull(s.get("id"), "Each sale should have an id");
        }
    }
}
