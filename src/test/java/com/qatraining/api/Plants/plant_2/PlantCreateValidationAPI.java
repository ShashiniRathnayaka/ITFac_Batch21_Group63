package com.qatraining.api.Plants.plant_2;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qatraining.utils.TokenHolder;
import org.json.JSONObject;

import java.util.Map;

public class PlantCreateValidationAPI {

    private static final String BASE_URL = "http://localhost:8080";
    private APIRequestContext apiRequest;
    private int statusCode;
    private String responseBody;

    public PlantCreateValidationAPI() {
        Playwright playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
    }

    public void createPlant(int categoryId, Map<String, Object> body) {
        try {
            String token = TokenHolder.getToken();
            String url = BASE_URL + "/api/plants/category/" + categoryId;
            
            // Prepare request options with Bearer token and JSON body
            JSONObject jsonBody = new JSONObject(body);
            RequestOptions requestOptions = RequestOptions.create()
                    .setHeader("Authorization", "Bearer " + token)
                    .setHeader("Content-Type", "application/json")
                    .setData(jsonBody.toString());
            
            // Send POST request
            com.microsoft.playwright.APIResponse response = apiRequest.post(url, requestOptions);
            
            // Capture status and body BEFORE closing context
            this.statusCode = response.status();
            this.responseBody = response.text();
            
            System.out.println("DEBUG: PlantCreateValidationAPI response status=" + statusCode);
            System.out.println("DEBUG: PlantCreateValidationAPI response body=" + responseBody);
            
        } catch (Exception e) {
            this.statusCode = -1;
            this.responseBody = e.toString();
            System.out.println("DEBUG: PlantCreateValidationAPI exception: " + e.toString());
        } finally {
            if (apiRequest != null) {
                apiRequest.dispose();
            }
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
