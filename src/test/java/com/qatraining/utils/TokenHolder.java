package com.qatraining.utils;

public class TokenHolder {

    private static String token;

    public static void setToken(String token) {
        TokenHolder.token = token;
    }

    public static String getToken() {
        return token;
    }
}
