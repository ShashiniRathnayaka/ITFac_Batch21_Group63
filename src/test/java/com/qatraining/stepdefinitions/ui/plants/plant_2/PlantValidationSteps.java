package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantAddValidation;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class PlantValidationSteps {

    private PlantAddValidation plantAddPage;

    public PlantValidationSteps() {
        this.plantAddPage = new PlantAddValidation(PlaywrightDriverManager.getPage());
    }

    @When("the user leaves all fields empty")
    public void userLeavesAllFieldsEmpty() {
        plantAddPage.clearAllFields();
    }

    @Then("validation messages should be visible below fields")
    public void validationMessagesShouldBeVisible() {
        Assertions.assertTrue(plantAddPage.areValidationMessagesVisible(), "Validation messages should be visible");
    }
}
