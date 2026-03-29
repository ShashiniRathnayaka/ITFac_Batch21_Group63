package com.qatraining.api.Plants.plant_2;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PlantSearchByNameAPI {

    private static final String BASE_URL = "http://127.0.0.1:8080";
    private APIRequestContext apiRequest;
    private int statusCode;
    private String responseBody;

    public PlantSearchByNameAPI() {
        Playwright playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
    }

    public void searchPlantsByName(String name, int page, int size, String sort) {
        try {
            String token = TokenHolder.getToken();
            String url = BASE_URL + "/api/plants/paged";
            
            // Build query string with URL encoding for name parameter
            StringBuilder queryString = new StringBuilder();
            queryString.append("?name=").append(URLEncoder.encode(name, StandardCharsets.UTF_8));
            queryString.append("&page=").append(page);
            queryString.append("&size=").append(size);
            if (sort != null && !sort.isEmpty()) {
                queryString.append("&sort=").append(sort);
            }

            RequestOptions requestOptions = RequestOptions.create()
                    .setHeader("Authorization", "Bearer " + token);

            com.microsoft.playwright.APIResponse response = apiRequest.get(url + queryString.toString(), requestOptions);

            this.statusCode = response.status();
            this.responseBody = response.text();

            System.out.println("DEBUG: PlantSearchByNameAPI status=" + statusCode);
            System.out.println("DEBUG: PlantSearchByNameAPI body=" + responseBody);

        } catch (Exception e) {
            this.statusCode = -1;
            this.responseBody = e.toString();
            System.out.println("DEBUG: PlantSearchByNameAPI exception: " + e.toString());
        } finally {
            if (apiRequest != null) {
                apiRequest.dispose();
            }
        }
    }

    public int getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
}
