package com.qatraining.pages;

import com.microsoft.playwright.Page;

//  Base Page Object containing common methods for all pages

public class BasePage {

    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    // Click on an element
    protected void click(String selector) {
        page.click(selector);
    }

    // Fill text in an input field
    protected void fill(String selector, String text) {
        page.fill(selector, text);
    }

    // Get text content of an element
    protected String getText(String selector) {
        return page.textContent(selector);
    }

    // Check if element is visible
    protected boolean isVisible(String selector) {
        return page.isVisible(selector);
    }

    // Wait for element to be visible
    protected void waitForElement(String selector) {
        page.waitForSelector(selector);
    }

    // Navigate to URL
    protected void navigate(String url) {
        page.navigate(url);
    }

    // Get current URL
    protected String getCurrentUrl() {
        return page.url();
    }

    // Get page title
    protected String getTitle() {
        return page.title();
    }
}
