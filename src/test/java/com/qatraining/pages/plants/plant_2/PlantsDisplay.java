package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Plants Display page - verifying button visibility
 */
public class PlantsDisplay extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String ADD_PLANT_BUTTON = "a[href='/ui/plants/add']";

    public PlantsDisplay(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(ADD_PLANT_BUTTON);
    }

    public boolean isAddPlantButtonVisible() {
        return page.locator(ADD_PLANT_BUTTON).count() > 0 && page.locator(ADD_PLANT_BUTTON).isVisible();
    }
}
