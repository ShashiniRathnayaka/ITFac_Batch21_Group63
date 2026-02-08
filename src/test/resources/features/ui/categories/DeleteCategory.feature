@UI @Categories @Negative
Feature: Delete Category

  @UI_CATEGORY_ADMIN_005
  Scenario: UI_CATEGORY_ADMIN_005 - Verify that the Admin cannot delete the parent category when sub-categories exist
    Given Admin user is logged in
    And At least one Parent Category with sub-categories exists
    When the user navigates to "/ui/categories"
    And the user clicks the Delete icon for category "Flowers"
    Then Confirmation popup is displayed and user confirms deletion
    And An error message is displayed: "Cannot delete category. Please delete sub-categories first."
    And The category "Flowers" is not deleted and remains in the list

  @UI_CATEGORY_USER_005
  Scenario: UI_CATEGORY_USER_005 - Verify user cannot delete a category
    Given User is logged in
    And At least one Category record exists
    When the user navigates to "/ui/categories"
    And the user observes the Delete icon
    And the user attempts to click Delete
    Then Delete action is disabled or restricted
    And Category is not deleted
    And No data changes occur
