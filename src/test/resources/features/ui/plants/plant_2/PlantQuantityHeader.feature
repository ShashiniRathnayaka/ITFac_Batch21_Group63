@UI @Plants @QuantityHeader @User
Feature: View quantity table header (User view)

  Background: User/Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: UI_PLANT_USER_001 - Verify quantity column header is visible
    When the user navigates to /ui/plants
    Then the quantity column header should be visible
