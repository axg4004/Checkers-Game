package com.webcheckers.appl;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A pure fabrication object used as an interface between the Post*AsyncRoute
 * route components and the rest of the application.
 */
public class AsyncServices {

    //
    // Constants
    //

    //
    // Attributes
    //
    private final PlayerLobby playerLobby;
    private final GameCenter gameCenter;

    //
    // Constructor
    //
    public AsyncServices(final PlayerLobby playerLobby, final GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");

        this.playerLobby = playerLobby;
        this.gameCenter = gameCenter;
    }

    //
    // Methods
    //

    /**
     * Start the asynchronous play request for all the synchronous games a
     * player is currently in.
     *
     * @param sessionID
     *      The session ID of the player requesting the transition to
     *      asynchronous mode.
     */
    public void startAsync(String sessionID) {
        // Get all of the player's games
        Player requestingPlayer = playerLobby.getPlayerBySessionID(sessionID);
        ArrayList<Game> games = gameCenter.getAllGames(requestingPlayer);

        // Start the async confirmation process in each synchronous game
        for(Game game : games) {
            // We don't need to check the game's state, because this method
            // does that for us.
            game.requestAsync(requestingPlayer);
        }
    }

    /**
     * Confirm the asynchronous play request for the game the player is
     * currently looking at.
     *
     * @param sessionID
     *      The session ID of the player confirming the transition to
     *      asynchronous mode.
     */
    public void confirmAsync(String sessionID) {
        Player confirmingPlayer = playerLobby.getPlayerBySessionID(sessionID);
        Game game = gameCenter.getGame(confirmingPlayer);
        game.acceptAsync();
    }

    /**
     * Deny the asynchronous play request for the game the player is currently
     * looking at.
     *
     * @param sessionID
     *      The session ID of the player denying the transition to asynchronous
     *      mode.
     */
    public void denyAsync(String sessionID) {
        Player denyingPlayer = playerLobby.getPlayerBySessionID(sessionID);
        Game game = gameCenter.getGame(denyingPlayer);
        game.rejectAsync();
    }

    /**
     * Check on an asynchronous request to see how many opponents still need to
     * respond to the player's request.
     *
     * @param player the player making the async request
     * @return the names of the opponents that still need to respond
     */
    public HashMap<String, Game.State> waitingForResponses(Player player) {
        // Get all of the player's games
        ArrayList<Game> games = gameCenter.getAllGames(player);

        HashMap<String, Game.State> opponents = new HashMap<>();

        // Check each game state
        for(Game game : games) {
            switch(game.getState()) {
                // ENDED and *ACTIVE states should not be included
                case ENDED:
                    break;
                case ACTIVE:
                    break;
                case ASYNC_ACTIVE:
                    break;
                default: // All other states represent an async req. response
                    if (game.isAsyncRequester(player)) {
                        opponents.put(game.getOpponentOf(player).getName(), game.getState());
                    }
            }
        }
        return opponents;
    }

    /**
     * Finish a player's asynchronous mode request by applying the rejections
     * or denials from all of their games.
     *
     * @param player the player who made an asynchronous mode request
     */
    public void finishAsyncRequest(Player player) {
        for(Game game : gameCenter.getAllGames(player)) {
            if (game.isAsyncRequester(player)) {
                game.asyncRequestCompleted();
            }
        }
    }
}
