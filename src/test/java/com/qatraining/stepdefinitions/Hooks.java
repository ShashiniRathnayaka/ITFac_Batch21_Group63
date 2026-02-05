package com.qatraining.stepdefinitions;

import com.qatraining.drivers.PlaywrightDriverManager;
import com.qatraining.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Cucumber Hooks for setup and teardown
public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @Before("@UI")
    public void beforeUIScenario(Scenario scenario) {
        logger.info("Starting UI scenario: " + scenario.getName());
        PlaywrightDriverManager.initializeDriver();
    }

    @Before("@API")
    public void beforeAPIScenario(Scenario scenario) {
        logger.info("Starting API scenario: " + scenario.getName());
        // Any API-specific setup can go here
    }

    @After("@UI")
    public void afterUIScenario(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                logger.error("Scenario failed: " + scenario.getName());

                // Take screenshot on failure
                byte[] screenshot = PlaywrightDriverManager.takeScreenshot();
                ScreenshotUtil.attachScreenshot(scenario.getName(), screenshot);

                // Save screenshot to file system
                String screenshotPath = ScreenshotUtil.saveScreenshot(screenshot, scenario.getName());
                logger.info("Screenshot saved: " + screenshotPath);
            } else {
                logger.info("Scenario passed: " + scenario.getName());
            }
        } catch (Exception e) {
            logger.error("Error in after hook", e);
        } finally {
            PlaywrightDriverManager.quitDriver();
        }
    }

    @After("@API")
    public void afterAPIScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("API Scenario failed: " + scenario.getName());
        } else {
            logger.info("API Scenario passed: " + scenario.getName());
        }
    }
}
