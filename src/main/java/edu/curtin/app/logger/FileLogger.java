package edu.curtin.app.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implements a file  based logger for the railway network simulation.
 * Log messages with a timestamp and specified log level to a file,
 * following the Logger interface.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file FileLogger.java
 * @since 2025-05-27
 */
public class FileLogger implements Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String logFilePath;

    /**
     * Constructs a FileLogger using  the specified log file path.
     *
     * @param logFilePath The path to the log file where the  messages will be written.
     */
    public FileLogger(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    /**
     * Logs a message with the log level and timestamp to the log file.
     *
     * @param level The severity level of the log message
     * @param message The message to be logged
     */
    @Override
    public void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logEntry = String.format("[%s] %s: %s%n", timestamp, level, message);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}