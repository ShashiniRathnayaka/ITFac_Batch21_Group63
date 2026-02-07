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
 * Step Definitions for Add Sub Category UI Tests
 */
public class AddSubCategorySteps {

    private AddCategoryPage addCategoryPage;
    private CategoriesPage categoriesPage;
    private LoginPage loginPage;
    private String lastEnteredCategoryName; // Store the category name for verification
    private String selectedParentCategoryText; // Store parent category text for verification

    public AddSubCategorySteps() {
        this.addCategoryPage = new AddCategoryPage(PlaywrightDriverManager.getPage());
        this.categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());
        this.loginPage = new LoginPage(PlaywrightDriverManager.getPage());
    }

    @Given("At least one Main Category record exists")
    public void atLeastOneMainCategoryRecordExists() {
        // This is a precondition - we assume main categories exist in the system
        // In a real scenario, you might verify this via API or database
    }

    @When("the user enters valid sub-category name {string} in the Category Name field")
    public void userEntersValidSubCategoryName(String categoryName) {
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
            baseName = "Sub"; // Fallback base name
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

    @When("the user selects {string} from the Parent Category dropdown")
    public void userSelectsParentCategoryFromDropdown(String parentCategory) {
        // Use text-based selection instead of hardcoded IDs (best practice)
        addCategoryPage.selectParentCategoryByText(parentCategory);
        selectedParentCategoryText = parentCategory; // Store for later verification
    }

    @Then("Visible selected Parent Category value in the input field")
    public void visibleSelectedParentCategoryValue() {
        String selectedText = addCategoryPage.getSelectedParentCategoryText();
        Assertions.assertNotNull(selectedText, "Parent category text should not be null");
        Assertions.assertFalse(selectedText.isBlank(), "Parent category should be selected");
        Assertions.assertNotEquals("Main Category", selectedText,
                "Should not be Main Category for sub-category");
    }

    @Then("The newly added Category Name {string} and the Parent appears in the list")
    public void newlyAddedCategoryNameAndParentAppearsInList(String categoryName) {
        // Wait for the table to load
        PlaywrightDriverManager.getPage().waitForTimeout(1000);

        // Use the last entered category name (which includes the unique timestamp)
        String searchName = lastEnteredCategoryName != null ? lastEnteredCategoryName : categoryName;

        // Search for the category name in the table
        String tableSelector = "table tbody tr, .table tbody tr";
        Locator rows = PlaywrightDriverManager.getPage().locator(tableSelector);

        boolean categoryFound = false;
        boolean parentFound = false;
        int rowCount = rows.count();

        for (int i = 0; i < rowCount; i++) {
            String rowText = rows.nth(i).textContent();
            if (rowText != null) {
                if (rowText.contains(searchName)) {
                    categoryFound = true;
                    // Check if the same row contains the parent category
                    if (selectedParentCategoryText != null && rowText.contains(selectedParentCategoryText)) {
                        parentFound = true;
                    }
                    break;
                }
            }
        }

        Assertions.assertTrue(categoryFound,
                "Newly added sub-category '" + searchName + "' should appear in the Categories list");

        if (selectedParentCategoryText != null) {
            Assertions.assertTrue(parentFound,
                    "Parent category '" + selectedParentCategoryText
                            + "' should appear alongside the sub-category in the list");
        }
    }

}
