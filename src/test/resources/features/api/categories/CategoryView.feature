@API @Categories @adminapi
Feature: Category API - View Categories
  As an admin user of the QA Training Application
  I want to be able to retrieve the list of all categories via API
  So that I can view category data programmatically

  @Smoke @Positive @API_CATEGORY_ADMIN_001
  Scenario: API_CATEGORY_ADMIN_001 - Verify admin can retrieve the category list
    Given Admin is authenticated with ADMIN role
    And A valid admin access token is available
    And Category record exists
    When Admin sends a GET request to "/api/categories"
    And Admin includes the authorization token in the request header
    Then API returns 200 OK status code
    And List of category records is returned in the response body
    And No data record is modified
