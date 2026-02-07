@API @Plants @User @nonadminapi @API_PLANT_USER_005
Feature: Retrieve plant summary information (User API)

  Scenario: API_PLANT_USER_005 - Retrieve plant summary
    When a user requests GET to /api/plants/summary
    Then summary response status should be 200
    And the response should contain plant summary information
