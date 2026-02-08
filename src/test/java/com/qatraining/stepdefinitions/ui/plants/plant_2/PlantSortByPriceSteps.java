package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantSortByPrice;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class PlantSortByPriceSteps {

    private PlantSortByPrice sortPage;

    public PlantSortByPriceSteps() {
        this.sortPage = new PlantSortByPrice(PlaywrightDriverManager.getPage());
    }

    @When("the user clicks on the Price column header")
    public void userClicksOnPriceHeader() {
        sortPage.clickPriceHeader();
    }

    @Then("the plant list should be sorted by price")
    public void plantListShouldBeSortedByPrice() {
        List<Double> prices = sortPage.getVisiblePrices();
        Assertions.assertTrue(prices.size() > 0, "No prices found in the plant list");
        boolean nonDecreasing = true;
        boolean nonIncreasing = true;
        for (int i = 1; i < prices.size(); i++) {
            if (prices.get(i) < prices.get(i - 1)) nonDecreasing = false;
            if (prices.get(i) > prices.get(i - 1)) nonIncreasing = false;
        }
        Assertions.assertTrue(nonDecreasing || nonIncreasing, "Plant list is not sorted by price: " + prices);
    }
}
