package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantLowQuantityBadge;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class PlantLowQuantityBadgeSteps {

    private PlantLowQuantityBadge plantLowQuantityBadgePage;

    public PlantLowQuantityBadgeSteps() {
        this.plantLowQuantityBadgePage = new PlantLowQuantityBadge(PlaywrightDriverManager.getPage());
    }

    @Then("a plant with quantity less than 5 should be displayed")
    public void plantWithLowQuantityShouldBeDisplayed() {
        Assertions.assertTrue(plantLowQuantityBadgePage.isPlantWithLowQuantityVisible(), 
                "A plant with quantity less than 5 should be displayed on the plants page");
    }

    @Then("the plant with low quantity should display a {string} badge")
    public void lowBadgeShouldBeDisplayed(String badgeText) {
        Assertions.assertTrue(plantLowQuantityBadgePage.isLowBadgeDisplayedForLowQuantityPlant(), 
                "The '" + badgeText + "' badge should be displayed for plants with low quantity");
    }
}
