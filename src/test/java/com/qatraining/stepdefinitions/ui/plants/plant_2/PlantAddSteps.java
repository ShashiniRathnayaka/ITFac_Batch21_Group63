package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantAdd;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for Plant Add test (UI_PLANT_ADMIN_002)
 */
public class PlantAddSteps {

    private PlantAdd plantAddPage;

    public PlantAddSteps() {
        this.plantAddPage = new PlantAdd(PlaywrightDriverManager.getPage());
    }

    @When("the user clicks on the Add Plant button")
    public void userClicksOnAddPlantButton() {
        plantAddPage.clickAddPlantButton();
    }

    @When("the user enters plant name {string}")
    public void userEntersPlantName(String plantName) {
        plantAddPage.enterPlantName(plantName);
    }

    @When("the user selects sub category {string}")
    public void userSelectsSubCategory(String subCategory) {
        plantAddPage.selectSubCategory(subCategory);
    }

    @When("the user enters price {string}")
    public void userEntersPrice(String price) {
        plantAddPage.enterPrice(price);
    }

    @When("the user enters quantity {string}")
    public void userEntersQuantity(String quantity) {
        plantAddPage.enterQuantity(quantity);
    }

    @When("the user clicks on the Save button")
    public void userClicksOnSaveButton() {
        plantAddPage.clickSaveButton();
    }

    @Then("the plant should be added successfully")
    public void plantShouldBeAddedSuccessfully() {
        Assertions.assertTrue(plantAddPage.isPlantAddedSuccessfully(), 
                "Plant should be added successfully");
    }
}
