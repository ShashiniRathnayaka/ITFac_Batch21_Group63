package com.qatraining.api.Plants.plant_2;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

public class PlantSummaryAPI {

    private static final String BASE_URL = "http://127.0.0.1:8080";
    private APIRequestContext apiRequest;
    private int statusCode;
    private String responseBody;

    public PlantSummaryAPI() {
        Playwright playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
    }

    public void getPlantSummary() {
        try {
            String token = TokenHolder.getToken();
            String url = BASE_URL + "/api/plants/summary";

            RequestOptions requestOptions = RequestOptions.create()
                    .setHeader("Authorization", "Bearer " + token);

            com.microsoft.playwright.APIResponse response = apiRequest.get(url, requestOptions);

            this.statusCode = response.status();
            this.responseBody = response.text();

            System.out.println("DEBUG: PlantSummaryAPI status=" + statusCode);
            System.out.println("DEBUG: PlantSummaryAPI body=" + responseBody);

        } catch (Exception e) {
            this.statusCode = -1;
            this.responseBody = e.toString();
            System.out.println("DEBUG: PlantSummaryAPI exception: " + e.toString());
        } finally {
            if (apiRequest != null) {
                apiRequest.dispose();
            }
        }
    }

    public int getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
}
