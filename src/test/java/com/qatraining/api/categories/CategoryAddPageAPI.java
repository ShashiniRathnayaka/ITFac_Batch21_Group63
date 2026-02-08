package com.qatraining.api.categories;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

import java.util.Map;

public class CategoryAddPageAPI {

    private static final String BASE_URL = "http://localhost:8080";
    private APIResponse response;

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
