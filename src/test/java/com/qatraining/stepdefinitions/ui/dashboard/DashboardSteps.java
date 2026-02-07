package com.qatraining.stepdefinitions.ui.dashboard;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.pages.dashboard.DashboardPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step Definitions for Dashboard UI Tests
 * Covers UI_DASH_ADMIN_001 through UI_DASH_ADMIN_005
 * and UI_DASH_USER_001 through UI_DASH_USER_005
 */
public class DashboardSteps {

    private static final Logger logger = LoggerFactory.getLogger(DashboardSteps.class);
    private DashboardPage dashboardPage;

    public DashboardSteps() {
        this.dashboardPage = new DashboardPage(PlaywrightDriverManager.getPage());
    }

    // ============================================
    // Dashboard Page Display Steps (UI_DASH_ADMIN_001)
    // ============================================

    @Then("the user should be redirected to the dashboard page")
    public void userRedirectedToDashboard() {
        logger.info("Verifying user is redirected to dashboard page");
        dashboardPage.waitForCardsToRender();
        Assertions.assertTrue(dashboardPage.isDashboardPageDisplayed(), 
            "Dashboard page should be displayed after successful login");
    }

    @Then("the dashboard page should be displayed")
    public void dashboardPageShouldBeDisplayed() {
        logger.info("Verifying dashboard page is displayed");
        dashboardPage.waitForCardsToRender();
        Assertions.assertTrue(dashboardPage.isDashboardPageDisplayed(), 
            "Dashboard page should be displayed");
    }

    // ============================================
    // Dashboard Load Time Steps (UI_DASH_ADMIN_001)
    // ============================================

    @Then("verify the dashboard page loads within acceptable time")
    public void verifyDashboardLoadsQuickly() {
        logger.info("Verifying dashboard loads within acceptable time");
        Assertions.assertTrue(dashboardPage.isDashboardLoadedQuickly(), 
            "Dashboard should load within 5 seconds");
    }

    // ============================================
    // Individual Card Visibility Steps (UI_DASH_ADMIN_001)
    // ============================================

    @Then("verify the categories card is fully visible on the dashboard")
    public void verifyCategoriesCardFullyVisible() {
        logger.info("Verifying categories card is fully visible");
        Assertions.assertTrue(dashboardPage.isCategoryCardFullyVisible(), 
            "Categories card should be fully visible on the dashboard. " + 
            dashboardPage.getCategoryCardBoundingBoxInfo());
    }

    @Then("verify the plants card is fully visible on the dashboard")
    public void verifyPlantsCardFullyVisible() {
        logger.info("Verifying plants card is fully visible");
        Assertions.assertTrue(dashboardPage.isPlantsCardFullyVisible(), 
            "Plants card should be fully visible on the dashboard");
    }

    @Then("verify the sales card is fully visible on the dashboard")
    public void verifySalesCardFullyVisible() {
        logger.info("Verifying sales card is fully visible");
        Assertions.assertTrue(dashboardPage.isSalesCardFullyVisible(), 
            "Sales card should be fully visible on the dashboard");
    }

    @Then("verify the inventory card is fully visible on the dashboard")
    public void verifyInventoryCardFullyVisible() {
        logger.info("Verifying inventory card is fully visible");
        Assertions.assertTrue(dashboardPage.isInventoryCardFullyVisible(), 
            "Inventory card should be fully visible on the dashboard");
    }

    // ============================================
    // All Cards Visibility Step (UI_DASH_ADMIN_001 / UI_DASH_ADMIN_002)
    // ============================================

    @Then("verify all dashboard cards are fully visible")
    public void verifyAllDashboardCardsFullyVisible() {
        logger.info("Verifying all dashboard cards are fully visible");
        Assertions.assertTrue(dashboardPage.areAllCardsFullyVisible(), 
            "All dashboard cards should be fully visible");
    }

    // ============================================
    // Card Click and Highlight Steps (UI_DASH_ADMIN_002)
    // ============================================

    @When("the user clicks on the {string} card on the dashboard")
    public void userClicksOnCardOnDashboard(String cardName) {
        logger.info("User clicks on the '" + cardName + "' card on the dashboard");
        dashboardPage.clickDashboardCard(cardName);
    }

    @Then("the {string} card should be enlarged and highlighted")
    public void cardShouldBeEnlargedAndHighlighted(String cardName) {
        logger.info("Verifying '" + cardName + "' card is enlarged and highlighted");
        Assertions.assertTrue(dashboardPage.isCardEnlargedAndHighlighted(cardName),
            "The '" + cardName + "' card should be enlarged and highlighted after clicking");
    }

    @Then("the {string} card should navigate to the correct page")
    public void cardShouldNavigateToCorrectPage(String cardName) {
        logger.info("Clicking action button and verifying '" + cardName + "' card navigates to the correct page");
        dashboardPage.clickCardActionButton(cardName);
        Assertions.assertTrue(dashboardPage.hasCardNavigatedToCorrectPage(cardName),
            "The '" + cardName + "' card should navigate to the /ui/" + cardName.toLowerCase() + " page. Current URL: "
            + PlaywrightDriverManager.getPage().url());
    }

    // ============================================
    // Navigation Menu Steps (UI_DASH_ADMIN_003)
    // ============================================

    @When("the user clicks on the {string} navigation menu item")
    public void userClicksOnNavigationMenuItem(String menuItemName) {
        logger.info("User clicks on the '" + menuItemName + "' navigation menu item");
        dashboardPage.clickNavigationMenuItem(menuItemName);
    }

    @Then("the {string} navigation menu item should be highlighted")
    public void navigationMenuItemShouldBeHighlighted(String menuItemName) {
        logger.info("Verifying '" + menuItemName + "' navigation menu item is highlighted");
        Assertions.assertTrue(dashboardPage.isNavigationMenuItemHighlighted(menuItemName),
            "The '" + menuItemName + "' navigation menu item should be highlighted/active");
    }

    @Then("the system should navigate to the {string} page")
    public void systemShouldNavigateToPage(String pageName) {
        logger.info("Verifying system navigated to the '" + pageName + "' page");
        Assertions.assertTrue(dashboardPage.isOnCorrectPage(pageName),
            "System should navigate to the '" + pageName + "' page. Current URL: " 
            + PlaywrightDriverManager.getPage().url());
    }

    // ============================================
    // XPath Specific Verification Steps (legacy)
    // ============================================

    @Then("verify the category card is visible using xpath")
    public void verifyCategoryCardUsingXPath() {
        logger.info("Verifying category card using XPath");
        Assertions.assertTrue(dashboardPage.isCategoryCardVisibleUsingXPath(), 
            "Category card should be visible using XPath: /html/body/div/div/div[2]/div[2]/div/div/div[1]/div");
    }

    @Then("verify plants card is visible on dashboard")
    public void verifyPlantsCardVisible() {
        logger.info("Verifying plants card is visible");
        Assertions.assertTrue(dashboardPage.isPlantsCardFullyVisible(), 
            "Plants card should be visible on dashboard");
    }

    @Then("verify sales card is visible on dashboard")
    public void verifySalesCardVisible() {
        logger.info("Verifying sales card is visible");
        Assertions.assertTrue(dashboardPage.isSalesCardFullyVisible(), 
            "Sales card should be visible on dashboard");
    }

    @Then("verify inventory card is visible on dashboard")
    public void verifyInventoryCardVisible() {
        logger.info("Verifying inventory card is visible");
        Assertions.assertTrue(dashboardPage.isInventoryCardFullyVisible(), 
            "Inventory card should be visible on dashboard");
    }

    // ============================================
    // Data Table Steps (UI_DASH_ADMIN_004, UI_DASH_USER_002/003)
    // ============================================

    @Then("the data table should be visible")
    public void dataTableShouldBeVisible() {
        logger.info("Verifying data table is visible on current page");
        Assertions.assertTrue(dashboardPage.isDataTableVisible(),
            "Data table should be visible on the current page");
    }

    @Then("the table should have column {string}")
    public void tableShouldHaveColumn(String columnName) {
        logger.info("Verifying table has column: " + columnName);
        Assertions.assertTrue(dashboardPage.hasTableColumn(columnName),
            "Table should have column '" + columnName + "'. Actual columns: "
            + dashboardPage.getTableColumnHeaders());
    }

    @Then("no add or edit buttons should be visible")
    public void noAddOrEditButtonsShouldBeVisible() {
        logger.info("Verifying no add/edit buttons are visible (read-only user)");
        Assertions.assertFalse(dashboardPage.isAddButtonVisible(),
            "No Add buttons should be visible for read-only user");
    }

    @Then("the {string} button should not be visible")
    public void buttonShouldNotBeVisible(String buttonText) {
        logger.info("Verifying button '" + buttonText + "' is NOT visible");
        Assertions.assertFalse(dashboardPage.isButtonVisible(buttonText),
            "Button '" + buttonText + "' should not be visible for this user");
    }

    @Then("the {string} button should be visible on plants page")
    public void buttonShouldBeVisibleOnPlantsPage(String buttonText) {
        logger.info("Verifying button '" + buttonText + "' is visible");
        // Navigate to plants page first to check
        dashboardPage.clickNavigationMenuItem("Plants");
        Assertions.assertTrue(dashboardPage.isButtonVisible(buttonText),
            "Button '" + buttonText + "' should be visible for admin");
    }

    // ============================================
    // Session Stability Steps (UI_DASH_ADMIN_005)
    // ============================================

    @Then("the user should still be logged in")
    public void userShouldStillBeLoggedIn() {
        logger.info("Verifying user is still logged in");
        Assertions.assertTrue(dashboardPage.isUserStillLoggedIn(),
            "User should still be logged in after navigation");
    }

    @Then("the page should have no errors")
    public void pageShouldHaveNoErrors() {
        logger.info("Verifying page has no errors");
        Assertions.assertTrue(dashboardPage.hasNoErrors(),
            "Page should not have any error messages");
    }

    // ============================================
    // Browser Navigation Steps (UI_DASH_USER_005)
    // ============================================

    @When("the user navigates back in the browser")
    public void userNavigatesBack() {
        logger.info("User navigates back in the browser");
        dashboardPage.navigateBack();
    }

    @When("the user navigates forward in the browser")
    public void userNavigatesForward() {
        logger.info("User navigates forward in the browser");
        dashboardPage.navigateForward();
    }
}
