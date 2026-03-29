@UI @Plants @Add
Feature: Add a new plant (Admin view)

  Background: Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: Verify Admin can add a new plant and return to Plants list
    Given the user is on the Plants list page
    Then the Add a Plant button should be visible
    When the user clicks the Add a Plant button
    And the user enters plant name "New Test Plant"
    And the user selects plant category "rose"
    And the user enters plant price "49.99"
    And the user enters plant quantity "15"
    And the user clicks the Save button
    Then a success message should be displayed
    And the user should be on the Plants list page
