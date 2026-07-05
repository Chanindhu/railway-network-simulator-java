package edu.curtin.app.core;

import edu.curtin.app.TownsInput;
import edu.curtin.app.messages.Message;
import edu.curtin.app.messages.TownData;
import edu.curtin.app.messages.RailwayData;

/**
 * Provides message parsing for railway network simulation input
 * Converts raw input strings from a TownsInput source into structured Message objects,
 * supporting events like town founding, population changes, and railway updates.
 * Implemented TownsInputProvider interface for   integration with the simulation core.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file SimpleTownsInputProvider.java
 * @since 2025-05-27
 */
public class SimpleTownsInputProvider implements TownsInputProvider {
    /** The input source providing raw message strings. */
    private final TownsInput townsInput;

    /**
     * Constructs a SimpleTownsInputProvider with a specified input source.
     *
     * @param townsInput The input source for raw message strings.
     */
    public SimpleTownsInputProvider(TownsInput townsInput) {
        this.townsInput = townsInput;
    }

    /**
     * Constructs a SimpleTownsInputProvider with a default TownsInput source.
     * Presented to support systems that expect classes to have a no-arg constructor.
     */
    public SimpleTownsInputProvider() {
        this(new TownsInput());
    }

    /**
     * Retrieves   and parses the next message from the input source.
     * Converts the raw Input string into a Message object based on the message type.
     * Returns an invalid message if the input is faulty or unrecognized.
     *
     * @return A Message object containing parsed data or null if no more input is available.
     */
    @Override
    public Message<?> nextMessage() {
        // Retrieve the next raw message from the input source.
        String rawMessage = townsInput.nextMessage();
        if (rawMessage == null) {
            return null; // No more input available.
        }

        // Split the message into parts and validate its structure.
        String[] parts = rawMessage.split(" ");
        if (parts.length != 3) {
            return new Message<>("invalid", null); 
        }

        String type = parts[0];
        try {
            switch (type) {
                case "town-founding":
                case "town-population":
                    // Parse population and create a town-related message.
                    int population = Integer.parseInt(parts[2]);
                    return new Message<>(type, new TownData(parts[1], population));
                case "railway-construction":
                case "railway-duplication":
                    // Create a railway-related message with two town names.
                    return new Message<>(type, new RailwayData(parts[1], parts[2]));
                default:
                    return new Message<>("invalid", null); // Unrecognized message type
            }
        } catch (NumberFormatException e) {
            return new Message<>("invalid", null); // Invalid population number format
        }
    }
}