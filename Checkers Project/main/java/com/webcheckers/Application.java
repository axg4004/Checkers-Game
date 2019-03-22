package com.webcheckers;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.TurnController;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.ui.WebServer;

import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;


/**
 * The entry point for the WebCheckers web application.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    //
    // Application Launch method
    //

    /**
     * Entry point for the WebCheckers web application.
     *
     * <p>
     * It wires the application components together.  This is an example
     * of <a href='https://en.wikipedia.org/wiki/Dependency_injection'>Dependency Injection</a>
     * </p>
     *
     * @param args Command line arguments; none expected.
     */
    public static void main(String[] args) {
        // initialize Logging
        try {
            ClassLoader classLoader = Application.class.getClassLoader();
            final InputStream logConfig = classLoader.getResourceAsStream("log.properties");
            LogManager.getLogManager().readConfiguration(logConfig);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not initialize log manager because: " + e.getMessage());
        }

        // The application uses FreeMarker templates to generate the HTML
        // responses sent back to the client. This will be the engine processing
        // the templates and associated data.
        final TemplateEngine templateEngine = new FreeMarkerEngine();

        // The application uses Gson to generate JSON representations of Java objects.
        // This should be used by your Ajax Routes to generate JSON for the HTTP
        // response to Ajax requests.
        final Gson gson = new Gson();

        // Create a GameCenter to keep track of the games going on
        final GameCenter gameCenter = new GameCenter();

        // Create the PlayerLobby with our GameCenter to keep track of users
        final PlayerLobby playerLobby = new PlayerLobby(gameCenter);

        // Create a turn controller so that we can handle turn translation
        // between the model and ui tiers
        final TurnController turnController = new TurnController(playerLobby);

        // inject the template engine, gson object, and player lobby into the
        // web server to assist with route view creation
        final WebServer webServer = new WebServer(templateEngine, gson, playerLobby, turnController, gameCenter);

        // inject web server into application
        final Application app = new Application(webServer);

        // start the application up
        app.initialize();
    }

    //
    // Attributes
    //

    private final WebServer webServer;

    //
    // Constructor
    //

    private Application(final WebServer webServer) {
        // validation
        Objects.requireNonNull(webServer, "webServer must not be null");
        //
        this.webServer = webServer;
    }

    //
    // Private methods
    //

    private void initialize() {
        LOG.config("WebCheckers is initializing.");

        // configure Spark and startup the Jetty web server
        webServer.initialize();

        // other applications might have additional services to configure

        LOG.config("WebCheckers initialization complete.");
    }

}