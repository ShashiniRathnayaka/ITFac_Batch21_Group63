@UI @Plants @Validation
Feature: Mandatory field validation (Admin view)

  Background: Admin is on Add Plant page
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user navigates to /ui/plants
    And the user clicks on the Add Plant button

  Scenario: UI_PLANT_ADMIN_003 - Verify validation messages appear for empty fields
    When the user leaves all fields empty
    And the user clicks on the Save button
    Then validation messages should be visible below fields
