package com.webcheckers.ui;

/**
 * A special data type required by the {@code game.ftl} template for rendering
 * a player.
 */
public class PlayerView {
    //
    // Attributes
    //
    public final String name;

    //
    // Constructor
    //
    public PlayerView(String name) {
        this.name = name;
    }

    //
    // Methods
    //
    public String getName() {
        return this.name;
    }
}
