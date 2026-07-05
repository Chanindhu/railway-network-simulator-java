package edu.curtin.app.factory;

import edu.curtin.app.domain.state.DualTrackState;
import edu.curtin.app.domain.state.RailwayState;
import edu.curtin.app.domain.state.SingleTrackState;
import edu.curtin.app.domain.state.UnderConstructionState;
import edu.curtin.app.logger.Logger;

/**
 * Creates railway state objects for railway links in the railway network simulation.
 * Implements the RailwayStateFactory interface to provide factory methods for generating
 * different states as part of the factory pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DefaultRailwayStateFactory.java
 * @since 2025-05-27
 */
public class DefaultRailwayStateFactory implements RailwayStateFactory {
    /** Logger for recording state creation and debugging events. */
    private final Logger logger;

    /**
     * Constructs a DefaultRailwayStateFactory with the specified logger.
     *
     * @param logger The logger used by created state objects.
     */
    public DefaultRailwayStateFactory(Logger logger) {
        this.logger = logger;
    }

    /**
     * Creates a new DualTrackState instance for a railway link.
     *
     * @return A new DualTrackState object
     */
    @Override
    public DualTrackState createDualTrackState() {
        return new DualTrackState(logger);
    }

    /**
     * Creates a new UnderConstructionState instance for a railway link.
     *
     * @param startDay The simulation day when construction or duplication is started.
     * @param isDuplication True if the railway is under duplication, false if under initial construction.
     * @param nextState The state to transition to after construction or duplication completes.
     * @return A new UnderConstructionState object.
     */
    @Override
    public UnderConstructionState createUnderConstructionState(long startDay, boolean isDuplication, RailwayState nextState) {
        return new UnderConstructionState(logger, startDay, isDuplication, nextState);
    }

    /**
     * Creates a new SingleTrackState instance for a railway link.
     *
     * @return A new SingleTrackState object.
     */
    @Override
    public SingleTrackState createSingleTrackState() {
        return new SingleTrackState(logger);
    }

    /**
     * Retrieves the logger used by this factory.
     *
     * @return The logger instance.
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}