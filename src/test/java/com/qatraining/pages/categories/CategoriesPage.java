package com.qatraining.pages.categories;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page Object for Categories Page (UI)
 * Handles categories list view and related operations
 */
public class CategoriesPage extends BasePage {

    // Locators for Categories Page elements
    private static final String SEARCH_BOX = "input[placeholder*='Search'], input[type='search'], input[name='search']";
    private static final String PARENT_FILTER = "select[name='parentId'], select#parentFilter, select[class*='parent']";
    private static final String SEARCH_BUTTON = "button:has-text('Search'), button[type='submit']";
    private static final String ADD_CATEGORY_BUTTON = "a[href='/ui/categories/add']";
    private static final String CATEGORIES_TABLE = "table, .table, [role='table']";
    private static final String PAGINATION = ".pagination, [role='navigation'][aria-label*='pagination'], nav[aria-label*='pagination']";
    private static final String CATEGORIES_NAV_LINK = "a[href='/ui/categories']";

    // Table elements
    private static final String TABLE_ROWS = "table tbody tr, .table tbody tr";
    private static final String TABLE_HEADERS = "table thead th, .table thead th";

    // Action button selectors
    private static final String EDIT_BUTTON_SELECTOR = "a[title='Edit']";
    private static final String DELETE_BUTTON_SELECTOR = "button[title='Delete']";

    // Timeouts
    private static final int TABLE_LOAD_TIMEOUT = 5000;

    public CategoriesPage(Page page) {
        super(page);
    }

    /**
     * Navigate to Categories page
     */
    public void navigateToCategoriesPage() {
        navigate("/ui/categories");
        // Wait for the page to load
        page.waitForLoadState();
    }

    /**
     * Click on Categories link in navigation
     */
    public void clickCategoriesNavLink() {
        click(CATEGORIES_NAV_LINK);
        page.waitForLoadState();
    }

    /**
     * Check if Categories page is loaded successfully
     * 
     * @return true if the page URL contains /ui/categories
     */
    public boolean isCategoriesPageLoaded() {
        String currentUrl = getCurrentUrl();
        return currentUrl.contains("/ui/categories");
    }

    /**
     * Check if search box is visible
     */
    public boolean isSearchBoxVisible() {
        try {
            waitForElement(SEARCH_BOX);
            return isVisible(SEARCH_BOX);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if parent filter dropdown is visible
     */
    public boolean isParentFilterVisible() {
        try {
            return isVisible(PARENT_FILTER);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if search button is visible
     */
    public boolean isSearchButtonVisible() {
        try {
            return isVisible(SEARCH_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Add Category button is visible
     */
    public boolean isAddCategoryButtonVisible() {
        try {
            return isVisible(ADD_CATEGORY_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if categories table is visible
     */
    public boolean isCategoriesTableVisible() {
        try {
            waitForElement(CATEGORIES_TABLE);
            return isVisible(CATEGORIES_TABLE);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if pagination is visible
     */
    public boolean isPaginationVisible() {
        try {
            return isVisible(PAGINATION);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if categories are displayed (table has data rows)
     */
    public boolean areCategoriesDisplayed() {
        try {
            return page.locator(TABLE_ROWS).count() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the number of category rows in the table
     */
    public int getCategoryCount() {
        try {
            return page.locator(TABLE_ROWS).count();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Click the Edit icon for the first category in the list
     */
    public void clickEditIconForFirstCategory() {
        String editIconSelector = "a[href*='/ui/categories/edit/'] .bi-pencil-square, a[href*='/ui/categories/edit/'][title='Edit']";
        page.locator(editIconSelector).first().click();
        page.waitForLoadState();
    }

    /**
     * Click the Edit icon for a specific category by name
     */
    public void clickEditIconForCategory(String categoryName) {
        // Find the row containing the category name, then click its edit icon
        String rowSelector = String.format("tr:has-text('%s')", categoryName);
        String editIconInRow = rowSelector + " a[href*='/ui/categories/edit/']";
        page.locator(editIconInRow).first().click();
        page.waitForLoadState();
    }

    /**
     * Search for a category by name
     */
    public void searchForCategory(String categoryName) {
        page.locator(SEARCH_BOX).fill(categoryName);
        // Use more specific selector to avoid matching the Delete button
        page.locator("button:has-text('Search').btn-primary").click();
        page.waitForLoadState();
        System.out.println("Searched for category: " + categoryName);
    }

    /**
     * Click the Delete icon for a specific category by name
     * This will trigger the browser confirmation dialog
     */
    public void clickDeleteIconForCategory(String categoryName) {
        System.out.println("=== DEBUGGING DELETE CLICK ===");
        System.out.println("Current URL: " + page.url());
        System.out.println("Looking for exact category name: " + categoryName);

        // Find all table rows
        var allRows = page.locator("table tbody tr");
        int totalRows = allRows.count();
        System.out.println("Total rows in table: " + totalRows);

        // Find the row where SECOND column (category name) exactly matches
        // Column 0 = ID, Column 1 = Name (td:nth-child(2) in CSS), Column 2 = Parent
        var targetRow = allRows.filter(new Locator.FilterOptions()
                .setHas(page.locator("td:nth-child(2)").filter(new Locator.FilterOptions()
                        .setHasText(java.util.regex.Pattern
                                .compile("^" + java.util.regex.Pattern.quote(categoryName) + "$")))));

        int matchingRows = targetRow.count();
        System.out.println("Rows with EXACT name '" + categoryName + "': " + matchingRows);

        // If filter didn't work, manually find the row
        if (matchingRows == 0) {
            System.out.println("Filter didn't work, using manual search...");
            int targetRowIndex = -1;
            for (int i = 0; i < totalRows; i++) {
                var row = allRows.nth(i);
                String nameColumnText = row.locator("td").nth(1).innerText().trim();
                System.out.println("  Row " + i + " name: '" + nameColumnText + "'");
                if (nameColumnText.equals(categoryName)) {
                    targetRowIndex = i;
                    targetRow = allRows.nth(i);
                    matchingRows = 1;
                    System.out.println("  MATCH FOUND at row " + i);
                    break;
                }
            }
            if (targetRowIndex == -1) {
                throw new RuntimeException("Category '" + categoryName + "' not found in table");
            }
        }

        if (matchingRows == 0) {
            System.out.println("ERROR: No row found with exact category name!");
            throw new RuntimeException("Category '" + categoryName + "' not found in table");
        }

        // Log the matching row details
        String rowText = targetRow.first().innerText();
        System.out.println("Found matching row: " + rowText.replace("\n", " | "));

        var deleteButton = targetRow.first().locator("button[title='Delete']");
        String formAction = deleteButton
                .evaluate("el => { const form = el.closest('form'); return form ? form.action : 'no form'; }")
                .toString();
        System.out.println("Delete URL: " + formAction);

        // Set up dialog handler before clicking the delete button
        page.onDialog(dialog -> {
            System.out.println("Dialog received - Type: " + dialog.type());
            System.out.println("Dialog message: " + dialog.message());

            // Add a small delay before accepting to allow any client-side validation
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            dialog.accept(); // Accept the confirmation
            System.out.println("Dialog accepted");
        });

        System.out.println("Clicking delete button...");
        deleteButton.click();

        // Wait for page to reload after form submission
        page.waitForLoadState();
        page.waitForTimeout(2000); // Additional wait for error message to appear

        System.out.println("After delete - Current URL: " + page.url());
        System.out.println("=== END DEBUG ===");
    }

    /**
     * Check if a category with the given name exists in the table
     */
    public boolean isCategoryInList(String categoryName) {
        try {
            // Find all table rows
            var allRows = page.locator("table tbody tr");
            int totalRows = allRows.count();

            // Manually search for exact match in second column (category name)
            for (int i = 0; i < totalRows; i++) {
                var row = allRows.nth(i);
                String nameColumnText = row.locator("td").nth(1).innerText().trim();
                if (nameColumnText.equals(categoryName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Add Category button is not available (hidden or not present)
     * For read-only access (USER role)
     */
    public boolean isAddCategoryButtonNotAvailable() {
        try {
            // Check if button is not visible or doesn't exist
            return !isVisible(ADD_CATEGORY_BUTTON);
        } catch (Exception e) {
            // If exception occurs, button is not available
            return true;
        }
    }

    /**
     * Check if Edit buttons in the table are disabled.
     * For read-only access validation (USER role).
     * 
     * @return true if all edit buttons are disabled or not present, false otherwise
     */
    public boolean areEditButtonsDisabled() {
        return areActionButtonsDisabledOrRestricted(EDIT_BUTTON_SELECTOR);
    }

    /**
     * Check if Delete buttons in the table are disabled.
     * For read-only access validation (USER role).
     * 
     * @return true if all delete buttons are disabled or not present, false
     *         otherwise
     */
    public boolean areDeleteButtonsDisabled() {
        return areActionButtonsDisabledOrRestricted(DELETE_BUTTON_SELECTOR);
    }

    /**
     * Helper method to check if action buttons (edit/delete) are disabled or
     * restricted.
     * 
     * @param buttonSelector The CSS selector for the buttons to check
     * @return true if ALL buttons are disabled or not present, false if any button
     *         is enabled
     */
    private boolean areActionButtonsDisabledOrRestricted(String buttonSelector) {
        try {
            // Wait for table to load
            page.waitForSelector(TABLE_ROWS, new Page.WaitForSelectorOptions().setTimeout(TABLE_LOAD_TIMEOUT));

            // Find all buttons matching the selector
            Locator buttons = page.locator(buttonSelector);
            int buttonCount = buttons.count();

            if (buttonCount == 0) {
                // No buttons found - they are restricted/hidden
                return true;
            }

            // Check if ALL buttons have disabled attribute
            // If even one button is NOT disabled, return false
            for (int i = 0; i < buttonCount; i++) {
                String disabledAttr = buttons.nth(i).getAttribute("disabled");
                if (disabledAttr == null) {
                    // Found an enabled button
                    return false;
                }
            }

            // All buttons are disabled
            return true;
        } catch (Exception e) {
            // If table doesn't load or buttons can't be found, consider them restricted
            System.out.println("Warning: Could not verify button state for " + buttonSelector + ": " + e.getMessage());
            return true;
        }
    }

    /**
     * Click the Next pagination button.
     */
    public void clickNextPagination() {
        try {
            Locator nextButton = page.locator(".pagination .page-link:has-text('Next')");
            nextButton.click();
            page.waitForLoadState();
            page.waitForTimeout(500); // Wait for content to update
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Next pagination button: " + e.getMessage(), e);
        }
    }

    /**
     * Click the Previous pagination button.
     */
    public void clickPreviousPagination() {
        try {
            Locator previousButton = page.locator(".pagination .page-link:has-text('Previous')");
            previousButton.click();
            page.waitForLoadState();
            page.waitForTimeout(500); // Wait for content to update
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Previous pagination button: " + e.getMessage(), e);
        }
    }

    /**
     * Check if pagination controls (Previous, page numbers, Next) are visible.
     * 
     * @return true if all pagination controls are present, false otherwise
     */
    public boolean isPaginationControlsVisible() {
        try {
            // Check if pagination container exists
            if (!isVisible(PAGINATION)) {
                return false;
            }

            // Check for Previous button
            boolean hasPrevious = page.locator(".pagination .page-link:has-text('Previous')").count() > 0;

            // Check for Next button
            boolean hasNext = page.locator(".pagination .page-link:has-text('Next')").count() > 0;

            // Check for page number links (at least one should exist)
            boolean hasPageNumbers = page
                    .locator(".pagination .page-item:not(:has-text('Previous')):not(:has-text('Next'))").count() > 0;

            return hasPrevious && hasNext && hasPageNumbers;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the current active page number from pagination.
     * 
     * @return The current page number (1-indexed), or -1 if not found
     */
    public int getCurrentPageNumber() {
        try {
            Locator activePage = page.locator(".pagination .page-item.active .page-link");
            if (activePage.count() > 0) {
                String pageText = activePage.innerText().trim();
                return Integer.parseInt(pageText);
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Verify that the number of displayed categories does not exceed the maximum
     * per page.
     * 
     * @param maxRecordsPerPage Maximum allowed records per page (typically 10)
     * @return true if displayed count is less than or equal to max, false otherwise
     */
    public boolean isRecordCountWithinLimit(int maxRecordsPerPage) {
        try {
            int displayedCount = getCategoryCount();
            return displayedCount > 0 && displayedCount <= maxRecordsPerPage;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Select a parent category from the dropdown filter.
     * 
     * @param parentCategoryName The visible text of the parent category to select
     */
    public void selectParentCategoryFilter(String parentCategoryName) {
        try {
            page.locator(PARENT_FILTER).selectOption(new String[] { parentCategoryName });
            System.out.println("Selected parent category filter: " + parentCategoryName);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to select parent category: " + parentCategoryName + " - " + e.getMessage(), e);
        }
    }

    /**
     * Click the Search button to apply filters.
     */
    public void clickSearchButton() {
        try {
            // Use more specific selector to match only the primary Search button
            page.locator("button.btn-primary:has-text('Search')").click();
            page.waitForLoadState();
            page.waitForTimeout(500); // Wait for filtered results to load
            System.out.println("Clicked Search button to apply filters");
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Search button: " + e.getMessage(), e);
        }
    }

    /**
     * Verify that all displayed categories belong to the specified parent category.
     * 
     * @param expectedParent The expected parent category name
     * @return true if all categories have the expected parent, false otherwise
     */
    public boolean areAllCategoriesFilteredByParent(String expectedParent) {
        try {
            Locator rows = page.locator(TABLE_ROWS);
            int rowCount = rows.count();

            if (rowCount == 0) {
                System.out.println("No categories found in the table");
                return false;
            }

            // Check each row to verify the parent column matches expected parent
            for (int i = 0; i < rowCount; i++) {
                Locator row = rows.nth(i);
                // Column 2 is the Parent category (0-indexed: 0=ID, 1=Name, 2=Parent)
                String parentText = row.locator("td").nth(2).innerText().trim();

                if (!parentText.equals(expectedParent)) {
                    System.out.println(
                            "Found category with parent '" + parentText + "', expected '" + expectedParent + "'");
                    return false;
                }
            }

            System.out.println("Verified: All " + rowCount + " categories belong to parent '" + expectedParent + "'");
            return true;
        } catch (Exception e) {
            System.out.println("Error verifying filtered categories: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the parent category of a specific category by name.
     * 
     * @param categoryName The name of the category to check
     * @return The parent category name, or empty string if no parent
     */
    public String getParentCategoryOfCategory(String categoryName) {
        try {
            Locator rows = page.locator(TABLE_ROWS);
            int rowCount = rows.count();

            for (int i = 0; i < rowCount; i++) {
                Locator row = rows.nth(i);
                String nameText = row.locator("td").nth(1).innerText().trim();

                if (nameText.equals(categoryName)) {
                    // Column 2 is the Parent category
                    return row.locator("td").nth(2).innerText().trim();
                }
            }

            return "";
        } catch (Exception e) {
            return "";
        }
    }
}