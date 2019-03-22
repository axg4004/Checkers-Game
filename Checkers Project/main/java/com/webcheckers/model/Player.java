package com.webcheckers.model;

import com.webcheckers.ui.PlayerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 */
public class Player {
    private String sessionID;
    private final String name;
    private int gameID;
    private ArrayList<Integer> currentGameIDs = new ArrayList<>();
    private ArrayList<String> currentOpponentNames = new ArrayList<>();

    /**
     * Create a new signed-out player
     *
     * @param name The name of the new player
     */
    public Player(String name) {
        this.name = name;
        this.sessionID = null;
        this.gameID = -1;
    }

    /**
     * Create a new signed-in player
     *
     * @param name      The name of the new player
     * @param sessionID The session ID to associate with the player
     */
    public Player(String name, String sessionID) {
        this.name = name;
        this.sessionID = sessionID;
        this.gameID = -1;
    }

    /**
     * Get the player's name
     *
     * @return The player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if a player is signed in with a given session id
     * @param sessionID the sessionID to check against
     * @return true if the sessionID matches this players sessionID
     */
    public boolean isSignedIn(String sessionID) {
        // Make sure a NullPointerException isn't thrown if the player is
        // signed out
        return this.sessionID != null && this.sessionID.equals(sessionID);
    }

    /**
     * Check if the player is currently signed in
     *
     * @return {@code true} if the player is signed in, {@code false} if the
     * player is not signed in
     */
    public boolean isSignedIn() {
        return this.sessionID != null;
    }

    /**
     * Sign in the player and associate a given session ID with the player
     *
     * @param sessionID The session ID to associate with the player
     */
    public void signIn(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * Sign out the player.
     */
    public void signOut() {
        this.sessionID = null;
    }

    /**
     * Construct a PlayerView representation of this player for use by
     * templates
     * @return a PlayerView object that represents this player
     */
    public PlayerView getPlayerView() {
        return new PlayerView(this.name);
    }

    /**
     * Change the ID of the game that the player should be looking at
     * @param gameID ID of the requested game
     */
    public void changeGame(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
    }

    public ArrayList<Integer> getCurrentGameIDs() {
        return this.currentGameIDs;
    }

    public ArrayList<String> getCurrentOpponentNames() {
        return this.currentOpponentNames;
    }

    public void addCurrentGameID(int gameID) {
        this.currentGameIDs.add(gameID);
    }

    public void addCurrentOpponentName(String name) {
        this.currentOpponentNames.add(name);
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void removeCurrentGame(Game game) {
        int id = game.getGameID();
        int index = currentGameIDs.indexOf(id);
        currentGameIDs.remove(index);
        currentOpponentNames.remove(index);
    }


//    public void addGame(int gameID, String opponentName) {
//        this.currentGames.put(gameID, opponentName);
//    }

//    public ArrayList<Integer> getCurrentGameIDs() {
//        ArrayList<Integer> IDs = new ArrayList<>();
//        for (Integer i : currentGames.keySet()) {
//            IDs.add(i);
//        }
//        return IDs;
//    }
//
//    public ArrayList<String> getCurrentGameOpponentNames() {
//        ArrayList<String> names = new ArrayList<>();
//        for (Integer key : currentGames.keySet()) {
//            String name = currentGames.get(key);
//            names.add(name);
//        }
//        return names;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Player)) return false;
        final Player that = (Player) obj;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Player{%s, %s", name, sessionID);
    }
}
