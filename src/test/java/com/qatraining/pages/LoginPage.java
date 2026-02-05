package com.qatraining.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page Object for Login Page (UI)
 * Designed to work with both Positive and Negative scenarios.
 */
public class LoginPage extends BasePage {

    // Locators (stable)
    private static final String USERNAME_INPUT = "input[name='username']";
    private static final String PASSWORD_INPUT = "input[name='password']";
    private static final String LOGIN_BUTTON   = "button[type='submit']";

    // Common message/toast/alert patterns (fallbacks)
    private static final String ALERT_ROLE      = "[role='alert']";
    private static final String TOAST_COMMON    = ".toast, .Toastify__toast, .MuiAlert-message, .alert, .snackbar";
    private static final String ANY_ERROR_TEXT  = "text=/invalid|error|failed/i";

    public LoginPage(Page page) {
        super(page);
    }

    /** Navigate to login page */
    public void navigateToLoginPage() {
        // baseURL is already set in PlaywrightDriverManager
        navigate("/ui/login");
        // ensure page is ready
        page.waitForSelector(LOGIN_BUTTON);
    }

    /** Enter username (can be empty, used for outline tests) */
    public void enterUsername(String username) {
        fill(USERNAME_INPUT, username == null ? "" : username);
    }

    /** Enter password (can be empty, used for outline tests) */
    public void enterPassword(String password) {
        fill(PASSWORD_INPUT, password == null ? "" : password);
    }

    /** Click login */
    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }

    /** Convenience login */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get error message shown after invalid login.
     * This method is flexible because different UIs show errors differently (toast/alert/text).
     *
     * Recommended expected text (per SRS): "Invalid username or password."
     */
    public String getErrorMessage() {
        // 1) Try the exact expected text first (fast + reliable if it exists)
        Locator exact = page.locator("text=Invalid username or password.").first();
        if (exact.count() > 0) {
            exact.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            return safeText(exact);
        }

        // 2) Try role=alert (common for error banners)
        Locator alert = page.locator(ALERT_ROLE).first();
        if (alert.count() > 0) {
            alert.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            String txt = safeText(alert);
            if (!txt.isBlank()) return txt;
        }

        // 3) Try common toast/snackbar containers
        Locator toast = page.locator(TOAST_COMMON).first();
        if (toast.count() > 0) {
            toast.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            String txt = safeText(toast);
            if (!txt.isBlank()) return txt;
        }

        // 4) As a last fallback, look for any visible error-ish text
        Locator any = page.locator(ANY_ERROR_TEXT).first();
        if (any.count() > 0) {
            any.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            String txt = safeText(any);
            if (!txt.isBlank()) return txt;
        }

        // Nothing found → return empty string (step will assert and show meaningful failure)
        return "";
    }

  


// Check if username field is invalid (HTML5 required validation)
public boolean isUsernameInvalid() {
    return (boolean) page.evalOnSelector(
            "input[name='username']",
            "el => !el.checkValidity()"
    );
}


// Check if password field is invalid (HTML5 required validation)
public boolean isPasswordInvalid() {
    return (boolean) page.evalOnSelector(
            "input[name='password']",
            "el => !el.checkValidity()"
    );
}

    /**
     * Browser validation text (Chrome shows "Please fill out this field." etc.)
     * Not always equal to your feature text, but useful if you want to assert browser messages.
     */
    public String getUsernameBrowserValidationMessage() {
        return page.locator(USERNAME_INPUT).evaluate("el => el.validationMessage").toString();
    }

    public String getPasswordBrowserValidationMessage() {
        return page.locator(PASSWORD_INPUT).evaluate("el => el.validationMessage").toString();
    }

    /** Check login page visible */
    public boolean isLoginPageDisplayed() {
        return isVisible(LOGIN_BUTTON);
    }

    // Helper: safe trim text
    private String safeText(Locator locator) {
        String txt = locator.textContent();
        return txt == null ? "" : txt.trim();
    }
}
