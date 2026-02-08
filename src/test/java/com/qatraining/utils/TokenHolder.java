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

    public static void setAdminToken(String token) {
        TokenHolder.adminToken = token;
    }

    public static void setUserToken(String token) {
        TokenHolder.userToken = token;
    }

    public static void useAdminToken() {
        TokenHolder.token = adminToken;
    }

    public static void useUserToken() {
        TokenHolder.token = userToken;
    }
}
