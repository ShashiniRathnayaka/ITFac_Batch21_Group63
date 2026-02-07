@API @Categories
Feature: Category API - View Categories
  As an admin user of the QA Training Application
  I want to be able to retrieve the list of all categories via API
  So that I can view category data programmatically

  @adminapi @Smoke @Positive @API_CATEGORY_ADMIN_001
  Scenario: API_CATEGORY_ADMIN_001 - Verify admin can retrieve the category list
    Given Admin is authenticated with ADMIN role
    And A valid admin access token is available
    And Category record exists
    When Admin sends a GET request to "/api/categories"
    And Admin includes the authorization token in the request header
    Then API returns 200 OK status code
    And List of category records is returned in the response body
    And No data record is modified

  @nonadminapi @Smoke @Positive @API_CATEGORY_USER_001
  Scenario: API_CATEGORY_USER_001 - Verify user can retrieve category list
    Given User is authenticated with USER role
    And Valid user access token is available
    And Category record exists
    When User sends a GET request to "/api/categories"
    And User includes the authorization token in the request header
    Then API returns 200 OK status code
    And List of category records is returned in the response body
    And No data record is modified

  @nonadminapi @Smoke @Positive @API_CATEGORY_USER_002
  Scenario: API_CATEGORY_USER_002 - Verify category list pagination for user
    Given User is authenticated with USER role
    And Valid user access token is available
    And More than 10 category records exist in API
    When User sends a GET request to "/api/categories/page" with page 0 and size 10
    And User includes the authorization token in the request header
    Then API returns 200 OK status code
    And Maximum 10 records returned per page
    And Pagination metadata is present in response

  @nonadminapi @Smoke @Positive @API_CATEGORY_USER_003
  Scenario: API_CATEGORY_USER_003 - Verify category search API for user
    Given User is authenticated with USER role
    And Valid user access token is available
    When User sends a GET request to "/api/categories/page" with search parameter "Flower"
    And User includes the authorization token in the request header
    Then API returns 200 OK status code
    And Only matching category records are returned
    And Response contains array of filtered data
