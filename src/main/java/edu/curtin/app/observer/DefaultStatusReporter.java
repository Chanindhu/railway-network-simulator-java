package edu.curtin.app.observer;

import edu.curtin.app.domain.RailwayLink;
import edu.curtin.app.domain.Town;
import edu.curtin.app.logger.Logger;
import edu.curtin.app.logger.LogLevel;
import edu.curtin.app.manager.RailwayManager;
import edu.curtin.app.manager.TownManager;
import edu.curtin.app.messages.Message;
import edu.curtin.app.messages.TownData;
import edu.curtin.app.messages.RailwayData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * implements status reporting for a railway network simulation.
 * observes  messages from  simulation, tracks daily events  and generates status report
 * including console output and network visualizations in DOT format.
 * implements the StatusReporter interface tto integrate with the  simulation's observer pattern.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file DefaultStatusReporter.java
 * @since 2025-05-27
 */
public class DefaultStatusReporter implements StatusReporter {
    /** manages town-related data for the simulation */
    private final TownManager townManager;
    /** manages railway link data for the simulation */
    private final RailwayManager railwayManager;
    /** logger for recording debug, warning, and error messages */
    private final Logger logger;
    /** stores messages received during a simulation day for reporting */
    private final List<Message<?>> dailyMessages = new ArrayList<>();
    /** stores pending railway duplication messages for validation */
    private final List<RailwayData> pendingDuplications = new ArrayList<>();
    /** track  set of town names from the previous day foor change detection */
    private Set<String> previousTowns = new HashSet<>();
    /** tracks railway links from the previous day for change detection */
    private List<String> previousRailways = new ArrayList<>();
    /** Maps message types to their respective console printing logic */
    private final Map<String, Consumer<Message<?>>> messagePrinters;

    /**
     * constructs a DefaultStatusReporter with dependencies for town, railway management and logging
     *
     * @param townManager    the manager for town related data
     * @param railwayManager the manager for railway link data
     * @param logger        the logger for recording simulation events
     */
    public DefaultStatusReporter(TownManager townManager, RailwayManager railwayManager, Logger logger) {
        this.townManager = townManager;
        this.railwayManager = railwayManager;
        this.logger = logger;
        this.messagePrinters = new HashMap<>();
        initializeMessagePrinters();
    }

    /**
     * initializes  message printer map with  handlers for different message types
     * each handler define  how a specific message type 
     * is formatted and printed to the console.
     */
    private void initializeMessagePrinters() {
        messagePrinters.put("town-founding", msg -> {
            TownData townData = (TownData) msg.getData();
            System.out.printf("town-founding %s %d%n", townData.getName(), townData.getPopulation());
        });
        messagePrinters.put("town-population", msg -> {
            TownData townData = (TownData) msg.getData();
            System.out.printf("town-population %s %d%n", townData.getName(), townData.getPopulation());
        });
        messagePrinters.put("railway-construction", msg -> {
            RailwayData railwayData = (RailwayData) msg.getData();
            System.out.printf("railway-construction %s %s%n", railwayData.getTown1(), railwayData.getTown2());
        });
        messagePrinters.put("railway-duplication", msg -> {
            RailwayData railwayData = (RailwayData) msg.getData();
            System.out.printf("railway-duplication %s %s%n", railwayData.getTown1(), railwayData.getTown2());
        });
        messagePrinters.put("invalid-railway-duplication", msg -> {
            RailwayData railwayData = (RailwayData) msg.getData();
            System.out.printf("invalid-railway-duplication %s %s%n", railwayData.getTown1(), railwayData.getTown2());
        });
    }

    /**
     * Process incomming simulation messages
     * store valid messages for daily reporting with railway duplication messages
     * queued for validation before processing
     *
     * @param message The message received from the simulation.
     */
    @Override
    public void onMessageReceived(Message<?> message) {
        if (message.getType().equals("invalid")) {
            return; // Ignore invalid messages.
        }
        if (message.getType().equals("railway-duplication")) {
            RailwayData railwayData = (RailwayData) message.getData();
            pendingDuplications.add(railwayData);
            logger.log(LogLevel.DEBUG, String.format("Stored pending railway-duplication message for %s <-> %s",
                    railwayData.getTown1(), railwayData.getTown2()));
        } else {
            dailyMessages.add(message);
        }
    }

    /**
     * handles the start of a new simulation day
     * no specific actions are required at day start.
     *
     * @param day the current simulation day
     */
    @Override
    public void onDayStart(long day) {
        // No action needed
    }

    /**
     * handle the end of a simulation day
     * generates a status report, updates network visualization, clears daily messages,
     * and updattes thhe previous state for change detection.
     *
     * @param day the current simulation day
     * @param totalGoodsTransported the total goods transported on this day
     */
    @Override
    public void onDayEnd(long day, int totalGoodsTransported) {
        logger.log(LogLevel.DEBUG, "DefaultStatusReporter: Generating status for day " + day);
        printStatusReport(day, totalGoodsTransported);
        updateNetworkVisualization();
        dailyMessages.clear();
        updatePreviousState();
    }

    /**
     * prints a comprehensive status report for the simulation day
     * which includes daily events, town statuse and total goods transported
     *
     * @param day The current simulation day.
     * @param totalGoodsTransported The total goods transported on this day.
     */
    private void printStatusReport(long day, int totalGoodsTransported) {
        printConsoleHeader(day);
        processPendingDuplications();
        printDailyEvents();
        printConsoleTowns();

        System.out.println("Network Summary:");
        System.out.println("-------------");
        System.out.println("Total Goods Transported Today: " + totalGoodsTransported);
        System.out.println();
    }

    /**
     * Printing a header to thee console status report
     * displays the simulation day in  formatted  header
     *
     * @param day The current simulation day.
     */
    private void printConsoleHeader(long day) {
        System.out.println("=====================================");
        System.out.println("Railway Network Status Report - Day " + day);
        System.out.println("=====================================");
        System.out.println();
    }

    /**
     * validate and processes pending railway duplication messages
     * checks if towns exist and if the railway link in a valid state for  duplication
     * addind valid or invalid duplication messages to the daily messages list
     */
    private void processPendingDuplications() {
        for (RailwayData duplication : pendingDuplications) {
            Town town1 = townManager.findTown(duplication.getTown1());
            Town town2 = townManager.findTown(duplication.getTown2());
            if (town1 != null && town2 != null) {
                RailwayLink link = railwayManager.findRailwayLink(town1, town2);
                String state = link != null ? link.getStateName() : "none";
                logger.log(LogLevel.DEBUG, String.format("Checking railway-duplication %s <-> %s: state=%s",
                        duplication.getTown1(), duplication.getTown2(), state));
                Message<?> message;
                if (link != null && (link.getStateName().equals("Dual-Track") || link.getStateName().equals("Under-Duplication"))) {
                    message = new Message<>("railway-duplication", duplication);
                } else {
                    message = new Message<>("invalid-railway-duplication", duplication);
                }
                dailyMessages.add(message);
            } else {
                Message<?> message = new Message<>("invalid-railway-duplication", duplication);
                dailyMessages.add(message);
            }
        }
        pendingDuplications.clear();
    }

    /**
     * prints every daily events to the console
     * useing the message printer map to format and display each event
     */
    private void printDailyEvents() {
        System.out.println("Daily Events:");
        System.out.println("-------------");
        if (dailyMessages.isEmpty()) {
            System.out.println("(No events)");
        } else {
            for (Message<?> msg : dailyMessages) {
                Consumer<Message<?>> printer = messagePrinters.getOrDefault(msg.getType(), m ->
                        logger.log(LogLevel.WARNING, "Unknown message type: " + m.getType()));
                printer.accept(msg);
            }
        }
        System.out.println();
    }

    /**
     * prints the status of all towns in the simulation
     * Includes population, railway counts, goods stockpile and goods transported.
     */
    private void printConsoleTowns() {
        System.out.println("Town Status:");
        System.out.println("------------");
        for (Town town : townManager.getTowns()) {
            int rs = countSingleTrackRailways(town);
            int rd = countDualTrackRailways(town);
            int gs = town.getGoodsStockpile();
            int gt = town.getGoodsTransportedToday();
            System.out.printf("%s p:%d rs:%d rd:%d gs:%d gt:%d%n",
                    town.getName(), town.getPopulation(), rs, rd, gs, gt);
        }
        System.out.println();
    }

    /**
     * updates the network   visualization by generating a DOT file if changes are detected
     * compare current towns and railways with the previous state
     */
    private void updateNetworkVisualization() {
        if (checkForChanges()) {
            writeDotFile();
        }
    }

    /**
     * Checks for changes in towns or railway links since the previous state
     * returns true if changes are detected and trigger a visualization update
     *
     * @return True if towns or railways have changed or false otherwise.
     */
    private boolean checkForChanges() {
        Set<String> currentTowns = new HashSet<>();
        for (Town town : townManager.getTowns()) {
            currentTowns.add(town.getName());
        }

        List<String> currentRailways = new ArrayList<>();
        for (RailwayLink link : railwayManager.getRailwayLinks()) {
            String state = link.getStateName();
            String town1 = link.getTown1().getName();
            String town2 = link.getTown2().getName();
            String edge = town1.compareTo(town2) < 0 ? town1 + "--" + town2 + ":" + state
                    : town2 + "--" + town1 + ":" + state;
            currentRailways.add(edge);
        }

        boolean townsChanged = !currentTowns.equals(previousTowns);
        boolean railwaysChanged = !currentRailways.equals(previousRailways);

        if (townsChanged || railwaysChanged) {
            logger.log(LogLevel.DEBUG, "Changes detected: townsChanged=" + townsChanged + ", railwaysChanged=" + railwaysChanged);
        }

        return townsChanged || railwaysChanged;
    }

    /**
     * update the previous state of towns and railways for change  detection
     * stores the current town names  aand railway links for comparison in the next cycle
     */
    private void updatePreviousState() {
        previousTowns.clear();
        for (Town town : townManager.getTowns()) {
            previousTowns.add(town.getName());
        }

        previousRailways.clear();
        for (RailwayLink link : railwayManager.getRailwayLinks()) {
            String state = link.getStateName();
            String town1 = link.getTown1().getName();
            String town2 = link.getTown2().getName();
            String edge = town1.compareTo(town2) < 0 ? town1 + "--" + town2 + ":" + state
                    : town2 + "--" + town1 + ":" + state;
            previousRailways.add(edge);
        }
    }

    /**
     * writes the network visualization to a DOT file
     * Generates a graph representation of towns, railway links and handling errors appropriately.
     */
    private void writeDotFile() {
        try (FileWriter writer = new FileWriter("simoutput.dot")) {
            writer.write(generateDotContent());
            logger.log(LogLevel.DEBUG, "Wrote simoutput.dot successfully");
        } catch (IOException e) {
            logger.log(LogLevel.ERROR, "Failed to write simoutput.dot: " + e.getMessage());
        }
    }

    /**
     * generates the content for the DOT file representing the railway network
     * includes  towns as nodes  and railway links as edges with appropriate styling
     *
     * @return a string containing the DOT file content.
     */
    private String generateDotContent() {
        StringBuilder dot = new StringBuilder("graph Towns {\n");

        for (Town town : townManager.getTowns()) {
            dot.append("    ").append(town.getName()).append("\n");
        }
        dot.append("\n");

        for (RailwayLink link : railwayManager.getRailwayLinks()) {
            String town1 = link.getTown1().getName();
            String town2 = link.getTown2().getName();
            String state = link.getStateName().trim();
            String edge;

            switch (state) {
                case "Under-Construction":
                    edge = String.format("    %s -- %s [style=\"dashed\"]\n", town1, town2);
                    break;
                case "Single-Track":
                    edge = String.format("    %s -- %s\n", town1, town2);
                    break;
                case "Under-Duplication":
                    edge = String.format("    %s -- %s [style=\"dashed\",color=\"black:black\"]\n", town1, town2);
                    break;
                case "Dual-Track":
                    edge = String.format("    %s -- %s [color=\"black:black\"]\n", town1, town2);
                    break;
                default:
                    logger.log(LogLevel.WARNING, "Unknown railway state: " + state);
                    continue;
            }
            dot.append(edge);
        }

        dot.append("}\n");
        return dot.toString();
    }

    /**
     * Count  the number of single track railway links connected to a town
     *
     * @param town The town to count single track railways for
     * @return the number of Single track railway links
     */
    private int countSingleTrackRailways(Town town) {
        return (int) railwayManager.getRailwayLinks().stream()
                .filter(link -> (link.getTown1().equals(town) || link.getTown2().equals(town))
                        && link.getStateName().equals("Single-Track"))
                .count();
    }

    /**
     * Count  thee number of dual track railway links connect to a town
     *
     * @param town the town to count Dual track railways for
     * @return the number of dual track railway links
     */
    private int countDualTrackRailways(Town town) {
        return (int) railwayManager.getRailwayLinks().stream()
                .filter(link -> (link.getTown1().equals(town) || link.getTown2().equals(town))
                        && link.getStateName().equals("Dual-Track"))
                .count();
    }
}