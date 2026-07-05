package edu.curtin.app.manager;

import edu.curtin.app.domain.Town;
import edu.curtin.app.factory.TownFactory;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;
import edu.curtin.app.messages.Message;
import edu.curtin.app.messages.TownData;

import java.util.ArrayList;
import java.util.List;

/**
 * implements a town manager for the railway network simulation
 * manages the creation,  population updates  and daily operations of towns using caching
 * Follows the TownManager interface to handle simulation events and messages.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DefaultTownManager.java
 * @since 2025-05-27
 */
public class DefaultTownManager implements TownManager {
    private final List<Town> towns = new ArrayList<>();
    private final TownFactory townFactory;
    private final Logger logger;
    private transient Town lastQueriedTown; // Cache for last queried town
    private transient String lastQueriedName; // Cache for last queried name

    /**
     * Constructs a DefaultTownManager with dependencies for town creation and logging.
     *
     * @param townFactory  factory for creating town objects
     * @param logger   logger for recording simulation events
     */
    public DefaultTownManager(TownFactory townFactory, Logger logger) {
        this.townFactory = townFactory;
        this.logger = logger;
        this.lastQueriedTown = null;
        this.lastQueriedName = null;
    }

    /**
     * returns the list of All towns in the simulation
     *
     * @return A list of town objects
     */
    @Override
    public List<Town> getTowns() {
        return towns;
    }

    /**
     * handle received messages related to town founding or population updates
     * creating new towns or updates existing town populations.
     *
     * @param message The message received from the simulation.
     */
    @Override
    public void onMessageReceived(Message<?> message) {
        if (message.getType().equals("invalid")) {
            logger.log(LogLevel.ERROR, "Invalid town message received");
            return;
        }
        if (message.getType().equals("town-founding")) {
            TownData data = (TownData) message.getData();
            Town newTown = townFactory.createTown(data.getName(), data.getPopulation());
            towns.add(newTown);
            logger.log(LogLevel.DEBUG, "Created town: " + newTown.getName());
            // Update cache if it matches the new town
            if (lastQueriedName != null && lastQueriedName.equals(data.getName())) {
                lastQueriedTown = newTown;
            }
        } else if (message.getType().equals("town-population")) {
            TownData data = (TownData) message.getData();
            Town town = findTown(data.getName());
            if (town != null) {
                town.setPopulation(data.getPopulation());
                logger.log(LogLevel.DEBUG, String.format("Updated population of %s to %d", data.getName(), data.getPopulation()));
            } else {
                logger.log(LogLevel.ERROR, "Town not found for population update: " + data.getName());
            }
        }
    }

    /**
     * triggers daily operations for all towns   resetting daily stats and producing goods
     *
     * @param day The day number of the simulation
     */
    @Override
    public void onDayStart(long day) {
        for (Town town : towns) {
            town.resetDailyStats();
            town.produceGoods();
            logger.log(LogLevel.DEBUG, String.format("Town %s produced %d goods", town.getName(), town.getPopulation()));
        }
    }

    /**
     * handle the end of a simulation day. No action is taken in this implementation
     *
     * @param day The day number of the simulation
     * @param goodsTransported The number of goods transported on this day
     */
    @Override
    public void onDayEnd(long day, int goodsTransported) {
        // No action needed
    }

    /**
     * Finds a town by its name, using caching 
     * Falls back to a stream based search  iif the cache is invalid
     *
     * @param name The name of the town to find
     * @return The town if found otherwise null
     */
    @Override
    public Town findTown(String name) {
        // Check cache first
        if (name != null && name.equals(lastQueriedName) && lastQueriedTown != null) {
            logger.log(LogLevel.DEBUG, "Found town " + name + " in cache");
            return lastQueriedTown;
        }
        // Fallback to stream-based search
        Town town = towns.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
        // Update cache
        lastQueriedName = name;
        lastQueriedTown = town;
        if (town != null) {
            logger.log(LogLevel.DEBUG, "Cached town " + name);
        }
        return town;
    }
}