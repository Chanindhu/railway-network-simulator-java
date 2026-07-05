package edu.curtin.app.logger;

import edu.curtin.app.messages.Message;
import edu.curtin.app.messages.TownData;
import edu.curtin.app.messages.RailwayData;
import edu.curtin.app.observer.SimulationObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Implements a simulation logger to observes and logs events in the railway network simulation.
 * processes messages and simulation related events and loggs them based on a minimum log level using a delegate logger
 * Follows the observer pattern to handle simulation updates
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file SimulationLogger.java
 * @since 2025-05-27
 */
public class SimulationLogger implements SimulationObserver {
    private final Logger delegateLogger;
    private final LogLevel minLevel;
    private final Map<String, Consumer<Message<?>>> messageHandlers;

    /**
     * Constructs a SimulationLogger with a delegate logger and minimum log level.
     *
     * @param delegateLogger The logger that is used to write log messages
     * @param minLevel The minimum log level for messages to be logged
     */
    public SimulationLogger(Logger delegateLogger, LogLevel minLevel) {
        this.delegateLogger = delegateLogger;
        this.minLevel = minLevel;
        this.messageHandlers = new HashMap<>();
        initializeMessageHandlers();
    }

    /**
     * Using handlers for different message types to process simulation events.
     */
    private void initializeMessageHandlers() {
        registerMessageHandler("invalid", msg -> log(LogLevel.ERROR, "Invalid message received"));
        registerMessageHandler("town-founding", msg -> {
            TownData townData = (TownData) msg.getData();
            log(LogLevel.INFO, String.format("Town %s founded with population %d", townData.getName(), townData.getPopulation()));
        });
        registerMessageHandler("town-population", msg -> {
            TownData townData = (TownData) msg.getData();
            log(LogLevel.INFO, String.format("Town %s population updated to %d", townData.getName(), townData.getPopulation()));
        });
        registerMessageHandler("railway-construction", msg -> {
            RailwayData railwayData = (RailwayData) msg.getData();
            log(LogLevel.INFO, String.format("Railway construction started between %s and %s", railwayData.getTown1(), railwayData.getTown2()));
        });
        registerMessageHandler("railway-duplication", msg -> {
            RailwayData railwayData = (RailwayData) msg.getData();
            log(LogLevel.INFO, String.format("Railway duplication started between %s and %s", railwayData.getTown1(), railwayData.getTown2()));
        });
    }

    /**
     * registers a  handler for a specific message type.
     *
     * @param type  The message type to handle
     * @param handler  The  consumer function to process the message
     */
    private void registerMessageHandler(String type, Consumer<Message<?>> handler) {
        messageHandlers.put(type, handler);
    }

    /**
     * Logs a message if its level meets or exceeds the minimum log level
     *
     * @param level The severity level of the log message
     * @param message The message to be logged.
     */
    private void log(LogLevel level, String message) {
        if (level.ordinal() >= minLevel.ordinal()) {
            delegateLogger.log(level, message);
        }
    }

    /**
     * handles a received message by logging it and invokig the appropriate handler
     *
     * @param message The message received from the simulation
     */
    @Override
    public void onMessageReceived(Message<?> message) {
        log(LogLevel.DEBUG, "Message received: " + message.getType());
        Consumer<Message<?>> handler = messageHandlers.getOrDefault(message.getType(), msg ->
            log(LogLevel.ERROR, "Unknown message type: " + msg.getType()));
        handler.accept(message);
    }

    /**
     * Logs the start of a new simulation day
     *
     * @param day The day number of  the simulation
     */
    @Override
    public void onDayStart(long day) {
        log(LogLevel.INFO, String.format("Day %d started", day));
    }

    /**
     * Logs the end of a simulation day with the number of goods that was transported
     *
     * @param day The day number of the simulation
     * @param goodsTransported The number of goods transported to this dayy
     */
    @Override
    public void onDayEnd(long day, int goodsTransported) {
        log(LogLevel.INFO, String.format("Day %d ended: %d goods transported", day, goodsTransported));
    }
}