package com.webcheckers.ui;

import com.webcheckers.appl.AsyncServices;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.ui.Message.MessageType;
import java.util.ArrayList;
import java.util.logging.Logger;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import spark.utils.Assert;

import static spark.Spark.halt;
import static spark.Spark.secure;

public class GetGameRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
    //
    // Constants
    //
    final static String CURRENT_PLAYER_ATTR = "currentPlayer";
    final static String WHITE_PLAYER_ATTR = "whitePlayer";
    final static String RED_PLAYER_ATTR = "redPlayer";
    final static String ACTIVE_COLOR_ATTR = "activeColor";
    final static String BOARD_VIEW_ATTR = "board";
    final static String VIEW_MODE_ATTR = "viewMode";
    final static String MESSAGE_ATTR = "message";
    final static String TEMPLATE_NAME = "game.ftl";
    final static String TITLE_ATTR = "title";
    final static String TITLE = "Game";
    final static String WINNER_ATTR = "winnerName";
    final static String ASYNC_REQUEST_ATTR = "asyncRequest";
    final static String ASYNC_MODE_ATTR = "async";

    final static String SIGNED_IN_PLAYERS = "signedInPlayers";
    final static String IS_SIGNED_IN = "isUserSignedIn";

    final static String PLAYER_IN_GAME_MSG = "Requested player is already in a game. Choose another player.";
    final static String NO_USERNAME_SELECTED = "You are not in a game. You must first start a game with another player.";
    final static String PLAYER_RESIGNED_MSG = "The other player has left, you win! Please go back to home page.";
    final static String ASYNC_REQUEST = "Your opponent has requested to switch to asynchronous mode. Would you like to switch to asynchronous mode?";
    final static String WAIT_RESPONSE_MSG = "Waiting on the following opponents to respond: ";
    final static String REJECTED_MSG = "At least one of your opponents has rejected your request to switch to asynchronous mode. If you sign out, you will be automatically resigned from those games. The following opponents rejected your request: ";
    final static String APPROVED_MSG = "All of your opponents have approved your request to switch to asynchronous mode!";

    public enum View {
        PLAY, SPECTATOR, REPLAY;
    }

    //
    // Attributes
    //
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final AsyncServices asyncServices;

    //
    // Constructor
    //
    GetGameRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, final AsyncServices asyncServices) {
        //validate
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby cannot be null");
        Objects.requireNonNull(asyncServices, "asyncServices cannot be null");
        //
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.asyncServices = asyncServices;
    }

    //
    // Methods
    //

    /**
     * Helper method to return a list string (like "Joe, Bob, Chuck") of the
     * names of opponents in games of a specified state.
     *
     * @param opponents the hashmap of opponent names to game states
     * @param state the game state to filter by
     * @return the rendered list string
     */
    static String opponentNames(HashMap<String, Game.State> opponents, Game.State state) {
        ArrayList<String> opponentNames = new ArrayList<>();
        for(Map.Entry<String, Game.State> entry : opponents.entrySet()) {
            if (entry.getValue() == state) {
                opponentNames.add(entry.getKey());
            }
        }
        return String.join(", ", opponentNames);
    }

    /**
     * Helper method to check the responses to an asynchronous request across
     * all a player's games.
     *
     * @param vm the view model used to render the game page
     * @param player the current player
     */
    private void checkResponses(Map<String, Object> vm, Player player) {
        // Check how many opponents still need to respond
        HashMap<String, Game.State> opponents = asyncServices.waitingForResponses(player);

        // The string for the message
        String message;

        // Determine what message will be displayed
        if (opponents.containsValue(Game.State.ASYNC_START)) {
            // At least one person still has to respond
            message = WAIT_RESPONSE_MSG + opponentNames(opponents, Game.State.ASYNC_START);
        } else if (opponents.containsValue(Game.State.ASYNC_DENIED)) {
            // At least one person denied
            message = REJECTED_MSG + opponentNames(opponents, Game.State.ASYNC_DENIED);
            asyncServices.finishAsyncRequest(player);
        } else {
            // Everyone accepted!
            message = APPROVED_MSG;
            asyncServices.finishAsyncRequest(player);

            // Make sure the game says that you're in async mode
            vm.put(ASYNC_MODE_ATTR, true);
        }

        // Add the message to the view model
        vm.put(MESSAGE_ATTR, new Message(message, MessageType.info));
    }

    /**
     * Helper method to render the game page.
     *
     * @param game the game being rendered
     * @param player the player viewing the game
     * @return the rendered game template
     */
    private String renderGame(Game game, Player player) {
        // Template set-up
        final Map<String, Object> vm = new HashMap<>();
        String winner = "NO_WINNER";

        /*
        If the current player is the white player in the game, then flip the
        board the other way around.
         */
        boolean opposite = player.equals(game.getWhitePlayer());

        // Check whose turn it is
        Game.Turn currentTurn = game.getTurn();

        // Perform the correct checking depending on the game state
        switch(game.getState()) {
            case ASYNC_START:
                // Disable the game controls for everyone
                currentTurn = game.getOpponentTurnColor(player);
                if (!game.isAsyncRequester(player)) {
                    vm.put(MESSAGE_ATTR, new Message(ASYNC_REQUEST, MessageType.info));
                    vm.put(ASYNC_REQUEST_ATTR, true);
                } else {
                    checkResponses(vm, player);
                }
                break;
            // These two do the same thing
            case ASYNC_DENIED:
            case ASYNC_ACCEPTED:
                // Disable the game controls for everyone
                currentTurn = game.getOpponentTurnColor(player);
                if (game.isAsyncRequester(player)) {
                    checkResponses(vm, player);
                }
                break;
            case ASYNC_ACTIVE:
                vm.put(ASYNC_MODE_ATTR, true);
                // Don't break so we fall through to the rest of the *ACTIVE logic
            case ENDED:
            case ACTIVE:
                // Check if any players have won the game
                game.calculateWinningPlayer();
                if (game.getWinningPlayerName() != null) {
                    LOG.fine("Inside get winning player");
                    winner = game.getWinningPlayerName();
                    player.removeCurrentGame(game);
                    playerLobby.endGame(game);
                }

                // Check if the opponent resigned
                if (game.getResigningPlayer() != null) {
                    LOG.fine("Inside get resigning player winner: " + game.getWinningPlayerName() + ", ResigningPlayer: " + game.getResigningPlayer().getName());
                    vm.put(MESSAGE_ATTR, new Message(PLAYER_RESIGNED_MSG, MessageType.info));
                }

                // Check if the opponent signed out
                if (game.getSignedoutPlayer() != null) {
                    LOG.fine("Inside get signed out player winner: " + game.getWinningPlayerName() + ", SignedOutPlayer: " + game.getSignedoutPlayer().getName());
                    vm.put(MESSAGE_ATTR, new Message(PLAYER_RESIGNED_MSG, MessageType.info));
                }
        }

        // Set attributes
        vm.put(WINNER_ATTR, winner);
        vm.put(TITLE_ATTR, TITLE);
        vm.put(CURRENT_PLAYER_ATTR, player);
        vm.put(VIEW_MODE_ATTR, View.PLAY);
        vm.put(RED_PLAYER_ATTR, game.getRedPlayer());
        vm.put(WHITE_PLAYER_ATTR, game.getWhitePlayer());
        vm.put(ACTIVE_COLOR_ATTR, currentTurn);
        vm.put(BOARD_VIEW_ATTR, game.getBoardView(opposite));

        return templateEngine.render(new ModelAndView(vm, TEMPLATE_NAME));
    }

    private String redirectToHome(Player currentPlayer, String message) {
        // Template set-up
        Map<String, Object> vm = new HashMap<>();

        // Set up list of signed-in players
        ArrayList<String> onlinePlayers = playerLobby.getSignedInPlayers();
        onlinePlayers.remove(currentPlayer.getName());

        // Set attributes
        vm.put(SIGNED_IN_PLAYERS, onlinePlayers);
        vm.put(IS_SIGNED_IN, true);
        vm.put(TITLE_ATTR, GetHomeRoute.TITLE);
        vm.put(MESSAGE_ATTR, new Message(message, Message.MessageType.error));

        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.TEMPLATE_NAME));
    }

  /**
   * Starts a new game and brings the red and white player to the game page
   * @param request the HTTP request
   * @param response the HTTP response
   * @return a rendering of the home or game page
   */
    @Override
    public Object handle(Request request, Response response) {
        // Get the current player
        final Session httpSession = request.session();
        Player thisPlayer = playerLobby.getPlayerBySessionID(httpSession.id());

        // If there is no player, redirect to the home page
        if (thisPlayer == null) {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }

        // Get the other player
        Player opponentPlayer;// = playerLobby.getOpponent(thisPlayer);

        // Check if the players are already in a game with each other
        if (playerLobby.getGame(thisPlayer) != null) {
            return renderGame(playerLobby.getGame(thisPlayer), thisPlayer);
        }
        // Players are not in a game with each other, we are starting a new game

        // Make sure they passed a username param
        String username = request.queryParams("username");
        String id = request.queryParams("id");
        if (username == null) {
            if (id != null) {
                playerLobby.changeGame(thisPlayer, Integer.parseInt(id));
                Game game = playerLobby.getGame(thisPlayer);
                return renderGame(game, thisPlayer);

            } else {
                return redirectToHome(thisPlayer, NO_USERNAME_SELECTED);
            }

        } else {
            opponentPlayer = playerLobby.getPlayer(username);

        }


        // Start new game
        Game game = playerLobby.startGame(thisPlayer, opponentPlayer);
        return renderGame(game, thisPlayer);

    }
}
