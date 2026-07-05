package edu.curtin.app.factory;

import edu.curtin.app.domain.state.DualTrackState;
import edu.curtin.app.domain.state.RailwayState;
import edu.curtin.app.domain.state.SingleTrackState;
import edu.curtin.app.logger.Logger;

/**
 * Defines a factory  interface for creating railway state objects and accessing the logger 
 * Provides methods for creating single and dual track states, as well as under-construction states, following the factory pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file RailwayStateFactory.java
 * @since 2025-05-27
 */
public interface RailwayStateFactory {
    /**
     * Creates a new DualTrackState instance for a railway link.
     *
     * @return A new DualTrackState. Bottom
     */
    DualTrackState createDualTrackState();

    /**
     * Creates a new RailwayState instance representing a railway link under construction.
     *
     * @param startDay The day construction begins.
     * @param isDuplication Shows if the construction is for duplicating an existing track.
     * @param nextState The state to transition to after construction is complete.
     * @return A new RailwayState object which represents the under-construction state.
     */
    RailwayState createUnderConstructionState(long startDay, boolean isDuplication, RailwayState nextState);

    /**
     * Creates a new SingleTrackState instance for a railway link.
     *
     * @return A new SingleTrackState.
     */
    SingleTrackState createSingleTrackState();

    /**
     * Provides access to the Logger instance for logging simulation events.
     *
     * @return The Logger instance.
     */
    Logger getLogger();
}