package com.qatraining.api.Plants.plant_2;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

import java.util.HashMap;
import java.util.Map;

public class PlantGetPagedAndSortedAPI {

    private static final String BASE_URL = "http://127.0.0.1:8080";
    private APIRequestContext apiRequest;
    private int statusCode;
    private String responseBody;

    public PlantGetPagedAndSortedAPI() {
        Playwright playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
    }

    public void getPlantsWithPagination(int page, int size, String... sortCriteria) {
        try {
            String token = TokenHolder.getToken();
            String url = BASE_URL + "/api/plants/paged";
            
            // Build query string
            StringBuilder queryString = new StringBuilder();
            queryString.append("?page=").append(page);
            queryString.append("&size=").append(size);
            if (sortCriteria != null) {
                for (String sort : sortCriteria) {
                    if (sort != null && !sort.isEmpty()) {
                        queryString.append("&sort=").append(sort);
                    }
                }
            }

            RequestOptions requestOptions = RequestOptions.create()
                    .setHeader("Authorization", "Bearer " + token);

            com.microsoft.playwright.APIResponse response = apiRequest.get(url + queryString.toString(), requestOptions);

            this.statusCode = response.status();
            this.responseBody = response.text();

            System.out.println("DEBUG: PlantGetPagedAndSortedAPI status=" + statusCode);
            System.out.println("DEBUG: PlantGetPagedAndSortedAPI body=" + responseBody);

        } catch (Exception e) {
            this.statusCode = -1;
            this.responseBody = e.toString();
            System.out.println("DEBUG: PlantGetPagedAndSortedAPI exception: " + e.toString());
        } finally {
            if (apiRequest != null) {
                apiRequest.dispose();
            }
        }
    }

    public int getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
}
