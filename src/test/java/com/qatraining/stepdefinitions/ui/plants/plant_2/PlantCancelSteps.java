package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantCancel;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class PlantCancelSteps {

    private PlantCancel plantCancelPage;

    public PlantCancelSteps() {
        this.plantCancelPage = new PlantCancel(PlaywrightDriverManager.getPage());
    }

    @When("the user clicks on the Cancel button")
    public void userClicksOnCancelButton() {
        plantCancelPage.clickCancelButton();
    }

    @Then("the user should be redirected to the plants page")
    public void userShouldBeRedirectedToPlantsPage() {
        Assertions.assertTrue(plantCancelPage.isOnPlantsPage(), "User should be redirected to plants page");
    }
}
