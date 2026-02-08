package com.qatraining.utils;

public class TokenHolder {

    private static String token;
    private static String adminToken;
    private static String userToken;

    public static void setToken(String token) {
        TokenHolder.token = token;
    }

    public static String getToken() {
        return token;
    }

    public static void setAdminToken(String adminToken) {
        TokenHolder.adminToken = adminToken;
    }

    public static void useAdminToken() {
        TokenHolder.token = TokenHolder.adminToken;
    }

    public static void setUserToken(String userToken) {
        TokenHolder.userToken = userToken;
    }

    public static void useUserToken() {
        TokenHolder.token = TokenHolder.userToken;
    }
}
