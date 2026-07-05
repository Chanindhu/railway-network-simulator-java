package edu.curtin.app.manager;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.domain.Town;
import edu.curtin.app.factory.RailwayFactory;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;
import edu.curtin.app.messages.Message;
import edu.curtin.app.messages.RailwayData;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a railway manager for the railway network simulation
 * manages the  creation, state updates, and querying of railway links and  use of caching for efficiency.
 * Follows the RailwayManager interface to handle simulation events and messages
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DefaultRailwayManager.java
 * @since 2025-05-27
 */
public class DefaultRailwayManager implements RailwayManager {
    private final List<RailwayLink> railwayLinks = new ArrayList<>();
    private final TownManager townManager;
    private final RailwayFactory railwayFactory;
    private final Logger logger;
    private long currentDay = 0;
    private transient RailwayLink lastQueriedLink; // Cache for last queried link
    private transient Town lastQueriedTown1; // Cache for first town
    private transient Town lastQueriedTown2; // Cache for second town

    /**
     * Constructs a DefaultRailwayManager with dependencies for town management, railway creation  and logging
     *
     * @param townManager The manager for town related operations
     * @param railwayFactory The factory to  create railway links
     * @param logger The logger to record simulation events.
     */
    public DefaultRailwayManager(TownManager townManager, RailwayFactory railwayFactory, Logger logger) {
        this.townManager = townManager;
        this.railwayFactory = railwayFactory;
        this.logger = logger;
        this.lastQueriedLink = null;
        this.lastQueriedTown1 = null;
        this.lastQueriedTown2 = null;
    }

    /**
     * return the list of all railway links in the simulation
     *
     * @return A list of RailwayLink objects
     */
    @Override
    public List<RailwayLink> getRailwayLinks() {
        return railwayLinks;
    }

    /**
     * handle received messages that are related to railway construction or duplication
     * Validates towns and manages railway link creation or duplication
     *
     * @param message The message received from the simulation.
     */
    @Override
    public void onMessageReceived(Message<?> message) {
        if (message.getType().equals("invalid")) {
            logger.log(LogLevel.ERROR, "Invalid railway message received");
            return;
        }
        if (message.getType().equals("railway-construction") || message.getType().equals("railway-duplication")) {
            RailwayData data = (RailwayData) message.getData();
            Town town1 = townManager.findTown(data.getTown1());
            Town town2 = townManager.findTown(data.getTown2());
            if (town1 != null && town2 != null) {
                if (message.getType().equals("railway-construction")) {
                    RailwayLink existingLink = findRailwayLink(town1, town2);
                    if (existingLink != null) {
                        logger.log(LogLevel.ERROR, String.format("Railway already exists or is under construction between %s and %s", data.getTown1(), data.getTown2()));
                        return;
                    }
                    RailwayLink link = railwayFactory.createRailwayLink(town1, town2, currentDay);
                    railwayLinks.add(link);
                    logger.log(LogLevel.DEBUG, String.format("Created railway: %s <-> %s", data.getTown1(), data.getTown2()));
                    // Update cache if it matches the new link
                    if (isSameLink(town1, town2, lastQueriedTown1, lastQueriedTown2)) {
                        lastQueriedLink = link;
                    }
                } else {
                    RailwayLink link = findRailwayLink(town1, town2);
                    if (link != null) {
                        logger.log(LogLevel.DEBUG, String.format("Checking state for duplication: %s <-> %s, current state: %s",
                                data.getTown1(), data.getTown2(), link.getStateName()));
                        if (!link.getStateName().equals("Single-Track")) {
                            logger.log(LogLevel.ERROR, String.format("Cannot duplicate railway between %s and %s: railway is not in single-track state (current state: %s)",
                                    data.getTown1(), data.getTown2(), link.getStateName()));
                            return;
                        }
                        railwayFactory.startDuplication(link, currentDay);
                        logger.log(LogLevel.DEBUG, String.format("Started duplication: %s <-> %s", data.getTown1(), data.getTown2()));
                    } else {
                        logger.log(LogLevel.ERROR, String.format("Railway not found for duplication: %s <-> %s", data.getTown1(), data.getTown2()));
                    }
                }
            } else {
                logger.log(LogLevel.ERROR, String.format("Invalid towns for %s: %s, %s", message.getType(), data.getTown1(), data.getTown2()));
            }
        }
    }

    /**
     * Update the current day and trigge  state updates for all railway links
     * Logs state transitions when they occur
     *
     * @param day The day number of the simulation.
     */
    @Override
    public void onDayStart(long day) {
        currentDay = day;
        for (RailwayLink link : railwayLinks) {
            String oldState = link.getStateName();
            link.updateState(day);
            if (!oldState.equals(link.getStateName())) {
                logger.log(LogLevel.INFO, String.format("Railway %s <-> %s transitioned to %s",
                        link.getTown1().getName(), link.getTown2().getName(), link.getStateName()));
            }
        }
    }

    /**
     * handles the end of a simulation day. No action is taken in this implementation
     *
     * @param day The day number of the simulation
     * @param goodsTransported  the number of goods transported on this day
     */
    @Override
    public void onDayEnd(long day, int goodsTransported) {
        // No action needed
    }

    /**
     * find a railway link between two towns, using caching.
     * fall back to a stream based search if the cache is invalid.
     *
     * @param town1 The first town.
     * @param town2 The second town.
     * @return The railway link if found otherwise return null.
     */
    @Override
    public RailwayLink findRailwayLink(Town town1, Town town2) {
        // check cache first
        if (isSameLink(town1, town2, lastQueriedTown1, lastQueriedTown2) && lastQueriedLink != null) {
            logger.log(LogLevel.DEBUG, String.format("Found railway %s <-> %s in cache", town1.getName(), town2.getName()));
            return lastQueriedLink;
        }
        // fallback to stream based search
        RailwayLink link = railwayLinks.stream()
                .filter(l -> (l.getTown1().equals(town1) && l.getTown2().equals(town2)) ||
                        (l.getTown1().equals(town2) && l.getTown2().equals(town1)))
                .findFirst()
                .orElse(null);
        // Update cache
        lastQueriedTown1 = town1;
        lastQueriedTown2 = town2;
        lastQueriedLink = link;
        if (link != null) {
            logger.log(LogLevel.DEBUG, String.format("Cached railway %s <-> %s", town1.getName(), town2.getName()));
        }
        return link;
    }

    /**
     * check if the provided towns match the cached towns for a railway link
     *
     * @param town1 The first town to check
     * @param town2 The second town to check
     * @param cachedTown1 The first cached town
     * @param cachedTown2 The second cached town
     * @return True if the towns match or false otherwise.
     */
    private boolean isSameLink(Town town1, Town town2, Town cachedTown1, Town cachedTown2) {
        if (town1 == null || town2 == null || cachedTown1 == null || cachedTown2 == null) {
            return false;
        }
        return (town1.equals(cachedTown1) && town2.equals(cachedTown2)) ||
               (town1.equals(cachedTown2) && town2.equals(cachedTown1));
    }
}