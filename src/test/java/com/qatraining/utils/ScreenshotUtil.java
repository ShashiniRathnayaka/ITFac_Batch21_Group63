package com.qatraining.utils;

import com.qatraining.config.ConfigManager;
import io.qameta.allure.Attachment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


// Screenshot utility for capturing and managing screenshots
public class ScreenshotUtil {

    private static ConfigManager config = ConfigManager.getInstance();
    private static final String SCREENSHOT_DIR = config.getScreenshotPath();

    // Save screenshot to file system
    public static String saveScreenshot(byte[] screenshot, String scenarioName) {
        try {
            // Create directory if not exists
            Path screenshotPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotPath)) {
                Files.createDirectories(screenshotPath);
            }

            // Generate filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
            Path filePath = screenshotPath.resolve(fileName);

            // Write screenshot
            Files.write(filePath, screenshot);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot", e);
        }
    }

  
    // Attach screenshot to Allure report
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }

   
    // Attach screenshot with custom name
    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachScreenshot(String name, byte[] screenshot) {
        return screenshot;
    }
}
