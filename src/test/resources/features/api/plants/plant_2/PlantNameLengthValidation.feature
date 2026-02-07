@API @Plants @Admin @adminapi @API_PLANT_ADMIN_003
Feature: Validate plant name length constraints (Admin API)

  Scenario: API_PLANT_ADMIN_003 - Verify validation error when name is too short
    When user sends POST to /api/plants/category/2 with name-too-short data:
      | id | name | price | quantity | category_Id |
      | 15 | An   | 140   | 25       | 2           |
    Then name-length validation response should have status 400
    And the response should contain validation error for name length
    And name-length validation response should contain error message "Validation failed"
