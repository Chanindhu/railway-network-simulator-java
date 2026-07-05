package edu.curtin.app.factory;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.domain.Town;
import edu.curtin.app.domain.state.SingleTrackState;
import edu.curtin.app.domain.state.UnderConstructionState;
import edu.curtin.app.logger.LogLevel;

/**
 * Creates and configures railway links for the railway network simulation.
 * Uses the factory pattern to instantiate railway links with appropriate initial states
 * and manage duplication processes. Depends on a RailwayStateFactory for state creation.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DefaultRailwayFactory.java
 * @since 2025-05-27
 */
public class DefaultRailwayFactory implements RailwayFactory {
    /** Factory for creating railway link states. */
    private final RailwayStateFactory stateFactory;

    /**
     * Constructs a DefaultRailwayFactory with tthe specified state factory.
     *
     * @param stateFactory The factory that's responsible for creating railway states.
     */
    public DefaultRailwayFactory(RailwayStateFactory stateFactory) {
        this.stateFactory = stateFactory;
    }

    /**
     * Creation of railway link between two towns with an initial under-construction state.
     * The link transitions to a single-track state after construction completes.
     *
     * @param town1 The first town in the railway link.
     * @param town2 The second town in the railway link.
     * @param startDay The simulation day when construction begins.
     * @return A new RailwayLink instance with an under-construction state.
     */
    @Override
    public RailwayLink createRailwayLink(Town town1, Town town2, long startDay) {
        // Create the single-track state for post-construction.
        SingleTrackState singleTrackState = stateFactory.createSingleTrackState();
        // Initialize with under-construction state, transitioning to single-track.
        UnderConstructionState initialState = (UnderConstructionState) stateFactory.createUnderConstructionState(startDay, false, singleTrackState);
        return new RailwayLink(town1, town2, initialState);
    }

    /**
     * Initiates the duplication process for an existing railway link.
     * Sets the link to an under-duplication state while maintaing the current state for limited transport
     * and transitioning to a dual-track state after completion.
     *
     * @param link The railway link to duplicate.
     * @param startDay The simulation day when duplication begins.
     */
    @Override
    public void startDuplication(RailwayLink link, long startDay) {
        // Creation of the dual-track state for post-duplication.
        UnderConstructionState duplicationState = (UnderConstructionState) stateFactory.createUnderConstructionState(startDay, true, stateFactory.createDualTrackState());
        // Preserve the current state for transport during duplication.
        duplicationState.setPreviousState(link.getCurrentState());
        // Update the link to the under-duplication state.
        link.setState(duplicationState);
        stateFactory.getLogger().log(LogLevel.DEBUG, String.format("Started duplication for railway %s <-> %s",
                link.getTown1().getName(), link.getTown2().getName()));
    }
}