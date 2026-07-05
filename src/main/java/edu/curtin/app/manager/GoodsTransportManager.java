package edu.curtin.app.manager;

import edu.curtin.app.observer.SimulationObserver;

/**
 * Defines a interface for managing goods transportattion on the railway network simulation
 * extends  the SimulationObserver to handle simulation events and provide a method for transporting goods
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file GoodsTransportManager.java
 * @since 2025-05-27
 */
public interface GoodsTransportManager extends SimulationObserver {
    /**
     * Manages the transportation of goods across railway links for a given day
     *
     * @param day The day number of the simulation
     * @return The total number of goods transported
     */
    int transportGoods(long day);
}