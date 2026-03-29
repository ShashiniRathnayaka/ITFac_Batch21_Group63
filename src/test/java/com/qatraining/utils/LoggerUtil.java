package com.qatraining.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// Logging utility wrapper
public class LoggerUtil {

  
    //  Get logger for the calling class
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }


    //  Log info message
    public static void info(Logger logger, String message) {
        logger.info(message);
    }

   
    // Log debug message
    public static void debug(Logger logger, String message) {
        logger.debug(message);
    }


    // Log error message
    public static void error(Logger logger, String message, Throwable throwable) {
        logger.error(message, throwable);
    }


    // Log warning message
    public static void warn(Logger logger, String message) {
        logger.warn(message);
    }
}
