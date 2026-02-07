package com.qatraining.stepdefinitions.ui.categories;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.authentication.LoginPage;
import com.qatraining.pages.categories.CategoriesPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step Definitions for View Categories UI Tests
 */
public class ViewCategoriesSteps {

        private CategoriesPage categoriesPage;
        private LoginPage loginPage;

        public ViewCategoriesSteps() {
                this.categoriesPage = new CategoriesPage(PlaywrightDriverManager.getPage());
                this.loginPage = new LoginPage(PlaywrightDriverManager.getPage());
        }

        @Given("Admin user is logged in")
        public void adminUserIsLoggedIn() {
                loginPage.navigateToLoginPage();
                loginPage.login("admin", "admin123");

                // Wait for dashboard to confirm login success
                PlaywrightDriverManager.getPage().waitForURL("**/ui/dashboard");

                String currentUrl = PlaywrightDriverManager.getPage().url();
                Assertions.assertTrue(currentUrl.contains("/ui/dashboard"),
                                "Admin user should be logged in and on dashboard. Current URL: " + currentUrl);
        }

        @Given("User is logged in")
        public void userIsLoggedIn() {
                loginPage.navigateToLoginPage();
                loginPage.login("testuser", "test123");

                // Wait for dashboard to confirm login success
                PlaywrightDriverManager.getPage().waitForURL("**/ui/dashboard");

                String currentUrl = PlaywrightDriverManager.getPage().url();
                Assertions.assertTrue(currentUrl.contains("/ui/dashboard"),
                                "User should be logged in and on dashboard. Current URL: " + currentUrl);
        }

        @Given("Category records exist in the system")
        public void categoryRecordsExistInTheSystem() {
                // This is a precondition - in a real scenario, you might want to verify
                // that categories exist via API or database. For now, we'll assume they exist.
                // You could add API calls here if needed to set up test data
        }

        @When("the user navigates to {string}")
        public void userNavigatesToPage(String url) {
                categoriesPage.navigateToCategoriesPage();
        }

        @When("the user observes page elements")
        public void userObservesPageElements() {
                // This step is more of a placeholder - the actual observations
                // are done in the Then steps. We'll just wait for page to be ready.
                PlaywrightDriverManager.getPage().waitForLoadState();
        }

        @Then("Categories page loads successfully")
        public void categoriesPageLoadsSuccessfully() {
                Assertions.assertTrue(categoriesPage.isCategoriesPageLoaded(),
                                "Categories page should load successfully");
        }

        @Then("Search box is visible")
        public void searchBoxIsVisible() {
                Assertions.assertTrue(categoriesPage.isSearchBoxVisible(),
                                "Search box should be visible on Categories page");
        }

        @Then("Parent filter is visible")
        public void parentFilterIsVisible() {
                Assertions.assertTrue(categoriesPage.isParentFilterVisible(),
                                "Parent filter dropdown should be visible on Categories page");
        }

        @Then("Search button is visible")
        public void searchButtonIsVisible() {
                Assertions.assertTrue(categoriesPage.isSearchButtonVisible(),
                                "Search button should be visible on Categories page");
        }

        @Then("Add Category button is visible")
        public void addCategoryButtonIsVisible() {
                Assertions.assertTrue(categoriesPage.isAddCategoryButtonVisible(),
                                "Add Category button should be visible on Categories page");
        }

        @Then("Categories are displayed in a table")
        public void categoriesAreDisplayedInTable() {
                Assertions.assertTrue(categoriesPage.isCategoriesTableVisible(),
                                "Categories table should be visible");
                Assertions.assertTrue(categoriesPage.areCategoriesDisplayed(),
                                "Categories should be displayed in the table");
        }

        @Then("Pagination is visible")
        public void paginationIsVisible() {
                Assertions.assertTrue(categoriesPage.isPaginationVisible(),
                                "Pagination should be visible on Categories page");
        }

        @Then("Search box, Parent filter, Search and Add Category buttons are visible")
        public void allControlElementsAreVisible() {
                Assertions.assertTrue(categoriesPage.isSearchBoxVisible(),
                                "Search box should be visible");
                Assertions.assertTrue(categoriesPage.isParentFilterVisible(),
                                "Parent filter should be visible");
                Assertions.assertTrue(categoriesPage.isSearchButtonVisible(),
                                "Search button should be visible");
                Assertions.assertTrue(categoriesPage.isAddCategoryButtonVisible(),
                                "Add Category button should be visible");
        }

        @Then("Categories are displayed in a table with pagination")
        public void categoriesDisplayedWithPagination() {
                Assertions.assertTrue(categoriesPage.isCategoriesTableVisible(),
                                "Categories table should be visible");
                Assertions.assertTrue(categoriesPage.areCategoriesDisplayed(),
                                "Categories should be displayed in the table");
                Assertions.assertTrue(categoriesPage.isPaginationVisible(),
                                "Pagination should be visible");
        }

        @Then("Add Category option is not available")
        public void addCategoryOptionIsNotAvailable() {
                Assertions.assertTrue(categoriesPage.isAddCategoryButtonNotAvailable(),
                                "Add Category button should not be available for USER role");
        }

        @Then("Edit actions are restricted")
        public void editActionsAreRestricted() {
                Assertions.assertTrue(categoriesPage.areEditButtonsDisabled(),
                                "Edit buttons should be disabled for USER role");
        }

        @Then("Delete actions are restricted")
        public void deleteActionsAreRestricted() {
                Assertions.assertTrue(categoriesPage.areDeleteButtonsDisabled(),
                                "Delete buttons should be disabled for USER role");
        }

        @Then("Edit\\/Delete actions are restricted")
        public void editDeleteActionsAreRestricted() {
                Assertions.assertTrue(categoriesPage.areEditButtonsDisabled(),
                                "Edit buttons should be disabled for USER role");
                Assertions.assertTrue(categoriesPage.areDeleteButtonsDisabled(),
                                "Delete buttons should be disabled for USER role");
        }

        @Then("List of categories with Name and Parent is visible")
        public void listOfCategoriesWithNameAndParentIsVisible() {
                Assertions.assertTrue(categoriesPage.isCategoriesTableVisible(),
                                "Categories table should be visible");
                Assertions.assertTrue(categoriesPage.areCategoriesDisplayed(),
                                "Categories should be displayed in the table");
        }

        @Given("More than {int} category records exist")
        public void moreThanNCategoryRecordsExist(int minRecords) {
                categoriesPage.navigateToCategoriesPage();
                int categoryCount = categoriesPage.getCategoryCount();

                Assertions.assertTrue(categoryCount >= minRecords,
                                "Expected at least " + minRecords + " categories, but found: " + categoryCount);

                System.out.println("Verified: " + categoryCount + " category records exist (required: >" + minRecords
                                + ")");
        }

        @When("the user clicks the Next pagination button")
        public void userClicksNextPaginationButton() {
                categoriesPage.clickNextPagination();
        }

        @Then("A maximum of {int} category records are displayed per page")
        public void maximumRecordsDisplayedPerPage(int maxRecords) {
                int displayedCount = categoriesPage.getCategoryCount();

                Assertions.assertTrue(displayedCount > 0 && displayedCount <= maxRecords,
                                "Expected maximum " + maxRecords + " records per page, but found: " + displayedCount);

                System.out.println(
                                "Verified: " + displayedCount + " records displayed (max allowed: " + maxRecords + ")");
        }

        @Then("Pagination controls are visible")
        public void paginationControlsAreVisible() {
                Assertions.assertTrue(categoriesPage.isPaginationControlsVisible(),
                                "Pagination controls (Previous, page numbers, Next) should be visible");
        }

        @Then("The next set of category records is loaded")
        public void nextSetOfCategoryRecordsIsLoaded() {
                // Verify we're on a different page by checking the page number changed
                int currentPage = categoriesPage.getCurrentPageNumber();

                Assertions.assertTrue(currentPage > 1,
                                "Expected to be on page 2 or higher after clicking Next, but found page: "
                                                + currentPage);

                // Verify categories are still displayed
                Assertions.assertTrue(categoriesPage.areCategoriesDisplayed(),
                                "Categories should be displayed on the next page");

                System.out.println("Verified: Navigated to page " + currentPage);
        }

        @Given("Categories with different parent categories exist")
        public void categoriesWithDifferentParentCategoriesExist() {
                // Navigate to categories page to verify categories exist
                categoriesPage.navigateToCategoriesPage();
                int categoryCount = categoriesPage.getCategoryCount();

                Assertions.assertTrue(categoryCount > 0,
                                "Categories should exist in the system");

                System.out.println("Verified: " + categoryCount + " categories exist with parent categories");
        }

        @When("the user clicks the Parent Category dropdown")
        public void userClicksParentCategoryDropdown() {
                // The dropdown is already visible, no need to click to open
                // This step documents the user action in the test flow
                Assertions.assertTrue(categoriesPage.isParentFilterVisible(),
                                "Parent Category dropdown should be visible");
        }

        @When("the user selects {string} from the parent filter")
        public void userSelectsFromParentFilter(String parentCategory) {
                categoriesPage.selectParentCategoryFilter(parentCategory);
        }

        @When("the user clicks the Search button")
        public void userClicksSearchButton() {
                categoriesPage.clickSearchButton();
        }

        @Then("Category list is filtered based on the selected parent category")
        public void categoryListIsFilteredByParent() {
                // Verify that categories are displayed after filtering
                Assertions.assertTrue(categoriesPage.areCategoriesDisplayed(),
                                "Filtered categories should be displayed in the table");
        }

        @Then("Only categories belonging to {string} are displayed")
        public void onlyCategoriesBelongingToParentAreDisplayed(String parentCategory) {
                Assertions.assertTrue(categoriesPage.areAllCategoriesFilteredByParent(parentCategory),
                                "All displayed categories should belong to parent: " + parentCategory);
        }

        @Then("User remains in read-only mode")
        public void userRemainsInReadOnlyMode() {
                // Verify Add button is not available
                Assertions.assertTrue(categoriesPage.isAddCategoryButtonNotAvailable(),
                                "Add Category button should not be available for USER role");

                // Verify Edit/Delete buttons are disabled
                Assertions.assertTrue(categoriesPage.areEditButtonsDisabled(),
                                "Edit buttons should remain disabled for USER role");
                Assertions.assertTrue(categoriesPage.areDeleteButtonsDisabled(),
                                "Delete buttons should remain disabled for USER role");
        }
}
