@UI @Categories
Feature: Edit Category
  As an admin user of the QA Training Application
  I want to be able to edit existing categories
  So that I can update category information as needed

  Background:
    Given Admin user is logged in
    And At least one Category record exists

  @Smoke @Positive @UI_CATEGORY_ADMIN_004
  Scenario: UI_CATEGORY_ADMIN_004 - Verify Admin can edit existing category successfully
    When the user navigates to "/ui/categories"
    And the user clicks the Edit icon on a Category
    Then System navigates to Edit Category page
    When the user enters a new valid category name "Update"
    And the user clicks the "Save" button on the form
    Then System redirects back to the Categories page
    And A Success message is displayed
    And The updated record appears in the Category list

  # Known Backend Bug: Edit icons ARE visible and clickable for USER role (should NOT be visible)
  # This test will FAIL until backend implements proper access control
  @Smoke @Negative @UI_CATEGORY_USER_004
  Scenario: UI_CATEGORY_USER_004 - Verify user cannot edit a category
    Given User is logged in
    And At least one Category record exists
    When the user navigates to "/ui/categories"
    Then Edit icon is not visible and clickable
