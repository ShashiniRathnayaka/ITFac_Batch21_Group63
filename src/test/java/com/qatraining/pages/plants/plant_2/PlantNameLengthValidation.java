package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for name length validation on Add Plant page
 */
public class PlantNameLengthValidation extends BasePage {

    private static final String PLANT_NAME_INPUT = "#name";
    private static final String SAVE_BUTTON = "button.btn.btn-primary";

    public PlantNameLengthValidation(Page page) {
        super(page);
    }

    public void enterPlantName(String plantName) {
        page.waitForSelector(PLANT_NAME_INPUT);
        page.fill(PLANT_NAME_INPUT, plantName);
    }

    public void clearPlantNameField() {
        page.fill(PLANT_NAME_INPUT, "");
    }

    public void clickSaveButton() {
        page.click(SAVE_BUTTON);
        // Short wait for validation message to appear
        try {
            page.waitForSelector("div.text-danger", new Page.WaitForSelectorOptions().setTimeout(2000));
        } catch (RuntimeException ignored) {
        }
    }

    public boolean isNameLengthValidationMessageVisible() {
        // Look for name length specific validation message or generic validation text
        boolean hasDangerMessage = page.locator("div.text-danger").count() > 0 && 
                                   page.locator("div.text-danger").first().isVisible();
        boolean hasInvalidFeedback = page.locator(".invalid-feedback").count() > 0 && 
                                     page.locator(".invalid-feedback").first().isVisible();
        return hasDangerMessage || hasInvalidFeedback;
    }
}
