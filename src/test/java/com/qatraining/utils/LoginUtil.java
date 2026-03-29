
package com.qatraining.utils;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.json.JSONObject;

public class LoginUtil {

    private static final String BASE_URL = "http://localhost:8080";

    public static String login(String username, String password) {

        try (Playwright playwright = Playwright.create()) {

            APIRequestContext request = playwright.request().newContext();

            JSONObject body = new JSONObject();
            body.put("username", username);
            body.put("password", password);

            APIResponse response = request.post(
                    BASE_URL + "/api/auth/login",
                    RequestOptions.create()
                            .setHeader("Content-Type", "application/json")
                            .setData(body.toString()));

            if (response.status() != 200) {
                throw new RuntimeException(
                        "Login failed with status: " + response.status());
            }

            JSONObject json = new JSONObject(response.text());
            return json.getString("token");
        }
    }
}
