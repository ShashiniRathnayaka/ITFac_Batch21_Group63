@API @Categories @Admin
Feature: Category Management API
  Scenario: Admin creates main category and sub-category successfully
    Given the admin is authenticated
    When the admin creates a main category with name prefix "API_DASH_001_Category"
    Then the response status should be 201
    And the created category ID should be stored as "parentCategoryId"
    When the admin creates a sub-category with name prefix "API_DASH_001_SubCategory" using the parent category id
    Then the response status should be 201
    And both categories should be retrievable via GET to the categories endpoint
