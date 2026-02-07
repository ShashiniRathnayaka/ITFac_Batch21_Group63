package com.qatraining.stepdefinitions.ui.plants.plant_2;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_2.PlantNameLengthValidation;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class PlantNameLengthValidationSteps {

    private PlantNameLengthValidation plantAddPage;

    public PlantNameLengthValidationSteps() {
        this.plantAddPage = new PlantNameLengthValidation(PlaywrightDriverManager.getPage());
    }

    @When("the user clears the plant name field")
    public void userClearsPlantNameField() {
        plantAddPage.clearPlantNameField();
    }

    @Then("name length validation message should be visible")
    public void nameLengthValidationMessageShouldBeVisible() {
        Assertions.assertTrue(plantAddPage.isNameLengthValidationMessageVisible(), 
                "Name length validation message should be visible");
    }
}
