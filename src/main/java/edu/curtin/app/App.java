package edu.curtin.app;

import edu.curtin.app.core.SimpleTownsInputProvider;
import edu.curtin.app.core.Simulation;
import edu.curtin.app.core.TownsInputProvider;
import edu.curtin.app.factory.DefaultRailwayFactory;
import edu.curtin.app.factory.DefaultRailwayStateFactory;
import edu.curtin.app.factory.DefaultTownFactory;
import edu.curtin.app.factory.RailwayFactory;
import edu.curtin.app.factory.RailwayStateFactory;
import edu.curtin.app.factory.TownFactory;
import edu.curtin.app.logger.FileLogger;
import edu.curtin.app.logger.LogLevel;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.SimulationLogger;
import edu.curtin.app.manager.DefaultGoodsTransportManager;
import edu.curtin.app.manager.DefaultRailwayManager;
import edu.curtin.app.manager.DefaultTownManager;
import edu.curtin.app.manager.GoodsTransportManager;
import edu.curtin.app.manager.RailwayManager;
import edu.curtin.app.manager.TownManager;
import edu.curtin.app.observer.DefaultStatusReporter;
import edu.curtin.app.observer.SimulationObserver;
import edu.curtin.app.observer.StatusReporter;

import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for the railway network simulation.
 * initializes and configures the simulation components, including factories, managers,
 * observers and the input provider to run the simulation.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file App.java
 * @since 2025-05-27
 */
public class App {
    /**
     * Entry point for the railway network simulation.
     * first sets up the logger, factories, managers, observers, and input provider,
     * then starts the simulation.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        // initializing  file logger for  Simulation logging
        Logger fileLogger = new FileLogger("simulation.log");
        SimulationObserver simulationLogger = new SimulationLogger(fileLogger, LogLevel.INFO);

        // set up factories for creating  Simulation components
        TownFactory townFactory = new DefaultTownFactory();
        RailwayStateFactory stateFactory = new DefaultRailwayStateFactory(fileLogger);
        RailwayFactory railwayFactory = new DefaultRailwayFactory(stateFactory);

        // initializing managers for handling towns, railways and goods transport
        TownManager townManager = new DefaultTownManager(townFactory, fileLogger);
        RailwayManager railwayManager = new DefaultRailwayManager(townManager, railwayFactory, fileLogger);
        GoodsTransportManager goodsTransportManager = new DefaultGoodsTransportManager(railwayManager, fileLogger);
        StatusReporter statusReporter = new DefaultStatusReporter(townManager, railwayManager, fileLogger);

        // configures  input provider for Simulation data
        TownsInputProvider inputProvider = new SimpleTownsInputProvider();

        // Creating a list of observers to monitor and manage Simulation events
        List<SimulationObserver> observers = new ArrayList<>();
        observers.add(townManager);
        observers.add(railwayManager);
        observers.add(goodsTransportManager);
        observers.add(statusReporter);
        observers.add(simulationLogger);

        // Initialize and run the simulation
        Simulation simulation = new Simulation(inputProvider, observers, fileLogger);
        simulation.run();
    }
}