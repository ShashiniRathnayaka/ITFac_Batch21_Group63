package com.qatraining.api.categories;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

/**
 * API Page Object for Category operations
 * Handles API requests related to categories
 */
public class CategoryPageAPI {

    private static final String BASE_URL = "http://localhost:8080";
    private APIResponse response;

    /**
     * Send GET request to retrieve all categories
     * Endpoint: GET /api/categories
     */
    public void getAllCategories() {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();

        response = request.get(
                BASE_URL + "/api/categories",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("accept", "application/json"));

        System.out.println("GET /api/categories - Status: " + response.status());
    }

    /**
     * Get the HTTP status code from the response
     * 
     * @return status code
     */
    public int getStatusCode() {
        return response.status();
    }

    /**
     * Get the response body as text
     * 
     * @return response body
     */
    public String getResponseBody() {
        return response.text();
    }

    /**
     * Get the full API response object
     * 
     * @return APIResponse object
     */
    public APIResponse getResponse() {
        return response;
    }
}
