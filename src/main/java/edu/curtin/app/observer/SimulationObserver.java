package edu.curtin.app.observer;

import edu.curtin.app.messages.Message;

/**
 * Defines an observer interface for the railway network simulation
 * provide methods to handle simulation events such as message receipt, day start and day end
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file SimulationObserver.java
 * @since 2025-05-27
 */
public interface SimulationObserver {
    /**
     * handle a received message during  the simulation.
     *
     * @param message The message received from  simulation
     */
    void onMessageReceived(Message<?> message);

    /**
     * handles the start of a new simulation day
     *
     * @param day The day number of the simulation
     */
    void onDayStart(long day);

    /**
     * Handles the end of a simulation day including the total goods transported
     *
     * @param day The day number of the simulation
     * @param goodsTransported The total number of goods transported on this day
     */
    void onDayEnd(long day, int goodsTransported);
}