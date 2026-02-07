package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Plant Low Quantity Badge - verifying low quantity badge visibility
 */
public class PlantLowQuantityBadge extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String PLANT_LIST_ROW = "table tbody tr";
    private static final String QUANTITY_CELL = "span.text-danger.fw-bold";
    private static final String LOW_BADGE = "span.badge.bg-danger";

    public PlantLowQuantityBadge(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(PLANT_LIST_ROW);
    }

    public boolean isPlantWithLowQuantityVisible() {
        try {
            int rows = page.locator(PLANT_LIST_ROW).count();
            
            for (int i = 0; i < rows; i++) {
                String rowSelector = PLANT_LIST_ROW + ":nth-child(" + (i + 1) + ")";
                try {
                    // Get quantity from the row
                    String quantityText = page.locator(rowSelector + " " + QUANTITY_CELL).textContent();
                    if (quantityText != null && !quantityText.trim().isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(quantityText.trim());
                            if (quantity < 5) {
                                System.out.println("DEBUG: Found plant with low quantity: " + quantity);
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("DEBUG: Could not parse quantity: " + quantityText);
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println("DEBUG: Error reading row " + i + ": " + e.getMessage());
                }
            }
            System.out.println("DEBUG: No plant with quantity < 5 found");
            return false;
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error checking low quantity plants: " + e.getMessage());
            return false;
        }
    }

    public boolean isLowBadgeDisplayedForLowQuantityPlant() {
        try {
            int rows = page.locator(PLANT_LIST_ROW).count();
            
            for (int i = 0; i < rows; i++) {
                String rowSelector = PLANT_LIST_ROW + ":nth-child(" + (i + 1) + ")";
                try {
                    // Get quantity from the row
                    String quantityText = page.locator(rowSelector + " " + QUANTITY_CELL).textContent();
                    if (quantityText != null && !quantityText.trim().isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(quantityText.trim());
                            if (quantity < 5) {
                                // Check if Low badge is visible in this row
                                int badgeCount = page.locator(rowSelector + " " + LOW_BADGE).count();
                                if (badgeCount > 0) {
                                    String badgeText = page.locator(rowSelector + " " + LOW_BADGE).first().textContent();
                                    if (badgeText != null && badgeText.trim().equals("Low")) {
                                        System.out.println("DEBUG: Found 'Low' badge for quantity " + quantity);
                                        return true;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("DEBUG: Could not parse quantity: " + quantityText);
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println("DEBUG: Error reading row " + i + ": " + e.getMessage());
                }
            }
            System.out.println("DEBUG: 'Low' badge not found for low quantity plant");
            return false;
        } catch (RuntimeException e) {
            System.out.println("DEBUG: Error checking low badge: " + e.getMessage());
            return false;
        }
    }
}
