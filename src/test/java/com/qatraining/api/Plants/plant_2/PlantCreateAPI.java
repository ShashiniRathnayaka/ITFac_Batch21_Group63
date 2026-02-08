package com.qatraining.api.Plants.plant_2;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;
import org.json.JSONObject;

import java.util.Map;

public class PlantCreateAPI {

    private static final String BASE_URL = "http://localhost:8080";
    private APIResponse response;
    private int statusCode;
    private String responseBody;

    public void createPlant(int categoryId, Map<String, Object> body) {
        try (Playwright playwright = Playwright.create()) {
            APIRequestContext request = playwright.request().newContext();

            JSONObject json = new JSONObject(body);

            response = request.post(
                    BASE_URL + "/api/plants/category/" + categoryId,
                    RequestOptions.create()
                            .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                            .setHeader("Content-Type", "application/json")
                            .setData(json.toString())
            );
            
            // Capture status and body before context closes
            this.statusCode = response.status();
            this.responseBody = response.text();
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public APIResponse getResponse() {
        return response;
    }
}
