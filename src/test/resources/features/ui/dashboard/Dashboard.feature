@UI @Dashboard
Feature: Dashboard - Card Display, Highlighting and Navigation
  As an admin user
  I want to verify that the dashboard loads correctly after login
  And that dashboard cards and navigation menu highlight properly
  So that I can navigate the application with clear visual feedback

  Background:
    Given the user navigates to the login page

  @Smoke @Positive
  Scenario: UI_DASH_ADMIN_001 - Dashboard will load quickly after admin successful login
    When the user enters username "admin"
    And the user enters password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    And the dashboard page should be displayed
    And verify the dashboard page loads within acceptable time
    And verify the categories card is fully visible on the dashboard
    And verify the plants card is fully visible on the dashboard
    And verify the sales card is fully visible on the dashboard
    And verify the inventory card is fully visible on the dashboard

  @Positive
  Scenario: UI_DASH_ADMIN_002 - Verify menu card page highlights the active page
    When the user enters username "admin"
    And the user enters password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    And verify all dashboard cards are fully visible
    When the user clicks on the "Categories" card on the dashboard
    Then the "Categories" card should be enlarged and highlighted
    And the "Categories" card should navigate to the correct page
    When the user clicks on the "Dashboard" navigation menu item
    And the user clicks on the "Sales" card on the dashboard
    Then the "Sales" card should be enlarged and highlighted
    And the "Sales" card should navigate to the correct page
    When the user clicks on the "Dashboard" navigation menu item
    And the user clicks on the "Plants" card on the dashboard
    Then the "Plants" card should be enlarged and highlighted
    And the "Plants" card should navigate to the correct page

  @Positive
  Scenario: UI_DASH_ADMIN_003 - Verify navigation menu highlights the active page
    When the user enters username "admin"
    And the user enters password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    And the "Dashboard" navigation menu item should be highlighted
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    And the "Categories" navigation menu item should be highlighted
    When the user clicks on the "Sales" navigation menu item
    Then the system should navigate to the "Sales" page
    And the "Sales" navigation menu item should be highlighted
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    And the "Plants" navigation menu item should be highlighted
    When the user clicks on the "Dashboard" navigation menu item
    Then the system should navigate to the "Dashboard" page
    And the "Dashboard" navigation menu item should be highlighted

  @Positive
  Scenario: UI_DASH_ADMIN_004 - All data tables display correctly with required columns
    When the user enters username "admin"
    And the user enters password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    And the data table should be visible
    And the table should have column "ID"
    And the table should have column "Name"
    And the table should have column "Parent"
    And the table should have column "Actions"
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    And the data table should be visible
    And the table should have column "Name"
    And the table should have column "Price"
    And the table should have column "Stock"
    And the table should have column "Actions"
    When the user clicks on the "Sales" navigation menu item
    Then the system should navigate to the "Sales" page
    And the data table should be visible
    And the table should have column "Plant"
    And the table should have column "Quantity"
    And the table should have column "Total Price"
    And the table should have column "Sold At"

  @Positive
  Scenario: UI_DASH_ADMIN_005 - Admin session remains stable during extended navigation
    When the user enters username "admin"
    And the user enters password "admin123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    And the user should still be logged in
    And the page should have no errors
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    And the user should still be logged in
    And the page should have no errors
    When the user clicks on the "Sales" navigation menu item
    Then the system should navigate to the "Sales" page
    And the user should still be logged in
    And the page should have no errors
    When the user clicks on the "Dashboard" navigation menu item
    Then the system should navigate to the "Dashboard" page
    And the user should still be logged in
    And the page should have no errors
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    When the user clicks on the "Sales" navigation menu item
    Then the system should navigate to the "Sales" page
    And the user should still be logged in
    And the page should have no errors
    And the "Add a Plant" button should be visible on plants page

  @Smoke @Positive
  Scenario: UI_DASH_USER_001 - Dashboard will load quickly after user successful login
    When the user enters username "testuser"
    And the user enters password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    And the dashboard page should be displayed
    And verify the dashboard page loads within acceptable time
    And verify the categories card is fully visible on the dashboard
    And verify the plants card is fully visible on the dashboard
    And verify the sales card is fully visible on the dashboard
    And verify the inventory card is fully visible on the dashboard

  @Positive
  Scenario: UI_DASH_USER_002 - User can navigate all read-only pages successfully
    When the user enters username "testuser"
    And the user enters password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    And the data table should be visible
    And no add or edit buttons should be visible
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    And the data table should be visible
    And no add or edit buttons should be visible
    When the user clicks on the "Sales" navigation menu item
    Then the system should navigate to the "Sales" page
    And the data table should be visible
    And no add or edit buttons should be visible

  @Positive
  Scenario: UI_DASH_USER_003 - User can view all data tables without edit capabilities
    When the user enters username "testuser"
    And the user enters password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    And the data table should be visible
    And the "Add A Category" button should not be visible
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    And the data table should be visible
    And the "Add a Plant" button should not be visible
    When the user clicks on the "Sales" navigation menu item
    Then the system should navigate to the "Sales" page
    And the data table should be visible

  @Positive
  Scenario: UI_DASH_USER_004 - Verify navigation menu bar
    When the user enters username "testuser"
    And the user enters password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    And the "Dashboard" navigation menu item should be highlighted
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    And the "Categories" navigation menu item should be highlighted
    When the user clicks on the "Sales" navigation menu item
    Then the system should navigate to the "Sales" page
    And the "Sales" navigation menu item should be highlighted
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    And the "Plants" navigation menu item should be highlighted
    When the user clicks on the "Dashboard" navigation menu item
    Then the system should navigate to the "Dashboard" page
    And the "Dashboard" navigation menu item should be highlighted

  @Positive
  Scenario: UI_DASH_USER_005 - User can navigate forward and back between pages
    When the user enters username "testuser"
    And the user enters password "test123"
    And the user clicks on the login button
    Then the user should be redirected to the dashboard page
    When the user clicks on the "Categories" navigation menu item
    Then the system should navigate to the "Categories" page
    When the user clicks on the "Plants" navigation menu item
    Then the system should navigate to the "Plants" page
    When the user navigates back in the browser
    Then the system should navigate to the "Categories" page
    And the user should still be logged in
    When the user navigates back in the browser
    Then the system should navigate to the "Dashboard" page
    And the user should still be logged in
    When the user navigates forward in the browser
    Then the system should navigate to the "Categories" page
    And the user should still be logged in
    And the page should have no errors
