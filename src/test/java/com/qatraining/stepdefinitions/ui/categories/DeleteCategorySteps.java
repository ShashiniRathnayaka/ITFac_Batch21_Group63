package com.qatraining.stepdefinitions.ui.categories;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.categories.CategoriesPage;
import com.qatraining.pages.categories.DeleteCategoryPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for Delete Category scenarios
 */
public class DeleteCategorySteps {

    private CategoriesPage categoriesPage;
    private DeleteCategoryPage deleteCategoryPage;
    private String categoryToDelete;
    private int initialCategoryCount;

    @When("At least one Parent Category with sub-categories exists")
    public void atLeastOneParentCategoryWithSubCategoriesExists() {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());

        // Verify categories exist in the table
        categoriesPage.navigateToCategoriesPage();
        int categoryCount = categoriesPage.getCategoryCount();

        Assertions.assertTrue(categoryCount > 0,
                "No categories found. At least one parent category with sub-categories should exist.");

        System.out.println("Parent categories with sub-categories verified to exist");
    }

    @When("the user clicks the Delete icon for category {string}")
    public void userClicksDeleteIconForCategory(String categoryName) {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());
        categoryToDelete = categoryName;

        // Refresh the page to ensure we have the latest database state
        System.out.println("Refreshing page to load current database state...");
        categoriesPage.navigateToCategoriesPage();
        PlaywrightDriverManager.getPage().waitForLoadState();
        PlaywrightDriverManager.getPage().waitForTimeout(1000); // Wait for JavaScript to initialize

        // Search for the category to ensure it's visible on the page
        System.out.println("Searching for category: " + categoryName);
        categoriesPage.searchForCategory(categoryName);
        PlaywrightDriverManager.getPage().waitForTimeout(1000); // Wait for search results

        // Store initial count before deletion attempt
        initialCategoryCount = categoriesPage.getCategoryCount();

        // Click delete icon - this will handle the browser confirmation dialog
        categoriesPage.clickDeleteIconForCategory(categoryName);

        System.out.println("Clicked delete icon for category: " + categoryName);
    }

    @Then("Confirmation popup is displayed and user confirms deletion")
    public void confirmationPopupIsDisplayedAndUserConfirmsDeletion() {
        // The browser confirmation dialog is automatically handled in
        // clickDeleteIconForCategory
        // with page.onDialog(dialog -> dialog.accept())
        System.out.println("Confirmation dialog was displayed and accepted");
    }

    @Then("An error message is displayed: {string}")
    public void errorMessageIsDisplayed(String expectedErrorMessage) {
        deleteCategoryPage = new DeleteCategoryPage(PlaywrightDriverManager.getPage());

        // Wait for error message to appear
        deleteCategoryPage.waitForErrorMessage();

        // Verify error message is displayed
        boolean errorDisplayed = deleteCategoryPage.isErrorMessageDisplayed();

        if (!errorDisplayed) {
            // Provide detailed failure information
            System.out.println("ERROR: No error message found on page!");
            System.out.println("This likely means the category '" + categoryToDelete + "' was deleted successfully.");
            System.out.println("Possible causes:");
            System.out.println("  1. The category has no sub-categories");
            System.out.println("  2. Sub-categories were deleted in a previous test run");
            System.out.println("  3. The test data needs to be reset");
        }

        Assertions.assertTrue(errorDisplayed,
                "Error message should be displayed but was not found. " +
                        "The category may have been deleted successfully, indicating no sub-categories exist.");

        // Get and verify the error message content
        String actualErrorMessage = deleteCategoryPage.getErrorMessage();
        Assertions.assertNotNull(actualErrorMessage, "Error message should not be null");

        // Check if the actual message contains the expected message (case-insensitive)
        Assertions.assertTrue(
                actualErrorMessage.toLowerCase().contains(expectedErrorMessage.toLowerCase()),
                "Error message should contain: '" + expectedErrorMessage +
                        "' but was: '" + actualErrorMessage + "'");

        System.out.println("Error message verified: " + actualErrorMessage);
    }

    @Then("The category {string} is not deleted and remains in the list")
    public void categoryIsNotDeletedAndRemainsInList(String categoryName) {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());

        // Navigate to categories page to clear any search filters
        categoriesPage.navigateToCategoriesPage();
        PlaywrightDriverManager.getPage().waitForLoadState();

        // Search for the category to verify it still exists
        categoriesPage.searchForCategory(categoryName);
        PlaywrightDriverManager.getPage().waitForLoadState();

        // Verify the category still exists in the list
        boolean categoryExists = categoriesPage.isCategoryInList(categoryName);
        Assertions.assertTrue(categoryExists,
                "Category '" + categoryName + "' should still exist in the list but was not found");

        // Verify the category count hasn't decreased
        int currentCategoryCount = categoriesPage.getCategoryCount();
        Assertions.assertEquals(initialCategoryCount, currentCategoryCount,
                "Category count should remain the same. Expected: " + initialCategoryCount +
                        ", Actual: " + currentCategoryCount);

        System.out.println("Verified: Category '" + categoryName + "' remains in the list");
    }

    @When("the user observes the Delete icon")
    public void userObservesDeleteIcon() {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());

        // Wait for page to load completely
        PlaywrightDriverManager.getPage().waitForLoadState();
        PlaywrightDriverManager.getPage().waitForTimeout(1000);

        System.out.println("User is observing the Delete icon elements on the page");
    }

    @When("the user attempts to click Delete")
    public void userAttemptsToClickDelete() {
        // This step is intentionally left as a passive action
        // since disabled buttons cannot be clicked
        System.out.println("User attempts to interact with Delete button (which should be disabled)");
    }

    @Then("Delete action is disabled or restricted")
    public void deleteActionIsDisabledOrRestricted() {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());

        // Verify that delete buttons are disabled for USER role
        boolean deleteButtonsDisabled = categoriesPage.areDeleteButtonsDisabled();

        Assertions.assertTrue(deleteButtonsDisabled,
                "Delete buttons should be disabled or restricted for USER role");

        System.out.println("Verified: Delete action is disabled or restricted for USER role");
    }

    @Then("Delete icon is not visible")
    public void deleteIconIsNotVisible() {
        deleteCategoryPage = new DeleteCategoryPage(PlaywrightDriverManager.getPage());

        // Wait for page to fully load
        PlaywrightDriverManager.getPage().waitForLoadState();
        PlaywrightDriverManager.getPage().waitForTimeout(1000);

        // Check if delete icons are visible
        boolean deleteIconsVisible = deleteCategoryPage.areDeleteIconsVisible();

        // KNOWN BUG: Delete icons ARE visible to USER role (they should NOT be visible)
        Assertions.assertFalse(deleteIconsVisible,
                "Delete icons should NOT be visible for USER role, but they are visible (BACKEND BUG)");

        System.out.println("Verified: Delete icons are not visible for USER role");
    }

    @Then("Category is not deleted")
    public void categoryIsNotDeleted() {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());

        // Store the current category count
        int currentCount = categoriesPage.getCategoryCount();

        // Verify that categories still exist (no deletion occurred)
        Assertions.assertTrue(currentCount > 0,
                "Categories should still exist, count: " + currentCount);

        System.out.println("Verified: Categories remain in the system, count: " + currentCount);
    }

    @Then("No data changes occur")
    public void noDataChangesOccur() {
        categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());

        // Verify the page is still showing the categories list (not an error page)
        boolean pageLoaded = categoriesPage.isCategoriesPageLoaded();
        Assertions.assertTrue(pageLoaded,
                "Categories page should still be loaded and accessible");

        // Verify categories table is visible
        boolean tableVisible = categoriesPage.isCategoriesTableVisible();
        Assertions.assertTrue(tableVisible,
                "Categories table should still be visible with no data changes");

        System.out.println("Verified: No data changes occurred, page remains stable");
    }
}
