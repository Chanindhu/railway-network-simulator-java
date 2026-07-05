package edu.curtin.app.factory;

import edu.curtin.app.domain.Town;

/**
 * Creates Town objects for the railway network simulation.
 * Implements the TownFactory interface to provide a factory method for instantiating
 * towns vwith specified names and populations, following the factory pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DefaultTownFactory.java
 * @since 2025-05-27
 */
public class DefaultTownFactory implements TownFactory {
    /**
     * Creation of new Town instance with the specified name and population.
     *
     * @param name The name of the town.
     * @param population The initial population of the town.
     * @return A new Town object.
     */
    @Override
    public Town createTown(String name, int population) {
        return new Town(name, population);
    }
}