@API @Plants @CategoryReference @Admin
Feature: Plant Category Reference
  
  Scenario: API_DASH_005 - Verify plant references correct parent category
    Given the admin is authenticated
    And a parent category exists
    And a sub-category exists under the parent category
    When the admin creates a plant under that sub-category with valid data
    Then the plant is created successfully with status 201
    When the admin retrieves the plant by ID
    Then the plant response returns status 200
    And the plant response contains correct category reference
    And the category reference includes parent information
