@UI @Plants @LowQuantityBadge @User
Feature: Display low quantity badge (User view)

  Background: User/Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: UI_PLANT_USER_005 - Verify low quantity badge is displayed
    When the user navigates to /ui/plants
    Then a plant with quantity less than 5 should be displayed
    And the plant with low quantity should display a "Low" badge
