package com.qatraining.pages.plants.plant_1;

import com.microsoft.playwright.Page;
import com.qatraining.pages.BasePage;

/**
 * Page object for deleting plants from the Plants list
 */
public class PlantsDelete extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String ROWS = "table tbody tr";
    private static final String DELETE_BUTTON_IN_ROW = "button[title='Delete'], button:has-text('Delete')";

    public PlantsDelete(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(ROWS);
    }

    public String getFirstPlantName() {
        if (page.locator(ROWS).count() == 0) return "";
        String text = page.locator(ROWS).first().locator("td").first().textContent();
        return text == null ? "" : text.trim();
    }

    public void clickDeleteForPlant(String plantName) {
        // Register dialog handler to accept confirmation alerts
        page.onceDialog(dialog -> dialog.accept());
        String sel = String.format("table tbody tr:has-text(\"%s\") %s", plantName, DELETE_BUTTON_IN_ROW);
        if (page.locator(sel).count() > 0) {
            page.locator(sel).first().click();
        }
    }

    public boolean isSuccessMessageVisible() {
        return page.locator(".alert-success").count() > 0
            || page.locator(".toast-success").count() > 0
            || page.locator("[role='alert']").count() > 0
            || page.locator(".Toastify__toast--success").count() > 0;
    }

    public boolean isPlantInList(String plantName) {
        return page.locator(String.format("table tbody tr:has-text('%s')", plantName)).count() > 0;
    }

}
