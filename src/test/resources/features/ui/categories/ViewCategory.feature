@UI @Categories
Feature: View Categories Page for Admin
  As an admin user of the QA Training Application
  I want to be able to view the Categories list page
  So that I can see all categories with proper UI elements

  Background:
    Given Admin user is logged in
    And Category records exist in the system

  @Smoke @Positive @UI_CATEGORY_ADMIN_001
  Scenario: UI_CATEGORY_ADMIN_001 - Verify Categories Page Load for Admin
    When the user navigates to "/ui/categories"
    And the user observes page elements
    Then Categories page loads successfully
    And Search box, Parent filter, Search and Add Category buttons are visible
    And Categories are displayed in a table with pagination

  @Smoke @Positive @UI_CATEGORY_USER_001
  Scenario: UI_CATEGORY_USER_001 - Verify user has read-only access to categories page
    Given User is logged in
    And Category records exist in the system
    When the user navigates to "/ui/categories"
    And the user observes page elements
    Then Categories page loads successfully
    And List of categories with Name and Parent is visible
    And Add Category option is not available
    And Edit/Delete actions are restricted

  @Smoke @Positive @UI_CATEGORY_USER_002
  Scenario: UI_CATEGORY_USER_002 - Verify user can view paginated list of categories
    Given User is logged in
    And More than 10 category records exist
    When the user navigates to "/ui/categories"
    And the user observes page elements
    Then A maximum of 10 category records are displayed per page
    And Pagination controls are visible
    When the user clicks the Next pagination button
    Then The next set of category records is loaded

  @Smoke @Positive @UI_CATEGORY_USER_003
  Scenario: UI_CATEGORY_USER_003 - Verify user can filter categories using Parent Category dropdown
    Given User is logged in
    And Categories with different parent categories exist
    When the user navigates to "/ui/categories"
    And the user clicks the Parent Category dropdown
    And the user selects "Flowers" from the parent filter
    And the user clicks the Search button
    Then Category list is filtered based on the selected parent category
    And Only categories belonging to "Flowers" are displayed
    And User remains in read-only mode
