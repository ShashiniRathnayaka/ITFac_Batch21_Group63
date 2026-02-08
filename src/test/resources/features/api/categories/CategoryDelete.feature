@API @Categories @adminapi @Smoke @Positive @API_CATEGORY_ADMIN_005
Feature: Categories API - Admin Delete Category

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
