
Feature: Plants API - Admin Create Plant

  @adminapi
  Scenario: Verify admin can create a new plant
    When Admin sends POST request to create a new plant
    Then API should return 201 Created for plant creation
    And Created plant details should be reflected in response
    And Plant should be persisted in the database

  @adminapi
  Scenario Outline: Verify plant input validation during plant creation
    When Admin sends POST request to create a plant with invalid data:
      | name      | price   | quantity | categoryId |
      | <name>    | <price> | <quantity> | <categoryId> |
    Then API should return 400 Bad Request for invalid plant creation
    And Validation error message for <field> should be returned
    And Plant should not be created in the system

    Examples:
      | name   | price | quantity | categoryId | field      |
      |        | 1500  | 50       | 4          | name       |
      | ab     | 1500  | 50       | 4          | name       |
      | Orange rose 1234522222222222222222222222222222223333333333 | 1500 | 50 | 4 | name |
      | Orange rose 1234 | -1500 | 50 | 4 | price |
      | Orange rose 1234 | 1500 | -5 | 4 | quantity |
