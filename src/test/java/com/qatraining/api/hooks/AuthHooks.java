package com.qatraining.api.hooks;

import com.qatraining.utils.LoginUtil;
import com.qatraining.utils.TokenHolder;
import io.cucumber.java.Before;

public class AuthHooks {

    private static String adminToken;
    private static String nonAdminToken;

    @Before("@adminapi")
    public void loginAsAdmin() {
        System.out.println("DEBUG: Executing loginAsAdmin hook");
        if (adminToken == null) {
            System.out.println("DEBUG: adminToken is null, performing login...");
            adminToken = LoginUtil.login("admin", "admin123");
            System.out.println(
                    "DEBUG: Login successful, token: " + (adminToken != null ? "Token received" : "Token is null"));
            TokenHolder.setAdminToken(adminToken);
        } else {
            System.out.println("DEBUG: Using cached adminToken");
        }
        TokenHolder.useAdminToken();
        System.out.println("DEBUG: Admin token activated in TokenHolder");
    }

    @Before("@nonadminapi")
    public void loginAsNonAdmin() {
        System.out.println("DEBUG: Executing loginAsNonAdmin hook");
        if (nonAdminToken == null) {
            System.out.println("DEBUG: nonAdminToken is null, performing login...");
            nonAdminToken = LoginUtil.login("testuser", "test123");
            System.out.println(
                    "DEBUG: Login successful, token: " + (nonAdminToken != null ? "Token received" : "Token is null"));
            TokenHolder.setUserToken(nonAdminToken);
        } else {
            System.out.println("DEBUG: Using cached nonAdminToken");
        }
        TokenHolder.useUserToken();
        System.out.println("DEBUG: User token activated in TokenHolder");
    }
}