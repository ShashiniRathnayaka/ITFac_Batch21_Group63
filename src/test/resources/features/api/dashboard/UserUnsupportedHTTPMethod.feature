Feature: Unsupported HTTP Method Handling
  Verify unsupported HTTP method handling
  Checks that the API properly handles unsupported HTTP methods

  Scenario: PUT request to categories endpoint should return 405 Method Not Allowed
    Given the admin is authenticated
    When the admin sends a PUT request to /api/categories
    Then the response status should be 405 Method Not Allowed

  Scenario: DELETE request to categories endpoint should return 405 Method Not Allowed
    Given the admin is authenticated
    When the admin sends a DELETE request to /api/categories
    Then the response status should be 405 Method Not Allowed

  Scenario: PATCH request to plants endpoint should return 405 Method Not Allowed
    Given the admin is authenticated
    When the admin sends a PATCH request to /api/plants
    Then the response status should be 405 Method Not Allowed

  Scenario: OPTIONS request to sales endpoint should be handled appropriately
    Given the admin is authenticated
    When the admin sends an OPTIONS request to /api/sales
    Then the response status should indicate method handling (200 or 405)
