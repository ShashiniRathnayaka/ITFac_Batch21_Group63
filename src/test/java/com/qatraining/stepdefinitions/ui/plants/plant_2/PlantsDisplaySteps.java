package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantsDisplay;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for Plants Display test (UI_PLANT_ADMIN_001)
 */
public class PlantsDisplaySteps {

    private PlantsDisplay plantsDisplayPage;

    public PlantsDisplaySteps() {
        this.plantsDisplayPage = new PlantsDisplay(PlaywrightDriverManager.getPage());
    }

    @When("the user navigates to \\/ui\\/plants")
    public void userNavigatesToPlantsPage() {
        plantsDisplayPage.navigateToPlantsPage();
    }

    @Then("the Add Plant button should be visible on the plants page")
    public void addPlantButtonShouldBeVisible() {
        Assertions.assertTrue(plantsDisplayPage.isAddPlantButtonVisible(), 
                "Add Plant button should be visible on the plants page");
    }
}
