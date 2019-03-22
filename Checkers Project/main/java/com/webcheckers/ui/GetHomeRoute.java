package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;

import com.webcheckers.model.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Game;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import static com.webcheckers.ui.WebServer.GAME_URL;
import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //
    // Constants
    //
    static final String TITLE = "Welcome!";
    static final String TITLE_ATTR = "title";
    static final String SIGNED_IN_PLAYERS = "signedInPlayers";
    static final String CURRENT_GAME_IDS = "currentGameIDs";
    static final String CURRENT_GAME_OPPONENT_NAMES = "currentGameOpponentNames";
    static final String IS_SIGNED_IN = "isUserSignedIn";
    static final String NUM_SIGNED_IN = "numPlayersOnline" ;
    static final String TEMPLATE_NAME = "home.ftl";
    static final String MESSAGE_ATTR = "message";

    //
    // Attributes
    //
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param playerLobby    The backend model that will be the master model for the game
     * @param templateEngine the HTML template rendering engine
     */
    public GetHomeRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        //
        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");

        // retrieve the http session
        final Session httpSession = request.session();
        final String sessionID = httpSession.id();

        Player thisPlayer = playerLobby.getPlayerBySessionID(sessionID);


        /*
        Game game = playerLobby.getGame(playerLobby.getPlayerBySessionID(sessionID));

        if(game != null && game.getState() == Game.State.ACTIVE) {
            response.redirect(GAME_URL);
            halt();
            return null;
        }
        */

        // start building the view-model
        Map<String, Object> vm = new HashMap<>();

        vm.put(TITLE_ATTR, TITLE);
        vm.put(MESSAGE_ATTR, "");

        // if the player is signed in return the name of the player
        String usersPlayer = playerLobby.getPlayerNameBySessionID(sessionID);
        if(usersPlayer != null) {
            LOG.finer("Player is returning: " + usersPlayer);
            playerLobby.changeGame(thisPlayer, -1);

            // list of signed in players to render to the user (excluding user)
            ArrayList<String> onlinePlayers = playerLobby.getSignedInPlayers();
            onlinePlayers.remove(usersPlayer);
            vm.put(SIGNED_IN_PLAYERS, onlinePlayers);
            vm.put(CURRENT_GAME_IDS, thisPlayer.getCurrentGameIDs());
            vm.put(CURRENT_GAME_OPPONENT_NAMES, thisPlayer.getCurrentOpponentNames());
            vm.put(IS_SIGNED_IN, true);
        } else {
            LOG.finer("New, non-registered player joined");
            vm.put(IS_SIGNED_IN, false);
            vm.put(NUM_SIGNED_IN, playerLobby.getSignedInPlayers().size());
        }

        return templateEngine.render(new ModelAndView(vm, TEMPLATE_NAME));
    }

}