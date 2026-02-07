package com.qatraining.stepdefinitions.api.categories;

import com.qatraining.api.categories.CategoryDeletePageAPI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDeleteSteps {

    private final CategoryDeletePageAPI categoryDeletePageAPI = new CategoryDeletePageAPI();
    private int deletedCategoryId;
    private String deletedCategoryName;

    @Given("Category exists with no sub categories linked")
    public void categoryExistsWithNoSubCategoriesLinked() {
        // Create a fresh category specifically for deletion to ensure no dependencies
        long nanoTime = System.nanoTime();
        int uniqueSuffix = (int) ((nanoTime / 1000) % 1000);
        deletedCategoryName = "Del" + String.format("%03d", uniqueSuffix);

        // Build request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", deletedCategoryName);
        requestBody.put("parent", null); // Main category with no parent
        requestBody.put("subCategories", new java.util.ArrayList<>());

        System.out.println("Creating category for deletion test: " + deletedCategoryName);

        categoryDeletePageAPI.createCategory(requestBody);

        // Verify category was created successfully
        int statusCode = categoryDeletePageAPI.getStatusCode();
        assertEquals(201, statusCode, "Category creation should return 201 Created");

        // Extract the created category ID from response
        String responseBody = categoryDeletePageAPI.getResponseBody();
        JSONObject responseJson = new JSONObject(responseBody);
        deletedCategoryId = responseJson.getInt("id");

        System.out.println("Created category without dependencies: ID=" + deletedCategoryId +
                ", Name=" + deletedCategoryName);
    }

    @When("Admin sends a DELETE request to remove the category")
    public void adminSendsDeleteRequestToRemoveCategory() {
        System.out.println("DELETE /api/categories/" + deletedCategoryId);

        categoryDeletePageAPI.deleteCategory(deletedCategoryId);

        assertNotNull(
                categoryDeletePageAPI.getResponse(),
                "API response is null. DELETE request may not have been sent.");
    }

    @Then("API returns {int} No Content status code for delete")
    public void apiReturnsNoContentStatusCodeForDelete(int expectedStatusCode) {
        int actualStatus = categoryDeletePageAPI.getStatusCode();
        System.out.println("Status Code: " + actualStatus);
        String responseBody = categoryDeletePageAPI.getResponseBody();
        System.out.println("Response Body: " + (responseBody.isEmpty() ? "(empty - 204 No Content)" : responseBody));

        // Accept both 200 OK and 204 No Content
        assertTrue(actualStatus == 200 || actualStatus == 204,
                "Expected 200 OK or 204 No Content but got " + actualStatus);
    }

    @Then("Category is deleted successfully from the system")
    public void categoryIsDeletedSuccessfullyFromSystem() {
        int statusCode = categoryDeletePageAPI.getStatusCode();
        assertTrue(statusCode == 200 || statusCode == 204,
                "Status code should be 200 or 204, indicating successful deletion");

        System.out.println("Category ID " + deletedCategoryId + " deleted successfully");
    }

    @Then("Deleted category no longer exists in the database")
    public void deletedCategoryNoLongerExistsInDatabase() {
        // Fetch all categories again and verify the deleted category is not present
        categoryDeletePageAPI.getCategoriesList();
        String response = categoryDeletePageAPI.getResponseBody();
        JSONArray categories = new JSONArray(response);

        boolean categoryStillExists = false;
        for (int i = 0; i < categories.length(); i++) {
            JSONObject category = categories.getJSONObject(i);
            if (category.getInt("id") == deletedCategoryId) {
                categoryStillExists = true;
                break;
            }
        }

        assertFalse(categoryStillExists,
                "Category with ID " + deletedCategoryId + " should not exist after deletion");

        System.out.println("Verified: Category ID " + deletedCategoryId + " no longer exists in database");
    }

    @Then("No error message is returned for delete operation")
    public void noErrorMessageIsReturnedForDeleteOperation() {
        int statusCode = categoryDeletePageAPI.getStatusCode();

        // 204 typically has no body, 200 may have a body but should not be an error
        assertTrue(statusCode == 200 || statusCode == 204,
                "Status code indicates successful operation (200 or 204)");

        String responseBody = categoryDeletePageAPI.getResponseBody();

        // If there's a response body, verify it's not an error response
        if (responseBody != null && !responseBody.isEmpty()) {
            // Check it doesn't contain error indicators
            assertFalse(responseBody.contains("\"error\""),
                    "Response should not contain error field");
            assertFalse(responseBody.contains("\"status\":400"),
                    "Response should not indicate 400 error");
            assertFalse(responseBody.contains("\"status\":403"),
                    "Response should not indicate 403 error");
            assertFalse(responseBody.contains("\"status\":404"),
                    "Response should not indicate 404 error");
        }

        System.out.println("Verified: No error message returned");
    }
}
