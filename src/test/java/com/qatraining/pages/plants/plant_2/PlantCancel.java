package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for Cancel button on Add Plant page
 */
public class PlantCancel extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String CANCEL_BUTTON = "a[href='/ui/plants'].btn.btn-secondary";

    public PlantCancel(Page page) {
        super(page);
    }

    public void clickCancelButton() {
        page.waitForSelector(CANCEL_BUTTON);
        page.click(CANCEL_BUTTON);
        page.waitForURL("**/ui/plants");
    }

    public boolean isOnPlantsPage() {
        return page.url().contains("/ui/plants");
    }
}
