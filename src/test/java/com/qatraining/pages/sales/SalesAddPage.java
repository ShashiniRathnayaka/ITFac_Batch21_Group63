package com.qatraining.pages.sales;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import com.qatraining.pages.BasePage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SalesAddPage - Page Object Model for Sell Plant Form
 * Handles the form to create/add new sales
 */
public class SalesAddPage extends BasePage {

    // Form Selectors
    private static final String SELL_PLANT_FORM = "form[action='/ui/sales']";
    private static final String PLANT_SELECT = "#plantId, select[name='plantId']";
    private static final String QUANTITY_INPUT = "#quantity, input[name='quantity']";
    private static final String SELL_BUTTON = "button:has-text('Sell'), button[type='submit']";
    private static final String CANCEL_BUTTON = "a[href='/ui/sales']:has-text('Cancel'), button:has-text('Cancel')";
    private static final String ERROR_MESSAGE = ".alert-danger, .error";

    public SalesAddPage(Page page) {
        super(page);
    }

    /**
     * Verify Sell Plant form is displayed
     */
    public void verifySellPlantFormDisplayed() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(page.isVisible(SELL_PLANT_FORM), "Sell Plant form not visible");
        assertTrue(page.isVisible(PLANT_SELECT), "Plant select field not visible");
        assertTrue(page.isVisible(QUANTITY_INPUT), "Quantity input field not visible");
    }

    /**
     * Verify form has required fields
     */
    public void verifyFormHasRequiredFields() {
        Locator plantSelect = page.locator(PLANT_SELECT);
        Locator quantityInput = page.locator(QUANTITY_INPUT);
        Locator sellBtn = page.locator(SELL_BUTTON);
        
        assertTrue(plantSelect.isVisible(), "Plant select not visible");
        assertTrue(quantityInput.isVisible(), "Quantity input not visible");
        assertTrue(sellBtn.isVisible(), "Sell button not visible");
    }

    /**
     * Select plant from dropdown
     */
    public void selectPlant(String plantName) {
        Locator plantSelect = page.locator(PLANT_SELECT);
        plantSelect.selectOption(plantName);
    }

    /**
     * Enter quantity
     */
    public void enterQuantity(String quantity) {
        Locator quantityInput = page.locator(QUANTITY_INPUT);
        quantityInput.fill(quantity);
    }

    /**
     * Click Sell button
     */
    public void clickSellButton() {
        page.locator(SELL_BUTTON).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Click Cancel button
     */
    public void clickCancelButton() {
        page.locator(CANCEL_BUTTON).click(new Locator.ClickOptions().setNoWaitAfter(true));
        try {
            page.waitForURL("**/ui/sales", new Page.WaitForURLOptions().setTimeout(30_000));
        } catch (Exception ignored) {
        }
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    /**
     * Get all available plant options
     */
    public int getPlantOptionsCount() {
        return page.locator(PLANT_SELECT + " option").count();
    }

    /**
     * Verify error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return page.isVisible(ERROR_MESSAGE);
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return page.textContent(ERROR_MESSAGE);
    }

    /**
     * Submit form with plant and quantity
     */
    public void submitSaleForm(String plant, String quantity) {
        selectPlant(plant);
        enterQuantity(quantity);
        clickSellButton();
    }

    /**
     * Verify form page URL
     */
    public void verifyFormPageUrl() {
        String currentUrl = page.url();
        assertTrue(currentUrl.contains("/ui/sales/new"), "Not on the sales form page. Current URL: " + currentUrl);
    }

    /**
     * Verify quantity field has correct minimum value
     */
    public void verifyQuantityFieldValidation() {
        Locator quantityInput = page.locator(QUANTITY_INPUT);
        String minValue = quantityInput.getAttribute("min");
        assertTrue(minValue != null && minValue.equals("1"), "Quantity field min value should be 1");
    }

    /**
     * Verify all form fields are visible
     */
    public void verifyAllFieldsVisible() {
        verifyFormHasRequiredFields();
    }

    /**
     * Verify plant dropdown is visible
     */
    public void verifyPlantDropdownVisible() {
        assertTrue(page.isVisible(PLANT_SELECT), "Plant dropdown not visible");
    }

    /**
     * Verify quantity input is visible
     */
    public void verifyQuantityInputVisible() {
        assertTrue(page.isVisible(QUANTITY_INPUT), "Quantity input not visible");
    }

    /**
     * Verify sell button is visible
     */
    public void verifySellButtonVisible() {
        assertTrue(page.isVisible(SELL_BUTTON), "Sell button not visible");
    }

    /**
     * Verify cancel button is visible
     */
    public void verifyCancelButtonVisible() {
        assertTrue(page.isVisible(CANCEL_BUTTON), "Cancel button not visible");
    }

    /**
     * Select first available plant from dropdown
     */
    public void selectFirstAvailablePlant() {
        Locator plantSelect = page.locator(PLANT_SELECT);
        Locator options = plantSelect.locator("option");

        // SelectOption with index 1 skips the placeholder (index 0), but options can be re-rendered.
        for (int t = 0; t < 60; t++) {
            int count = options.count();
            if (count > 1) {
                Locator option = options.nth(1);
                String value = option.getAttribute("value");
                String label = option.innerText();

                try {
                    if (value != null && !value.isBlank()) {
                        plantSelect.selectOption(value);
                    } else if (label != null && !label.isBlank()) {
                        plantSelect.selectOption(new SelectOption().setLabel(label.trim()));
                    } else {
                        plantSelect.selectOption(new SelectOption().setIndex(1));
                    }
                    return;
                } catch (Exception ignored) {
                    // retry
                }
            }
            page.waitForTimeout(500);
        }

        fail("Plant dropdown options did not populate in time - no selectable option found");
    }

    /**
     * Get currently selected plant value
     */
    public String getSelectedPlant() {
        Locator plantSelect = page.locator(PLANT_SELECT);
        return plantSelect.inputValue();
    }

    /**
     * Get number of available plants in dropdown
     */
    public int getAvailablePlantsCount() {
        return getPlantOptionsCount();
    }

    /**
     * Get current quantity value entered
     */
    public String getQuantityValue() {
        Locator quantityInput = page.locator(QUANTITY_INPUT);
        return quantityInput.inputValue();
    }

    /**
     * Verify minimum validation for quantity field
     */
    public void verifyQuantityMinimumValidation() {
        verifyQuantityFieldValidation();
    }
}
