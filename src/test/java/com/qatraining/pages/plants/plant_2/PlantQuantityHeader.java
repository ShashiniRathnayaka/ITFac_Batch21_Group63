package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object to verify quantity (Stock) table header visibility
 */
public class PlantQuantityHeader extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String QUANTITY_HEADER_LINK = "th a[href*='sortField=quantity']";
    private static final String QUANTITY_TEXT_SELECTOR = "thead th:has-text('Quantity')";
    private static final String STOCK_TEXT_SELECTOR = "thead th:has-text('Stock')";

    public PlantQuantityHeader(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        try {
            page.waitForSelector(QUANTITY_HEADER_LINK, new Page.WaitForSelectorOptions().setTimeout(5000));
        } catch (RuntimeException ignored) {
        }
    }

    public boolean isQuantityHeaderVisible() {
        try {
            // Check if the "Quantity" text is displayed (expected requirement)
            int quantityCount = page.locator(QUANTITY_TEXT_SELECTOR).count();
            if (quantityCount > 0 && page.locator(QUANTITY_TEXT_SELECTOR).isVisible()) {
                String text = page.locator(QUANTITY_TEXT_SELECTOR).textContent();
                System.out.println("DEBUG: 'Quantity' header found and visible. Text: '" + text + "'");
                return true;
            }

            // Log what we actually found (Stock instead of Quantity - BUG)
            int stockCount = page.locator(STOCK_TEXT_SELECTOR).count();
            if (stockCount > 0) {
                System.out.println("DEBUG: BUG DETECTED - Found 'Stock' column header instead of 'Quantity'");
            }

            System.out.println("DEBUG: 'Quantity' header NOT found (quantityCount=" + quantityCount + ", stockCount=" + stockCount + ")");
            return false;
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error checking quantity header: " + e.getMessage());
            return false;
        }
    }
}
