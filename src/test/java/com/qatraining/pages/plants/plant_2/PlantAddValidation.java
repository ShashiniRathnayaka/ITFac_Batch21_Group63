package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for validation checks on Add Plant page
 */
public class PlantAddValidation extends BasePage {

    private static final String PLANT_ADD_PATH = "/ui/plants/add";
    private static final String PLANT_NAME_INPUT = "#name";
    private static final String CATEGORY_DROPDOWN = "#categoryId";
    private static final String PRICE_INPUT = "#price";
    private static final String QUANTITY_INPUT = "#quantity";
    private static final String SAVE_BUTTON = "button.btn.btn-primary";

    public PlantAddValidation(Page page) {
        super(page);
    }

    public void navigateToAddPlantPage() {
        navigate(PLANT_ADD_PATH);
        page.waitForSelector(PLANT_NAME_INPUT);
    }

    public void clearAllFields() {
        // Ensure fields are empty
        try { page.fill(PLANT_NAME_INPUT, ""); } catch (RuntimeException ignored) {}
        try { page.selectOption(CATEGORY_DROPDOWN, ""); } catch (RuntimeException ignored) {}
        try { page.fill(PRICE_INPUT, ""); } catch (RuntimeException ignored) {}
        try { page.fill(QUANTITY_INPUT, ""); } catch (RuntimeException ignored) {}
    }

    public void clickSaveButton() {
        page.click(SAVE_BUTTON);
        // short wait for validation messages to appear
        try {
            page.waitForSelector(".invalid-feedback, .text-danger, div.alert.alert-danger", new Page.WaitForSelectorOptions().setTimeout(2000));
        } catch (RuntimeException ignored) {
        }
    }

    public boolean areValidationMessagesVisible() {
        boolean invalidFeedback = page.locator(".invalid-feedback").count() > 0 && page.locator(".invalid-feedback").first().isVisible();
        boolean textDanger = page.locator("div.text-danger").count() > 0 && page.locator("div.text-danger").first().isVisible();
        boolean alertDanger = page.locator("div.alert.alert-danger").count() > 0 && page.locator("div.alert.alert-danger").first().isVisible();
        return invalidFeedback || textDanger || alertDanger;
    }
}
