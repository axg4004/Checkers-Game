package com.webcheckers.ui;

import com.webcheckers.appl.TurnController;
import com.webcheckers.appl.PlayerLobby;
import java.util.logging.Logger;
import java.util.Objects;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public class PostValidateMoveRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    //
    // Constants
    //

    //
    // Attributes
    //
    final PlayerLobby playerLobby;
    final TurnController turnController;

    /**
     * Create the Spark Route (UI controller) for the {@code POST /signin} HTTP
     * request.
     *
     * @param playerLobby The model that handles player-tracking
     */
    public PostValidateMoveRoute(PlayerLobby playerLobby, TurnController turnController) {
        // Validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");

        this.playerLobby = playerLobby;
        this.turnController = turnController;

        LOG.config("PostValidateMoveRoute is initialized.");
    }


    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final String moveToBeValidated = request.body();

        Message res = turnController.handleValidation(moveToBeValidated, httpSession.id());
        return turnController.MessageFromModeltoUI(res);

    }
}
