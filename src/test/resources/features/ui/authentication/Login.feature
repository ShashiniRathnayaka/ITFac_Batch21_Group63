@UI @Authentication
Feature: User Authentication - Login Functionality
  As a user of the QA Training Application
  I want to be able to log in to the system
  So that I can access the application features

  Background:
    Given the user navigates to the login page

  @Smoke @Positive
  Scenario Outline: Successful login redirects to dashboard
    When the user enters username "<username>"
    And the user enters password "<password>"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    And the dashboard page should be displayed

    Examples:
      | username | password |
      | admin    | admin123 |
      | testuser | test123  |

  @Negative
  Scenario: Login with invalid username or password
    When the user enters invalid username "invaliduser"
    And the user enters invalid password "wrongpass"
    And the user clicks on the login button
    Then the user should see an error message "Invalid username or password."
    And the user should remain on the login page

  @Negative
  Scenario Outline: Login with missing credentials
    When the user enters username "<username>"
    And the user enters password "<password>"
    And the user clicks on the login button
    Then the user should remain on the login page

    Examples:
      | username | password |
      |          | admin123 |
      | admin    |          |
      |          |          |
