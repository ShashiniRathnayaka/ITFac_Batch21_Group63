@UI @Plants @Delete
Feature: Delete a plant (Admin view)

  Background: Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: Verify Admin can delete a plant successfully
    Given the user navigates to Plants page
    When the user selects an existing plant for deletion
    And the user clicks the Delete icon for that plant
    And the user confirms deletion
    Then a success message should be displayed after deletion
    And the plant should no longer appear in list
    And after refreshing the page the plant is still not present
