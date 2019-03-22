package com.webcheckers.ui;

/**
 * A special data type required by the {@code game.ftl} template for rendering
 * messages from the server to the user.
 */
public class Message {
    public enum MessageType {
        info, error
    }

    //
    // Attributes
    //
    private final String text;
    private final MessageType type;

    //
    // Constructors
    //

    /**
     * Full constructor for server-to-user messages
     * @param text the text of the message
     * @param type the message type
     */
    public Message(String text, MessageType type) {
        this.text = text;
        this.type = type;
    }

    /**
     * Helper constructor for informational messages
     * @param text the text of the informational message
     */
    public Message(String text) {
        this(text, MessageType.info);
    }

    //
    // Methods
    //
    public String getText() {
        return this.text;
    }

    public MessageType getType() {
        return this.type;
    }
}
