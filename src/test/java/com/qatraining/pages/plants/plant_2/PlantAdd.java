package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Plant Add page - adding a new plant with valid details
 */
public class PlantAdd extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String PLANT_ADD_PATH = "/ui/plants/add";
    private static final String ADD_PLANT_BUTTON = "a[href='/ui/plants/add']";
    private static final String PLANT_NAME_INPUT = "#name";
    private static final String CATEGORY_DROPDOWN = "#categoryId";
    private static final String PRICE_INPUT = "#price";
    private static final String QUANTITY_INPUT = "#quantity";
    private static final String SAVE_BUTTON = "button.btn.btn-primary";
    private static final String SUCCESS_MESSAGE = "div.alert.alert-success";

    public PlantAdd(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(ADD_PLANT_BUTTON);
    }

    public void clickAddPlantButton() {
        page.waitForSelector(ADD_PLANT_BUTTON);
        page.click(ADD_PLANT_BUTTON);
        page.waitForURL("**/ui/plants/add");
        page.waitForSelector(PLANT_NAME_INPUT);
    }

    public void enterPlantName(String plantName) {
        page.fill(PLANT_NAME_INPUT, plantName);
    }

    public void selectSubCategory(String subCategory) {
        page.selectOption(CATEGORY_DROPDOWN, new String[]{subCategory});
    }

    public void enterPrice(String price) {
        page.fill(PRICE_INPUT, price);
    }

    public void enterQuantity(String quantity) {
        page.fill(QUANTITY_INPUT, quantity);
    }

    public void clickSaveButton() {
        page.click(SAVE_BUTTON);
        // Try to detect success without blocking long on a single selector.
        // Prefer a redirect to the plants list (URL change) and fall back to a short wait for success alert.
        try {
            page.waitForURL("**/ui/plants", new Page.WaitForURLOptions().setTimeout(30000));
        } catch (RuntimeException e) {
            // navigation didn't happen within timeout; try short success alert check
            try {
                page.waitForSelector(SUCCESS_MESSAGE, new Page.WaitForSelectorOptions().setTimeout(2000));
            } catch (RuntimeException ignored) {
            }
        }
    }

    public boolean isPlantAddedSuccessfully() {
        // Success if redirected to plants list or success alert is visible.
        if (page.url().contains("/ui/plants")) {
            return true;
        }
        if (page.locator(SUCCESS_MESSAGE).count() > 0 && page.locator(SUCCESS_MESSAGE).isVisible()) {
            return true;
        }
        // Detect common validation/server error indicators and return false if present.
        if (page.locator("div.alert.alert-danger").count() > 0 || page.locator(".invalid-feedback").count() > 0 || page.locator(".text-danger").count() > 0) {
            return false;
        }
        return false;
    }
}
