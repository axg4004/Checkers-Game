package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.TurnController;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.ui.Message.MessageType;
import java.util.logging.Logger;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

public class PostSubmitTurnRoute implements Route {
    public static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    //
    // Constants
    //
    static final String ERROR_MESSAGE = "Submitted turn is incomplete";
    static final String SUCCESS_MESSAGE = "Turn submitted";

    //
    // Attributes
    //
    private final PlayerLobby playerLobby;

    //
    // Constructor
    //

    public PostSubmitTurnRoute(PlayerLobby playerLobby) {

        Objects.requireNonNull(playerLobby, "playerLobby must not be null");

        this.playerLobby = playerLobby;
    }

    //
    // Methods
    //
    @Override
    public Object handle(Request request, Response response) {
        LOG.fine("PostSubmitTurnRoute invoked");
        final Session httpSession = request.session();
        final String sessionID = httpSession.id();

        Player thisPlayer = playerLobby.getPlayerBySessionID(sessionID);

        if (thisPlayer == null) {
            response.redirect("/");
            halt();
            return null;
        }

        Game game = playerLobby.getGame(thisPlayer);
        TurnController turnController = new TurnController(playerLobby);
        if (game.madeKing) {
            game.madeKing = false;
            game.applyTurnMoves();
            return turnController.MessageFromModeltoUI(new Message(SUCCESS_MESSAGE, MessageType.info));
        }
        if (game.movesLeft()) {
            return turnController.MessageFromModeltoUI(new Message(ERROR_MESSAGE, MessageType.error));
        } else {
            game.applyTurnMoves();
            return turnController.MessageFromModeltoUI(new Message(SUCCESS_MESSAGE, MessageType.info));
        }
    }
}
