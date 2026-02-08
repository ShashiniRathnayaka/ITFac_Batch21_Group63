@UI @sales
Feature: Sales UI

  @admin @smoke @UI_SALES_ADMIN_001
  Scenario: UI_SALES_ADMIN_001 - Admin can view Sales List page with all required elements
    Given I login to the UI as "admin" with password "admin123"
    When I navigate to "/ui/sales"
    Then the Sales list page should load successfully
    And I should see a paginated list of sales
    And I should see the column "Plant name"
    And I should see the column "Quantity"
    And I should see the column "Total price"
    And I should see the column "Sold At"
    And I should see the "Sell Plant" button
    And each row should have a "Delete" action
    And default sorting should be by "Sold At" (descending)

  @admin
  Scenario: Admin can access Sell Plant page from Sales List
    Given I login to the UI as "admin" with password "admin123"
    When I navigate to "/ui/sales"
    Then the "Sell Plant" button should be visible
    When I click the "Sell Plant" button
    Then I should be navigated to "/ui/sales/new"
    And the sell plant form should be displayed

  @admin @form @UI_SALES_ADMIN_001
  Scenario: Sell Plant form displays all required fields for Admin
    Given I login to the UI as "admin" with password "admin123"
    When I navigate to the sell plant form
    Then the sell plant form should be displayed
    And all form fields should be visible
    And the form should have a plant dropdown
    And the form should have a quantity input field
    And the form should have a sell button
    And the form should have a cancel button

  @admin @form
  Scenario: Sell Plant form plant dropdown shows available plants
    Given I login to the UI as "admin" with password "admin123"
    When I navigate to the sell plant form
    Then the plant dropdown should be populated with available plants
    And the form should have proper labels for all fields

  @admin @form
  Scenario: Sell Plant form quantity input has proper validation
    Given I login to the UI as "admin" with password "admin123"
    When I navigate to the sell plant form
    Then the quantity field should have a minimum value constraint of 1
    And the quantity field should accept only whole numbers
    And the quantity field should accept positive numbers only

  @admin @create
  Scenario: Admin can create a new sale successfully with form
    Given I login to the UI as "admin" with password "admin123"
    When I navigate to "/ui/sales/new"
    Then the plant dropdown should show available plants with stock greater than 0
    When I select a plant with available stock
    And I enter a valid quantity less than or equal to available stock
    And I submit the sale form
    Then the sale should be created successfully
    And I should be redirected to "/ui/sales"
    And the new sale should appear in the sales list

  @admin @form @cancel
  Scenario: Admin can cancel Sell Plant form and return to Sales List
    Given I login to the UI as "admin" with password "admin123"
    When I navigate to the sell plant form
    And I select the first available plant
    And I enter quantity "5"
    And I click the cancel button
    Then I should be returned to the sales list page
    And the form should not submit

  @admin @delete
  Scenario: Admin can delete a sale with confirmation
    Given I login to the UI as "admin" with password "admin123"
    And there is at least one sale record
    When I navigate to "/ui/sales"
    And I click "Delete" on a sale record
    Then I should see a deletion confirmation prompt
    When I confirm the deletion
    Then the sale record should be removed from the list
    And the sales list should be refreshed

  @admin @sorting
  Scenario: Admin can sort by Plant name, Quantity, Total price, and Sold At
    Given I login to the UI as "admin" with password "admin123"
    And there are multiple sales records
    When I navigate to "/ui/sales"
    Then each column header should be clickable for sorting
    When I sort by "Plant name"
    Then the list should be sorted by "Plant name" (asc/desc)
    When I sort by "Quantity"
    Then the list should be sorted by "Quantity" (asc/desc)
    When I sort by "Total price"
    Then the list should be sorted by "Total price" (asc/desc)
    When I sort by "Sold date"
    Then the list should be sorted by "Sold At" (asc/desc)

  @user @readonly @smoke
  Scenario: User can view Sales List page in read-only mode
    Given I login to the UI as "testuser" with password "test123"
    When I navigate to "/ui/sales"
    Then the Sales list page should load successfully
    And I should see sales records
    And I should NOT see the "Sell Plant" button
    And I should NOT see the "Delete" action for any record

  @user @security
  Scenario: User cannot access Sell Plant page directly
    Given I login to the UI as "testuser" with password "test123"
    When I navigate to "/ui/sales/new"
    Then I should see an access denied page (403) or be redirected to login
    And I should see an access denied message

  @user @sorting
  Scenario: User can sort sales list columns
    Given I login to the UI as "testuser" with password "test123"
    And there are multiple sales records
    When I navigate to "/ui/sales"
    And I sort by "Plant name"
    Then the list should be sorted by "Plant name" (asc/desc)
    When I sort by "Quantity"
    Then the list should be sorted by "Quantity" (asc/desc)
    When I sort by "Total price"
    Then the list should be sorted by "Total price" (asc/desc)
    When I sort by "Sold date"
    Then the list should be sorted by "Sold At" (asc/desc)
    And default sorting should be by "Sold At" (descending)

  @user @pagination
  Scenario: User can use pagination controls on Sales List
    Given I login to the UI as "testuser" with password "test123"
    And there are more sales records than one page can display
    When I navigate to "/ui/sales"
    Then pagination controls should be visible
    When I go to the next page
    Then the next page records should be displayed
    When I go to the previous page
    Then the previous page records should be displayed
    When I change the page size
    Then the records should update according to the new page size

  @user @empty
  Scenario: User sees "No sales found" message when there are no sales
    Given I login to the UI as "testuser" with password "test123"
    And there are no sales records in the system
    When I navigate to "/ui/sales"
    Then I should see the message "No sales found"
    And I should NOT see the sales table
