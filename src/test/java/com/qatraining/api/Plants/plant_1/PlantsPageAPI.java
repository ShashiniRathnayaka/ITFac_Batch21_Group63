package com.qatraining.api.Plants.plant_1;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;
// import org.json.JSONArray;
// import org.json.JSONObject;

import java.util.Map;

public class PlantsPageAPI {
        public void deletePlant(int plantId) {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        response = request.delete(
            BASE_URL + "/api/plants/" + plantId,
            RequestOptions.create()
                .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                .setHeader("Accept", "*/*"));
        }

        public void getPlantById(int plantId) {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        response = request.get(
            BASE_URL + "/api/plants/" + plantId,
            RequestOptions.create()
                .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                .setHeader("Accept", "*/*"));
        }
    public void createPlant(int categoryId, Map<String, Object> body) {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        response = request.post(
                BASE_URL + "/api/plants/category/" + categoryId,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("Content-Type", "application/json")
                        .setData(body));
    }

    private static final String BASE_URL = "http://localhost:8080";
    private APIResponse response;

    public void updatePlant(int plantId, Map<String, Object> body) {

        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();

        response = request.put(
                BASE_URL + "/api/plants/" + plantId,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer " + TokenHolder.getToken())
                        .setHeader("Content-Type", "application/json")
                        .setData(body));
    }

    public void getPlantsList() {
        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();

        response = request.get(
                BASE_URL + "/api/plants",
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
