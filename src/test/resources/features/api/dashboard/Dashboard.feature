@API @Dashboard @Admin
Feature: Dashboard API
  @CreateSale
  Scenario: Admin retrieves dashboard summary data after creating a sale
    Given the admin is authenticated
    When the admin creates a sale for a plant with quantity 1
    And the admin requests dashboard summary data
    Then GET to the categories endpoint returns 200 and non-empty list
    And GET to the plants endpoint returns 200 and non-empty list
    And GET to the sales endpoint returns 200 and non-empty list

  Scenario: Admin retrieves dashboard summary data
    Given the admin is authenticated
    When the admin requests dashboard summary data
    Then GET to the categories endpoint returns 200 and non-empty list
    And GET to the plants endpoint returns 200 and non-empty list
    And GET to the sales endpoint returns 200 and non-empty list
