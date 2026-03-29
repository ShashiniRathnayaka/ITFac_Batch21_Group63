@UI @Plants @Pagination
Feature: Plants pagination (Admin view)

  Background: Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  Scenario: Verify Plants Page Load for Admin with Pagination
    When the user navigates to the Plants page
    And the user observes number of plants shown on first page
    Then the first page should display at most 10 plants
    And pagination controls should be visible
    When the user clicks Next
    Then the next page should display remaining plants
