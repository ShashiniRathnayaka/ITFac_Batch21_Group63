package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.qatraining.pages.BasePage;

/**
 * Page object to verify sorting arrow visibility for table headers
 */
public class PlantSortArrow extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String PRICE_HEADER_LINK = "th a[href*='sortField=price']";
    private static final String STOCK_HEADER_LINK = "th a[href*='sortField=quantity']";
    private static final String HEADER_ARROW_REL = " span"; // relative selector to find arrow inside header link

    public PlantSortArrow(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        try { page.waitForSelector(PRICE_HEADER_LINK, new Page.WaitForSelectorOptions().setTimeout(5000)); } catch (RuntimeException ignored) {}
    }

    public boolean isPriceSortArrowVisible() {
        try {
            // Click header to ensure sort arrow appears (page may render arrow after sorting)
            try {
                page.click(PRICE_HEADER_LINK);
                try { page.waitForLoadState(LoadState.NETWORKIDLE); } catch (RuntimeException ignored) {}
            } catch (RuntimeException ignored) {}

            int count = page.locator(PRICE_HEADER_LINK + HEADER_ARROW_REL).count();
            if (count == 0) return false;
            String txt = page.locator(PRICE_HEADER_LINK + HEADER_ARROW_REL).first().textContent();
            boolean visible = page.locator(PRICE_HEADER_LINK + HEADER_ARROW_REL).first().isVisible();
            System.out.println("DEBUG: Price header arrow count=" + count + " text='" + txt + "' visible=" + visible);
            return visible && txt != null && !txt.trim().isEmpty();
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error checking price arrow: " + e.getMessage());
            return false;
        }
    }

    public boolean isStockSortArrowVisible() {
        try {
            // Click header to ensure sort arrow appears
            try {
                page.click(STOCK_HEADER_LINK);
                try { page.waitForLoadState(LoadState.NETWORKIDLE); } catch (RuntimeException ignored) {}
            } catch (RuntimeException ignored) {}

            int count = page.locator(STOCK_HEADER_LINK + HEADER_ARROW_REL).count();
            if (count == 0) return false;
            String txt = page.locator(STOCK_HEADER_LINK + HEADER_ARROW_REL).first().textContent();
            boolean visible = page.locator(STOCK_HEADER_LINK + HEADER_ARROW_REL).first().isVisible();
            System.out.println("DEBUG: Stock header arrow count=" + count + " text='" + txt + "' visible=" + visible);
            return visible && txt != null && !txt.trim().isEmpty();
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error checking stock arrow: " + e.getMessage());
            return false;
        }
    }
}
