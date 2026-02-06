@API @Plants @Admin
Feature: Plant Management API
  Scenario: Admin creates plant under sub-category
    Given the admin is authenticated
    And a sub-category exists
    When the admin creates a plant under that sub-category with valid data
    Then the plant creation response status should be 201
    And the created plant should be linked to the sub-category
