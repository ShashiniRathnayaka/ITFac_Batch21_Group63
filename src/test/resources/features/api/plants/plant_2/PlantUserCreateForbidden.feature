@API @Plants @User @nonadminapi @API_PLANT_USER_001
Feature: Verify regular user cannot create new plants (User API)

  Scenario: API_PLANT_USER_001 - User forbidden to create plants
    When a regular user sends a POST to /api/plants/category/2 with body:
      | id | name       | price | quantity | category_Id |
      | 18 | Black Rose | 100   | 10       | 2           |
    Then user creation response should have status 403
    And user creation response should contain forbidden error
