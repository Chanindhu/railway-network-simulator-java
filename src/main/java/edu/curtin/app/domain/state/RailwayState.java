package edu.curtin.app.domain.state;

import edu.curtin.app.domain.RailwayLink;

/**
 * Defines the contract for railway link states 
 * Part of the state pattern where this interface specifies behavior for goods transport,
 * state transitions and state identification for railway links.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file RailwayState.java
 * @since 2025-05-27
 */
public interface RailwayState {
    /**
     * Transports goods  the railway link for the current day.
     *
     * @param link The railway link connecting two towns
     * @param day The current simulation day
     * @return the total number of goods transported.
     */
    int transportGoods(RailwayLink link, long day);

    /**
     * Updates the state of the railway link, transitioning to a new state.
     *
     * @param link The railway   link to update.
     * @param day the current simulation day.
     */
    void updateState(RailwayLink link, long day);

    /**
     * To Retrieve the name of the current railway state
     *
     * @return The name of the state 
     */
    String getStateName();
}