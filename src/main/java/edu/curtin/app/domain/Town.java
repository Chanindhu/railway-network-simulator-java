package edu.curtin.app.domain;

/**
 * Represents a town in the railway network simulation.
 * Manages the town's population, goods stockpile, and daily goods transport statistics,
 * supporting goods production and transport operations.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file Town.java
 * @since 2025-05-27
 */
public class Town {
    /** The immutable name of the town. */
    private final String name;

    /** the current population of the town, affecting goods production. */
    private int population;

    /** The current stockpile of goods available for transport. */
    private int goodsStockpile;

    /** The number of goods transported for the day and reset daily. */
    private int goodsTransportedToday = 0;

    /**
     * Constructs a Town with the specified name and population.
     *
     * @param name The name of the town.
     * @param population The initial population of the town.
     */
    public Town(String name, int population) {
        this.name = name;
        this.population = population;
        this.goodsStockpile = 0;
    }

    /**
     * Produces goods based on the town's population.
     * Adds one good per person to the stockpile each day.
     */
    public void produceGoods() {
        goodsStockpile += population; // 1 good per person per day
    }

    /**
     * Removes the specified amount of goods from the stockpile.
     * Ensures the stockpile does not go below zero.
     *
     * @param amount The number of goods to remove.
     */
    public void removeGoods(int amount) {
        goodsStockpile = Math.max(0, goodsStockpile - amount);
    }

    /**
     * Ads the specified amoount to the daily transported goods counter.
     *
     * @param amount The number of goods transported for today.
     */
    public void addTransportedGoods(int amount) {
        goodsTransportedToday += amount;
    }

    /**
     * Resets the daily transported goods counter to zero.
     */
    public void resetDailyStats() {
        goodsTransportedToday = 0;
    }

    /**
     * Returns the current goods stockpile. This value is for reading only and should not be modified externally.
     *
     * @return the goods stockpile
     */
    public int getGoodsStockpile() {
        return goodsStockpile;
    }

    /**
     * Returns the town name. This value is immutable and for reading only.
     *
     * @return the town name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current population. This value is for reading only and should not be modified externally.
     *
     * @return the population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets the town's population, and ensures it is non-negative.
     *
     * @param population The new population value.
     */
    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }

    /**
     * Returns the goods transported today. This value is for reading only and should not be modified externally.
     *
     * @return  goods transported today
     */
    public int getGoodsTransportedToday() {
        return goodsTransportedToday;
    }
}