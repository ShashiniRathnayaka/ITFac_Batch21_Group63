@API @Plants @Admin @adminapi @API_PLANT_ADMIN_004
Feature: Prevent creation of plant with invalid price (Admin API)

  Scenario: API_PLANT_ADMIN_004 - Prevent creation of plant with price <= 0
    When user sends POST to /api/plants/category/2 with invalid-price data:
      | id | name | price | quantity | category_Id |
      | 16 | Antt | 0     | 25       | 2           |
    Then price validation response should have status 400
    And the response should contain validation error for price
    And price validation response should contain error message "Validation failed"
