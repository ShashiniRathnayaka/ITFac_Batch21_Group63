package com.qatraining.api.Plants.plant_2;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

public class PlantGetByCategoryAPI {

    private static final String BASE_URL = "http://127.0.0.1:8080";
    private APIRequestContext apiRequest;
    private int statusCode;
    private String responseBody;

    public PlantGetByCategoryAPI() {
        Playwright playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
    }

    public void getPlantsByCategory(int categoryId) {
        try {
            String token = TokenHolder.getToken();
            String url = BASE_URL + "/api/plants/category/" + categoryId;

            RequestOptions requestOptions = RequestOptions.create()
                    .setHeader("Authorization", "Bearer " + token);

            com.microsoft.playwright.APIResponse response = apiRequest.get(url, requestOptions);

            this.statusCode = response.status();
            this.responseBody = response.text();

            System.out.println("DEBUG: PlantGetByCategoryAPI status=" + statusCode);
            System.out.println("DEBUG: PlantGetByCategoryAPI body=" + responseBody);

        } catch (Exception e) {
            this.statusCode = -1;
            this.responseBody = e.toString();
            System.out.println("DEBUG: PlantGetByCategoryAPI exception: " + e.toString());
        } finally {
            if (apiRequest != null) {
                apiRequest.dispose();
            }
        }
    }

    public int getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
}
