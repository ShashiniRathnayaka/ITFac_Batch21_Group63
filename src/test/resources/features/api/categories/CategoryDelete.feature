@API @Categories
Feature: Categories API - Admin Delete Category

  @adminapi @Smoke @Positive @API_CATEGORY_ADMIN_005
  Scenario: API_CATEGORY_ADMIN_005 - Verify admin can delete a category without dependencies
    Given Admin is authenticated with ADMIN role
    And A valid admin access token is available
    And Category exists with no sub categories linked
    When Admin sends a DELETE request to remove the category
    And Admin includes the authorization token in the request header
    Then API returns 204 No Content status code for delete
    And Category is deleted successfully from the system
    And Deleted category no longer exists in the database
    And No error message is returned for delete operation

  @nonadminapi @Negative @API_CATEGORY_USER_005
  Scenario: API_CATEGORY_USER_005 - Verify user cannot delete a category
    Given User is authenticated with USER role
    And Valid user access token is available
    And Category record exists
    When User sends a DELETE request to "/api/categories" with category id
    And User includes the authorization token in the request header
    Then API returns 403 Forbidden status code for delete operation
    And Category is not deleted from system
    And Error message indicates permission denied