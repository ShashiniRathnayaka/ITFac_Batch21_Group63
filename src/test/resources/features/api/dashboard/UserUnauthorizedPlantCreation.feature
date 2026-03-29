Feature: User Unauthorized Plant Creation
  Verify user is unauthorized to create plant
  Ensures that role-based access control prevents a standard user from creating a plant

  Scenario: User cannot create a plant
    Given the user is authenticated as a standard user
    When the user attempts to create a plant with valid data
    Then the response status should be 403 or 401
    And the plant is not created

  Scenario: User cannot create a plant in a valid category
    Given the user is authenticated as a standard user
    And a valid category exists
    When the user attempts to create a plant in the valid category
    Then the response status should be 403 or 401
    And the plant is not added to the category

  Scenario: User cannot create a plant in a sub-category
    Given the user is authenticated as a standard user
    When the user attempts to create a plant in a sub-category
    Then the response status should be 403 or 401
    And no new plant is created in the sub-category
