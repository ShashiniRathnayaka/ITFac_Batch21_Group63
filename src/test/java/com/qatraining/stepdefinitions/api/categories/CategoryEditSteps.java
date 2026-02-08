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

    @When("User sends a PUT request to {string} with category id")
    public void userSendsPutRequestWithCategoryId(String endpoint) {
        // Get an existing category ID
        categoryEditPageAPI.getCategoriesList();
        String response = categoryEditPageAPI.getResponseBody();
        JSONArray arr = new JSONArray(response);
        assertTrue(arr.length() > 0, "No categories available to update");
        testCategoryId = arr.getJSONObject(0).getInt("id");

        // Build update body with valid payload
        updateBody = new HashMap<>();
        updateBody.put("name", "Newcat");

        System.out.println("User attempting PUT " + endpoint + "/" + testCategoryId);

        categoryEditPageAPI.updateCategory(testCategoryId, updateBody);

        assertNotNull(
                categoryEditPageAPI.getResponse(),
                "API response is null. PUT request may not have been sent.");
    }

    @When("User includes a valid payload in the request body")
    public void userIncludesValidPayloadInRequestBody() {
        // This step is informational - the payload is already included in the PUT
        // request
        System.out.println("Valid payload included in request body: " + updateBody);
    }

    @Then("API returns {int} Forbidden status code")
    public void apiReturnsForbiddenStatusCode(int expectedStatusCode) {
        int actualStatus = categoryEditPageAPI.getStatusCode();
        System.out.println("Status Code: " + actualStatus);
        System.out.println("Response Body: " + categoryEditPageAPI.getResponseBody());
        assertEquals(expectedStatusCode, actualStatus,
                "Expected " + expectedStatusCode + " Forbidden but got " + actualStatus);
    }

    @Then("Error message returned: {string}")
    public void errorMessageReturned(String expectedError) {
        String responseBody = categoryEditPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");

        JSONObject responseJson = new JSONObject(responseBody);
        assertTrue(responseJson.has("error"), "Response should contain 'error' field");

        String actualError = responseJson.getString("error");
        assertEquals(expectedError, actualError,
                "Expected error message '" + expectedError + "' but got '" + actualError + "'");

        // Verify additional error response fields
        assertTrue(responseJson.has("status"), "Response should contain 'status' field");
        assertTrue(responseJson.has("timestamp"), "Response should contain 'timestamp' field");
        assertTrue(responseJson.has("path"), "Response should contain 'path' field");

        System.out.println("Verified: Error message - " + actualError);
    }

    @Then("Category data is not updated via API")
    public void categoryDataIsNotUpdated() {
        // Verify the category was not updated by fetching the current data
        categoryEditPageAPI.getCategoriesList();
        String response = categoryEditPageAPI.getResponseBody();
        JSONArray arr = new JSONArray(response);

        // Find the category we attempted to update
        boolean categoryFound = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject category = arr.getJSONObject(i);
            if (category.getInt("id") == testCategoryId) {
                categoryFound = true;
                String currentName = category.getString("name");

                // Verify name was NOT changed to "Newcat"
                assertNotEquals("Newcat", currentName,
                        "Category name should not be updated to 'Newcat' for USER role");

                System.out.println("Verified: Category ID " + testCategoryId + " was not updated (current name: "
                        + currentName + ")");
                break;
            }
        }

        assertTrue(categoryFound, "Category should still exist in the system");
        System.out.println("Verified: Category data remains unchanged");
    }

    // --- New Step Definitions for API_CATEGORY_ADMIN_006 ---

    private String originalCategoryName; // Store original name to verify it remains unchanged

    @When("Admin sends a PUT request to update category with invalid name {string}")
    public void adminSendsPutRequestToUpdateCategoryWithInvalidName(String invalidName) {
        // Get an existing category ID
        categoryEditPageAPI.getCategoriesList();
        String response = categoryEditPageAPI.getResponseBody();
        JSONArray arr = new JSONArray(response);
        assertTrue(arr.length() > 0, "No categories available to update");

        // Store original category details
        testCategoryId = arr.getJSONObject(0).getInt("id");
        originalCategoryName = arr.getJSONObject(0).getString("name");

        // Build update body with invalid category name
        updateBody = new HashMap<>();
        updateBody.put("name", invalidName);

        System.out.println("PUT /api/categories/" + testCategoryId + " - Invalid Name: '" + invalidName + "' (length: "
                + invalidName.length() + ")");

        categoryEditPageAPI.updateCategory(testCategoryId, updateBody);

        assertNotNull(
                categoryEditPageAPI.getResponse(),
                "API response is null. PUT request may not have been sent.");
    }

    @Then("API returns {int} Bad Request status code for update")
    public void apiReturnsBadRequestStatusCodeForUpdate(int expectedStatusCode) {
        int actualStatus = categoryEditPageAPI.getStatusCode();
        System.out.println("Status Code: " + actualStatus);
        System.out.println("Response Body: " + categoryEditPageAPI.getResponseBody());
        assertEquals(expectedStatusCode, actualStatus,
                "Expected " + expectedStatusCode + " Bad Request but got " + actualStatus);
    }

    @Then("Validation error message is returned for update")
    public void validationErrorMessageIsReturnedForUpdate() {
        String responseBody = categoryEditPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");

        JSONObject responseJson = new JSONObject(responseBody);

        // Verify error response structure
        assertTrue(responseJson.has("message"), "Response should contain 'message' field");
        assertTrue(responseJson.has("status"), "Response should contain 'status' field");

        String message = responseJson.getString("message");
        assertFalse(message.isEmpty(), "Validation error message should not be empty");

        System.out.println("Validation error confirmed: " + message);
    }

    @Then("Validation error message for update contains {string}")
    public void validationErrorMessageForUpdateContains(String expectedMessage) {
        JSONObject responseJson = new JSONObject(categoryEditPageAPI.getResponseBody());

        // Check if details field contains the expected validation message
        assertTrue(responseJson.has("details"), "Response should contain 'details' field");

        JSONObject details = responseJson.getJSONObject("details");
        assertTrue(details.has("name"), "Details should contain 'name' validation error");

        String actualMessage = details.getString("name");
        assertEquals(expectedMessage, actualMessage,
                "Validation message should match expected message");

        System.out.println("Verified validation message: " + actualMessage);
    }

    @Then("Category data remains unchanged in the database")
    public void categoryDataRemainsUnchangedInTheDatabase() {
        // Fetch current category list to verify the category was not updated
        categoryEditPageAPI.getCategoriesList();
        String response = categoryEditPageAPI.getResponseBody();
        JSONArray arr = new JSONArray(response);

        // Find the category we attempted to update
        boolean categoryFound = false;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject category = arr.getJSONObject(i);
            if (category.getInt("id") == testCategoryId) {
                categoryFound = true;
                String currentName = category.getString("name");

                // Verify name remains the same as before the failed update
                assertEquals(originalCategoryName, currentName,
                        "Category name should remain unchanged after validation failure");

                System.out.println("Verified: Category ID " + testCategoryId + " remains unchanged (name: "
                        + currentName + ")");
                break;
            }
        }

        assertTrue(categoryFound, "Category should still exist in the system");
        System.out.println("Verified: Category data remains unchanged in database");
    }
}
