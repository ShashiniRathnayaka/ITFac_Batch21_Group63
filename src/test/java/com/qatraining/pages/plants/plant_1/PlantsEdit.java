package com.qatraining.pages.plants.plant_1;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Plants page (pagination and list view helpers)
 */
public class PlantsEdit extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String ROWS_SELECTOR = "table tbody tr";
    private static final String PAGINATION_SELECTORS = "ul.pagination, .pagination, nav[aria-label='Page navigation']";
    private static final String NEXT_SELECTOR_TEXT = "text=Next";

    public PlantsEdit(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(ROWS_SELECTOR);
    }

    public int getVisiblePlantsCount() {
        return (int) page.locator(ROWS_SELECTOR).count();
    }

    public boolean isPaginationVisible() {
        return page.locator(PAGINATION_SELECTORS).count() > 0;
    }

    public void clickNext() {
        if (page.locator(NEXT_SELECTOR_TEXT).count() > 0) {
            page.locator(NEXT_SELECTOR_TEXT).first().click();
        } else if (page.locator("ul.pagination li >> text=Next").count() > 0) {
            page.locator("ul.pagination li >> text=Next").first().click();
        }
        page.waitForTimeout(800);
        page.waitForSelector(ROWS_SELECTOR);
    }

    public void clickPageNumber(int pageNumber) {
        String sel = String.format("ul.pagination >> text=\"%d\"", pageNumber);
        if (page.locator(sel).count() > 0) {
            page.locator(sel).first().click();
            page.waitForTimeout(800);
            page.waitForSelector(ROWS_SELECTOR);
        }
    }

}
