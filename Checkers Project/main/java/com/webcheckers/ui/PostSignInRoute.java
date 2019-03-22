package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI controller to handle POST sign in requests.
 *
 * @author <a href='mailto:sxn6296@rit.edu'>Sean Newman</a>
 */
public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    //
    // Constants
    //
    public static final String ERROR_MESSAGE = "The username you entered is either already in use, or invalid.  Please try again.";

    //
    // Attributes
    //
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) for the {@code POST /signin} HTTP
     * request.
     *
     * @param playerLobby The model that handles player-tracking
     * @param templateEngine The HTML template rendering engine
     */
    public PostSignInRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        // Validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;

        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * Invoked when a POST request is made to "/signin"
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return
     *      a redirect to the home page if the sign in was successful,
     *      otherwise the sign in page is returned with an error.
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSignInRoute is invoked.");
        boolean signInResult = playerLobby.signIn(request.queryParams("username"), request.session().id());
        if (signInResult) {
            // The user has been signed in, send them back to the home page
            response.redirect("/");
            halt();
            return null;
        } else {
            // Something failed, make them retry sign in
            Map<String, Object> vm = new HashMap<>();
            vm.put(GetSignInRoute.TITLE_ATTR, "Retry Sign-in");
            vm.put(GetSignInRoute.MESSAGE_ATTR, ERROR_MESSAGE);
            return templateEngine.render(new ModelAndView(vm, GetSignInRoute.TEMPLATE_NAME));
        }
    }
}
