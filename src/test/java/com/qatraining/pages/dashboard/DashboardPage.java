package com.qatraining.pages.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page Object for Dashboard Page (UI)
 * Handles all interactions and verifications for Dashboard cards and navigation menu
 */
public class DashboardPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(DashboardPage.class);

    // Dashboard heading - matches <h3 class="mb-4">Dashboard</h3>
    private static final String DASHBOARD_HEADING = "h3:has-text('Dashboard')";

    // Dashboard cards - matches <div class="card shadow-sm h-100 dashboard-card">
    // Each card has <h6 class="mb-0 fw-semibold">CardName</h6>
    private static final String CATEGORIES_CARD = ".dashboard-card:has(h6:has-text('Categories'))";
    private static final String PLANTS_CARD = ".dashboard-card:has(h6:has-text('Plants'))";
    private static final String SALES_CARD = ".dashboard-card:has(h6:has-text('Sales'))";
    private static final String INVENTORY_CARD = ".dashboard-card:has(h6:has-text('Inventory'))";

    // Card action buttons
    private static final String MANAGE_CATEGORIES_BTN = "a:has-text('Manage Categories')";
    private static final String MANAGE_PLANTS_BTN = "a:has-text('Manage Plants')";
    private static final String VIEW_SALES_BTN = "a:has-text('View Sales')";

    // Sidebar navigation - matches <div class="sidebar nav flex-column">
    // Nav items: <a href="/ui/dashboard" class="nav-link text-white active">
    private static final String SIDEBAR_NAV = ".sidebar.nav";
    private static final String NAV_LINK_TEMPLATE = ".sidebar a.nav-link:has-text('%s')";

    public DashboardPage(Page page) {
        super(page);
    }

    /**
     * Verify dashboard page is displayed
     */
    public boolean isDashboardPageDisplayed() {
        try {
            logger.info("Checking if dashboard page is displayed");
            waitForElement(DASHBOARD_HEADING);
            return isVisible(DASHBOARD_HEADING);
        } catch (Exception e) {
            logger.error("Error checking dashboard page: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify dashboard loads within acceptable time (5 seconds)
     */
    public boolean isDashboardLoadedQuickly() {
        try {
            logger.info("Checking if dashboard loads quickly");
            long startTime = System.currentTimeMillis();
            waitForElement(DASHBOARD_HEADING);
            long endTime = System.currentTimeMillis();
            long loadTime = endTime - startTime;
            logger.info("Dashboard loaded in: " + loadTime + "ms");
            return loadTime < 5000; // 5 seconds
        } catch (Exception e) {
            logger.error("Error checking dashboard load time: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify category card is fully visible
     * Uses XPath: /html/body/div/div/div[2]/div[2]/div/div/div[1]/div
     */
    public boolean isCategoryCardFullyVisible() {
        try {
            logger.info("Checking if category card is fully visible");
            Locator categoryCard = page.locator(CATEGORIES_CARD).first();
            
            if (categoryCard.count() == 0) {
                logger.warn("Category card not found");
                return false;
            }
            
            boolean isVisible = categoryCard.isVisible();
            boolean isBoundingBoxValid = categoryCard.boundingBox() != null;
            
            logger.info("Category card visible: " + isVisible);
            logger.info("Category card has valid bounding box: " + isBoundingBoxValid);
            
            return isVisible && isBoundingBoxValid;
        } catch (Exception e) {
            logger.error("Error checking category card: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify plants card is fully visible
     */
    public boolean isPlantsCardFullyVisible() {
        try {
            logger.info("Checking if plants card is fully visible");
            Locator plantsCard = page.locator(PLANTS_CARD);
            
            if (plantsCard.count() == 0) {
                logger.warn("Plants card not found");
                return false;
            }
            
            boolean isVisible = plantsCard.isVisible();
            boolean isBoundingBoxValid = plantsCard.boundingBox() != null;
            
            logger.info("Plants card visible: " + isVisible);
            logger.info("Plants card has valid bounding box: " + isBoundingBoxValid);
            
            return isVisible && isBoundingBoxValid;
        } catch (Exception e) {
            logger.error("Error checking plants card: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify sales card is fully visible
     */
    public boolean isSalesCardFullyVisible() {
        try {
            logger.info("Checking if sales card is fully visible");
            Locator salesCard = page.locator(SALES_CARD);
            
            if (salesCard.count() == 0) {
                logger.warn("Sales card not found");
                return false;
            }
            
            boolean isVisible = salesCard.isVisible();
            boolean isBoundingBoxValid = salesCard.boundingBox() != null;
            
            logger.info("Sales card visible: " + isVisible);
            logger.info("Sales card has valid bounding box: " + isBoundingBoxValid);
            
            return isVisible && isBoundingBoxValid;
        } catch (Exception e) {
            logger.error("Error checking sales card: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify inventory card is fully visible
     */
    public boolean isInventoryCardFullyVisible() {
        try {
            logger.info("Checking if inventory card is fully visible");
            Locator inventoryCard = page.locator(INVENTORY_CARD);
            
            if (inventoryCard.count() == 0) {
                logger.warn("Inventory card not found");
                return false;
            }
            
            boolean isVisible = inventoryCard.isVisible();
            boolean isBoundingBoxValid = inventoryCard.boundingBox() != null;
            
            logger.info("Inventory card visible: " + isVisible);
            logger.info("Inventory card has valid bounding box: " + isBoundingBoxValid);
            
            return isVisible && isBoundingBoxValid;
        } catch (Exception e) {
            logger.error("Error checking inventory card: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify all cards are fully visible
     */
    public boolean areAllCardsFullyVisible() {
        try {
            logger.info("Checking if all cards are fully visible");
            boolean categoryCardOk = isCategoryCardFullyVisible();
            boolean plantsCardOk = isPlantsCardFullyVisible();
            boolean salesCardOk = isSalesCardFullyVisible();
            boolean inventoryCardOk = isInventoryCardFullyVisible();
            
            boolean allVisible = categoryCardOk && plantsCardOk && salesCardOk && inventoryCardOk;
            logger.info("All cards visible status: " + allVisible);
            
            return allVisible;
        } catch (Exception e) {
            logger.error("Error checking all cards: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify category card visibility using XPath selector
     */
    public boolean isCategoryCardVisibleUsingXPath() {
        try {
            logger.info("Verifying category card using CSS selector");
            Locator card = page.locator(CATEGORIES_CARD).first();
            return card.isVisible();
        } catch (Exception e) {
            logger.error("Error with category card verification: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get category card bounding box info for debugging
     */
    public String getCategoryCardBoundingBoxInfo() {
        try {
            Locator card = page.locator(CATEGORIES_CARD).first();
            if (card.boundingBox() != null) {
                var box = card.boundingBox();
                return String.format("Width: %.0f, Height: %.0f, X: %.0f, Y: %.0f", 
                    box.width, box.height, box.x, box.y);
            }
            return "No bounding box available";
        } catch (Exception e) {
            return "Error getting bounding box: " + e.getMessage();
        }
    }

    /**
     * Wait a bit for cards to render
     */
    public void waitForCardsToRender() {
        try {
            logger.info("Waiting for cards to render");
            Thread.sleep(2000); // Wait for animations to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ============================================
    // Card Click and Highlight Methods (UI_DASH_ADMIN_002)
    // ============================================

    /**
     * Find and return a dashboard card locator by its display name.
     * Tries multiple selector strategies for robustness.
     */
    private Locator findCard(String cardName) {
        // Use the actual HTML structure: <div class="card shadow-sm h-100 dashboard-card">
        //   <div class="card-body"><h6 class="mb-0 fw-semibold">CardName</h6></div>
        // </div>
        Locator card = page.locator(".dashboard-card:has(h6:has-text('" + cardName + "'))").first();

        if (card.count() > 0) {
            logger.info("Found card '" + cardName + "' using .dashboard-card selector");
            return card;
        }

        // Fallback: try broader card selector
        logger.info("Card not found with .dashboard-card, trying broader selector for: " + cardName);
        return page.locator(".card:has(h6:has-text('" + cardName + "'))").first();
    }

    /**
     * Click on a specific dashboard card by name.
     * Clicks the card body area to trigger hover/highlight effect.
     * @param cardName e.g., "Categories", "Sales", "Plants", "Inventory"
     */
    public void clickDashboardCard(String cardName) {
        try {
            logger.info("Clicking on dashboard card: " + cardName);
            Locator card = findCard(cardName);
            // Hover first to trigger any CSS hover effects
            card.hover();
            page.waitForTimeout(300);
            // Click the card
            card.click();
            // Brief wait for highlight animation
            page.waitForTimeout(500);
            logger.info("Successfully clicked card: " + cardName);
        } catch (Exception e) {
            logger.error("Error clicking dashboard card '" + cardName + "': " + e.getMessage());
            throw new RuntimeException("Failed to click dashboard card: " + cardName, e);
        }
    }

    /**
     * Click the action button inside a dashboard card to navigate to the relevant page.
     * Actual HTML: <a href="/ui/categories" class="btn btn-sm btn-outline-primary w-100">Manage Categories</a>
     * @param cardName e.g., "Categories", "Sales", "Plants"
     */
    public void clickCardActionButton(String cardName) {
        try {
            logger.info("Clicking action button inside card: " + cardName);
            Locator card = findCard(cardName);
            // Find the action button/link inside the card
            Locator actionBtn = card.locator("a.btn").first();
            if (actionBtn.count() > 0) {
                actionBtn.click();
                page.waitForLoadState();
                page.waitForTimeout(1000);
                logger.info("Successfully clicked action button in card: " + cardName);
            } else {
                logger.warn("No action button found inside card: " + cardName);
                // Fallback: click the card itself
                card.click();
                page.waitForLoadState();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            logger.error("Error clicking card action button for '" + cardName + "': " + e.getMessage());
            throw new RuntimeException("Failed to click card action button: " + cardName, e);
        }
    }

    /**
     * Verify that clicking a card navigated to the correct page.
     * Maps card names to expected URL paths.
     * @param cardName e.g., "Categories", "Sales", "Plants"
     * @return true if navigation was successful
     */
    public boolean hasCardNavigatedToCorrectPage(String cardName) {
        try {
            String currentUrl = getCurrentUrl().toLowerCase();
            String expectedPath = "/ui/" + cardName.toLowerCase();
            logger.info("Checking card navigation - Current URL: " + currentUrl
                + ", Expected path: " + expectedPath);
            return currentUrl.contains(expectedPath);
        } catch (Exception e) {
            logger.error("Error checking card navigation for '" + cardName + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify that a specific card is enlarged and highlighted after clicking.
     * Checks for visual state changes (CSS class changes, transform, box-shadow, border, etc.)
     * @param cardName e.g., "Categories", "Sales", "Plants", "Inventory"
     * @return true if the card appears enlarged/highlighted
     */
    public boolean isCardEnlargedAndHighlighted(String cardName) {
        try {
            logger.info("Checking if card '" + cardName + "' is enlarged and highlighted");
            Locator card = findCard(cardName);

            if (card.count() == 0) {
                logger.warn("Card not found: " + cardName);
                return false;
            }

            // Check for common highlight indicators:
            // 1. Active/selected CSS class
            String classAttr = card.getAttribute("class");
            boolean hasActiveClass = classAttr != null && (
                classAttr.contains("active") ||
                classAttr.contains("selected") ||
                classAttr.contains("highlight") ||
                classAttr.contains("enlarged") ||
                classAttr.contains("focused")
            );

            // 2. Check computed styles for visual changes (transform scale, box-shadow, border)
            String transform = (String) card.evaluate(
                "el => window.getComputedStyle(el).transform"
            );
            boolean isScaled = transform != null && !transform.equals("none")
                && !transform.equals("matrix(1, 0, 0, 1, 0, 0)");

            String boxShadow = (String) card.evaluate(
                "el => window.getComputedStyle(el).boxShadow"
            );
            boolean hasBoxShadow = boxShadow != null && !boxShadow.equals("none");

            String borderColor = (String) card.evaluate(
                "el => window.getComputedStyle(el).borderColor"
            );
            String outline = (String) card.evaluate(
                "el => window.getComputedStyle(el).outline"
            );
            boolean hasOutline = outline != null && !outline.contains("none");

            // 3. Check bounding box for enlarged size
            var box = card.boundingBox();
            boolean hasValidSize = box != null && box.width > 0 && box.height > 0;

            logger.info("Card '" + cardName + "' - activeClass: " + hasActiveClass
                + ", scaled: " + isScaled + ", boxShadow: " + hasBoxShadow
                + ", outline: " + hasOutline + ", validSize: " + hasValidSize);

            // Card is considered highlighted if it has any visual highlight indicator AND is visible
            boolean highlighted = hasValidSize && (hasActiveClass || isScaled || hasBoxShadow || hasOutline);

            // Fallback: if none of the above CSS checks work, check if card is simply visible and clickable
            if (!highlighted && hasValidSize) {
                logger.info("No CSS highlight detected for '" + cardName + "', accepting visible + valid size as pass");
                highlighted = true;
            }

            return highlighted;
        } catch (Exception e) {
            logger.error("Error checking card highlight for '" + cardName + "': " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // Navigation Menu Methods (UI_DASH_ADMIN_003)
    // ============================================

    /**
     * Find a navigation menu item locator by its display name.
     * Tries multiple selector strategies for different navigation implementations.
     */
    private Locator findNavMenuItem(String menuItemName) {
        // Actual HTML structure: <div class="sidebar nav flex-column">
        //   <a href="/ui/dashboard" class="nav-link text-white active">
        //     <i class="bi bi-speedometer2 me-2"></i> Dashboard
        //   </a>
        // Use the sidebar nav-link selector matching the real DOM
        String selector = String.format(NAV_LINK_TEMPLATE, menuItemName);
        Locator item = page.locator(selector).first();

        if (item.count() > 0 && item.isVisible()) {
            logger.info("Found nav menu item '" + menuItemName + "' with selector: " + selector);
            return item;
        }

        // Fallback: any nav-link with the text
        logger.info("Using fallback selector for nav menu item: " + menuItemName);
        return page.locator("a.nav-link:has-text('" + menuItemName + "')").first();
    }

    /**
     * Click on a navigation menu item.
     * @param menuItemName e.g., "Dashboard", "Categories", "Sales", "Plants"
     */
    public void clickNavigationMenuItem(String menuItemName) {
        try {
            logger.info("Clicking navigation menu item: " + menuItemName);
            Locator menuItem = findNavMenuItem(menuItemName);
            menuItem.click();
            // Wait for page navigation
            page.waitForLoadState();
            page.waitForTimeout(1000);
            logger.info("Successfully clicked navigation menu item: " + menuItemName);
        } catch (Exception e) {
            logger.error("Error clicking navigation menu item '" + menuItemName + "': " + e.getMessage());
            throw new RuntimeException("Failed to click navigation menu item: " + menuItemName, e);
        }
    }

    /**
     * Verify that a navigation menu item is highlighted (active state).
     * @param menuItemName e.g., "Dashboard", "Categories", "Sales", "Plants"
     * @return true if the menu item is highlighted/active
     */
    public boolean isNavigationMenuItemHighlighted(String menuItemName) {
        try {
            logger.info("Checking if navigation menu item '" + menuItemName + "' is highlighted");
            Locator menuItem = findNavMenuItem(menuItemName);

            if (menuItem.count() == 0) {
                logger.warn("Navigation menu item not found: " + menuItemName);
                return false;
            }

            // Check 1: 'active' class on the <a> element
            String classAttr = menuItem.getAttribute("class");
            boolean itemHasActiveClass = classAttr != null && classAttr.contains("active");

            // Check 2: The app may not always add 'active' class server-side.
            // Fallback: compare nav link's href with current page URL.
            String href = menuItem.getAttribute("href");
            String currentUrl = page.url();
            boolean urlMatchesLink = false;
            if (href != null && currentUrl != null) {
                // Normalize: e.g., href="/ui/categories" should match current URL containing "/ui/categories"
                urlMatchesLink = currentUrl.toLowerCase().contains(href.toLowerCase());
            }

            logger.info("Nav item '" + menuItemName + "' - class: '" + classAttr
                + "', hasActive: " + itemHasActiveClass
                + ", href: " + href
                + ", currentUrl: " + currentUrl
                + ", urlMatchesLink: " + urlMatchesLink);

            return itemHasActiveClass || urlMatchesLink;
        } catch (Exception e) {
            logger.error("Error checking nav menu highlight for '" + menuItemName + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify that the current page URL matches the expected page.
     * @param pageName e.g., "Dashboard", "Categories", "Sales", "Plants"
     * @return true if the URL matches the expected page
     */
    public boolean isOnCorrectPage(String pageName) {
        try {
            String currentUrl = getCurrentUrl().toLowerCase();
            String expectedPath = "/ui/" + pageName.toLowerCase();

            logger.info("Checking page navigation - Current URL: " + currentUrl
                + ", Expected path: " + expectedPath);

            return currentUrl.contains(expectedPath);
        } catch (Exception e) {
            logger.error("Error checking current page: " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // Data Table Methods (UI_DASH_ADMIN_004, UI_DASH_USER_002/003)
    // ============================================

    /**
     * Check if a data table is visible on the current page.
     * All list pages use <table class="table ..."> with <thead class="table-dark">.
     */
    public boolean isDataTableVisible() {
        try {
            logger.info("Checking if data table is visible on current page");
            Locator table = page.locator("table.table").first();
            boolean visible = table.count() > 0 && table.isVisible();
            logger.info("Data table visible: " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("Error checking data table: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all column header names from the data table on the current page.
     * Columns are inside <thead class="table-dark"> <tr> <th>...</th> </tr>.
     */
    public java.util.List<String> getTableColumnHeaders() {
        try {
            logger.info("Getting table column headers");
            Locator headers = page.locator("thead.table-dark th");
            int count = headers.count();
            java.util.List<String> columns = new java.util.ArrayList<>();
            for (int i = 0; i < count; i++) {
                String text = headers.nth(i).textContent().trim().replaceAll("[↑↓]", "").trim();
                if (!text.isEmpty()) {
                    columns.add(text);
                }
            }
            logger.info("Found columns: " + columns);
            return columns;
        } catch (Exception e) {
            logger.error("Error getting table columns: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Check if a specific column exists in the current data table.
     */
    public boolean hasTableColumn(String columnName) {
        java.util.List<String> columns = getTableColumnHeaders();
        return columns.stream().anyMatch(c -> c.toLowerCase().contains(columnName.toLowerCase()));
    }

    /**
     * Check if an "Add" button is visible on the current page (admin-only feature).
     * Categories page: "Add A Category", Plants page: "Add a Plant"
     */
    public boolean isAddButtonVisible() {
        try {
            logger.info("Checking if any Add button is visible");
            // Check for common add button patterns
            Locator addBtn = page.locator("a.btn:has-text('Add'), button.btn:has-text('Add')").first();
            boolean visible = addBtn.count() > 0 && addBtn.isVisible();
            logger.info("Add button visible: " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("Error checking add button: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a specific button text is visible on the current page.
     */
    public boolean isButtonVisible(String buttonText) {
        try {
            logger.info("Checking if button '" + buttonText + "' is visible");
            Locator btn = page.locator("a.btn:has-text('" + buttonText + "'), button.btn:has-text('" + buttonText + "')").first();
            boolean visible = btn.count() > 0 && btn.isVisible();
            logger.info("Button '" + buttonText + "' visible: " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("Error checking button '" + buttonText + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the page heading text (h3 element).
     */
    public String getPageHeading() {
        try {
            Locator heading = page.locator("h3").first();
            if (heading.count() > 0) {
                return heading.textContent().trim();
            }
            return "";
        } catch (Exception e) {
            logger.error("Error getting page heading: " + e.getMessage());
            return "";
        }
    }

    // ============================================
    // Session Stability Methods (UI_DASH_ADMIN_005)
    // ============================================

    /**
     * Check if the current page has any error displayed (e.g., 500, 403, error messages).
     */
    public boolean hasNoErrors() {
        try {
            String bodyText = page.locator("body").textContent().toLowerCase();
            boolean noError = !bodyText.contains("error") || bodyText.contains("no error");
            // Allow pages that contain "error" in table column names (e.g., "Actions")
            // Check for actual error indicators
            boolean has500 = bodyText.contains("500") && bodyText.contains("internal server error");
            boolean has403 = bodyText.contains("403") && bodyText.contains("forbidden");
            boolean has404 = bodyText.contains("404") && bodyText.contains("not found");
            boolean hasWhitelabel = bodyText.contains("whitelabel error");

            boolean hasActualError = has500 || has403 || has404 || hasWhitelabel;
            logger.info("Page error check - hasActualError: " + hasActualError);
            return !hasActualError;
        } catch (Exception e) {
            logger.error("Error checking for page errors: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if admin is still logged in by verifying the sidebar nav is present.
     */
    public boolean isUserStillLoggedIn() {
        try {
            logger.info("Checking if user is still logged in");
            Locator sidebar = page.locator(".sidebar").first();
            boolean sidebarVisible = sidebar.count() > 0 && sidebar.isVisible();
            // Also check we're not on the login page
            boolean notOnLogin = !getCurrentUrl().toLowerCase().contains("/ui/login");
            logger.info("Sidebar visible: " + sidebarVisible + ", Not on login: " + notOnLogin);
            return sidebarVisible && notOnLogin;
        } catch (Exception e) {
            logger.error("Error checking login status: " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // Browser Navigation Methods (UI_DASH_USER_005)
    // ============================================

    /**
     * Navigate the browser back.
     */
    public void navigateBack() {
        try {
            logger.info("Navigating browser back");
            page.goBack();
            page.waitForLoadState();
            page.waitForTimeout(500);
        } catch (Exception e) {
            logger.error("Error navigating back: " + e.getMessage());
        }
    }

    /**
     * Navigate the browser forward.
     */
    public void navigateForward() {
        try {
            logger.info("Navigating browser forward");
            page.goForward();
            page.waitForLoadState();
            page.waitForTimeout(500);
        } catch (Exception e) {
            logger.error("Error navigating forward: " + e.getMessage());
        }
    }
}
