@API @Categories
Feature: Categories API - Admin Update Category

  @adminapi @Smoke @Positive @API_CATEGORY_ADMIN_004
  Scenario: API_CATEGORY_ADMIN_004 - Verify admin can update an existing category
    Given Admin is authenticated with ADMIN role
    And A valid admin access token is available
    And At least one category exists in the database for update
    When Admin sends a PUT request to update category details
    And Admin includes a valid request payload in the request body
    And Admin includes the authorization token in the request header
    Then API returns 200 OK status code for category update
    And Category details are updated successfully
    And Response body reflects the updated category values
    And Updated category data is saved in the database

  @nonadminapi @Negative @API_CATEGORY_USER_004
  Scenario: API_CATEGORY_USER_004 - Verify user cannot update a category
    Given User is authenticated with USER role
    And Valid user access token is available
    And Category record exists
    When User sends a PUT request to "/api/categories" with category id
    And User includes a valid payload in the request body
    And User includes the authorization token in the request header
    Then API returns 403 Forbidden status code
    And Error message returned: "Forbidden"
    And Category data is not updated via API

  @adminapi @Negative @API_CATEGORY_ADMIN_006
  Scenario Outline: API_CATEGORY_ADMIN_006 - Validate that updating an existing category with invalid name is rejected
    Given Admin is authenticated with ADMIN role
    And A valid admin access token is available
    And At least one category exists in the database for update
    When Admin sends a PUT request to update category with invalid name "<invalidName>"
    And Admin includes the authorization token in the request header
    Then API returns 400 Bad Request status code for update
    And Validation error message is returned for update
    And Validation error message for update contains "<expectedMessage>"
    And Category data remains unchanged in the database

    Examples:
      | invalidName     | expectedMessage                                       |
      |                 | Category name must be between 3 and 10 characters     |
      | ab              | Category name must be between 3 and 10 characters     |
      | 12345678901     | Category name must be between 3 and 10 characters     |
