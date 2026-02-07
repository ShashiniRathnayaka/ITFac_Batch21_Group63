@API @Categories @adminapi @Smoke @Positive @API_CATEGORY_ADMIN_002
Feature: Categories API - Admin Create Category

  Scenario: API_CATEGORY_ADMIN_002 - Verify admin can create a new category
    Given Admin is authenticated with ADMIN role
    And A valid admin access token is available
    And Category name to be created does not already exist
    When Admin sends a POST request to "/api/categories" with valid category details
    And Admin includes the authorization token in the request header
    Then API returns 201 Created status code
    And New category is created successfully
    And Response body contains the created category details
    And Category is persisted in the database

  @API @Categories @adminapi @Negative @API_CATEGORY_ADMIN_003
  Scenario Outline: API_CATEGORY_ADMIN_003 - Verify category name validation during category creation
    Given Admin is authenticated with ADMIN role
    And A valid admin access token is available
    When Admin sends a POST request to "/api/categories" with category name "<categoryName>"
    And Admin includes the authorization token in the request header
    Then API returns 400 Bad Request status code
    And Validation error message is returned
    And Validation error message contains "<expectedMessage>"
    And Category is not created
    And No data is saved in the database

    Examples:
      | categoryName    | expectedMessage                                       |
      |                 | Category name is mandatory                            |
      | ab              | Category name must be between 3 and 10 characters     |
      | 12345678901     | Category name must be between 3 and 10 characters     |
