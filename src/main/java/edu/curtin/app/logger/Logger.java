package edu.curtin.app.logger;

/**
 * Define a logging interface for the railway network simulation.
 * Provides a method for logging messages with a specified log level.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file Logger.java
 * @since 2025-05-27
 */
public interface Logger {
    /**
     * Logs a message with the specified log level.
     *
     * @param level The severity level of the log message.
     * @param message The message to be logged.
     */
    void log(LogLevel level, String message);
}