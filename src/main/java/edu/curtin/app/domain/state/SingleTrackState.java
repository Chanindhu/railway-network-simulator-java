package edu.curtin.app.domain.state;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.domain.Town;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;

/**
 * Represents the single-track state of a railway link in the railway  network simulation.
 * Supports goods transport  one direction per day, chhanging between towns based on the day number,
 * with a capacity of 100 goods per day. Implements the RailwayState interface as part of the state pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file SingleTrackState.java
 * @since 2025-05-27
 */
public class SingleTrackState implements RailwayState {
    /** Logger for recording transport events and debugging information */
    private final Logger logger;

    /**
     * Constructs a SingleTrackState with the specified logger
     *
     * @param logge  The logger for recording transport events.
     */
    public SingleTrackState(Logger logger) {
        this.logger = logger;
    }

    /**
     * Transports goods along railway link for the current day.
     * Alternates transport direction daily 
     * with a maximum of 100 goods per day.
     *
     * @param link The railway link connecting two towns.
     * @param day The current simulation day that used to determine transport direction.
     * @return The number of goods transported in the current direction.
     */
    @Override
    public int transportGoods(RailwayLink link, long day) {
        // Alters direction each day
        Town source, destination;
        if (day % 2 == 0) { 
            source = link.getTown1();
            destination = link.getTown2();
        } else {
            source = link.getTown2();
            destination = link.getTown1();
        }

        // Calculate and transport up to 100 goods from the sourcee town.
        int goodsAvailable = source.getGoodsStockpile();
        int goodsToTransport = Math.min(goodsAvailable, 100); 
        if (goodsToTransport > 0) {
            source.removeGoods(goodsToTransport);
            source.addTransportedGoods(goodsToTransport); 
            logger.log(LogLevel.DEBUG, String.format("Transported %d goods from %s to %s",
                    goodsToTransport, source.getName(), destination.getName()));
        }
        return goodsToTransport;
    }

    /**
     * Updates the state of the railway link.
     * In the single-track state, no state changes ocurs unless a duplication process is processes.
     *
     * @param link The railway link to update.
     * @param day The current simulation day.
     */
    @Override
    public void updateState(RailwayLink link, long day) {
        // No state change in single-track state unless duplication starts.
    }

    /**
     * Returns the name of the railway state.
     *
     * @return The string "Single-Track".
     */
    @Override
    public String getStateName() {
        return "Single-Track";
    }
}