@UI @Plants @SortArrow @User
Feature: Display sorting arrow for Price and Stock (User view)

  Background: User/Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: UI_PLANT_USER_002 - Verify sorting arrow for price and stock is displayed
    When the user navigates to /ui/plants
    Then the sort arrow for Price should be visible
    And the sort arrow for Stock should be visible
