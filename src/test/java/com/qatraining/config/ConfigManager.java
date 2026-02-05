package com.qatraining.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Manager to load and manage application properties
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;

    private static final String CONFIG_FILE = "src/test/resources/config.properties";

    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE, e);
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    // Application URLs
    public String getBaseUrl() {
        return getProperty("app.base.url");
    }

    public String getApiBaseUrl() {
        return getProperty("api.base.url");
    }

    // Browser Configuration
    public String getBrowserType() {
        return getProperty("browser.type", "chromium");
    }

    public boolean isBrowserHeadless() {
        return Boolean.parseBoolean(getProperty("browser.headless", "false"));
    }

    public int getBrowserTimeout() {
        return Integer.parseInt(getProperty("browser.timeout", "30000"));
    }

    // Test User Credentials
    public String getAdminUsername() {
        return getProperty("test.user.admin.username");
    }

    public String getAdminPassword() {
        return getProperty("test.user.admin.password");
    }

    public String getSalesUsername() {
        return getProperty("test.user.user.username");
    }

    public String getSalesPassword() {
        return getProperty("test.user.user.password");
    }

    // API Configuration
    public int getApiTimeout() {
        return Integer.parseInt(getProperty("api.timeout", "10000"));
    }

    // Screenshot Configuration
    public boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    public String getScreenshotPath() {
        return getProperty("screenshot.path", "target/screenshots");
    }
}
