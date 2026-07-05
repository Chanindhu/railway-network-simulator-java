package edu.curtin.app.manager;

import edu.curtin.app.messages.Message;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;
import edu.curtin.app.domain.RailwayLink;

/**
 * Implement   goods transport manager for the railway network simulation.
 * Manages the transportation of goods through  the railway links and logs transport activities
 * Follows the GoodsTransportManager interface to handle simulation events.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DefaultGoodsTransportManager.java
 * @since 2025-05-27
 */
public class DefaultGoodsTransportManager implements GoodsTransportManager {
    private final RailwayManager railwayManager;
    private final Logger logger;

    /**
     * Constructs a DefaultGoodsTransportManager with a railway manager and logger.
     *
     * @param railwayManager The manager providing access to railway links.
     * @param logger The logger used to record transport activities.
     */
    public DefaultGoodsTransportManager(RailwayManager railwayManager, Logger logger) {
        this.railwayManager = railwayManager;
        this.logger = logger;
    }

    /**
     * handles received messages but no action is taken for messages for this implementation.
     *
     * @param message The message received from the simulation.
     */
    @Override
    public void onMessageReceived(Message<?> message) {
        // No action needed 
    }

    /**
     * handles  the start of a new simulation day And no  action is taken for this implementation.
     *
     * @param day The day number of the simulation.
     */
    @Override
    public void onDayStart(long day) {
        // No action needed
    }

    /**
     * Handles the end of a simulation day. No action is taken as transport is handled in transportGoods.
     *
     * @param day The day number of the simulation.
     * @param goodsTransported The number of goods transported this day
     */
    @Override
    public void onDayEnd(long day, int goodsTransported) {
        // Handled in transportGoods
    }

    /**
     * manages of the transportation of goods across all railway links for a given day
     * Logs the number of goods transported on each Railwaylink and returns the total
     *
     * @param day The day number of the simulation.
     * @return The total number of goods transported across all railway links.
     */
    @Override
    public int transportGoods(long day) {
        int totalGoodsTransported = 0;
        for (RailwayLink link : railwayManager.getRailwayLinks()) {
            int goods = link.transportGoods(day);
            if (goods > 0) {
                logger.log(LogLevel.DEBUG, String.format("Transported %d goods on railway %s <-> %s",
                        goods, link.getTown1().getName(), link.getTown2().getName()));
            }
            totalGoodsTransported += goods;
        }
        return totalGoodsTransported;
    }
}