package com.qatraining.stepdefinitions.ui.authentication;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.authentication.LoginPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step Definitions for Authentication UI Tests
 */
public class AuthenticationSteps {

    private LoginPage loginPage;

    public AuthenticationSteps() {
        this.loginPage = new LoginPage(PlaywrightDriverManager.getPage());
    }

    @Given("the user navigates to the login page")
    public void userNavigatesToLoginPage() {
        loginPage.navigateToLoginPage();
        Assertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
    }

    @When("the user enters valid username {string}")
    public void userEntersValidUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("the user enters valid password {string}")
    public void userEntersValidPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("the user enters invalid username {string}")
    public void userEntersInvalidUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("the user enters invalid password {string}")
    public void userEntersInvalidPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("the user enters username {string}")
    public void userEntersUsername(String username) {
        // Outline can send empty -> still fill empty so HTML5 validation can trigger
        loginPage.enterUsername(username);
    }

    @When("the user enters password {string}")
    public void userEntersPassword(String password) {
        // Outline can send empty -> still fill empty so HTML5 validation can trigger
        loginPage.enterPassword(password);
    }

    @When("the user clicks on the login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("the user should see an error message {string}")
    public void userShouldSeeErrorMessage(String expectedMessage) {
        String actualMessage = loginPage.getErrorMessage();
        Assertions.assertFalse(actualMessage.isBlank(),
                "Expected an error message, but none was found on the page.");
        Assertions.assertTrue(actualMessage.contains(expectedMessage),
                "Error message should contain: " + expectedMessage + " but was: " + actualMessage);
    }

    @Then("the user should remain on the login page")
    public void userShouldRemainOnLoginPage() {
        Assertions.assertTrue(loginPage.isLoginPageDisplayed(),
                "User should remain on login page");
    }

  @Then("the user should see validation message {string}")
public void userShouldSeeValidationMessage(String expectedMessage) {

    switch (expectedMessage) {

        case "Username is required":
            Assertions.assertTrue(
                    loginPage.isUsernameInvalid(),
                    "Username should be invalid (required)"
            );
            break;

        case "Password is required":
            Assertions.assertTrue(
                    loginPage.isPasswordInvalid(),
                    "Password should be invalid (required)"
            );
            break;

        case "Username and Password required":
            Assertions.assertTrue(
                    loginPage.isUsernameInvalid(),
                    "Username should be invalid (required)"
            );
            Assertions.assertTrue(
                    loginPage.isPasswordInvalid(),
                    "Password should be invalid (required)"
            );
            break;

        default:
            Assertions.fail("Unknown validation case: " + expectedMessage);
    }
}

}
