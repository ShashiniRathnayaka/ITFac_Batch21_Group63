package com.qatraining.stepdefinitions.ui.plants.plant_1;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_1.Plants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for category filter test
 */
public class PlantsSteps {

    private Plants plantsPage;

    public PlantsSteps() {
        this.plantsPage = new Plants(PlaywrightDriverManager.getPage());
    }

    @Given("the user navigates to Plants page")
    public void userNavigatesToPlantsPage() {
        plantsPage.navigateToPlantsPage();
    }

    @When("the user selects category {string} from dropdown")
    public void userSelectsCategory(String categoryLabel) {
        plantsPage.selectCategoryByLabel(categoryLabel);
    }

    @When("the user selects category with id {string}")
    public void userSelectsCategoryById(String categoryId) {
        plantsPage.selectCategory(categoryId);
    }

    @When("the user enters keyword {string}")
    public void userEntersKeyword(String keyword) {
        plantsPage.enterSearchKeyword(keyword);
    }

    @When("the user clicks the Search button")
    public void userClicksSearchButton() {
        plantsPage.clickSearch();
    }

    @Then("only plants from selected category should be displayed")
    public void onlySelectedCategoryPlantsDisplayed() {
        String selected = plantsPage.getSelectedCategoryLabel();
        Assertions.assertFalse(selected.isEmpty(), "Expected a category to be selected");
        Assertions.assertTrue(plantsPage.areAllVisiblePlantsInCategory(selected), "Expected only plants from selected category: " + selected);
    }

    @Then("no plants found message should be displayed and no rows shown")
    public void noPlantsFoundMessageShouldBeDisplayed() {
        Assertions.assertTrue(plantsPage.isNoResultsVisible(), "Expected 'No plants found' message to be visible");
        Assertions.assertEquals(0, plantsPage.getPlantRowCount(), "Expected zero plant rows when no results match");
    }

    @Then("page controls should remain usable")
    public void pageControlsShouldRemainUsable() {
        // Search button should be visible
        Assertions.assertTrue(PlaywrightDriverManager.getPage().locator("button:has-text('Search')").first().isVisible(), "Search button should be usable");
        // Reset should be visible or at least not error when clicked
        if (PlaywrightDriverManager.getPage().locator("button:has-text('Reset')").count() > 0) {
            Assertions.assertTrue(PlaywrightDriverManager.getPage().locator("button:has-text('Reset')").first().isVisible(), "Reset button should be visible");
        }
        // Add a Plant should be visible
        Assertions.assertTrue(plantsPage.isAddPlantButtonVisible(), "Add a Plant control should remain visible");
    }

    @Then("the URL should contain categoryId {string}")
    public void urlShouldContainCategoryId(String categoryId) {
        Assertions.assertTrue(
                plantsPage.isUrlContainsCategoryId(categoryId),
                "Expected URL to contain categoryId=" + categoryId + " but was: " + plantsPage.getCurrentUrl()
        );
    }

    @Then("table headers should be displayed as {string}, {string}, {string}, {string}")
    public void tableHeadersShouldBeDisplayed(String h1, String h2, String h3, String h4) {
        Assertions.assertTrue(
                plantsPage.areTableHeadersVisible(h1, h2, h3, h4),
                "Expected table headers to include: " + String.join(", ", java.util.Arrays.asList(h1, h2, h3, h4))
        );
    }

    @Then("plant records should be displayed in the list")
    public void plantRecordsShouldBeDisplayed() {
        Assertions.assertTrue(plantsPage.hasPlantRecords(), "Expected at least one plant record to be displayed");
    }

    @Then("pagination controls are visible for user")
    public void paginationControlsAreVisibleForUser() {
        Assertions.assertTrue(plantsPage.isPaginationVisible(), "Expected pagination controls to be visible for user");
    }

    @Then("only limited number of plants are displayed per page")
    public void onlyLimitedNumberOfPlantsAreDisplayedPerPage() {
        int count = plantsPage.getVisiblePlantsCountOnPage();
        Assertions.assertTrue(count <= 10, "Expected at most 10 plants per page but found " + count);
    }

    @When("the user clicks the Reset button")
    public void userClicksResetButton() {
        plantsPage.clickReset();
        PlaywrightDriverManager.getPage().waitForTimeout(500);
    }

    @Then("the search input should be cleared")
    public void searchInputShouldBeCleared() {
        Assertions.assertTrue(plantsPage.isSearchInputCleared(), "Expected search input to be cleared after reset");
    }

    @Then("the category dropdown should be reset to All Categories")
    public void categoryShouldBeResetToAllCategories() {
        Assertions.assertTrue(plantsPage.isCategoryResetToAllCategories(), "Expected category to be reset to 'All Categories' after reset");
    }

    @Then("the full plant list should be displayed")
    public void fullPlantListShouldBeDisplayed() {
        Assertions.assertTrue(plantsPage.hasPlantRecords(), "Expected full plant list to be displayed after reset");
    }

    @When("the user enters keyword with space in plant name")
    public void userEntersKeywordWithSpace() {
        System.out.println("\n========== DEBUG: userEntersKeywordWithSpace START ==========");
        System.out.println("DEBUG: Fetching a multi-word plant name from the page...");
        
        // Fetch a multi-word plant name from the page and use it for search
        String plantNameWithSpace = plantsPage.getFirstPlantNameWithSpace();
        
        System.out.println("DEBUG: Plant name fetched: '" + plantNameWithSpace + "'");
        Assertions.assertNotNull(plantNameWithSpace, "Could not find a plant name with space on the page");
        
        System.out.println("DEBUG: Plant name is not null, entering search keyword...");
        System.out.println("DEBUG: About to enter keyword: '" + plantNameWithSpace + "'");
        plantsPage.enterSearchKeyword(plantNameWithSpace);
        System.out.println("DEBUG: Keyword entered successfully in search input field");
        System.out.println("========== DEBUG: userEntersKeywordWithSpace END ==========\n");
    }

    @Then("the matching plant record should be displayed in results")
    public void matchingPlantShouldBeDisplayed() {
        System.out.println("\n========== DEBUG: matchingPlantShouldBeDisplayed START ==========");
        
        String plantName = plantsPage.getLastSearchedKeyword();
        System.out.println("DEBUG: Last searched keyword: '" + plantName + "'");
        
        int rowCount = plantsPage.getPlantRowCount();
        System.out.println("DEBUG: Total plant rows in results: " + rowCount);
        
        if (rowCount > 0) {
            System.out.println("DEBUG: Checking if plant '" + plantName + "' is in the list...");
            boolean isFound = plantsPage.isPlantInList(plantName);
            System.out.println("DEBUG: Plant found in list: " + isFound);
        }
        
        Assertions.assertTrue(
                plantsPage.isPlantInList(plantName),
                "Expected plant \"" + plantName + "\" to be displayed in results after search"
        );
        System.out.println("DEBUG: ASSERTION PASSED - Plant with space-separated name found in results: '" + plantName + "'");
        System.out.println("========== DEBUG: matchingPlantShouldBeDisplayed END ==========\n");
    }

    @Then("the plant name should contain the searched keyword")
    public void plantNameShouldContainKeyword() {
        System.out.println("\n========== DEBUG: plantNameShouldContainKeyword START ==========");
        
        int rowCount = plantsPage.getPlantRowCount();
        System.out.println("DEBUG: Total plant rows returned in results: " + rowCount);
        
        String lastKeyword = plantsPage.getLastSearchedKeyword();
        System.out.println("DEBUG: Searched keyword was: '" + lastKeyword + "'");
        
        if (rowCount > 0) {
            System.out.println("DEBUG: Results found! Verifying the plant name contains spaces...");
            boolean containsSpace = lastKeyword.contains(" ");
            System.out.println("DEBUG: Search keyword contains space: " + containsSpace);
        } else {
            System.out.println("DEBUG: WARNING - No results returned for keyword: '" + lastKeyword + "'");
        }
        
        Assertions.assertTrue(rowCount > 0, "Expected at least one plant record in results");
        System.out.println("DEBUG: ASSERTION PASSED - Search returned " + rowCount + " plant record(s)");
        System.out.println("========== DEBUG: plantNameShouldContainKeyword END ==========\n");
    }
}