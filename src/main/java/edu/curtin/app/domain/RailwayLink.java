package edu.curtin.app.domain;

import edu.curtin.app.domain.state.RailwayState;

/**
 * Represents a railway link between two towns 
 * Manages the state of the link and delegates
 * transport and state update operations to the current state, following the state pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file RailwayLink.java
 * @since 2025-05-27
 */
public class RailwayLink {
    /** The first town connected by the railway link. */
    private final Town town1;

    /** The second town connected by the railway link. */
    private final Town town2;

    /** The current state of the railway link to define its behavior. */
    private RailwayState state;

    /**
     * Constructs a RailwayLink whcih connects two towns with an initial state.
     *
     * @param town1 The first town in the link.
     * @param town2 The second town in the link.
     * @param initialState The initial state of the railway link.
     */
    public RailwayLink(Town town1, Town town2, RailwayState initialState) {
        this.town1 = town1;
        this.town2 = town2;
        this.state = initialState;
    }

    /**
     * Returns the first town in the railway link.
     * The returned Town object shouldn't be modified externally.
     *
     * @return the first town
     */
    public Town getTown1() {
        return town1;
    }

    /**
     * Returns the second town in the railway link.
     * The returned Town object shouldn't be modified externally.
     *
     * @return the second town
     */
    public Town getTown2() {
        return town2;
    }

    /**
     * Retrieves the name of the current railway state.
     *
     * @return The name of the current state of the railway link.
     */
    public String getStateName() {
        return state.getStateName();
    }

    /**
     * Retrieves the current state of the railway link.
     *
     * @return The current RailwayState object.
     */
    public RailwayState getCurrentState() {
        return state;
    }

    /**
     * Transports goods through the railway link for the specified day.
     * Assign the transport operation to the current state.
     *
     * @param day The current simulation day.
     * @return The number of goods transported.
     */
    public int transportGoods(long day) {
        return state.transportGoods(this, day);
    }

    /**
     * Updates the state of the railway link for the specified day.
     * Delegates the update operation to the current state, transitioning to a new state.
     *
     * @param day The current simulation day.
     */
    public void updateState(long day) {
        state.updateState(this, day);
    }

    /**
     * Sets the current state of the railway link.
     *
     * @param state The new RailwayState to set.
     */
    public void setState(RailwayState state) {
        this.state = state;
    }
}