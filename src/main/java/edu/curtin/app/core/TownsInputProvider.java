package edu.curtin.app.core;

import edu.curtin.app.messages.Message;

/**
 * Defines a contract for providing input messages to the railway network simulation.
 * Implementation  supply messages representing events like town founding,
 * population changes , or railway  updates.
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file TownsInputProvider.java
 * @since 2025-05-27
 */
public interface TownsInputProvider {
    /**
     * Retrieves the next message from the input source.
     *
     * @return A Message object containing event data, or null if no more messages are availabl
     */
    Message<?> nextMessage();
}