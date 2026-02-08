package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Plant Reset functionality - verifying reset button resets filters
 */
public class PlantReset extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String SEARCH_INPUT = "input[name='name'][placeholder='Search plant']";
    private static final String CATEGORY_DROPDOWN = "select[name='categoryId']";
    private static final String RESET_BUTTON = "a[href='/ui/plants'].btn.btn-outline-secondary";
    private static final String PLANT_LIST_ROW = "table tbody tr";

    public PlantReset(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(SEARCH_INPUT);
        page.waitForSelector(CATEGORY_DROPDOWN);
    }

    public void enterSearchText(String searchText) {
        page.waitForSelector(SEARCH_INPUT);
        page.fill(SEARCH_INPUT, searchText);
        System.out.println("DEBUG: Entered search text: " + searchText);
    }

    public void selectCategoryFromDropdown(String categoryName) {
        page.waitForSelector(CATEGORY_DROPDOWN);
        
        // Get the option value for the category name
        String optionValue = getOptionValue(categoryName);
        
        if (optionValue != null) {
            page.selectOption(CATEGORY_DROPDOWN, optionValue);
            System.out.println("DEBUG: Selected category: " + categoryName + " (value: " + optionValue + ")");
            // Wait for page to update after category change
            try {
                page.waitForLoadState();
            } catch (RuntimeException ignored) {
            }
        }
    }

    public void clickResetButton() {
        page.waitForSelector(RESET_BUTTON);
        page.click(RESET_BUTTON);
        System.out.println("DEBUG: Clicked reset button");
        // Wait for page to navigate/reload after reset
        try {
            page.waitForURL("**/ui/plants**", new Page.WaitForURLOptions().setTimeout(10000));
        } catch (RuntimeException ignored) {
        }
        page.waitForSelector(SEARCH_INPUT);
    }

    public boolean isSearchFieldEmpty() {
        try {
            String searchValue = page.inputValue(SEARCH_INPUT);
            boolean isEmpty = searchValue == null || searchValue.trim().isEmpty();
            System.out.println("DEBUG: Search field value: '" + searchValue + "' | Empty: " + isEmpty);
            return isEmpty;
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error checking search field: " + e.getMessage());
            return false;
        }
    }

    public boolean isCategoryDropdownShowingAllCategories() {
        try {
            String selectedValue = page.inputValue(CATEGORY_DROPDOWN);
            boolean isAllCategories = selectedValue == null || selectedValue.trim().isEmpty();
            System.out.println("DEBUG: Category dropdown value: '" + selectedValue + "' | All Categories: " + isAllCategories);
            return isAllCategories;
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error checking category dropdown: " + e.getMessage());
            return false;
        }
    }

    public int getPlantCount() {
        try {
            int count = page.locator(PLANT_LIST_ROW).count();
            System.out.println("DEBUG: Plant count on page: " + count);
            return count;
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error counting plants: " + e.getMessage());
            return 0;
        }
    }

    private String getOptionValue(String categoryName) {
        switch (categoryName) {
            case "Dicots":
                return "6";
            case "Monocots":
                return "5";
            case "Solitary":
                return "2";
            case "Spike":
                return "4";
            case "Umbel":
                return "3";
            default:
                return null;
        }
    }
}
