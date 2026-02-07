package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantQuantityHeader;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class PlantQuantityHeaderSteps {

    private final PlantQuantityHeader page = new PlantQuantityHeader(PlaywrightDriverManager.getPage());

    @Then("the quantity column header should be visible")
    public void quantityHeaderShouldBeVisible() {
        Assertions.assertTrue(page.isQuantityHeaderVisible(), 
                "Quantity column header should be visible on the plants page");
    }
}
