package com.qatraining.api.Plants.plant_1;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;

import java.util.Map;

public class PlantsPageAPI {

    private static final String BASE_URL = "http://localhost:8080";
    private APIResponse response;

    public void updatePlant(int plantId, Map<String, Object> body) {

        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        System.out.println("TOKEN USED: " + TokenHolder.getToken());

        response = request.put(
                BASE_URL + "/api/plants/" + plantId,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("Content-Type", "application/json")
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
