package com.qatraining.runners;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;


//  Test Runner for executing Cucumber tests
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, " +
        "html:target/cucumber-reports/cucumber.html, " +
        "json:target/cucumber-reports/cucumber.json, " +
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.qatraining.stepdefinitions")
// @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@Smoke")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
public class TestRunner {
    // This class will be empty - configuration is done through annotations
}
