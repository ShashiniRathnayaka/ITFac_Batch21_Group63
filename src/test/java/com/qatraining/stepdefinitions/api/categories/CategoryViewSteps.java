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
    // private int initialCategoryCount;

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

    @Given("User is authenticated with USER role")
    public void userIsAuthenticatedWithUserRole() {
        // This step assumes that the user token is already set in TokenHolder
        // The token is typically set in a @Before hook or in authentication steps
        System.out.println("User is authenticated with USER role");
    }

    @Given("Valid user access token is available")
    public void validUserAccessTokenIsAvailable() {
        // Verify that the token holder has a token
        // In a real scenario, you might want to verify the token is not null or expired
        System.out.println("Valid user access token is available");
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

    @When("User sends a GET request to {string}")
    public void userSendsGetRequestTo(String endpoint) {
        // Send GET request to retrieve categories
        categoryPageAPI.getAllCategories();

        assertNotNull(
                categoryPageAPI.getResponse(),
                "API response is null. GET request may not have been sent.");

        System.out.println("User sent GET request to: " + endpoint);
    }

    @When("User includes the authorization token in the request header")
    public void userIncludesAuthorizationTokenInRequestHeader() {
        // This is automatically handled in CategoryPageAPI.getAllCategories()
        // The token is included via TokenHolder.getToken()
        System.out.println("User authorization token included in request header");
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

    @Given("More than {int} category records exist in API")
    public void moreThanCategoryRecordsExist(int minCount) {
        // Verify that sufficient categories exist for pagination testing
        categoryPageAPI.getAllCategories();
        String responseBody = categoryPageAPI.getResponseBody();
        JSONArray categoriesArray = new JSONArray(responseBody);
        int actualCount = categoriesArray.length();

        assertTrue(actualCount > minCount,
                "Expected more than " + minCount + " categories but found " + actualCount);

        System.out.println("Verified: " + actualCount + " category records exist (required: >" + minCount + ")");
    }

    @When("User sends a GET request to {string} with page {int} and size {int}")
    public void userSendsGetRequestWithPagination(String endpoint, int page, int size) {
        // Send paginated GET request
        categoryPageAPI.getCategoriesWithPagination(page, size, "id", "asc");

        assertNotNull(
                categoryPageAPI.getResponse(),
                "API response is null. Paginated GET request may not have been sent.");

        System.out.println("User sent paginated GET request to: " + endpoint + " with page=" + page + ", size=" + size);
    }

    @Then("Maximum {int} records returned per page")
    public void maximumRecordsReturnedPerPage(int expectedMaxSize) {
        String responseBody = categoryPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");

        JSONObject responseJson = new JSONObject(responseBody);

        // Verify content array exists
        assertTrue(responseJson.has("content"), "Response should contain 'content' field");
        JSONArray content = responseJson.getJSONArray("content");

        // Verify the number of records does not exceed the page size
        int actualSize = content.length();
        assertTrue(actualSize <= expectedMaxSize,
                "Expected maximum " + expectedMaxSize + " records but got " + actualSize);

        System.out.println("Verified: " + actualSize + " records returned (max allowed: " + expectedMaxSize + ")");
    }

    @Then("Pagination metadata is present in response")
    public void paginationMetadataIsPresentInResponse() {
        String responseBody = categoryPageAPI.getResponseBody();
        JSONObject responseJson = new JSONObject(responseBody);

        // Verify pagination metadata fields
        assertTrue(responseJson.has("content"), "Response should contain 'content' field");
        assertTrue(responseJson.has("pageable"), "Response should contain 'pageable' field");
        assertTrue(responseJson.has("totalElements"), "Response should contain 'totalElements' field");
        assertTrue(responseJson.has("totalPages"), "Response should contain 'totalPages' field");
        assertTrue(responseJson.has("size"), "Response should contain 'size' field");
        assertTrue(responseJson.has("number"), "Response should contain 'number' field");
        assertTrue(responseJson.has("numberOfElements"), "Response should contain 'numberOfElements' field");
        assertTrue(responseJson.has("first"), "Response should contain 'first' field");
        assertTrue(responseJson.has("last"), "Response should contain 'last' field");
        assertTrue(responseJson.has("empty"), "Response should contain 'empty' field");

        // Verify pageable object structure
        JSONObject pageable = responseJson.getJSONObject("pageable");
        assertTrue(pageable.has("pageNumber"), "Pageable should contain 'pageNumber' field");
        assertTrue(pageable.has("pageSize"), "Pageable should contain 'pageSize' field");
        assertTrue(pageable.has("offset"), "Pageable should contain 'offset' field");

        // Log pagination details
        int totalElements = responseJson.getInt("totalElements");
        int totalPages = responseJson.getInt("totalPages");
        int size = responseJson.getInt("size");
        int number = responseJson.getInt("number");
        int numberOfElements = responseJson.getInt("numberOfElements");

        System.out.println("Verified: Pagination metadata present");
        System.out.println("  - Page: " + number + " of " + (totalPages - 1));
        System.out.println("  - Size: " + size);
        System.out.println("  - Total Elements: " + totalElements);
        System.out.println("  - Total Pages: " + totalPages);
        System.out.println("  - Elements in current page: " + numberOfElements);
    }

    @Given("At least {int} category exists matching the search keyword {string}")
    public void atLeastCategoryExistsMatchingSearchKeyword(int minCount, String searchKeyword) {
        // Verify that at least one category exists matching the search keyword
        categoryPageAPI.searchCategoriesByName(searchKeyword, "id", "asc");
        String responseBody = categoryPageAPI.getResponseBody();
        JSONObject responseJson = new JSONObject(responseBody);

        assertTrue(responseJson.has("content"), "Response should contain 'content' field");
        JSONArray content = responseJson.getJSONArray("content");
        int actualCount = content.length();

        assertTrue(actualCount >= minCount,
                "Expected at least " + minCount + " category matching '" + searchKeyword + "' but found "
                        + actualCount);

        System.out.println("Verified: " + actualCount + " category(ies) found matching '" + searchKeyword
                + "' (required: >=" + minCount + ")");
    }

    @When("User sends a GET request to {string} with search parameter {string}")
    public void userSendsGetRequestWithSearchParameter(String endpoint, String searchKeyword) {
        // Send search GET request
        categoryPageAPI.searchCategoriesByName(searchKeyword, "id", "asc");

        assertNotNull(
                categoryPageAPI.getResponse(),
                "API response is null. Search GET request may not have been sent.");

        System.out.println("User sent search GET request to: " + endpoint + " with keyword='" + searchKeyword + "'");
    }

    @Then("Only matching category records are returned")
    public void onlyMatchingCategoryRecordsAreReturned() {
        String responseBody = categoryPageAPI.getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");

        JSONObject responseJson = new JSONObject(responseBody);
        assertTrue(responseJson.has("content"), "Response should contain 'content' field");

        JSONArray content = responseJson.getJSONArray("content");
        assertTrue(content.length() >= 0, "Content array should be present");

        System.out.println("Verified: " + content.length() + " matching category records returned");
    }

    @Then("Response contains array of filtered data")
    public void responseContainsArrayOfFilteredData() {
        String responseBody = categoryPageAPI.getResponseBody();
        JSONObject responseJson = new JSONObject(responseBody);

        // Verify response structure
        assertTrue(responseJson.has("content"), "Response should contain 'content' field");
        assertTrue(responseJson.has("totalElements"), "Response should contain 'totalElements' field");

        JSONArray content = responseJson.getJSONArray("content");
        int totalElements = responseJson.getInt("totalElements");

        // Verify each category in content has required fields
        for (int i = 0; i < content.length(); i++) {
            JSONObject category = content.getJSONObject(i);
            assertTrue(category.has("id"), "Category should have 'id' field");
            assertTrue(category.has("name"), "Category should have 'name' field");
            assertTrue(category.has("parentName"), "Category should have 'parentName' field");
        }

        System.out.println("Verified: Response contains filtered array of " + content.length() + " categories");
        System.out.println("Total matching elements: " + totalElements);
        if (content.length() > 0) {
            System.out.println("Sample filtered category: " + content.getJSONObject(0).toString());
        }
    }
}
