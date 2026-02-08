package com.qatraining.pages.plants.plant_1;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Add Plant functionality
 */
public class PlantsAdd extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String ADD_PLANT_BUTTON = "a[href='/ui/plants/add']";
    private static final String NAME_INPUT = "input[name='name']";
    private static final String CATEGORY_DROPDOWN = "select[name='categoryId']";
    private static final String PRICE_INPUT = "input[name='price']";
    private static final String QUANTITY_INPUT = "input[name='quantity']";
    private static final String SAVE_BUTTON = "button:has-text('Save')";
    public PlantsAdd(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(ADD_PLANT_BUTTON);
    }

    public boolean isAddPlantButtonVisible() {
        return page.isVisible(ADD_PLANT_BUTTON);
    }

    public void clickAddPlantButton() {
        click(ADD_PLANT_BUTTON);
        page.waitForURL("**/ui/plants/add");
        page.waitForSelector(NAME_INPUT);
    }

    public void enterPlantName(String name) {
        fill(NAME_INPUT, name);
    }

    public void selectCategory(String categoryLabel) {
        page.selectOption(CATEGORY_DROPDOWN, new com.microsoft.playwright.options.SelectOption().setLabel(categoryLabel));
    }

    public void selectCategoryById(String categoryId) {
        page.selectOption(CATEGORY_DROPDOWN, categoryId);
    }

    public void enterPrice(String price) {
        fill(PRICE_INPUT, price);
    }

    public void enterQuantity(String quantity) {
        fill(QUANTITY_INPUT, quantity);
    }

    public void clickSaveButton() {
        click(SAVE_BUTTON);
        page.waitForTimeout(3000);
    }

    public boolean isErrorMessageVisible() {
        // Check for common error message containers
        return page.locator(".alert-danger").count() > 0 
            || page.locator(".toast-error").count() > 0
            || page.locator("[role='alert']").count() > 0
            || page.locator(".Toastify__toast--error").count() > 0
            || page.locator("text=already exists").count() > 0
            || page.locator("text=Already exists").count() > 0;
    }

    public String getErrorMessage() {
        if (page.locator(".alert-danger").count() > 0) {
            return page.locator(".alert-danger").first().textContent();
        }
        if (page.locator(".toast-error").count() > 0) {
            return page.locator(".toast-error").first().textContent();
        }
        if (page.locator("[role='alert']").count() > 0) {
            return page.locator("[role='alert']").first().textContent();
        }
        if (page.locator(".Toastify__toast--error").count() > 0) {
            return page.locator(".Toastify__toast--error").first().textContent();
        }
        // Check for text containing "already exists"
        if (page.locator("text=already exists").count() > 0) {
            return page.locator("text=already exists").first().textContent();
        }
        if (page.locator("text=Already exists").count() > 0) {
            return page.locator("text=Already exists").first().textContent();
        }
        return "";
    }

    public boolean isSuccessMessageVisible() {
        // Check for common success message containers
        return page.locator(".alert-success").count() > 0 
            || page.locator(".toast-success").count() > 0
            || page.locator("[role='alert']").count() > 0
            || page.locator(".Toastify__toast--success").count() > 0;
    }

    public String getSuccessMessage() {
        if (page.locator(".alert-success").count() > 0) {
            return page.locator(".alert-success").first().textContent();
        }
        if (page.locator(".toast-success").count() > 0) {
            return page.locator(".toast-success").first().textContent();
        }
        if (page.locator("[role='alert']").count() > 0) {
            return page.locator("[role='alert']").first().textContent();
        }
        if (page.locator(".Toastify__toast--success").count() > 0) {
            return page.locator(".Toastify__toast--success").first().textContent();
        }
        return "";
    }

    public boolean isPlantInList(String plantName) {
        // Check if plant name exists in any table row
        return page.locator(String.format("table tbody tr:has-text('%s')", plantName)).count() > 0;
    }

    public String getCurrentUrl() {
        return page.url();
    }

}
