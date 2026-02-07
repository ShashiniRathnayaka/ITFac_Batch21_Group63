package com.qatraining.stepdefinitions.ui.plants.plant_1;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_1.PlantsDelete;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for Delete Plant test
 */
public class PlantsDeleteSteps {

    private PlantsDelete plantsDelete;
    private String deletedPlantName;

    public PlantsDeleteSteps() {
        this.plantsDelete = new PlantsDelete(PlaywrightDriverManager.getPage());
    }

    @When("the user selects an existing plant for deletion")
    public void selectExistingPlantForDeletion() {
        deletedPlantName = plantsDelete.getFirstPlantName();
        Assertions.assertFalse(deletedPlantName.isEmpty(), "No plant available to delete");
    }

    @When("the user clicks the Delete icon for that plant")
    public void userClicksDeleteIcon() {
        plantsDelete.clickDeleteForPlant(deletedPlantName);
    }

    @When("the user confirms deletion")
    public void userConfirmsDeletion() {
        // Confirmation is handled via dialog acceptance in the page object
    }

    @Then("a success message should be displayed after deletion")
    public void successMessageShouldBeDisplayedAfterDeletion() {
        Assertions.assertTrue(plantsDelete.isSuccessMessageVisible(), "Expected success message after delete");
    }

    @Then("the plant should no longer appear in list")
    public void plantShouldNoLongerAppear() {
        Assertions.assertFalse(plantsDelete.isPlantInList(deletedPlantName), "Deleted plant should not be visible in list");
    }

    @Then("after refreshing the page the plant is still not present")
    public void afterRefreshPlantStillNotPresent() {
        PlaywrightDriverManager.getPage().reload();
        PlaywrightDriverManager.getPage().waitForTimeout(1000);
        Assertions.assertFalse(plantsDelete.isPlantInList(deletedPlantName), "Deleted plant should remain deleted after refresh");
    }

}
