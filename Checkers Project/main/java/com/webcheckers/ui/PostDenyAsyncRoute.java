package com.webcheckers.ui;

import com.webcheckers.appl.AsyncServices;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostDenyAsyncRoute implements Route {

    //
    // Constants
    //

    //
    // Attributes
    //
    private static final Logger LOG = Logger.getLogger(PostDenyAsyncRoute.class.getName());
    private final AsyncServices asyncServices;

    //
    // Constructor
    //

    public PostDenyAsyncRoute(final AsyncServices asyncServices) {
        // validation
        Objects.requireNonNull(asyncServices, "asyncServices must not be null");

        this.asyncServices = asyncServices;

        LOG.config("PostDenyAsyncRoute is initialized.");
    }

    //
    // Methods
    //

    /**
     * Invoked when a request is made on this route's corresponding path e.g. '/hello'
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * @return The content to be set in the response
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostDenyAsyncRoute is invoked.");
        this.asyncServices.denyAsync(request.session().id());
        response.redirect("/game");
        halt();
        return null;
    }

}
