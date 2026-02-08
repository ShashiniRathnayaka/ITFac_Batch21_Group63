package com.qatraining.pages.categories;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.qatraining.pages.BasePage;

/**
 * Page Object for Add Category Page (UI)
 * Handles adding new categories (both main and sub-categories)
 */
public class AddCategoryPage extends BasePage {

    // Locators for Add Category Page elements
    private static final String PAGE_HEADING = "h3:has-text('Add Category')";
    private static final String CATEGORY_NAME_INPUT = "input#name";
    private static final String PARENT_CATEGORY_DROPDOWN = "select#parentId";
    private static final String SAVE_BUTTON = "button[type='submit']:has-text('Save')";
    private static final String CANCEL_BUTTON = "a[href='/ui/categories'].btn-secondary";

    // Success/Error message patterns
    private static final String ALERT_ROLE = "[role='alert']";
    private static final String TOAST_COMMON = ".toast, .Toastify__toast, .MuiAlert-message, .alert, .snackbar";
    private static final String SUCCESS_MESSAGE_PATTERN = "text=/success|created|added/i";

    public AddCategoryPage(Page page) {
        super(page);
    }

    /**
     * Navigate to Add Category page
     */
    public void navigateToAddCategoryPage() {
        navigate("/ui/categories/add");
        page.waitForLoadState();
    }

    /**
     * Check if Add Category page is loaded
     */
    public boolean isAddCategoryPageLoaded() {
        try {
            waitForElement(PAGE_HEADING);
            return getCurrentUrl().contains("/ui/categories/add");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enter category name
     */
    public void enterCategoryName(String categoryName) {
        fill(CATEGORY_NAME_INPUT, categoryName);
    }

    /**
     * Select parent category by value (use empty string for Main Category)
     */
    public void selectParentCategory(String parentValue) {
        page.selectOption(PARENT_CATEGORY_DROPDOWN, parentValue);
    }

    /**
     * Select parent category by visible text (label)
     * Best practice: Use text instead of hardcoded IDs for environment independence
     */
    public void selectParentCategoryByText(String parentText) {
        if (parentText == null || parentText.trim().isEmpty() || parentText.equalsIgnoreCase("Main Category")) {
            // Select the first option (Main Category / empty)
            selectParentCategory("");
        } else {
            page.selectOption(PARENT_CATEGORY_DROPDOWN, new SelectOption().setLabel(parentText));
        }
    }

    /**
     * Leave parent category empty (select Main Category)
     */
    public void leaveParentCategoryEmpty() {
        selectParentCategory("");
    }

    /**
     * Click Save button
     */
    public void clickSaveButton() {
        click(SAVE_BUTTON);
        page.waitForLoadState();
    }

    /**
     * Click Cancel button
     */
    public void clickCancelButton() {
        click(CANCEL_BUTTON);
        page.waitForLoadState();
    }

    /**
     * Get the current value of category name input
     */
    public String getCategoryNameValue() {
        return page.locator(CATEGORY_NAME_INPUT).inputValue();
    }

    /**
     * Get the selected value of parent category dropdown
     */
    public String getSelectedParentCategory() {
        return page.locator(PARENT_CATEGORY_DROPDOWN).inputValue();
    }

    /**
     * Get the selected parent category text (display text, not value)
     */
    public String getSelectedParentCategoryText() {
        Locator dropdown = page.locator(PARENT_CATEGORY_DROPDOWN);
        Locator selectedOption = dropdown.locator("option:checked");
        return selectedOption.textContent();
    }

    /**
     * Check if Save button is visible
     */
    public boolean isSaveButtonVisible() {
        return isVisible(SAVE_BUTTON);
    }

    /**
     * Check if Cancel button is visible
     */
    public boolean isCancelButtonVisible() {
        return isVisible(CANCEL_BUTTON);
    }

    /**
     * Get success message after saving category
     * This method tries multiple common patterns for success messages
     */
    public String getSuccessMessage() {
        try {
            // 1) Try role=alert first (common for success banners)
            Locator alert = page.locator(ALERT_ROLE).first();
            if (alert.count() > 0) {
                alert.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                String txt = safeText(alert);
                if (!txt.isBlank())
                    return txt;
            }

            // 2) Try common toast/snackbar containers
            Locator toast = page.locator(TOAST_COMMON).first();
            if (toast.count() > 0) {
                toast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                String txt = safeText(toast);
                if (!txt.isBlank())
                    return txt;
            }

            // 3) Try pattern matching for success-related text
            Locator success = page.locator(SUCCESS_MESSAGE_PATTERN).first();
            if (success.count() > 0) {
                success.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                String txt = safeText(success);
                if (!txt.isBlank())
                    return txt;
            }

            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        String message = getSuccessMessage();
        return !message.isBlank();
    }

    // Helper: safe trim text
    private String safeText(Locator locator) {
        String txt = locator.textContent();
        return txt == null ? "" : txt.trim();
    }
}
