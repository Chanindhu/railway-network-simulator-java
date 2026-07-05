package edu.curtin.app.core;

import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;
import edu.curtin.app.messages.Message;
import edu.curtin.app.observer.SimulationObserver;
import edu.curtin.app.manager.GoodsTransportManager;

import java.io.IOException;
import java.util.List;

/**
 * arrange the railway network simulation by processing daily events and coordinating observers.
 * Reads messages from a TownsInputProvider, notifies observers of events, and manages goods transport.
 * Implemented the observer pattern to decouple event processing from simulation logic.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file Simulation.java
 * @since 2025-05-27
 */
public class Simulation {
    /** provides input messages for the simulation. */
    private final TownsInputProvider inputProvider;

    /**A List of observers notified of simulation events. */
    private final List<SimulationObserver> observers;

    /** Logger for record simulation events and errors. */
    private final Logger logger;

    /** Current day of the simulation, starting at day 0. */
    private long currentDay;

    /**
     * Constructs a Simulation with the specified input provider, observers, and logger.
     *
     * @param inputProvider  Supplies messages for simulation events.
     * @param observers List of observers to notify of simulation events.
     * @param logger to Log simulation activities and errors
     */
    public Simulation(TownsInputProvider inputProvider, List<SimulationObserver> observers, Logger logger) {
        this.inputProvider = inputProvider;
        this.observers = observers;
        this.logger = logger;
        this.currentDay = 0;
    }

    /**
     * Runs the simulation until user press the "Enter" is received.
     * Processes daily  events, notifies observers, and handles goods transport.
     * To Log errors for IO issues and wrap InterruptedException in an AssertionError.
     *
     * @throws AssertionError when the simulation thread is interrupted.
     */
    public void run() {
        startSimulation();
        try {
            // Continue simulation until user presses Enter.
            while (System.in.available() == 0) {
                processDay();
                Thread.sleep(1000); // Pause for 1 second between days.
            }
            clearInputBuffer();
        } catch (IOException e) {
            logger.log(LogLevel.ERROR, "Error checking user input: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
        endSimulation();
    }

    /**
     * Initializes the simulation and logs its start.
     * Displays a message to the user to press Enter to exit.
     */
    private void startSimulation() {
        logger.log(LogLevel.INFO, "Simulation started");
        System.out.println("Simulation running. Press Enter to exit.");
    }

    /**
     * To Process a single simulation day.
     * Notifies  observers of the day  start, processes all input messages,
     *  manages goods transport, and notify observers of the day end.
     */
    private void processDay() {
        // Notify observers of start of the current day.
        logger.log(LogLevel.DEBUG, "Starting day " + currentDay);
        for (SimulationObserver observer : observers) {
            observer.onDayStart(currentDay);
        }

        // Process all messages to the current day.
        Message<?> message;
        while ((message = inputProvider.nextMessage()) != null) {
            if (message.getType().equals("invalid")) {
                logger.log(LogLevel.ERROR, "Invalid message received");
                continue; 
            }
            logger.log(LogLevel.DEBUG, "Simulation: Processing message type " + message.getType());
            for (SimulationObserver observer : observers) {
                observer.onMessageReceived(message);
            }
        }

        // Calculat total goods transported by relevant observers.
        int totalGoodsTransported = 0;
        for (SimulationObserver observer : observers) {
            if (observer instanceof GoodsTransportManager) {
                totalGoodsTransported += ((GoodsTransportManager) observer).transportGoods(currentDay);
            }
        }

        // Notify observers of the day’s end with the total goods transported.
        logger.log(LogLevel.DEBUG, "Ending day " + currentDay + " with " + totalGoodsTransported + " goods transported");
        for (SimulationObserver observer : observers) {
            observer.onDayEnd(currentDay, totalGoodsTransported);
        }

        // Move to the next day.
        currentDay++;
    }

    /**
     * Clears input buffer to consume any remaining user input.
     *
     * @throws IOException If an error occurs when reading the input.
     */
    private void clearInputBuffer() throws IOException {
        // Read and log any remaining input to clear the buffer.
        while (System.in.available() > 0) {
            int input = System.in.read();
            logger.log(LogLevel.DEBUG, "Consumed input: " + input + " (" + (char)input + ")");
        }
    }

    /**
     * To Finalize the simulation and logs completion.
     */
    private void endSimulation() {
        logger.log(LogLevel.INFO, "Simulation ended");
    }
}