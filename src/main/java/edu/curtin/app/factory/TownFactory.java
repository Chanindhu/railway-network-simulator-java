package edu.curtin.app.factory;

import edu.curtin.app.domain.Town;

/**
 * Defines a factory interface for creating Town objects in the railway network simulation.
 * Provides a method for  iinstantiating towns with specified names and populations,
 * following the factory pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file TownFactory.java
 * @since 2025-05-27
 */
public interface TownFactory {
    /**
     * Creates a new Town instance with the specified name and population.
     *
     * @param name The name of the town.
     * @param population The initial population of the town.
     * @return A new Town object.
     */
    Town createTown(String name, int population);
}