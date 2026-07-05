package edu.curtin.app.logger;

/**
 * Defines the log levels for logging system in the railway network simulation.
 * Providess  enumerated values for different severity levels of log messages
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file LogLevel.java
 * @since 2025-05-27
 */
public enum LogLevel {
    /**
     * represents a debug  level log message for detailed troubleshooting information
     */
    DEBUG,

    /**
     * represents an info level log message for general operational information
     */
    INFO,

    /**
     * represents a warning level log message for possible harmful situations
     */
    WARNING,

    /**
     * represents an error-level log message for error events that may prevent normal operation
     */
    ERROR
}