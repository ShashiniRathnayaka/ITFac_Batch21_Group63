package com.qatraining.pages.categories;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.qatraining.pages.BasePage;

/**
 * Page Object for Edit Category Page (UI)
 * Handles editing existing categories
 */
public class EditCategoryPage extends BasePage {

    // Locators for Edit Category Page elements
    private static final String PAGE_HEADING = "h3:has-text('Edit Category')";
    private static final String CATEGORY_NAME_INPUT = "input#name";
    private static final String PARENT_CATEGORY_DROPDOWN = "select#parentId";
    private static final String SAVE_BUTTON = "button[type='submit']:has-text('Save')";
    private static final String CANCEL_BUTTON = "a[href='/ui/categories'].btn-secondary";

    // Success/Error message patterns
    private static final String ALERT_ROLE = "[role='alert']";
    private static final String TOAST_COMMON = ".toast, .Toastify__toast, .MuiAlert-message, .alert, .snackbar";
    private static final String SUCCESS_MESSAGE_PATTERN = "text=/success|updated|saved/i";
    private static final String ERROR_MESSAGE = ".alert.alert-danger";

    public EditCategoryPage(Page page) {
        super(page);
    }

    /**
     * Check if Edit Category page is loaded
     */
    public boolean isEditCategoryPageLoaded() {
        try {
            waitForElement(PAGE_HEADING);
            return getCurrentUrl().contains("/ui/categories/edit/");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the current value of category name input
     */
    public String getCategoryNameValue() {
        return page.locator(CATEGORY_NAME_INPUT).inputValue();
    }

    /**
     * Clear and enter new category name
     */
    public void updateCategoryName(String categoryName) {
        page.locator(CATEGORY_NAME_INPUT).fill(categoryName);
    }

    /**
     * Select parent category by value
     */
    public void selectParentCategory(String parentValue) {
        page.selectOption(PARENT_CATEGORY_DROPDOWN, parentValue);
    }

    /**
     * Select parent category by visible text (label)
     * Best practice: Use text instead of hardcoded IDs for environment independence
     */
    public void selectParentCategoryByText(String parentText) {
        page.selectOption(PARENT_CATEGORY_DROPDOWN, new SelectOption().setLabel(parentText));
    }

    /**
     * Get the selected parent category value
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
     * Get success message after saving category
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

    /**
     * Get error message from the alert
     */
    public String getErrorMessage() {
        try {
            Locator errorAlert = page.locator(ERROR_MESSAGE).first();
            if (errorAlert.count() > 0) {
                errorAlert.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                return safeText(errorAlert);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            Locator errorAlert = page.locator(ERROR_MESSAGE).first();
            return errorAlert.count() > 0 && errorAlert.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if error message contains "403 Forbidden"
     */
    public boolean isErrorMessage403Forbidden() {
        String errorMessage = getErrorMessage();
        return errorMessage.contains("403") && errorMessage.toLowerCase().contains("forbidden");
    }

    /**
     * Select a different parent category (not the currently selected one)
     * Dynamically selects the first available option that differs from current
     * selection
     */
    public void selectDifferentParentCategory() {
        try {
            Locator dropdown = page.locator(PARENT_CATEGORY_DROPDOWN);
            String currentValue = dropdown.inputValue();

            // Get all option values
            Locator options = dropdown.locator("option");
            int optionCount = options.count();

            // Find the first option with a different value than current
            for (int i = 0; i < optionCount; i++) {
                String optionValue = options.nth(i).getAttribute("value");
                if (optionValue != null && !optionValue.equals(currentValue)) {
                    dropdown.selectOption(optionValue);
                    System.out.println("Changed parent category from '" + currentValue + "' to '" + optionValue + "'");
                    return;
                }
            }

            // If no different option found (shouldn't happen), log warning
            System.out.println("Warning: No different parent category option available");
        } catch (Exception e) {
            System.out.println("Error selecting different parent category: " + e.getMessage());
        }
    }

    /**
     * Check if edit icons/buttons are visible on the categories page
     * Used to verify access control for USER role
     * 
     * @return true if any edit button is visible
     */
    public boolean areEditIconsVisible() {
        try {
            // Wait for page to load
            page.waitForLoadState();
            page.waitForTimeout(1000);

            // Check for edit buttons with title="Edit"
            int editButtonCount = page.locator("a[title='Edit']").count();
            System.out.println("Edit buttons found: " + editButtonCount);

            return editButtonCount > 0;
        } catch (Exception e) {
            System.out.println("Exception checking edit icon visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if edit buttons are clickable (not disabled)
     * Note: HTML anchor tags don't support disabled attribute natively
     * 
     * @return true if any edit button is clickable
     */
    public boolean areEditIconsClickable() {
        try {
            // Find all edit buttons
            Locator editButtons = page.locator("a[title='Edit']");
            int buttonCount = editButtons.count();

            if (buttonCount == 0) {
                System.out.println("No edit buttons found");
                return false;
            }

            // Check if at least one button is clickable
            // Note: disabled attribute on <a> tags is not standard HTML and browsers ignore
            // it
            for (int i = 0; i < buttonCount; i++) {
                Locator button = editButtons.nth(i);
                if (button.isVisible() && button.isEnabled()) {
                    System.out.println("Edit button " + (i + 1) + " is clickable");
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.out.println("Exception checking edit icon clickability: " + e.getMessage());
            return false;
        }
    }
}
