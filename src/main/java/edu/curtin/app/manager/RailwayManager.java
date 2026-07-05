package edu.curtin.app.manager;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.domain.Town;
import edu.curtin.app.observer.SimulationObserver;

import java.util.List;

/**
 * Defines an interface to manage railway links in the railway network simulation
 * extends SimulationObserver to handle simulation events and provides methods for accessing and querying railway links
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file RailwayManager.java
 * @since 2025-05-27
 */
public interface RailwayManager extends SimulationObserver {
    /**
     * returns the list of all railway links in the simulation
     *
     * @return a list of RailwayLink objects
     */
    List<RailwayLink> getRailwayLinks();

    /**
     * Finds a railway link between two towns
     *
     * @param town1 the first town
     * @param town2 the second town
     * @return the railway link if found otherwise null.
     */
    RailwayLink findRailwayLink(Town town1, Town town2);
}