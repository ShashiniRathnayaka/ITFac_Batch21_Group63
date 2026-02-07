package com.qatraining.api.categories;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

import java.util.Map;

public class CategoryDeletePageAPI {

    private static final String BASE_URL = "http://localhost:8080";
    private APIResponse response;

    /**
     * Get list of all categories
     * Used to find a category with no sub-categories for deletion
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
     * Create a new category via POST /api/categories
     * 
     * @param body Map containing category data (name, parent, subCategories)
     */
    public void createCategory(Map<String, Object> body) {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();

        response = request.post(
                BASE_URL + "/api/categories",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setData(body));
    }

    /**
     * Delete a category via DELETE /api/categories/{id}
     * 
     * @param categoryId The ID of the category to delete
     */
    public void deleteCategory(int categoryId) {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();

        response = request.delete(
                BASE_URL + "/api/categories/" + categoryId,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("Accept", "application/json"));
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
