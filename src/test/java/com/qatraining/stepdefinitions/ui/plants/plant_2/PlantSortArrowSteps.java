package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantSortArrow;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class PlantSortArrowSteps {

    private final PlantSortArrow page = new PlantSortArrow(PlaywrightDriverManager.getPage());

    @Then("the sort arrow for Price should be visible")
    public void priceSortArrowShouldBeVisible() {
        Assertions.assertTrue(page.isPriceSortArrowVisible(), "Price sort arrow should be visible");
    }

    @Then("the sort arrow for Stock should be visible")
    public void stockSortArrowShouldBeVisible() {
        Assertions.assertTrue(page.isStockSortArrowVisible(), "Stock sort arrow should be visible");
    }
}
