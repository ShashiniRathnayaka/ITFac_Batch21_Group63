@UI @Plants @Sort @User
Feature: Sort plants by price (User view)

  Background: User/Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: UI_PLANT_USER_004 - Verify plant list sorting by price
    When the user navigates to /ui/plants
    And the user clicks on the Price column header
    Then the plant list should be sorted by price
