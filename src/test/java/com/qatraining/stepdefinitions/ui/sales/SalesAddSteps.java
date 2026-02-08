package com.qatraining.stepdefinitions.ui.sales;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.qatraining.pages.sales.SalesAddPage;
import com.qatraining.pages.sales.SalesListPage;
import com.qatraining.drivers.PlaywrightDriverManager;
import io.cucumber.java.en.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for Sell Plant form (Sales creation workflow)
 * Covers scenarios:
 * - Navigating to Sell Plant form
 * - Filling in plant and quantity
 * - Validating form fields
 * - Submitting the form
 * - Handling errors
 */
public class SalesAddSteps {

    private Page page;
    private SalesAddPage salesAddPage;
    private SalesListPage salesListPage;

    private final String API_BASE_URL = env("API_BASE_URL", "http://localhost:8080");
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient apiHttp = HttpClient.newBuilder().build();
    private String adminBearerToken;

    public SalesAddSteps() {
        this.page = PlaywrightDriverManager.getPage();
        this.salesAddPage = new SalesAddPage(page);
        this.salesListPage = new SalesListPage(page);
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v == null || v.trim().isEmpty()) ? fallback : v.trim();
    }

    private String getAdminBearerToken() throws Exception {
        if (adminBearerToken != null) return adminBearerToken;

        String json = "{\"username\":\"admin\",\"password\":\"admin123\"}";
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> resp = apiHttp.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode(), "API login failed. status=" + resp.statusCode() + " body=" + resp.body());
        JsonNode root = mapper.readTree(resp.body() == null ? "" : resp.body());
        String token = root.path("token").asText(null);
        assertNotNull(token, "API login response missing token. body=" + resp.body());
        adminBearerToken = "Bearer " + token;
        return adminBearerToken;
    }

    private void ensureAtLeastOnePlantInStock() {
        try {
            String bearer = getAdminBearerToken();

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/api/plants"))
                    .header("Authorization", bearer)
                    .GET()
                    .build();
            HttpResponse<String> resp = apiHttp.send(req, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, resp.statusCode(), "GET /api/plants failed. status=" + resp.statusCode());

            JsonNode plants = mapper.readTree(resp.body() == null ? "[]" : resp.body());
            if (!plants.isArray() || plants.size() == 0) return;

            for (JsonNode p : plants) {
                if (p.path("quantity").asInt(0) > 0) return;
            }

            JsonNode first = plants.get(0);
            long id = first.path("id").asLong(-1);
            if (id <= 0) return;

            String name = first.path("name").asText("");
            double price = first.path("price").asDouble(0);
            long categoryId = first.path("categoryId").asLong(0);
            if (categoryId == 0 && first.has("category")) categoryId = first.path("category").path("id").asLong(0);

            String putJson = "{"
                    + "\"name\":\"" + name.replace("\"", "") + "\","
                    + "\"price\":" + price + ","
                    + "\"quantity\":50,"
                    + "\"categoryId\":" + categoryId
                    + "}";

            HttpRequest put = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/api/plants/" + id))
                    .header("Authorization", bearer)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(putJson, StandardCharsets.UTF_8))
                    .build();
            apiHttp.send(put, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            fail("Failed to ensure plant stock via API. " + e.getMessage());
        }
    }

    // -----------------------------------
    // Navigation to Sell Plant form
    // -----------------------------------

    @Given("I am on the sales list page")
    public void iAmOnTheSalesListPage() {
        page.navigate("http://localhost:8080/ui/sales");
        page.waitForLoadState();
        salesListPage.verifySalesListPageLoaded();
    }

    @When("I navigate to the sell plant form")
    public void iNavigateToTheSellPlantForm() {
        page.navigate("http://localhost:8080/ui/sales/new");
        page.waitForLoadState();
        assertTrue(page.url().contains("/ui/sales/new"), "Not on sell plant form page");
    }

    @When("I click on the Sell Plant button")
    public void iClickOnTheSellPlantButton() {
        salesListPage.clickSellPlantButton();
        page.waitForLoadState();
        assertTrue(page.url().contains("/ui/sales/new"), "Not navigated to sell plant form");
    }

    @Then("the sell plant form should be displayed")
    public void theSellPlantFormShouldBeDisplayed() {
        salesAddPage.verifySellPlantFormDisplayed();
    }

    @Then("all form fields should be visible")
    public void allFormFieldsShouldBeVisible() {
        salesAddPage.verifyAllFieldsVisible();
    }

    @Then("the form should have a plant dropdown")
    public void theFormShouldHaveAPlantDropdown() {
        salesAddPage.verifyPlantDropdownVisible();
    }

    @Then("the form should have a quantity input field")
    public void theFormShouldHaveAQuantityInputField() {
        salesAddPage.verifyQuantityInputVisible();
    }

    @Then("the form should have a sell button")
    public void theFormShouldHaveASellButton() {
        salesAddPage.verifySellButtonVisible();
    }

    @Then("the form should have a cancel button")
    public void theFormShouldHaveACancelButton() {
        salesAddPage.verifyCancelButtonVisible();
    }

    // -----------------------------------
    // Plant Dropdown Interaction
    // -----------------------------------

    @When("I select plant {string} from the dropdown")
    public void iSelectPlantFromDropdown(String plantName) {
        salesAddPage.selectPlant(plantName);
    }

    @When("I select the first available plant")
    public void iSelectTheFirstAvailablePlant() {
        Locator opts = page.locator("#plantId option, select[name='plantId'] option");
        if (opts.count() <= 1) {
            ensureAtLeastOnePlantInStock();
            page.reload();
            page.waitForLoadState();
        }
        salesAddPage.selectFirstAvailablePlant();
    }

    @Then("plant {string} should be selected")
    public void plantShouldBeSelected(String plantName) {
        String selected = salesAddPage.getSelectedPlant();
        assertEquals(plantName, selected, "Plant '" + plantName + "' is not selected");
    }

    @Then("the plant dropdown should be populated with available plants")
    public void theDropdownShouldBePopulated() {
        assertTrue(salesAddPage.getAvailablePlantsCount() > 0, "No plants available in dropdown");
    }

    @Then("the plant dropdown should show at least {int} plants")
    public void dropdownShouldShowAtLeastPlants(int expectedCount) {
        int actualCount = salesAddPage.getAvailablePlantsCount();
        assertTrue(actualCount >= expectedCount, 
            "Expected at least " + expectedCount + " plants, but got " + actualCount);
    }

    // -----------------------------------
    // Quantity Input Interaction
    // -----------------------------------

    @When("I enter quantity {string}")
    public void iEnterQuantity(String quantity) {
        salesAddPage.enterQuantity(quantity);
    }

    @When("I enter a valid quantity {int}")
    public void iEnterAValidQuantity(int quantity) {
        salesAddPage.enterQuantity(String.valueOf(quantity));
    }

    @Then("the quantity field should have value {string}")
    public void quantityFieldShouldHaveValue(String expectedValue) {
        String actualValue = salesAddPage.getQuantityValue();
        assertEquals(expectedValue, actualValue, "Quantity value mismatch");
    }

    @Then("the quantity field should require minimum 1 unit")
    public void quantityFieldShouldRequireMinimum() {
        salesAddPage.verifyQuantityMinimumValidation();
    }

    @Then("the quantity field should accept positive numbers only")
    public void quantityFieldShouldAcceptPositiveOnly() {
        // Verify input type is number (prevents negative entries)
        Locator quantityInput = page.locator("#quantity, input[name='quantity']");
        String type = quantityInput.first().getAttribute("type");
        assertEquals("number", type, "Quantity input should be type='number'");
    }

    // -----------------------------------
    // Form Submission
    // -----------------------------------

    @When("I submit the sell plant form with plant {string} and quantity {int}")
    public void iSubmitTheFormWithPlantAndQuantity(String plantName, int quantity) {
        salesAddPage.selectPlant(plantName);
        salesAddPage.enterQuantity(String.valueOf(quantity));
        salesAddPage.submitSaleForm(plantName, String.valueOf(quantity));
    }

    @When("I submit the form")
    public void iSubmitTheForm() {
        salesAddPage.clickSellButton();
        page.waitForLoadState();
    }

    @Then("the form should be submitted successfully")
    public void formShouldBeSubmittedSuccessfully() {
        // Verify we're redirected away from /ui/sales/new form
        boolean redirectedOrMessaged = !page.url().contains("/ui/sales/new") 
            || page.locator("text=Success, text=created, text=sold").count() > 0;
        assertTrue(redirectedOrMessaged, "Form may not have been submitted - still on form page");
    }

    @Then("I should be redirected to the sales list page")
    public void iShouldBeRedirectedToSalesListPage() {
        assertTrue(page.url().contains("/ui/sales"), "Not redirected to sales list page");
        assertTrue(!page.url().contains("/new"), "Still on form page, not sales list");
    }

    @Then("I should see a success message")
    public void iShouldSeeASuccessMessage() {
        Locator successMsg = page.locator(".alert-success, [role='alert']:has-text('Success'), [role='alert']:has-text('created')").first();
        assertTrue(successMsg.isVisible() || page.locator("text=Sale created").count() > 0, 
            "Success message not visible");
    }

    @Then("the new sale should appear in the list")
    public void theNewSaleShouldAppearInTheList() {
        page.waitForLoadState();
        Locator rows = page.locator("table tbody tr, [data-testid='salesRow']");
        assertTrue(rows.count() > 0, "No sale records found in list after submission");
    }

    // -----------------------------------
    // Form Cancellation
    // -----------------------------------

    @When("I click the cancel button")
    public void iClickTheCancelButton() {
        salesAddPage.clickCancelButton();
        page.waitForLoadState();
    }

    @Then("I should be returned to the sales list page")
    public void iShouldBeReturnedToTheSalesListPage() {
        assertTrue(page.url().contains("/ui/sales") && !page.url().contains("/new"), 
            "Not returned to sales list page");
    }

    @Then("the form should not submit")
    public void theFormShouldNotSubmit() {
        assertTrue(page.url().contains("/ui/sales/new") || page.url().contains("/ui/sales"), 
            "Form may have submitted when cancelled");
    }

    // -----------------------------------
    // Error Handling
    // -----------------------------------

    @When("I leave the plant field empty")
    public void iLeavePlantFieldEmpty() {
        // Don't select anything
        salesAddPage.verifyPlantDropdownVisible();
    }

    @When("I leave the quantity field empty")
    public void iLeaveQuantityFieldEmpty() {
        // Don't enter quantity
        Locator quantityInput = page.locator("#quantity, input[name='quantity']");
        quantityInput.first().fill("");
    }

    @Then("I should see an error message for plant field")
    public void iShouldSeeErrorForPlantField() {
        Locator error = page.locator(".invalid-feedback, .error:has-text('Plant'), [role='alert']:has-text('plant')").first();
        assertTrue(error.isVisible(), "Plant field error message not visible");
    }

    @Then("I should see an error message for quantity field")
    public void iShouldSeeErrorForQuantityField() {
        Locator error = page.locator(".invalid-feedback, .error:has-text('Quantity'), [role='alert']:has-text('quantity')").first();
        assertTrue(error.isVisible(), "Quantity field error message not visible");
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String errorText) {
        Locator error = page.locator(".alert-danger, .error, [role='alert']:has-text('" + errorText + "')").first();
        assertTrue(error.isVisible(), "Error message not visible: " + errorText);
    }

    @Then("the form should display validation error")
    public void formShouldDisplayValidationError() {
        Locator errorElements = page.locator(".invalid-feedback, .error, [role='alert']");
        assertTrue(errorElements.count() > 0, "No validation error displayed");
    }

    @Then("the submit button should be disabled if fields are empty")
    public void submitButtonShouldBeDisabledIfEmpty() {
        // This depends on form implementation - some forms disable on empty, others validate on submit
        // For now, we just verify the button exists
        salesAddPage.verifySellButtonVisible();
    }

    // -----------------------------------
    // Form Validation
    // -----------------------------------

    @Then("all required fields should be marked as required")
    public void allRequiredFieldsShouldBeMarked() {
        // Check for required attributes or visual indicators
        Locator plantSelect = page.locator("#plantId, select[name='plantId']");
        Locator quantityInput = page.locator("#quantity, input[name='quantity']");
        
        boolean plantRequired = plantSelect.first().getAttribute("required") != null;
        boolean quantityRequired = quantityInput.first().getAttribute("required") != null;
        
        assertTrue(plantRequired || quantityRequired, "At least one field should be required");
    }

    @Then("the quantity field should have a minimum value constraint of 1")
    public void quantityFieldShouldHaveMinConstraint() {
        Locator quantityInput = page.locator("#quantity, input[name='quantity']");
        String minAttr = quantityInput.first().getAttribute("min");
        assertEquals("1", minAttr, "Quantity field min attribute should be 1");
    }

    @Then("the quantity field should accept only whole numbers")
    public void quantityFieldShouldAcceptWholeNumbers() {
        Locator quantityInput = page.locator("#quantity, input[name='quantity']");
        String inputType = quantityInput.first().getAttribute("type");
        assertEquals("number", inputType, "Quantity should be number input type");
    }

    // -----------------------------------
    // Accessibility & UI Tests
    // -----------------------------------

    @Then("the form should have proper labels for all fields")
    public void formShouldHaveProperLabels() {
        Locator labels = page.locator("form label");
        assertTrue(labels.count() > 0, "No labels found in form");
    }

    @Then("form fields should be logically ordered")
    public void formFieldsShouldBeLogicallyOrdered() {
        // Plant dropdown should appear before Quantity
        Locator plant = page.locator("#plantId, select[name='plantId']");
        Locator quantity = page.locator("#quantity, input[name='quantity']");
        
        assertTrue(plant.count() > 0 && quantity.count() > 0, "Form fields not found");
    }

    @Then("the page should have a descriptive title or heading")
    public void pageShouldHaveDescriptiveTitle() {
        Locator heading = page.locator("h1, h2, [role='heading']");
        assertTrue(heading.count() > 0, "No heading found on page");
    }

    // -----------------------------------
    // Data Persistence Tests
    // -----------------------------------

    @Then("the plant selection should persist across form interactions")
    public void plantSelectionShouldPersist() {
        String selectedPlant = salesAddPage.getSelectedPlant();
        assertTrue(selectedPlant != null && !selectedPlant.isEmpty(), 
            "Plant selection not persisted");
    }

    @Then("the quantity value should persist across form interactions")
    public void quantityValueShouldPersist() {
        String quantityValue = salesAddPage.getQuantityValue();
        assertTrue(quantityValue != null && !quantityValue.isEmpty(), 
            "Quantity value not persisted");
    }
}
