@API @Plants @Admin @adminapi @API_PLANT_ADMIN_001
Feature: Create a new plant (Admin API)

  Scenario: API_PLANT_ADMIN_001 - Create a new plant with valid data
    When the admin sends a POST to /api/plants/category/2 with body:
      | id | name              | price | quantity | category_Id |
      | 13 | Anthurium_{time}  | 150   | 25       | 2           |
    Then the response status code should be 201
    And the response should contain the created plant with name containing "Anthurium_" and category id 2
