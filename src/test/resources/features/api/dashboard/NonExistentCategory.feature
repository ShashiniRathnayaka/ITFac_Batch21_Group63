Feature: Non-existent Category Returns 404 Error
  Ensures the API returns an appropriate error when accessing a non-existent category

  Scenario: GET non-existent category by ID should return 404 Not Found
    Given the admin is authenticated
    When the admin requests a non-existent category with ID 999999
    Then the response status should be 404 Not Found
    And an error message should be returned

  Scenario: GET non-existent plant should return 404 Not Found
    Given the admin is authenticated
    When the admin requests a non-existent plant with ID 999999
    Then the response status should be 404 Not Found

  Scenario: Multiple non-existent resources should return 404
    Given the admin is authenticated
    When the admin requests non-existent resources
    Then all requests should return 404 Not Found

  Scenario: Invalid resource ID format should return 4xx error
    Given the admin is authenticated
    When the admin requests a category with invalid ID format
    Then the response status should be 400 or 404
