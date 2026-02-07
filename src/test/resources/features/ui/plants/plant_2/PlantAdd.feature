@UI @Plants @Add
Feature: Add Plant with valid details (Admin view)

  Background: Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: UI_PLANT_ADMIN_002 - Verify admin can add a plant using valid inputs
    When the user navigates to /ui/plants
    And the user clicks on the Add Plant button
    And the user enters plant name "GloxiniaArborea"
    And the user selects sub category "Dicots"
    And the user enters price "150.50"
    And the user enters quantity "100"
    And the user clicks on the Save button
    Then the plant should be added successfully
