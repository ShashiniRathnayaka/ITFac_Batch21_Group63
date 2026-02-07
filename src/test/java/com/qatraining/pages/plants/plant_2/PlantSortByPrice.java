package com.qatraining.pages.plants.plant_2;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.qatraining.pages.BasePage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Page object for sorting plants by price
 */
public class PlantSortByPrice extends BasePage {

    private static final String PLANTS_PATH = "/ui/plants";
    private static final String PRICE_HEADER = "th a[href*='sortField=price']";
    private static final String PLANT_LIST_ROW = "table tbody tr";

    private static final Pattern PRICE_PATTERN = Pattern.compile("\\d+(?:\\.\\d+)?");

    public PlantSortByPrice(Page page) {
        super(page);
    }

    public void navigateToPlantsPage() {
        navigate(PLANTS_PATH);
        page.waitForSelector(PRICE_HEADER);
    }

    public void clickPriceHeader() {
        page.waitForSelector(PRICE_HEADER);
        page.click(PRICE_HEADER);
        try {
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } catch (RuntimeException ignored) {
        }
        try {
            page.waitForSelector(PLANT_LIST_ROW, new Page.WaitForSelectorOptions().setTimeout(5000));
        } catch (RuntimeException ignored) {
        }
    }

    public List<Double> getVisiblePrices() {
        int rows = page.locator(PLANT_LIST_ROW).count();

        // 1) Try to discover the Price column by inspecting table headers
        String headerSelector = "table thead tr th";
        int headerCount = 0;
        try { headerCount = page.locator(headerSelector).count(); } catch (RuntimeException ignored) {}
        int priceColumnIndex = -1;
        for (int h = 0; h < headerCount; h++) {
            try {
                String hdr = page.locator(headerSelector).nth(h).textContent();
                if (hdr != null && hdr.toLowerCase().contains("price")) {
                    priceColumnIndex = h + 1; // nth-child is 1-based
                    break;
                }
            } catch (RuntimeException ignored) {
            }
        }

        List<Double> prices = new ArrayList<>();
        if (priceColumnIndex != -1) {
            for (int i = 0; i < rows; i++) {
                String selector = PLANT_LIST_ROW + " td:nth-child(" + priceColumnIndex + ")";
                try {
                    if (page.locator(selector).count() <= i) continue;
                    String cellText = page.locator(selector).nth(i).textContent();
                    if (cellText == null) continue;
                    String normalized = cellText.replaceAll(",", "").trim();
                    Matcher m = PRICE_PATTERN.matcher(normalized);
                    if (m.find()) {
                        try { prices.add(Double.parseDouble(m.group())); }
                        catch (NumberFormatException e) { System.out.println("DEBUG: parse failed for '"+m.group()+"'"); }
                    }
                } catch (RuntimeException e) {
                    System.out.println("DEBUG: error reading price cell: " + e.getMessage());
                }
            }
            System.out.println("DEBUG: Extracted prices from header-detected column (" + priceColumnIndex + "): " + prices);
            return prices;
        }

        // 2) Fallback: try common td indexes and accept partial parses
        int[] candidateTdIndexes = new int[]{3, 4, 5};
        for (int tdIndex : candidateTdIndexes) {
            List<Double> candidatePrices = new ArrayList<>();
            for (int i = 0; i < rows; i++) {
                String selector = PLANT_LIST_ROW + " td:nth-child(" + tdIndex + ")";
                try {
                    if (page.locator(selector).count() <= i) continue;
                    String cellText = page.locator(selector).nth(i).textContent();
                    if (cellText == null) continue;
                    String normalized = cellText.replaceAll(",", "").trim();
                    Matcher m = PRICE_PATTERN.matcher(normalized);
                    if (m.find()) {
                        try { candidatePrices.add(Double.parseDouble(m.group())); }
                        catch (NumberFormatException e) { /* skip */ }
                    }
                } catch (RuntimeException ignored) { }
            }
            if (candidatePrices.size() >= Math.max(1, rows / 2)) {
                System.out.println("DEBUG: Extracted prices from td:nth-child(" + tdIndex + "): " + candidatePrices);
                return candidatePrices;
            }
        }

        // 3) Final fallback: parse first numeric token from full row text
        for (int i = 0; i < rows; i++) {
            try {
                String rowText = page.locator(PLANT_LIST_ROW).nth(i).textContent();
                if (rowText == null) continue;
                Matcher m = PRICE_PATTERN.matcher(rowText.replaceAll(",", ""));
                if (m.find()) {
                    try { prices.add(Double.parseDouble(m.group())); }
                    catch (NumberFormatException e) { /* skip */ }
                }
            } catch (RuntimeException ignored) { }
        }
        System.out.println("DEBUG: Fallback extracted prices: " + prices);
        return prices;
    }
}
