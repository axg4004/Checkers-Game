package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.ui.Message.MessageType;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public class PostResignGameRoute implements Route {
    private static Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());

    static final String RESIGNATION_MESSAGE = "You have resigned and lost the game.";

    //
    // Attributes
    //
    private final PlayerLobby playerLobby;
    private final Gson gson;

    public PostResignGameRoute(PlayerLobby playerLobby, Gson gson) {
        this.playerLobby = playerLobby;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final String sessionID = httpSession.id();

        Player resigningPlayer = playerLobby.getPlayerBySessionID(sessionID);
        Game gameToResignFrom = playerLobby.getGame(resigningPlayer);
        playerLobby.resignPlayerFromGame(gameToResignFrom, resigningPlayer);
        LOG.fine(String.format("%s invoked PostResignRoute.", resigningPlayer.getName()));

        Message resignation = new Message(RESIGNATION_MESSAGE, MessageType.info);
        String jsonResignation = gson.toJson(resignation, Message.class);
        return jsonResignation;
    }

}
