@UI @Categories
Feature: Add Main Category
  As an admin user of the QA Training Application
  I want to be able to add a new Main Category
  So that I can organize products effectively

  Background:
    Given Admin user is logged in

  @Smoke @Positive @UI_CATEGORY_ADMIN_002
  Scenario: UI_CATEGORY_ADMIN_002 - Verify Admin can Add new Main Category successfully with valid inputs
    When the user navigates to "/ui/categories"
    And the user clicks the "Add A Category" button
    Then System navigates to Add Category page
    When the user enters valid category name "cat" in the Category Name field
    And the user leaves the Parent Category dropdown empty
    And the user clicks the "Save" button on the form
    Then System redirects back to the Categories page
    And A Success message is displayed
    And The newly added category "cat" appears in the list
