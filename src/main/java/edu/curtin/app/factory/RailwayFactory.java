package edu.curtin.app.factory;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.domain.Town;

/**
 * Defines a factory interface for creating and managing railway links
 * Provides methods for creating railway links between towns and initiating their duplication process,
 * following the factory pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file RailwayFactory.java
 * @since 2025-05-27
 */
public interface RailwayFactory {
    /**
     * Creates a new RailwayLink instance which connects two towns with a specified start day.
     *
     * @param town1 The first town to connect.
     * @param town2 The second town to connect.
     * @param startDay The day the railway link is established.
     * @return A new RailwayLink object.
     */
    RailwayLink createRailwayLink(Town town1, Town town2, long startDay);

    /**
     * Initiates the duplication process for a specified railway link starting on a given day.
     *
     * @param link The railway link to duplicate.
     * @param startDay The day the duplication process begins.
     */
    void startDuplication(RailwayLink link, long startDay);
}