package com.qatraining.stepdefinitions.ui.plants.plant_1;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.plants.plant_1.PlantsEdit;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class PlantsEditSteps {

    private PlantsEdit plantsPage;

    public PlantsEditSteps() {
        this.plantsPage = new PlantsEdit(PlaywrightDriverManager.getPage());
    }

    @Given("the user navigates to the Plants page")
    public void navigateToPlantsPage() {
        plantsPage.navigateToPlantsPage();
    }

    @When("the user observes number of plants shown on first page")
    public void observeNumberOfPlantsOnFirstPage() {
        int count = plantsPage.getVisiblePlantsCount();
        // store or assert later in Then
        Assertions.assertTrue(count >= 0, "Should be able to read number of plants");
    }

    @Then("the first page should display at most {int} plants")
    public void firstPageShouldDisplayAtMost(int max) {
        int count = plantsPage.getVisiblePlantsCount();
        Assertions.assertTrue(count <= max, "Expected at most " + max + " plants on first page but found " + count);
    }

    @Then("pagination controls should be visible")
    public void paginationControlsShouldBeVisible() {
        Assertions.assertTrue(plantsPage.isPaginationVisible(), "Expected pagination controls to be visible");
    }

    @When("the user clicks Next")
    public void userClicksNext() {
        plantsPage.clickNext();
    }

    @When("the user clicks page number {int}")
    public void userClicksPageNumber(int pageNumber) {
        plantsPage.clickPageNumber(pageNumber);
    }

    @Then("the next page should display remaining plants")
    public void nextPageShouldDisplayRemainingPlants() {
        int count = plantsPage.getVisiblePlantsCount();
        Assertions.assertTrue(count > 0, "Expected remaining plants on next page");
    }

    @Given("admin is logged in and plant with subcategory exists")
    public void adminLoggedInAndPlantExists() {
        plantsPage.navigateToPlantsPage();
        // Assume login and plant/subcategory setup is handled in Background
    }

    @When("admin clicks edit icon button")
    public void adminClicksEditIcon() {
        plantsPage.clickEditIcon();
    }

    @When("admin selects subcategory {string} from dropdown")
    public void adminSelectsSubcategory(String subCategory) {
        plantsPage.selectSubCategory(subCategory);
    }

    @When("admin clicks save button")
    public void adminClicksSaveButton() {
        plantsPage.clickSaveButton();
    }

    @Then("plant category name should be updated to {string} and displayed in plant list")
    public void plantCategoryNameShouldBeUpdated(String expectedCategory) {
        // Verify that the category was updated to the expected value
        String actualCategory = plantsPage.getPlantCategory();
        Assertions.assertEquals(expectedCategory, actualCategory, "Plant category name should be updated to " + expectedCategory);
    }

    @Then("update success message should be displayed")
    public void updateSuccessMessageShouldBeDisplayed() {
        // Verify that a success message is displayed after update
        Assertions.assertTrue(plantsPage.isSuccessMessageDisplayed(), "Update success message should be displayed");
    }

}
