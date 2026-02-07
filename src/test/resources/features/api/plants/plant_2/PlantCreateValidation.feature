@API @Plants @Admin @adminapi @API_PLANT_ADMIN_002
Feature: Validate required fields when creating a plant (Admin API)

  Scenario: API_PLANT_ADMIN_002 - Verify validation error when price is missing
    When user sends POST to /api/plants/category/2 with incomplete data:
      | id | name                     | quantity | category_Id |
      | 14 | ValidatePlant_{time}     | 25       | 2           |
    Then validation response should have status 400
    And the response should contain validation error for field "price"
    And validation response should contain error message "Validation failed"
