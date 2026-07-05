package edu.curtin.app.messages;

/**
 * represents data for  town in the railway network simulation
 * stores  name and population of a town
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file TownData.java
 * @since 2025-05-27
 */
public class TownData {
    private final String name;
    private final int population;

    /**
     * constructs   TownData object with the specified name and population
     *
     * @param name The name of the town
     * @param population The population of the town
     */
    public TownData(String name, int population) {
        this.name = name;
        this.population = population;
    }

    /**
     * retrieves the name of the town
     *
     * @return the name of the town
     */
    public String getName() {
        return name;
    }

    /**
     * retrieves the population of the town
     *
     * @return the population of the town
     */
    public int getPopulation() {
        return population;
    }
}