package com.qatraining.drivers;

import com.qatraining.config.ConfigManager;
import com.microsoft.playwright.*;

/**
 * Playwright Driver Manager for browser initialization and management
 */
public class PlaywrightDriverManager {

    private static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static ThreadLocal<Page> page = new ThreadLocal<>();

    private static ConfigManager config = ConfigManager.getInstance();

    /**
     * Initialize Playwright and create a new browser instance
     */
    public static void initializeDriver() {
        Playwright playwrightInstance = Playwright.create();
        playwright.set(playwrightInstance);

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(config.isBrowserHeadless())
                .setTimeout(config.getBrowserTimeout());

        Browser browserInstance = createBrowser(playwrightInstance, config.getBrowserType(), launchOptions);
        browser.set(browserInstance);

        BrowserContext contextInstance = browserInstance.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setBaseURL(config.getBaseUrl()));
        context.set(contextInstance);

        // Set default timeout for all pages
        contextInstance.setDefaultTimeout(config.getBrowserTimeout());

        Page pageInstance = contextInstance.newPage();
        page.set(pageInstance);
    }

    /**
     * Create browser based on browser type
     */
    private static Browser createBrowser(Playwright playwright, String browserType, BrowserType.LaunchOptions options) {
        switch (browserType.toLowerCase()) {
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            case "chromium":
            default:
                return playwright.chromium().launch(options);
        }
    }

    /**
     * Get current page instance
     */
    public static Page getPage() {
        return page.get();
    }

    /**
     * Get current browser instance
     */
    public static Browser getBrowser() {
        return browser.get();
    }

    /**
     * Get current browser context
     */
    public static BrowserContext getContext() {
        return context.get();
    }

    /**
     * Navigate to URL
     */
    public static void navigateTo(String url) {
        getPage().navigate(url);
    }

    /**
     * Take screenshot
     */
    public static byte[] takeScreenshot() {
        return getPage().screenshot(new Page.ScreenshotOptions()
                .setFullPage(true));
    }

    /**
     * Close browser and cleanup
     */
    public static void quitDriver() {
        if (page.get() != null) {
            page.get().close();
            page.remove();
        }

        if (context.get() != null) {
            context.get().close();
            context.remove();
        }

        if (browser.get() != null) {
            browser.get().close();
            browser.remove();
        }

        if (playwright.get() != null) {
            playwright.get().close();
            playwright.remove();
        }
    }
}
