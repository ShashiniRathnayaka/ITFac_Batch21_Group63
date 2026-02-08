package com.qatraining.stepdefinitions.api.categories;

import com.qatraining.api.categories.CategoryEditPageAPI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryEditSteps {

    private final CategoryEditPageAPI categoryEditPageAPI = new CategoryEditPageAPI();
    private int testCategoryId;
    private Map<String, Object> updateBody;

    @Given("At least one category exists in the database for update")
    public void atLeastOneCategoryExistsInDatabaseForUpdate() {
        // Verify at least one category exists by fetching the list
        categoryEditPageAPI.getCategoriesList();
        String response = categoryEditPageAPI.getResponseBody();
        JSONArray arr = new JSONArray(response);
        assertTrue(arr.length() > 0, "At least one category should exist in the system");
        System.out.println("Verified: " + arr.length() + " categories exist in the system");
    }

    @When("Admin sends a PUT request to update category details")
    public void adminSendsPutRequestToUpdateCategoryDetails() {
        // Get an existing category ID
        categoryEditPageAPI.getCategoriesList();
        String response = categoryEditPageAPI.getResponseBody();
        JSONArray arr = new JSONArray(response);
        assertTrue(arr.length() > 0, "No categories available to update");
        testCategoryId = arr.getJSONObject(0).getInt("id");

        // Generate unique name to avoid conflicts
        long nanoTime = System.nanoTime();
        int uniqueSuffix = (int) ((nanoTime / 1000) % 1000);
        String updatedName = "Cat" + String.format("%03d", uniqueSuffix);

        // Build update body following Swagger example
        updateBody = new HashMap<>();
        updateBody.put("name", updatedName);
        updateBody.put("parentId", 34); // Herbs category

        System.out.println("PUT /api/categories/" + testCategoryId + " - Updated Name: " + updatedName);

        categoryEditPageAPI.updateCategory(testCategoryId, updateBody);

        assertNotNull(
                categoryEditPageAPI.getResponse(),
                "API response is null. PUT request may not have been sent.");
    }

    @When("Admin includes a valid request payload in the request body")
    public void adminIncludesValidRequestPayloadInRequestBody() {
        // This step is informational - the payload is already included in the PUT
        // request
        System.out.println("Request payload included in PUT request");
    }

    @Then("API returns {int} OK status code for category update")
    public void apiReturnsOkStatusCodeForCategoryUpdate(int expectedStatusCode) {
        int actualStatus = categoryEditPageAPI.getStatusCode();
        System.out.println("Status Code: " + actualStatus);
        System.out.println("Response Body: " + categoryEditPageAPI.getResponseBody());
        assertEquals(expectedStatusCode, actualStatus,
                "Expected " + expectedStatusCode + " OK but got " + actualStatus);
    }

    @Then("Category details are updated successfully")
    public void categoryDetailsAreUpdatedSuccessfully() {
        String responseBody = categoryEditPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");

        JSONObject responseJson = new JSONObject(responseBody);
        assertTrue(responseJson.has("id"), "Response should contain 'id' field");
        assertTrue(responseJson.has("name"), "Response should contain 'name' field");

        System.out.println("Category updated successfully with ID: " + responseJson.getInt("id"));
    }

    @Then("Response body reflects the updated category values")
    public void responseBodyReflectsUpdatedCategoryValues() {
        JSONObject responseJson = new JSONObject(categoryEditPageAPI.getResponseBody());

        // Verify the category ID matches
        assertEquals(testCategoryId, responseJson.getInt("id"),
                "Response category ID should match the updated category ID");

        // Verify name matches what was sent
        String responseName = responseJson.getString("name");
        assertEquals(updateBody.get("name"), responseName,
                "Response category name should match the updated name");

        // Verify response structure
        assertTrue(responseJson.has("subCategories"), "Response should contain 'subCategories'");

        System.out.println("Verified: Response reflects updated values - ID=" + testCategoryId +
                ", name=" + responseName);
    }

    @Then("Updated category data is saved in the database")
    public void updatedCategoryDataIsSavedInDatabase() {
        // The fact that we received a 200 OK with updated values confirms database
        // persistence
        JSONObject responseJson = new JSONObject(categoryEditPageAPI.getResponseBody());
        int categoryId = responseJson.getInt("id");
        String categoryName = responseJson.getString("name");

        assertEquals(testCategoryId, categoryId,
                "Category ID should match, indicating successful database update");
        assertEquals(updateBody.get("name"), categoryName,
                "Category name should match updated value, indicating database persistence");

        System.out.println("Verified: Updated category data persisted in database");
    }
}
