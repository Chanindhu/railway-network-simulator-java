package edu.curtin.app.domain.state;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;

/**
 * Represents the under-construction or under-duplication state of a railway link 
 * Handles initial construction or duplication, transitioning to the next state after 5 days.
 * Implements the RailwayState interface as part of the state pattern to manage railway link behavior.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file UnderConstructionState.java
 * @since 2025-05-27
 */
public class UnderConstructionState implements RailwayState {
    /** Logger to record state transitions and transport events. */
    private final Logger logger;

    /** The day construction or duplication started */
    private final long startDay;

    /** Indicates whether this is a duplication orr  initial construction  */
    private final boolean isDuplication;

    /** The state to transition after construction or duplication completes */
    private final RailwayState nextState;

    /** the previous state, used during duplication to allow limited transport. */
    private RailwayState previousState;

    /**
     * Constructs an UnderConstructionState with the specified parameters.
     *
     * @param logger The logger for recording state transitions and events.
     * @param startDay thhe simulation day when construction or duplication started.
     * @param isDuplication True if the railway is under duplication, false if under initial construction.
     * @param nextState The state to transition to after completion.
     */
    public UnderConstructionState(Logger logger, long startDay, boolean isDuplication, RailwayState nextState) {
        this.logger = logger;
        this.startDay = startDay;
        this.isDuplication = isDuplication;
        this.nextState = nextState;
        this.previousState = null;
    }

    /**
     * Sets the previous state use during duplication.
     *
     * @param previousState The state of the railway link before duplication began.
     */
    public void setPreviousState(RailwayState previousState) {
        this.previousState = previousState;
    }

    /**
     * Transports goods along the railway link for the current day.
     * During the start of construction, no goods are transported. During duplication, transport is delegated
     * to the previous state   with a cap of 100 goods per day.
     *
     * @param link The railway link connecting two towns.
     * @param day The current simulation day.
     * @return the number of goods transported, or 0 during initial construction.
     */
    @Override
    public int transportGoods(RailwayLink link, long day) {
        if (isDuplication && previousState instanceof SingleTrackState) {
            // Delegate transport to the previous state during duplication.
            int goods = previousState.transportGoods(link, day);
            // Cap transport at 100 goods/day to align  wwith SingleTrackState and DualTrackState.
            int cappedGoods = Math.min(goods, 100);
            if (cappedGoods < goods) {
                logger.log(LogLevel.DEBUG, String.format("Capped transport to 100 goods on railway %s <-> %s during duplication",
                        link.getTown1().getName(), link.getTown2().getName()));
            }
            return cappedGoods;
        }
        return 0; // No transport during initial construction.
    }

    /**
     * Updates the state of the railway link.
     * Transists to the next state after 5 days of construction or duplication.
     *
     * @param link The railway link to update.
     * @param day The current simulation day.
     */
    @Override
    public void updateState(RailwayLink link, long day) {
        // Transition to the next state after 5 days.
        if (day >= startDay + 5) {
            link.setState(nextState);
            logger.log(LogLevel.INFO, String.format("Railway %s <-> %s completed %s",
                    link.getTown1().getName(), link.getTown2().getName(),
                    isDuplication ? "duplication" : "construction"));
        }
    }

    /**
     * Returns the name of the railway state.
     *
     * @return "Under-Duplication" if duplicating, otherwise "Under-Construction".
     */
    @Override
    public String getStateName() {
        return isDuplication ? "Under-Duplication" : "Under-Construction";
    }
}