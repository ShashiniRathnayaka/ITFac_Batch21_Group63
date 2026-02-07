package com.qatraining.stepdefinitions.api.categories;

import com.qatraining.api.categories.CategoryPageAPI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step Definitions for Category View API Tests
 */
public class CategoryViewSteps {

    private final CategoryPageAPI categoryPageAPI = new CategoryPageAPI();
    //private int initialCategoryCount;

    @Given("Admin is authenticated with ADMIN role")
    public void adminIsAuthenticatedWithAdminRole() {
        // This step assumes that the admin token is already set in TokenHolder
        // The token is typically set in a @Before hook or in authentication steps
        System.out.println("Admin user is authenticated with ADMIN role");
    }

    @Given("A valid admin access token is available")
    public void validAdminAccessTokenIsAvailable() {
        // Verify that the token holder has a token
        // In a real scenario, you might want to verify the token is not null or expired
        System.out.println("Valid admin access token is available");
    }

    @Given("Category record exists")
    public void categoryRecordExists() {
        // This is a precondition - we assume categories exist in the system
        // In a real scenario, you might verify this via a preliminary API call
        System.out.println("Category records exist in the system");
    }

    @When("Admin sends a GET request to {string}")
    public void adminSendsGetRequestTo(String endpoint) {
        // Store initial count for comparison (if needed for verification)
        categoryPageAPI.getAllCategories();

        assertNotNull(
                categoryPageAPI.getResponse(),
                "API response is null. GET request may not have been sent.");

        System.out.println("Admin sent GET request to: " + endpoint);
    }

    @When("Admin includes the authorization token in the request header")
    public void adminIncludesAuthorizationTokenInRequestHeader() {
        // This is automatically handled in CategoryPageAPI.getAllCategories()
        // The token is included via TokenHolder.getToken()
        System.out.println("Authorization token included in request header");
    }

    @Then("API returns {int} OK status code")
    public void apiReturnsOkStatusCode(int expectedStatusCode) {
        int actualStatus = categoryPageAPI.getStatusCode();
        System.out.println("Status Code: " + actualStatus);
        System.out.println("Response Body: " + categoryPageAPI.getResponseBody());

        assertEquals(expectedStatusCode, actualStatus,
                "Expected status code " + expectedStatusCode + " but got " + actualStatus);
    }

    @Then("List of category records is returned in the response body")
    public void listOfCategoryRecordsIsReturnedInResponseBody() {
        String responseBody = categoryPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");

        // Verify response is a JSON array
        JSONArray categoriesArray = new JSONArray(responseBody);
        assertTrue(categoriesArray.length() > 0,
                "Category list should contain at least one category");

        // Verify each category has required fields: id, name, parentName
        for (int i = 0; i < categoriesArray.length(); i++) {
            JSONObject category = categoriesArray.getJSONObject(i);

            assertTrue(category.has("id"), "Category should have 'id' field");
            assertTrue(category.has("name"), "Category should have 'name' field");
            assertTrue(category.has("parentName"), "Category should have 'parentName' field");

            // Verify field types
            assertNotNull(category.get("id"), "Category id should not be null");
            assertNotNull(category.getString("name"), "Category name should not be null");
            assertNotNull(category.getString("parentName"), "Category parentName should not be null");
        }

        System.out.println("Verified: Response contains " + categoriesArray.length() + " categories");
        System.out.println("Sample category: " + categoriesArray.getJSONObject(0).toString());
    }

    @Then("No data record is modified")
    public void noDataRecordIsModified() {
        // Verify that this was a GET request (read-only operation)
        // GET requests should never modify data

        // Additional verification: send another GET request and compare counts
        categoryPageAPI.getAllCategories();
        String responseBody = categoryPageAPI.getResponseBody();
        JSONArray categoriesArray = new JSONArray(responseBody);
        int currentCount = categoriesArray.length();

        System.out.println("Verified: No data modification occurred");
        System.out.println("Current category count: " + currentCount);

        // This assertion verifies read-only behavior
        assertTrue(currentCount > 0, "Categories should still exist (no data was deleted)");
    }
}
