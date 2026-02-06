@API @Sales @Admin
Feature: Sales API
  Scenario: Admin creates a sale and it appears in sales list
    Given the admin is authenticated
    When the admin creates a sale for a plant with quantity 2
    Then the sale creation response should be 201
    And the created sale should be present in the sales list

  Scenario: Admin retrieves all sales records
    Given the admin is authenticated
    When the admin requests the sales list
    Then the sales endpoint returns 200 and JSON array