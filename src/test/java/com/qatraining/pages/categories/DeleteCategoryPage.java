package com.qatraining.pages.categories;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page Object for Category Delete operations
 * Handles error messages and validation for delete operations
 */
public class DeleteCategoryPage extends BasePage {

    // Locators for error messages
    private static final String ERROR_MESSAGE = ".alert-danger, .error, .text-danger";
    private static final String SUCCESS_MESSAGE = ".alert-success, .success";
    private static final String MESSAGE_CONTAINER = ".alert";

    public DeleteCategoryPage(Page page) {
        super(page);
    }

    /**
     * Get the error message displayed on the page
     * 
     * @return the error message text, or null if no error message is found
     */
    public String getErrorMessage() {
        try {
            // Wait for alert-danger to appear
            page.waitForSelector("div.alert-danger", new Page.WaitForSelectorOptions().setTimeout(5000));

            // Get text from span inside alert-danger (most precise)
            try {
                String spanText = page.locator("div.alert-danger span").first().textContent().trim();
                if (!spanText.isEmpty()) {
                    System.out.println("Error message found: " + spanText);
                    return spanText;
                }
            } catch (Exception ex) {
                // Fall through to get full alert text
            }

            // Get text from the alert div itself
            String alertText = page.locator("div.alert-danger").first().textContent().trim();
            // Remove close button character if present
            alertText = alertText.replace("×", "").trim();
            System.out.println("Error message: " + alertText);
            return alertText;
        } catch (Exception e) {
            System.out.println("No error message found");
            return null;
        }
    }

    /**
     * Check if an error message is displayed
     * 
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
        try {
            // Debug logging
            System.out.println("Checking for error message on page: " + page.url());

            // Check all alerts on the page
            int totalAlerts = page.locator(".alert").count();
            System.out.println("Total .alert elements found: " + totalAlerts);

            // Check for success message (would indicate deletion succeeded)
            int successAlerts = page.locator(".alert-success").count();
            if (successAlerts > 0) {
                String successMsg = page.locator(".alert-success").first().textContent();
                System.out.println("WARNING: Success message found (deletion succeeded): " + successMsg);
            }

            // Check for error message
            int errorAlerts = page.locator("div.alert-danger").count();
            System.out.println("Error alerts found: " + errorAlerts);

            if (errorAlerts > 0) {
                String errorText = page.locator("div.alert-danger").first().textContent();
                System.out.println("Error alert content: " + errorText);
            }

            return errorAlerts > 0;
        } catch (Exception e) {
            System.out.println("Exception checking for error message: " + e.getMessage());
            return false;
        }
    }

    /**
     * Wait for error message to appear
     */
    public void waitForErrorMessage() {
        try {
            page.waitForSelector("div.alert-danger",
                    new Page.WaitForSelectorOptions().setTimeout(5000));
        } catch (Exception e) {
            System.out.println("Error message did not appear within timeout");
        }
    }

    /**
     * Check if delete icons/buttons are visible on the categories page
     * Used to verify access control for USER role
     * 
     * @return true if any delete button is visible
     */
    public boolean areDeleteIconsVisible() {
        try {
            // Wait for page to load
            page.waitForLoadState();
            page.waitForTimeout(1000);

            // Check for delete buttons with title="Delete"
            int deleteButtonCount = page.locator("button[title='Delete']").count();
            System.out.println("Delete buttons found: " + deleteButtonCount);

            return deleteButtonCount > 0;
        } catch (Exception e) {
            System.out.println("Exception checking delete icon visibility: " + e.getMessage());
            return false;
        }
    }
}
