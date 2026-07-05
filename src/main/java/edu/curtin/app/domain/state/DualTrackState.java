package edu.curtin.app.domain.state;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.domain.Town;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;

/**
 * Represents the dual-track state of a railway link.
 * Allows simultaneous goods to Transport in both directions with capacity of 100 goods per day per direction.
 * Implements the RailwayState interface as part of the state pattern to manage railwayევ
 * railway link behavior.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DualTrackState.java
 * @since 2025-05-27
 */
public class DualTrackState implements RailwayState {
    /** Logger fto record transport events and debugging information. */
    private final Logger logger;

    /**
     * Constructs a DualTrackState with the specified logger.
     *
     * @param  The logger for recording transport events.
     */
    public DualTrackState(Logger logger) {
        this.logger = logger;
    }

    /**
     * Transports goods between the towns that are connected by the railway link.
     * To Support simultaneous transport in both directions, with a maximum of 100 goods per day per direction.
     *
     * @param link The railway lik connecting the towns.
     * @param day The current simulation day
     * @return The total number of goods transport in both directions.
     */
    @Override
    public int transportGoods(RailwayLink link, long day) {
        int totalGoodsTransported = 0;
        Town town1 = link.getTown1();
        Town town2 = link.getTown2();

        // Transport goods from town1 to town2.
        int goodsAvailable1 = town1.getGoodsStockpile();
        int goodsToTransport1 = Math.min(goodsAvailable1, 100); // 100 goods/day
        if (goodsToTransport1 > 0) {
            town1.removeGoods(goodsToTransport1);
            town1.addTransportedGoods(goodsToTransport1); 
            logger.log(LogLevel.DEBUG, String.format("Transported %d goods from %s to %s",
                    goodsToTransport1, town1.getName(), town2.getName()));
            totalGoodsTransported += goodsToTransport1;
        }

        // Transport goods from town2 to town1.
        int goodsAvailable2 = town2.getGoodsStockpile();
        int goodsToTransport2 = Math.min(goodsAvailable2, 100); // 100 goods/day
        if (goodsToTransport2 > 0) {
            town2.removeGoods(goodsToTransport2);
            town2.addTransportedGoods(goodsToTransport2); 
            logger.log(LogLevel.DEBUG, String.format("Transported %d goods from %s to %s",
                    goodsToTransport2, town2.getName(), town1.getName()));
            totalGoodsTransported += goodsToTransport2;
        }

        return totalGoodsTransported;
    }

    /**
     * to Update state of the railway link.
     * In the dual track stat, no further state changes occur.
     *
     * @param link The railway link  to updat
     * @param day The  curent simulation day.
     */
    @Override
    public void updateState(RailwayLink link, long day) {
        // No state change in dual-track state
    }

    /**
     * Returns name of the railway state.
     *
     * @return The string "Dual-Track".
     */
    @Override
    public String getStateName() {
        return "Dual-Track";
    }
}