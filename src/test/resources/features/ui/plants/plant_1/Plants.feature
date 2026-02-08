@Plants_FullSuite
@UI @Plants @Filter
Feature: Plants module functionality

  Background: Admin is logged in
    Given the user navigates to the login page
    When the user enters valid username "admin"
    And the user enters valid password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page

  @ui-plant-admin-filter
  Scenario: Verify Category Filter Functionality for Admin
    When the user navigates to Plants page
    And the user selects category "rose" from dropdown
    And the user clicks the Search button
    Then only plants from selected category should be displayed
    And the URL should contain categoryId "4"

  @ui-plant-admin-nofound-plant
  Scenario: Verify No Plants Found Message for Admin
    When the user navigates to Plants page
    And the user enters keyword "nonexistent-plant-xyz"
    And the user clicks the Search button
    Then no plants found message should be displayed and no rows shown
    And page controls should remain usable

  Scenario: Verify Plants List Loads Successfully for User
    Given the user navigates to the login page
    When the user enters valid username "testuser"
    And the user enters valid password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user navigates to Plants page
    Then table headers should be displayed as "Name", "Category", "Price", "Stock"
    And plant records should be displayed in the list

  Scenario: Verify Pagination Availability for User
    Given the user navigates to the login page
    When the user enters valid username "testuser"
    And the user enters valid password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user navigates to Plants page
    Then pagination controls are visible for user
    And only limited number of plants are displayed per page

  Scenario: Verify Category-Based Filtering for User
    Given the user navigates to the login page
    When the user enters valid username "testuser"
    And the user enters valid password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user navigates to Plants page
    And the user selects category "rose" from dropdown
    And the user clicks the Search button
    Then only plants from selected category should be displayed

  Scenario: Verify Reset Button Clears Search and Filters
    Given the user navigates to the login page
    When the user enters valid username "testuser"
    And the user enters valid password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user navigates to Plants page
    And the user enters keyword "test plant"
    And the user clicks the Search button
    And the user clicks the Reset button
    Then the search input should be cleared
    And the category dropdown should be reset to All Categories
    And the full plant list should be displayed

  @SearchWithSpace @BugVerification
  Scenario: Verify user can search plant by name with space
    Given the user navigates to the login page
    When the user enters valid username "testuser"
    And the user enters valid password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user navigates to Plants page
    And the user enters keyword with space in plant name
    And the user clicks the Search button
    Then the matching plant record should be displayed in results
    And the plant name should contain the searched keyword
