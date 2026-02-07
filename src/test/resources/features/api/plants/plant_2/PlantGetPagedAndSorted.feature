@API @Plants @User @nonadminapi @API_PLANT_USER_004
Feature: Retrieve plants with pagination and sorting (User API)

  Scenario: API_PLANT_USER_004 - Retrieve paginated and sorted plants
    When a user requests GET to /api/plants/paged with pagination and multi-sort:
      | page | size | sort1     | sort2    |
      | 1    | 3    | price,asc | name,asc |
    Then paginated response status should be 200
    And the response should contain paginated plant content
    And the response page number should be 1
    And the response should contain exactly 3 plants
