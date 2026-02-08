@API @Categories @adminapi @Smoke @Positive @API_CATEGORY_ADMIN_004
Feature: Categories API - Admin Update Category

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
