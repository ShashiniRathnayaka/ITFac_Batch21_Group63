@UI @Plants @Display @UI_PLANT_ADMIN_001
Feature: Display Add Plant button (Admin view)

  Background: Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: UI_PLANT_ADMIN_001 - Verify Add Plant button is visible to Admin
    When the user navigates to /ui/plants
    Then the Add Plant button should be visible on the plants page
