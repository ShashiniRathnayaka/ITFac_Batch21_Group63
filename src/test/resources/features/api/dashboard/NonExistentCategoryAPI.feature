Feature: Non-existent Category 404 Error
  Verify API returns 404 error when accessing a non-existent category
  Ensures the API returns an appropriate error when accessing a non-existent category

  Scenario: GET request to non-existent category returns 404
    Given the admin is authenticated
    When the admin requests a non-existent category
    Then the response status should be 404
    And the error message should be returned

  Scenario: GET request to non-existent plant returns 404
    Given the admin is authenticated
    When the admin requests a non-existent plant
    Then the response status should be 404

  Scenario: Multiple invalid category requests return 404
    Given the admin is authenticated
    When the admin requests multiple non-existent categories
    Then all responses should return 404 status
