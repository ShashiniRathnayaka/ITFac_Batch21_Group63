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
    private static final String EDIT_ICON_SELECTOR = "i.bi.bi-pencil-square";
    private static final String CATEGORY_DROPDOWN_SELECTOR = "select#categoryId";
    private static final String SAVE_BUTTON_SELECTOR = "button.btn.btn-primary";

    public PlantsEdit(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(ROWS_SELECTOR);
    }

    public void clickEditIcon() {
        page.locator(EDIT_ICON_SELECTOR).first().click();
    }

    public void selectSubCategory(String subCategory) {
        // Get all options from the dropdown
        int optionCount = (int) page.locator(CATEGORY_DROPDOWN_SELECTOR + " option").count();
        String optionValue = "";
        
        // Search for the option matching the category name
        for (int i = 0; i < optionCount; i++) {
            String optionText = page.locator(CATEGORY_DROPDOWN_SELECTOR + " option").nth(i).textContent().trim();
            if (optionText.equalsIgnoreCase(subCategory)) {
                optionValue = page.locator(CATEGORY_DROPDOWN_SELECTOR + " option").nth(i).getAttribute("value");
                break;
            }
        }
        
        // If option value found, use it; otherwise use the subCategory as-is
        if (!optionValue.isEmpty()) {
            page.locator(CATEGORY_DROPDOWN_SELECTOR).selectOption(optionValue);
        } else {
            page.locator(CATEGORY_DROPDOWN_SELECTOR).selectOption(subCategory);
        }
    }

    public void clickSaveButton() {
        page.locator(SAVE_BUTTON_SELECTOR).click();
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

    public String getPlantCategory() {
        // Get the category value from the first plant row
        // Table structure: Plant Name (0) | Category (1) | Price (2) | Actions (3)
        // Read directly from column index 1 (Category column)
        int tdCount = (int) page.locator(ROWS_SELECTOR + " td").count();
        
        String categoryValue = "";
        
        // Try to get the category from the 2nd column (index 1)
        if (tdCount > 1) {
            categoryValue = page.locator(ROWS_SELECTOR + " td").nth(1).textContent().trim();
        }
        
        return categoryValue;
    }

    public boolean isSuccessMessageDisplayed() {
        // Check for success message (common selectors for bootstrap alerts)
        int alertCount = page.locator(".alert-success, .success-message, [role='alert']").count();
        return alertCount > 0;
    }

}
