package com.webcheckers.ui;

import static spark.Spark.halt;

import com.webcheckers.appl.PlayerLobby;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

public class GetSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //
    // Constants
    //
    static final String TITLE_ATTR = "title";
    static final String MESSAGE_ATTR = "message";
    static final String TEMPLATE_NAME = "signin.ftl";

    //
    // Attributes
    //
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    public GetSignInRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        // Validate the template engine
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;


        LOG.config("Get SignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers sign-in page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the sign-in page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");

        // retrieve the http session
        final Session httpSession = request.session();
        final String sessionID = httpSession.id();

        // set up the template for rendering
        Map<String, Object> vm = new HashMap<>();

        String usersPlayer = playerLobby.getPlayerNameBySessionID(sessionID);
        if(usersPlayer != null) {
            response.redirect("/");
            halt();
            return null;
        } else {
            vm.put(TITLE_ATTR, "Sign-in");
            vm.put(MESSAGE_ATTR, "Please enter your username to sign in.");
            return templateEngine.render(new ModelAndView(vm, TEMPLATE_NAME));
        }
    }
}
