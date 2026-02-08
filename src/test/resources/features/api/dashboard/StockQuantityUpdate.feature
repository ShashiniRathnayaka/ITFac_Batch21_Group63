@API @Sales @StockUpdate @Admin
Feature: Stock Quantity Update After Sale
  
  Scenario: API_DASH_006 - Verify stock quantity is reduced after recording a sale
    Given the admin is authenticated
    And a parent category exists
    And a sub-category exists under the parent category
    When the admin creates a plant with 100 units of stock
    Then the plant is created successfully with status 201
    And the initial stock quantity is verified as 100
    When the admin records a sale with quantity 25
    Then the sale is recorded successfully with status 201
    And the stock quantity is reduced to 75
    And the reduction amount is exactly 25 units
    
  Scenario: API_DASH_006 - Verify multiple sales reduce stock correctly
    Given the admin is authenticated
    And a parent category exists
    And a sub-category exists under the parent category
    And the admin creates a plant with 100 units of stock
    When the admin records a sale with quantity 25
    Then the stock quantity is reduced to 75
    When the admin records another sale with quantity 30
    Then the stock quantity is reduced to 45
