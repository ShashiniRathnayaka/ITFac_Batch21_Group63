@UI @Plants @NameLengthValidation
Feature: Name length validation (Admin view)

  Background: Admin is on Add Plant page
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user navigates to /ui/plants
    And the user clicks on the Add Plant button

  Scenario: UI_PLANT_ADMIN_004 - Verify name length must be between 3 and 25 characters
    When the user enters plant name "ab"
    And the user clicks on the Save button
    Then name length validation message should be visible
    When the user clears the plant name field
    And the user enters plant name "abcdefghijklmnopqrstuvwxyz"
    And the user clicks on the Save button
    Then name length validation message should be visible
