package com.qatraining.api.Plants.plant_2;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;
import org.json.JSONObject;

import java.util.Map;

public class PlantNameLengthValidationAPI {

    private static final String BASE_URL = "http://127.0.0.1:8080";
    private APIRequestContext apiRequest;
    private int statusCode;
    private String responseBody;

    public PlantNameLengthValidationAPI() {
        Playwright playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
    }

    public void createPlant(int categoryId, Map<String, Object> body) {
        try {
            String token = TokenHolder.getToken();
            String url = BASE_URL + "/api/plants/category/" + categoryId;

            JSONObject jsonBody = new JSONObject(body);
            RequestOptions requestOptions = RequestOptions.create()
                    .setHeader("Authorization", "Bearer " + token)
                    .setHeader("Content-Type", "application/json")
                    .setData(jsonBody.toString());

            com.microsoft.playwright.APIResponse response = apiRequest.post(url, requestOptions);

            this.statusCode = response.status();
            this.responseBody = response.text();

            System.out.println("DEBUG: PlantNameLengthValidationAPI status=" + statusCode);
            System.out.println("DEBUG: PlantNameLengthValidationAPI body=" + responseBody);

        } catch (Exception e) {
            this.statusCode = -1;
            this.responseBody = e.toString();
            System.out.println("DEBUG: PlantNameLengthValidationAPI exception: " + e.toString());
        } finally {
            if (apiRequest != null) {
                apiRequest.dispose();
            }
        }
    }

    public int getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
}
