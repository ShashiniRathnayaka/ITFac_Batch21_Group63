package com.qatraining.api.categories;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

import java.util.Map;

public class CategoryEditPageAPI {

    private static final String BASE_URL = "http://localhost:8080";
    private APIResponse response;

    /**
     * Get list of all categories
     * Used to find an existing category ID for update operations
     */
    public void getCategoriesList() {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();

        response = request.get(
                BASE_URL + "/api/categories",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("Accept", "application/json"));
    }

    /**
     * Update an existing category via PUT /api/categories/{id}
     * 
     * @param categoryId The ID of the category to update
     * @param body       Map containing updated category data (name, parentId)
     */
    public void updateCategory(int categoryId, Map<String, Object> body) {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();

        response = request.put(
                BASE_URL + "/api/categories/" + categoryId,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setData(body));
    }

    public int getStatusCode() {
        return response.status();
    }

    public String getResponseBody() {
        return response.text();
    }

    public APIResponse getResponse() {
        return response;
    }
}
