@API @sales
Feature: Sales API

  @admin @smoke
  Scenario: Admin can retrieve sales list via GET /api/sales
    Given I am authenticated as "admin" with password "admin123"
    When I send a GET request to "/api/sales"
    Then the response status code should be 200
    And the response should contain an array of sales
    And each sale should include "id"
    And each sale should include "plant"
    And each sale should include "quantity"
    And each sale should include "totalPrice"
    And each sale should include "soldDate"
    And the response should include pagination metadata
    And default sorting should be by "soldAt" descending

  @admin @create
  Scenario: Admin can create a new sale via POST /api/sales with valid data
    Given I am authenticated as "admin" with password "admin123"
    And there is a plant with stock greater than 0
    When I send a POST request to "/api/sales" with body:
      | plantId  | <plantId> |
      | quantity | <qty>     |
    Then the response status code should be 200 or 201
    And the response should contain the created sale object
    And the plant stock should be reduced by "<qty>"
    And the created sale should appear in GET "/api/sales"

  @admin @validation
  Scenario Outline: Admin receives validation errors when creating a sale with invalid data
    Given I am authenticated as "admin" with password "admin123"
    When I send a POST request to "/api/sales" with body:
      | plantId  | <plantId> |
      | quantity | <qty>     |
    Then the response status code should be 400
    And the error message should contain "<errorMessage>"
    And no sale should be created

    

  @admin @delete
  Scenario: Admin can delete a sale via DELETE /api/sales/{id}
    Given I am authenticated as "admin" with password "admin123"
    And there is an existing sale with id "<saleId>"
    When I send a DELETE request to "/api/sales/<saleId>"
    Then the response status code should be 200 or 204
    And the sale should be removed from the database
    When I send a GET request to "/api/sales/<saleId>"
    Then the response status code should be 404

  @admin @sorting
  Scenario Outline: Admin can sort sales list using sort query param
    Given I am authenticated as "admin" with password "admin123"
    And there are multiple sales records in the system
    When I send a GET request to "/api/sales?sort=<sortParam>"
    Then the response status code should be 200
    And the results should be sorted correctly by "<field>"

   

  @user @readonly
  Scenario: User can retrieve sales list via GET /api/sales
    Given I am authenticated as "testuser" with password "test123"
    When I send a GET request to "/api/sales"
    Then the response status code should be 200
    And the response should contain an array of sales
    And pagination and sorting should work correctly

  @user @security
  Scenario: User cannot create a sale via POST /api/sales
    Given I am authenticated as "testuser" with password "test123"
    And there is a plant with stock greater than 0
    When I send a POST request to "/api/sales" with body:
      | plantId  | <plantId> |
      | quantity | 1         |
    Then the response status code should be 401 or 403
    And the error message should indicate insufficient permissions
    And no sale should be created
    And the plant stock should remain unchanged

  @user @security
  Scenario: User cannot delete a sale via DELETE /api/sales/{id}
    Given I am authenticated as "testuser" with password "test123"
    And there is an existing sale with id "<saleId>"
    When I send a DELETE request to "/api/sales/<saleId>"
    Then the response status code should be 401 or 403
    And the sale record should NOT be deleted

  @user @pagination
  Scenario: User can paginate sales list using page & size parameters
    Given I am authenticated as "testuser" with password "test123"
    And there are more than 10 sales records
    When I send a GET request to "/api/sales?page=<page>&size=<size>"
    Then the response status code should be 200
    And the response should include total count, current page, and total pages


  @user
  Scenario: User can retrieve a single sale via GET /api/sales/{id}
    Given I am authenticated as "testuser" with password "test123"
    And there is an existing sale with id "<saleId>"
    When I send a GET request to "/api/sales/<saleId>"
    Then the response status code should be 200
    And the response should contain a complete sale object
    And the response should include "id"
    And the response should include "plant"
    And the response should include "quantity"
    And the response should include "totalPrice"
    And the response should include "soldAt"
