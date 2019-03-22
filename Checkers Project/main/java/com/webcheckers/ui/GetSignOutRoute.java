package com.webcheckers.ui;

import static spark.Spark.halt;

import com.webcheckers.appl.PlayerLobby;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

/** UI Controller to GET the home page upon signing out */
public class GetSignOutRoute implements Route {
    //
    // Attributes
    //
    private PlayerLobby playerLobby;

    //
    // Constructor
    //
    public GetSignOutRoute(PlayerLobby playerLobby) {
        Objects.requireNonNull(playerLobby, "playerLobby cannot be null");

        this.playerLobby = playerLobby;
    }

    //
    // Methods
    //

    /**
     * Renders the Home page and signs the user out
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return rendered HTML for the Home page
     */
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final String sessionID = httpSession.id();

        final String playerName = playerLobby.getPlayerNameBySessionID(sessionID);
        final Player playerSO = playerLobby.getPlayer(playerName);

        // if this player is currently signed in, sign them out
        if (playerName != null) {
            playerLobby.signOutFromAllGames(playerSO);
        }
        response.redirect("/");
        halt();
        return null;
    }
}
