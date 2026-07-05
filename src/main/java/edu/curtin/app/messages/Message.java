package edu.curtin.app.messages;

/**
 * represents a generic message used in the railway network simulation
 * encapsulates   message type and associated data with validation
 *
 * @author Chanindhu Bandara - curtinID: 21799509
 * @file Message.java
 * @since 2025-05-27
 */
public class Message<T> {
    private final String type;
    private final T data;

    /**
     * constructs a Message with the specified type and data
     *
     * @param type The type of the message
     * @param data The data associated with the message
     */
    public Message(String type, T data) {
        this.type = type;
        this.data = data;
    }

    /**
     * retrieves the type of the message
     *
     * @return the message type
     */
    public String getType() {
        return type;
    }

    /**
     * retrieves   data associated with the message
     *
     * @return the message data
     */
    public T getData() {
        return data;
    }


}