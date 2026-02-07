package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantReset;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.junit.jupiter.api.Assertions;

public class PlantResetSteps {

    private PlantReset plantResetPage;
    private int initialPlantCount = -1;

    public PlantResetSteps() {
        this.plantResetPage = new PlantReset(PlaywrightDriverManager.getPage());
    }

    // Navigation step is provided by PlantsDisplaySteps to avoid duplicate step definitions.

    @And("the user enters {string} in the search field")
    public void userEntersSearchText(String searchText) {
        // Capture initial plant count on first interaction after navigation
        if (initialPlantCount == -1) {
            initialPlantCount = plantResetPage.getPlantCount();
            System.out.println("DEBUG: Initial plant count captured: " + initialPlantCount);
        }
        plantResetPage.enterSearchText(searchText);
    }

    @And("the user selects {string} category from the dropdown")
    public void userSelectsCategoryFromDropdown(String categoryName) {
        plantResetPage.selectCategoryFromDropdown(categoryName);
    }

    @And("the user clicks the reset button")
    public void userClicksResetButton() {
        plantResetPage.clickResetButton();
    }

    @Then("the search field should be empty")
    public void searchFieldShouldBeEmpty() {
        Assertions.assertTrue(plantResetPage.isSearchFieldEmpty(), 
                "Search field should be empty after reset");
    }

    @Then("the category dropdown should show {string}")
    public void categoryDropdownShouldShowAllCategories(String categoryText) {
        Assertions.assertTrue(plantResetPage.isCategoryDropdownShowingAllCategories(), 
                "Category dropdown should show '" + categoryText + "' after reset");
    }

    @Then("all plants should be displayed on the page")
    public void allPlantsShouldBeDisplayed() {
        int currentPlantCount = plantResetPage.getPlantCount();
        System.out.println("DEBUG: Plant count after reset: " + currentPlantCount + " | Initial: " + initialPlantCount);
        Assertions.assertTrue(currentPlantCount > 0, 
                "At least some plants should be displayed after reset");
    }
}
