package com.qatraining.stepdefinitions.ui.categories;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.authentication.LoginPage;
import com.qatraining.pages.categories.CategoriesPage;
import com.qatraining.pages.categories.EditCategoryPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step Definitions for Edit Category UI Tests
 */
public class EditCategorySteps {

    private EditCategoryPage editCategoryPage;
    private CategoriesPage categoriesPage;
    private LoginPage loginPage;
    private String updatedCategoryName; // Store the updated category name for verification

    public EditCategorySteps() {
        this.editCategoryPage = new EditCategoryPage(PlaywrightDriverManager.getPage());
        this.categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());
        this.loginPage = new LoginPage(PlaywrightDriverManager.getPage());
    }

    @Given("At least one Category record exists")
    public void atLeastOneCategoryRecordExists() {
        // This is a precondition - we assume at least one category exists
        // In a real scenario, you might verify this via API or database
    }

    @When("the user clicks the Edit icon on a Category")
    public void userClicksEditIconOnCategory() {
        // Click the first category's edit icon
        categoriesPage.clickEditIconForFirstCategory();
    }

    @When("the user enters a new valid category name {string}")
    public void userEntersNewValidCategoryName(String categoryName) {
        // Make the category name unique while keeping it within 3-10 character limit
        // Use nanoTime for better uniqueness - take last 3 digits (000-999)
        long nanoTime = System.nanoTime();
        int uniqueSuffix = (int) ((nanoTime / 1000) % 1000); // 3 digits for uniqueness
        String suffix = String.format("%03d", uniqueSuffix); // Always 3 digits: 000-999

        int maxBaseLength = 10 - suffix.length();
        String baseName = categoryName.length() > maxBaseLength
                ? categoryName.substring(0, maxBaseLength)
                : categoryName;

        if (baseName.length() < 1) {
            baseName = "Edit";
        }

        String uniqueCategoryName = baseName + suffix;

        // Ensure the final name is between 3-10 characters
        if (uniqueCategoryName.length() < 3 || uniqueCategoryName.length() > 10) {
            throw new IllegalArgumentException(
                    "Category name '" + uniqueCategoryName + "' is not within 3-10 character limit. Length: "
                            + uniqueCategoryName.length());
        }

        editCategoryPage.updateCategoryName(uniqueCategoryName);
        updatedCategoryName = uniqueCategoryName; // Store for later verification
    }

    @When("the user changes the parent category to {string}")
    public void userChangesParentCategoryTo(String parentCategory) {
        // Use text-based selection instead of hardcoded IDs (best practice)
        editCategoryPage.selectParentCategoryByText(parentCategory);
    }

    @Then("System navigates to Edit Category page")
    public void systemNavigatesToEditCategoryPage() {
        Assertions.assertTrue(editCategoryPage.isEditCategoryPageLoaded(),
                "Edit Category page should be loaded");
    }

    @Then("Fields accepts the new input values")
    public void fieldsAcceptsNewInputValues() {
        // Verify that the field contains a value (either original or updated)
        String currentValue = editCategoryPage.getCategoryNameValue();
        Assertions.assertFalse(currentValue.isBlank(),
                "Category name field should contain a value");

        // If we updated the name, verify it's the new value
        if (updatedCategoryName != null) {
            Assertions.assertEquals(updatedCategoryName, currentValue,
                    "Category name field should contain the updated value");
        }
    }

    @Then("The updated record appears in the Category list")
    public void updatedRecordAppearsInCategoryList() {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());

        // Ensure we're on the categories page and it's fully loaded
        // Use a shorter timeout and check if we're already there
        String currentUrl = PlaywrightDriverManager.getPage().url();
        if (!currentUrl.contains("/ui/categories")) {
            PlaywrightDriverManager.getPage().waitForURL("**/ui/categories**",
                    new Page.WaitForURLOptions().setTimeout(10000));
        }
        PlaywrightDriverManager.getPage().waitForLoadState();

        // Wait for the table to be visible (confirms page is loaded)
        String tableSelector = "table tbody tr, .table tbody tr";
        PlaywrightDriverManager.getPage().locator(tableSelector).first().waitFor();
        PlaywrightDriverManager.getPage().waitForTimeout(1000);

        if (updatedCategoryName != null) {
            // Wait for search box to be visible before searching
            String searchBoxSelector = "input[placeholder*='Search'], input[type='search'], input[name='search']";
            PlaywrightDriverManager.getPage().locator(searchBoxSelector).waitFor();

            // Search for the updated category to ensure it's visible on the page
            System.out.println("Searching for updated category: " + updatedCategoryName);
            categoriesPage.searchForCategory(updatedCategoryName);
            PlaywrightDriverManager.getPage().waitForLoadState();
            PlaywrightDriverManager.getPage().waitForTimeout(1000);

            // Verify the category exists in the list
            boolean categoryFound = categoriesPage.isCategoryInList(updatedCategoryName);

            System.out.println("Updated category '" + updatedCategoryName + "' found: " + categoryFound);

            Assertions.assertTrue(categoryFound,
                    "Updated category '" + updatedCategoryName + "' should appear in the Categories list");
        } else {
            // If no updates were made, just verify the table has data
            Locator rows = PlaywrightDriverManager.getPage().locator(tableSelector);
            Assertions.assertTrue(rows.count() > 0, "Categories table should have data rows");
        }
    }

    @When("the user clicks the Edit icon for a category")
    public void userClicksEditIconForACategory() {
        categoriesPage.clickEditIconForFirstCategory();
    }

    @When("the user changes the value of the {string} field")
    public void userChangesValueOfField(String fieldName) {
        if (fieldName.equalsIgnoreCase("Category Name")) {
            // Enter a new category name with unique suffix to avoid conflicts
            // Use nanoTime for better uniqueness - 3 digits (000-999)
            long nanoTime = System.nanoTime();
            int uniqueSuffix = (int) ((nanoTime / 1000) % 1000);
            String updatedName = "Upd" + String.format("%03d", uniqueSuffix); // Fits in 6 chars
            editCategoryPage.updateCategoryName(updatedName);
        } else if (fieldName.equalsIgnoreCase("Parent Category")) {
            // Dynamically select a different parent category from available options
            // This avoids hard-coding IDs that may vary across environments
            editCategoryPage.selectDifferentParentCategory();
        }
    }

    @Then("Edit action is restricted")
    public void editActionIsRestricted() {
        // Verify that error message is displayed indicating restriction
        Assertions.assertTrue(editCategoryPage.isErrorMessageDisplayed(),
                "Error message should be displayed indicating edit action is restricted");
    }

    @Then("Error message is displayed: {int} Forbidden")
    public void errorMessageIsDisplayed(int statusCode) {
        PlaywrightDriverManager.getPage().waitForTimeout(1000); // Wait for error message
        Assertions.assertTrue(editCategoryPage.isErrorMessage403Forbidden(),
                "Error message should contain '403 Forbidden'");

        String errorMessage = editCategoryPage.getErrorMessage();
        System.out.println("Error message displayed: " + errorMessage);
        Assertions.assertTrue(errorMessage.contains(String.valueOf(statusCode)),
                "Error message should contain status code " + statusCode);
    }

    @Then("Category data is not updated")
    public void categoryDataIsNotUpdated() {
        // Verify we're still on the Edit Category page (not redirected to list)
        Assertions.assertTrue(editCategoryPage.isEditCategoryPageLoaded(),
                "Should remain on Edit Category page as update failed");

        // Another way to verify is to check that error message is present
        Assertions.assertTrue(editCategoryPage.isErrorMessageDisplayed(),
                "Error message should still be displayed, confirming update failed");
    }

    @Then("Edit icon is not visible and clickable")
    public void editIconIsNotVisibleAndClickable() {
        editCategoryPage = new EditCategoryPage(PlaywrightDriverManager.getPage());

        // Wait for page to fully load
        PlaywrightDriverManager.getPage().waitForLoadState();
        PlaywrightDriverManager.getPage().waitForTimeout(1000);

        // Check if edit icons are visible
        boolean editIconsVisible = editCategoryPage.areEditIconsVisible();

        // KNOWN BUG: Edit icons ARE visible to USER role (they should NOT be visible)
        Assertions.assertFalse(editIconsVisible,
                "Edit icons should NOT be visible for USER role, but they are visible (BACKEND BUG)");

        // Check if edit icons are clickable (additional validation)
        boolean editIconsClickable = editCategoryPage.areEditIconsClickable();
        Assertions.assertFalse(editIconsClickable,
                "Edit icons should NOT be clickable for USER role, but they are clickable (BACKEND BUG)");

        System.out.println("Verified: Edit icons are not visible and not clickable for USER role");
    }
}
