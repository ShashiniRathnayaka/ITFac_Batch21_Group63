package com.qatraining.stepdefinitions.ui.categories;

import com.microsoft.playwright.Locator;
import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.authentication.LoginPage;
import com.qatraining.pages.categories.AddCategoryPage;
import com.qatraining.pages.categories.CategoriesPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step Definitions for Add Main Category UI Tests
 */
public class AddMainCategorySteps {

    private AddCategoryPage addCategoryPage;
    private CategoriesPage categoriesPage;
    private LoginPage loginPage;
    private String lastEnteredCategoryName; // Store the category name for verification

    public AddMainCategorySteps() {
        this.addCategoryPage = new AddCategoryPage(PlaywrightDriverManager.getPage());
        this.categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());
        this.loginPage = new LoginPage(PlaywrightDriverManager.getPage());
    }

    @When("the user clicks the {string} button")
    public void userClicksButton(String buttonText) {
        if (buttonText.equals("Add A Category")) {
            // Click using the specific locator for Add A Category button
            PlaywrightDriverManager.getPage().click("a[href='/ui/categories/add']");
            PlaywrightDriverManager.getPage().waitForLoadState();
        }
    }

    @When("the user enters valid category name {string} in the Category Name field")
    public void userEntersValidCategoryName(String categoryName) {
        // Make the category name unique while keeping it within 3-10 character limit
        // Use nanoTime for better uniqueness - take last 3 digits (000-999)
        long nanoTime = System.nanoTime();
        int uniqueSuffix = (int) ((nanoTime / 1000) % 1000); // 3 digits for uniqueness
        String suffix = String.format("%03d", uniqueSuffix); // Always 3 digits: 000-999

        // Calculate how many chars we can use from the base name (max 10 - 3 = 7)
        int maxBaseLength = 10 - suffix.length(); // Always 7
        String baseName = categoryName.length() > maxBaseLength
                ? categoryName.substring(0, maxBaseLength)
                : categoryName;

        // If base name is too short, pad it to ensure minimum length
        if (baseName.length() < 1) {
            baseName = "Cat"; // Fallback base name
        }

        String uniqueCategoryName = baseName + suffix;

        // Ensure the final name is between 3-10 characters
        if (uniqueCategoryName.length() < 3 || uniqueCategoryName.length() > 10) {
            throw new IllegalArgumentException(
                    "Category name '" + uniqueCategoryName + "' is not within 3-10 character limit. Length: "
                            + uniqueCategoryName.length());
        }

        addCategoryPage.enterCategoryName(uniqueCategoryName);
        lastEnteredCategoryName = uniqueCategoryName; // Store for later verification
    }

    @When("the user leaves the Parent Category dropdown empty")
    public void userLeavesParentCategoryEmpty() {
        addCategoryPage.leaveParentCategoryEmpty();
    }

    @When("the user clicks the {string} button on the form")
    public void userClicksFormButton(String buttonText) {
        if (buttonText.equals("Save")) {
            addCategoryPage.clickSaveButton();
        } else if (buttonText.equals("Cancel")) {
            addCategoryPage.clickCancelButton();
        }
    }

    @Then("System navigates to Add Category page")
    public void systemNavigatesToAddCategoryPage() {
        Assertions.assertTrue(addCategoryPage.isAddCategoryPageLoaded(),
                "Add Category page should be loaded");
    }

    @Then("Field accepts the input value")
    public void fieldAcceptsInputValue() {
        String enteredValue = addCategoryPage.getCategoryNameValue();
        Assertions.assertFalse(enteredValue.isBlank(),
                "Category name field should contain a value");
    }

    @Then("System redirects back to the Categories page")
    public void systemRedirectsBackToCategoriesPage() {
        // Wait for navigation to complete
        PlaywrightDriverManager.getPage().waitForLoadState();
        PlaywrightDriverManager.getPage().waitForTimeout(2000);

        String currentUrl = PlaywrightDriverManager.getPage().url();

        // If still on add page, there might be a validation error
        if (currentUrl.contains("/ui/categories/add")) {
            // Check for validation errors on the page
            var alertElements = PlaywrightDriverManager.getPage()
                    .locator(".alert, .error, .invalid-feedback, .text-danger");
            int alertCount = alertElements.count();

            String errorDetails = "";
            if (alertCount > 0) {
                for (int i = 0; i < Math.min(alertCount, 5); i++) {
                    try {
                        String alertText = alertElements.nth(i).innerText().trim();
                        if (!alertText.isEmpty()) {
                            errorDetails += "\n  Alert " + i + ": " + alertText;
                        }
                    } catch (Exception e) {
                        // Ignore if can't read alert
                    }
                }
            }

            // Check form validation state
            var invalidFields = PlaywrightDriverManager.getPage().locator("input.is-invalid, select.is-invalid");
            int invalidCount = invalidFields.count();
            if (invalidCount > 0) {
                errorDetails += "\n  Invalid fields found: " + invalidCount;
            }

            System.out.println("ERROR: Page did not redirect. Current URL: " + currentUrl);
            System.out.println("Category name used: " + lastEnteredCategoryName);
            System.out.println("Validation errors found: " + errorDetails);

            Assertions.fail("Page did not redirect to categories list. Still on Add Category page. " +
                    "Current URL: " + currentUrl + "\n" +
                    "Category name: " + lastEnteredCategoryName +
                    (errorDetails.isEmpty() ? "\nNo validation errors detected on page."
                            : "\nValidation errors:" + errorDetails));
        }

        Assertions.assertTrue(categoriesPage.isCategoriesPageLoaded(),
                "Should be redirected back to Categories page. Current URL: " + currentUrl);
    }

    @Then("A Success message is displayed")
    public void successMessageIsDisplayed() {
        // Wait a moment for the success message to appear
        PlaywrightDriverManager.getPage().waitForTimeout(1000);

        boolean messageDisplayed = addCategoryPage.isSuccessMessageDisplayed();
        Assertions.assertTrue(messageDisplayed,
                "Success message should be displayed after adding category");
    }

    @Then("The newly added category {string} appears in the list")
    public void newlyAddedCategoryAppearsInList(String categoryName) {
        // Wait for the table to load
        PlaywrightDriverManager.getPage().waitForTimeout(1000);

        // Use the last entered category name (which includes the unique timestamp)
        String searchName = lastEnteredCategoryName != null ? lastEnteredCategoryName : categoryName;

        // Search for the category name in the table
        String tableSelector = "table tbody tr, .table tbody tr";
        Locator rows = PlaywrightDriverManager.getPage().locator(tableSelector);

        boolean categoryFound = false;
        int rowCount = rows.count();

        for (int i = 0; i < rowCount; i++) {
            String rowText = rows.nth(i).textContent();
            if (rowText != null && rowText.contains(searchName)) {
                categoryFound = true;
                break;
            }
        }

        Assertions.assertTrue(categoryFound,
                "Newly added category '" + searchName + "' should appear in the Categories list");
    }
}
