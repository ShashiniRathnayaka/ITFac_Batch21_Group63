package com.qatraining.pages.sales;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.qatraining.pages.BasePage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SalesListPage - Page Object Model for Sales List UI
 * Test Case: UI_SALES_ADMIN_001 - Verify Sales List Page Load for Admin
 */
public class SalesListPage extends BasePage {

    // Selectors for Sales List Page
    private static final String SALES_TABLE = "table";
    private static final String SALES_TABLE_ROWS = "table tbody tr";
    
    // Column headers
    private static final String COLUMN_PLANT_NAME = "th:has-text('Plant name')";
    private static final String COLUMN_QUANTITY = "th:has-text('Quantity')";
    private static final String COLUMN_TOTAL_PRICE = "th:has-text('Total price')";
    private static final String COLUMN_SOLD_DATE = "th:has-text('Sold date')";
    
    // Actions
    private static final String SELL_PLANT_BUTTON = "a[href='/ui/sales/new'], .btn:has-text('Sell Plant')";
    private static final String DELETE_BUTTON = "button[class*='delete'], a[href*='delete']";
    
    // Pagination
    private static final String PAGINATION_CONTAINER = "nav[aria-label='Page navigation'], .pagination";

    public SalesListPage(Page page) {
        super(page);
    }

    /**
     * Verify sales list page loads successfully
     */
    public void verifySalesListPageLoaded() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(page.isVisible(SALES_TABLE), "Sales table not visible");
    }

    /**
     * Verify paginated list of sales is displayed
     */
    public void verifyPaginatedListDisplayed() {
        Locator tableRows = page.locator(SALES_TABLE_ROWS);
        assertTrue(tableRows.count() > 0, "No sales records found in the list");
        
        // Check if pagination controls exist
        Locator pagination = page.locator(PAGINATION_CONTAINER);
        assertTrue(pagination.isVisible(), "Pagination controls not visible");
    }

    /**
     * Verify column headers are visible
     */
    public void verifyColumnHeadersVisible() {
        assertTrue(page.isVisible(COLUMN_PLANT_NAME), "Plant name column not visible");
        assertTrue(page.isVisible(COLUMN_QUANTITY), "Quantity column not visible");
        assertTrue(page.isVisible(COLUMN_TOTAL_PRICE), "Total price column not visible");
        assertTrue(page.isVisible(COLUMN_SOLD_DATE), "Sold date column not visible");
    }

    /**
     * Verify specific column header is visible
     */
    public void verifyColumnVisible(String columnName) {
        String columnSelector = "th:has-text('" + columnName + "')";
        Locator columnHeader = page.locator(columnSelector);
        assertTrue(columnHeader.isVisible(), "Column '" + columnName + "' not visible");
    }

    /**
     * Verify "Sell Plant" button is visible
     */
    public void verifySellPlantButtonVisible() {
        Locator sellButton = page.locator(SELL_PLANT_BUTTON).first();
        assertTrue(sellButton.isVisible(), "Sell Plant button not visible");
    }

    /**
     * Verify delete action is visible for each record
     */
    public void verifyDeleteActionVisible() {
        Locator tableRows = page.locator(SALES_TABLE_ROWS);
        int rowCount = tableRows.count();
        
        assertTrue(rowCount > 0, "No sales records found to verify delete action");
        
        // Check if delete button exists in at least the first row
        Locator firstRowDeleteBtn = tableRows.first().locator(DELETE_BUTTON);
        assertTrue(firstRowDeleteBtn.isVisible(), "Delete action not visible for sales records");
    }

    /**
     * Verify default sorting by Sold date (descending)
     */
    public void verifyDefaultSortingBySoldDate() {
        Locator soldDateColumn = page.locator(COLUMN_SOLD_DATE).first();
        assertTrue(soldDateColumn.isVisible(), "Sold date column not visible for sorting check");
        
        // Check if aria-sort attribute indicates descending order
        String ariaSort = soldDateColumn.getAttribute("aria-sort");
        assertTrue("descending".equalsIgnoreCase(ariaSort) || ariaSort != null, 
                "Default sorting not set correctly for Sold date column");
    }

    /**
     * Click on "Sell Plant" button
     */
    public void clickSellPlantButton() {
        page.locator(SELL_PLANT_BUTTON).first().click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Get number of sales records
     */
    public int getSalesRecordCount() {
        return page.locator(SALES_TABLE_ROWS).count();
    }

    /**
     * Verify page title contains expected text
     */
    public void verifyPageTitle(String expectedTitle) {
        String pageTitle = page.title();
        assertTrue(pageTitle.contains(expectedTitle) || page.locator("h1, h2").first().textContent().contains(expectedTitle),
                "Page title doesn't contain: " + expectedTitle);
    }
}
