@UI @Categories
Feature: Add Sub Category
  As an admin user of the QA Training Application
  I want to be able to add a new Sub Category
  So that I can organize products under parent categories

  Background:
    Given Admin user is logged in
    And At least one Main Category record exists

  @Smoke @Positive @UI_CATEGORY_ADMIN_003
  Scenario: UI_CATEGORY_ADMIN_003 - Verify Admin user can Add a new sub Category successfully with valid inputs
    When the user navigates to "/ui/categories"
    And the user clicks the "Add A Category" button
    Then System navigates to Add Category page
    When the user enters valid sub-category name "subcat" in the Category Name field
    And the user selects "Flowers" from the Parent Category dropdown
    Then Visible selected Parent Category value in the input field
    When the user clicks the "Save" button on the form
    Then System redirects back to the Categories page
    And A Success message is displayed
    And The newly added Category Name "subcat" and the Parent appears in the list
