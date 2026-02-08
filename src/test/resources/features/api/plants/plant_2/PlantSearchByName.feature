@API @Plants @User @nonadminapi @API_PLANT_USER_003
Feature: Search plants by name with custom pagination (User API)

  Scenario: API_PLANT_USER_003 - Search plants by name with pagination
    When a user searches for plants with name filter:
      | name | page | size | sort       |
      | rose | 0    | 5    | price,desc |
    Then search response status should be 200
    And the response should contain plants matching search criteria
    And all returned plants should have name containing "rose"
    And search results should be sorted by price descending
