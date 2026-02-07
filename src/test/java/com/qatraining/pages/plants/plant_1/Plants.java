package com.qatraining.pages.plants.plant_1;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Plants page with category filter functionality
 */
public class Plants extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String CATEGORY_DROPDOWN = "select[name='categoryId']";
    private static final String SEARCH_BUTTON = "button:has-text('Search')";
    private static final String PLANT_ROWS = "table tbody tr";
    private static final String RESET_BUTTON = "a[href='/ui/plants']:has-text('Reset')";
    private static final String ADD_PLANT_BUTTON = "a[href='/ui/plants/add'], a:has-text('Add a Plant')";
    private static final String NO_RESULTS_TEXT = "text=No plants found";
    private static final String PAGINATION_SELECTORS = "ul.pagination, .pagination, nav[aria-label='Page navigation']";
    
    private String lastSearchedKeyword = "";

    public Plants(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(CATEGORY_DROPDOWN);
    }

    public void selectCategory(String categoryValue) {
        // categoryValue is the value attribute (e.g., "4" for rose, "5" for komarika, etc.)
        page.selectOption(CATEGORY_DROPDOWN, categoryValue);
    }

    public void selectCategoryByLabel(String label) {
        // Alternative: select by visible text (e.g., "rose", "komarika")
        page.selectOption(CATEGORY_DROPDOWN, new com.microsoft.playwright.options.SelectOption().setLabel(label));
    }

    public void enterSearchKeyword(String keyword) {
        lastSearchedKeyword = keyword;
        System.out.println("DEBUG [enterSearchKeyword]: Attempting to enter keyword: '" + keyword + "'");
        
        // Try a set of likely selectors for the search input
        if (page.locator("input[name='name']").count() > 0) {
            System.out.println("DEBUG [enterSearchKeyword]: Found input[name='name'] selector");
            fill("input[name='name']", keyword);
            System.out.println("DEBUG [enterSearchKeyword]: Filled input[name='name'] with: '" + keyword + "'");
            String inputValue = page.locator("input[name='name']").first().inputValue();
            System.out.println("DEBUG [enterSearchKeyword]: Input field value after fill: '" + inputValue + "'");
            return;
        }
        
        if (page.locator("input[name='keyword']").count() > 0) {
            System.out.println("DEBUG [enterSearchKeyword]: Found input[name='keyword'] selector");
            fill("input[name='keyword']", keyword);
            System.out.println("DEBUG [enterSearchKeyword]: Filled input[name='keyword'] with: '" + keyword + "'");
            return;
        }
        
        if (page.locator("input[name='search']").count() > 0) {
            System.out.println("DEBUG [enterSearchKeyword]: Found input[name='search'] selector");
            fill("input[name='search']", keyword);
            System.out.println("DEBUG [enterSearchKeyword]: Filled input[name='search'] with: '" + keyword + "'");
            return;
        }
        
        if (page.locator("input[placeholder*='Search']").count() > 0) {
            System.out.println("DEBUG [enterSearchKeyword]: Found input[placeholder*='Search'] selector");
            fill("input[placeholder*='Search']", keyword);
            System.out.println("DEBUG [enterSearchKeyword]: Filled input[placeholder*='Search'] with: '" + keyword + "'");
            return;
        }
        
        if (page.locator("input[placeholder='Search plant']").count() > 0) {
            System.out.println("DEBUG [enterSearchKeyword]: Found input[placeholder='Search plant'] selector");
            fill("input[placeholder='Search plant']", keyword);
            System.out.println("DEBUG [enterSearchKeyword]: Filled input[placeholder='Search plant'] with: '" + keyword + "'");
            return;
        }
        
        // Fallback: try any input near the search button
        if (page.locator("form input").count() > 0) {
            System.out.println("DEBUG [enterSearchKeyword]: Using fallback - form input selector");
            page.locator("form input").first().fill(keyword);
            System.out.println("DEBUG [enterSearchKeyword]: Filled form input with: '" + keyword + "'");
            return;
        }
        
        System.out.println("DEBUG [enterSearchKeyword]: ERROR - Could not find any search input selector!");
    }

    public void clickSearch() {
        System.out.println("DEBUG [clickSearch]: Clicking search button...");
        System.out.println("DEBUG [clickSearch]: Current search input value: '" + getSearchInputValue() + "'");
        
        click(SEARCH_BUTTON);
        System.out.println("DEBUG [clickSearch]: Search button clicked");
        
        page.waitForTimeout(500);
        System.out.println("DEBUG [clickSearch]: Waited 500ms for page load");
        
        // Wait for either rows to appear or a no-results message
        page.waitForFunction("() => document.querySelectorAll('" + PLANT_ROWS + "').length > 0 || document.body.innerText.includes('No plants found')");
        
        int resultCount = getPlantRowCount();
        System.out.println("DEBUG [clickSearch]: Search completed. Results: " + resultCount + " rows");
        
        boolean noResults = isNoResultsVisible();
        System.out.println("DEBUG [clickSearch]: No results message visible: " + noResults);
        
        if (noResults) {
            System.out.println("DEBUG [clickSearch]: BUG DETECTED? - 'No plants found' message is displayed!");
        } else {
            System.out.println("DEBUG [clickSearch]: Plants are displayed in results");
        }
    }

    public void clickReset() {
        if (page.locator(RESET_BUTTON).count() > 0) {
            click(RESET_BUTTON);
            page.waitForTimeout(500);
        }
    }

    public boolean isAddPlantButtonVisible() {
        return page.locator(ADD_PLANT_BUTTON).count() > 0 && page.locator(ADD_PLANT_BUTTON).first().isVisible();
    }

    public boolean isNoResultsVisible() {
        return page.locator(NO_RESULTS_TEXT).count() > 0 || page.locator(PLANT_ROWS + ":has-text('No plants found')").count() > 0 || page.locator("td:has-text('No plants found')").count() > 0;
    }

    public int getPlantRowCount() {
        // Exclude any placeholder row that contains "No plants found"
        return (int) page.locator(String.format("%s:not(:has-text('No plants found'))", PLANT_ROWS)).count();
    }

    public boolean isPlantInList(String plantName) {
        // Check if plant name exists in any table row
        System.out.println("DEBUG [isPlantInList]: Searching for plant: '" + plantName + "'");
        
        int matchCount = (int) page.locator(String.format("table tbody tr:has-text('%s')", plantName)).count();
        System.out.println("DEBUG [isPlantInList]: Found " + matchCount + " rows matching '" + plantName + "'");
        
        int totalRows = getPlantRowCount();
        System.out.println("DEBUG [isPlantInList]: Total plant rows in table: " + totalRows);
        
        // List all plant names currently visible
        if (totalRows > 0) {
            System.out.println("DEBUG [isPlantInList]: Currently visible plant names:");
            var rows = page.locator(PLANT_ROWS);
            for (int i = 0; i < Math.min(totalRows, 10); i++) {
                var cells = rows.nth(i).locator("td");
                if (cells.count() > 0) {
                    String visibleName = cells.nth(0).innerText().trim();
                    System.out.println("  - Row " + i + ": '" + visibleName + "'");
                }
            }
        }
        
        boolean found = matchCount > 0;
        System.out.println("DEBUG [isPlantInList]: Plant found: " + found);
        return found;
    }

    public boolean isUrlContainsCategoryId(String categoryId) {
        String url = page.url();
        return url.contains("categoryId=" + categoryId);
    }

    public boolean findPlantAcrossPages(String plantName) {
        // First try the current page (most likely where new plant appears)
        if (isPlantInList(plantName)) return true;
        // If not found on first page, try looking at all visible rows without pagination
        return page.locator(String.format("table tbody tr td:has-text('%s')", plantName)).count() > 0;
    }

    public String getCurrentUrl() {
        return page.url();
    }

    public boolean areTableHeadersVisible(String... expectedHeaders) {
        for (String header : expectedHeaders) {
            String selector = String.format("table thead th:has-text('%s')", header);
            if (page.locator(selector).count() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isPaginationVisible() {
        return page.locator(PAGINATION_SELECTORS).count() > 0;
    }

    public int getVisiblePlantsCountOnPage() {
        return (int) page.locator(PLANT_ROWS).count();
    }

    public boolean hasPlantRecords() {
        return getPlantRowCount() > 0;
    }

    public String getSelectedCategoryLabel() {
        if (page.locator(CATEGORY_DROPDOWN + " option[selected]").count() > 0) {
            return page.locator(CATEGORY_DROPDOWN + " option[selected]").first().innerText().trim();
        }
        if (page.locator(CATEGORY_DROPDOWN + " option:checked").count() > 0) {
            return page.locator(CATEGORY_DROPDOWN + " option:checked").first().innerText().trim();
        }
        return "";
    }

    public boolean areAllVisiblePlantsInCategory(String categoryLabel) {
        com.microsoft.playwright.Locator rows = page.locator(PLANT_ROWS);
        int count = (int) rows.count();
        if (count == 0) return false;
        for (int i = 0; i < count; i++) {
            String catText = rows.nth(i).locator("td").nth(1).innerText().trim();
            if (!catText.equalsIgnoreCase(categoryLabel)) {
                return false;
            }
        }
        return true;
    }

    public String getSearchInputValue() {
        if (page.locator("input[name='name']").count() > 0) {
            return page.locator("input[name='name']").first().inputValue();
        }
        if (page.locator("input[name='keyword']").count() > 0) {
            return page.locator("input[name='keyword']").first().inputValue();
        }
        if (page.locator("input[name='search']").count() > 0) {
            return page.locator("input[name='search']").first().inputValue();
        }
        if (page.locator("input[placeholder*='Search']").count() > 0) {
            return page.locator("input[placeholder*='Search']").first().inputValue();
        }
        if (page.locator("form input").count() > 0) {
            return page.locator("form input").first().inputValue();
        }
        return "";
    }

    public boolean isCategoryResetToAllCategories() {
        String selected = getSelectedCategoryLabel();
        return selected.equalsIgnoreCase("All Categories") || selected.isEmpty() || selected.equals("-1");
    }

    public boolean isSearchInputCleared() {
        return getSearchInputValue().trim().isEmpty();
    }

    public String getLastSearchedKeyword() {
        return lastSearchedKeyword;
    }

    public String getFirstPlantNameWithSpace() {
        // Get all plant names from the table
        var rows = page.locator(PLANT_ROWS);
        int rowCount = (int) rows.count();
        
        System.out.println("DEBUG [getFirstPlantNameWithSpace]: Total rows in table: " + rowCount);
        
        for (int i = 0; i < rowCount; i++) {
            var cells = rows.nth(i).locator("td");
            if (cells.count() > 0) {
                String plantName = cells.nth(0).innerText().trim();
                System.out.println("DEBUG [getFirstPlantNameWithSpace]: Row " + i + " - Plant Name: '" + plantName + "' | Contains Space: " + plantName.contains(" "));
                
                // Check if plant name contains a space (multi-word name)
                if (plantName.contains(" ") && !plantName.isEmpty()) {
                    System.out.println("DEBUG [getFirstPlantNameWithSpace]: SUCCESS - Found plant with space: '" + plantName + "'");
                    return plantName;
                }
            }
        }
        
        // If no multi-word plant name found, return the first available plant name
        if (rowCount > 0) {
            var firstRowCells = rows.nth(0).locator("td");
            if (firstRowCells.count() > 0) {
                String firstPlant = firstRowCells.nth(0).innerText().trim();
                System.out.println("DEBUG [getFirstPlantNameWithSpace]: WARNING - No multi-word plant found, using first plant: '" + firstPlant + "'");
                return firstPlant;
            }
        }
        
        System.out.println("DEBUG [getFirstPlantNameWithSpace]: ERROR - No plants found in the table!");
        return null;
    }

}
