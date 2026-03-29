@API @Plants @Admin @adminapi @API_PLANT_ADMIN_005
Feature: Prevent creation of plant with negative quantity (Admin API)

  Scenario: API_PLANT_ADMIN_005 - Prevent creation of plant with negative quantity
    When user sends POST to /api/plants/category/2 with negative-quantity data:
      | id | name | price | quantity | category_Id |
      | 17 | Antt | 100   | -1       | 2           |
    Then quantity validation response should have status 400
    And the response should contain validation error for quantity
    And quantity validation response should contain error message "Validation failed"
