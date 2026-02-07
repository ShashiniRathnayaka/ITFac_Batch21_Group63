@UI @Plants @Reset @User
Feature: Reset plant filters and search (User view)

  Background: User/Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: UI_PLANT_USER_003 - Verify reset button resets all filters and search
    When the user navigates to /ui/plants
    And the user enters "Rose" in the search field
    And the user selects "Dicots" category from the dropdown
    And the user clicks the reset button
    Then the search field should be empty
    And the category dropdown should show "All Categories"
    And all plants should be displayed on the page
