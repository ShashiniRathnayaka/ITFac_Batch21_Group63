@API @Plants @User @nonadminapi @API_PLANT_USER_002
Feature: Retrieve plants by category (User API)

  Scenario: API_PLANT_USER_002 - Retrieve plants in a category
    When a regular user requests GET to /api/plants/category/2
    Then get-by-category response status should be 200
    And the response should contain plants for category id 2
