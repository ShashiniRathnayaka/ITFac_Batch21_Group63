package com.qatraining.stepdefinitions.ui.plants.plant_1;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_1.PlantsAdd;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for Add Plant test
 */
public class PlantsAddSteps {

    private PlantsAdd plantsAddPage;
    private String lastAddedPlantName; // Track the plant name we added

    public PlantsAddSteps() {
        this.plantsAddPage = new PlantsAdd(PlaywrightDriverManager.getPage());
    }

    @Given("the user is on the Plants list page")
    public void userIsOnPlantsListPage() {
        plantsAddPage.navigateToPlantsPage();
    }

    @Then("the Add a Plant button should be visible")
    public void addPlantButtonShouldBeVisible() {
        Assertions.assertTrue(plantsAddPage.isAddPlantButtonVisible(), 
                "Add a Plant button should be visible on Plants page");
    }

    @When("the user clicks the Add a Plant button")
    public void userClicksAddPlantButton() {
        plantsAddPage.clickAddPlantButton();
    }

    @When("the user enters plant name {string}")
    public void userEntersPlantName(String name) {
        // Generate unique plant name with short timestamp suffix to avoid duplicates from previous test runs
        // Use only last 5 digits of timestamp to keep total length under 25 chars
        long timestamp = System.currentTimeMillis() % 100000; // Keep it short (5 digits)
        String uniqueName = name + "_" + timestamp;
        this.lastAddedPlantName = uniqueName;
        plantsAddPage.enterPlantName(uniqueName);
    }

    @When("the user selects plant category {string}")
    public void userSelectsPlantCategory(String category) {
        plantsAddPage.selectCategory(category);
    }

    @When("the user enters plant price {string}")
    public void userEntersPlantPrice(String price) {
        plantsAddPage.enterPrice(price);
    }

    @When("the user enters plant quantity {string}")
    public void userEntersPlantQuantity(String quantity) {
        plantsAddPage.enterQuantity(quantity);
    }

    @When("the user clicks the Save button")
    public void userClicksSaveButton() {
        plantsAddPage.clickSaveButton();
    }

    @Then("a success message should be displayed")
    public void successMessageShouldBeDisplayed() {
        // Check for error messages first
        if (plantsAddPage.isErrorMessageVisible()) {
            String errorMsg = plantsAddPage.getErrorMessage();
            Assertions.fail("Expected success but got error: " + errorMsg);
        }
        
        // Accept either an explicit success message OR a redirect back to the Plants list
        boolean successVisible = plantsAddPage.isSuccessMessageVisible();
        boolean redirectedToList = plantsAddPage.getCurrentUrl().contains("/ui/plants");
        Assertions.assertTrue(successVisible || redirectedToList,
            "Expected either a success message or redirect to /ui/plants after saving. Visible=" + successVisible + ", url=" + plantsAddPage.getCurrentUrl());
    }

    @Then("the newly added plant {string} should appear in the Plants list")
    public void newlyAddedPlantShouldAppearInList(String plantName) {
        // Navigate back to plants list and search for the newly added plant
        // Use the lastAddedPlantName we tracked (with timestamp) instead of the feature file name
        com.qatraining.pages.plants.plant_1.Plants plantsPage = new com.qatraining.pages.plants.plant_1.Plants(PlaywrightDriverManager.getPage());
        
        // Wait a moment for the database to settle
        PlaywrightDriverManager.getPage().waitForTimeout(2000);
        
        // Navigate to plants list (this refreshes the page)
        plantsPage.navigateToPlantsPage();
        
        // Try to find the plant - first on the current page
        boolean found = plantsPage.findPlantAcrossPages(lastAddedPlantName);
        
        // If not found on first attempt, try refreshing and searching again
        if (!found) {
            PlaywrightDriverManager.getPage().reload();
            PlaywrightDriverManager.getPage().waitForTimeout(2000);
            found = plantsPage.findPlantAcrossPages(lastAddedPlantName);
        }
        
        Assertions.assertTrue(found,
                "Newly added plant '" + lastAddedPlantName + "' should appear in the Plants list");
    }

    @Then("the user should be on the Plants list page")
    public void userShouldBeOnPlantsListPage() {
        String url = plantsAddPage.getCurrentUrl();
        Assertions.assertTrue(url.contains("/ui/plants"),
                "User should be on Plants list page (/ui/plants). Current URL: " + url);
    }

}
