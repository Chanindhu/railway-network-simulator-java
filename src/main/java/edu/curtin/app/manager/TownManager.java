package edu.curtin.app.manager;

import edu.curtin.app.domain.Town;
import edu.curtin.app.observer.SimulationObserver;

import java.util.List;

/**
 * defines a interface  to manage towns in the railway network simulation
 * extends SimulationObserver to handle simulation events and provides methods for accessing and querying towns
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file TownManager.java
 * @since 2025-05-27
 */
public interface TownManager extends SimulationObserver {
    /**
     * returns the list of all towns in the simulation
     *
     * @return a  list of town objects
     */
    List<Town> getTowns();

    /**
     * Finds a town by its name
     *
     * @param name The name of the town to find.
     * @return The town if found otherwise null.
     */
    Town findTown(String name);
}