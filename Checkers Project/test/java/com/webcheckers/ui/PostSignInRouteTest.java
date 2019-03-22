package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class PostSignInRouteTest {

    //
    // Constants
    //
    private static final String SESSION_ID = "12345";
    private static final String USERNAME = "test";

    //
    // Attributes
    //
    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private TemplateEngine templateEngine;
    private Session session;
    private PostSignInRoute CuT;

    //
    // Setup
    //
    @BeforeEach
    public void setUp() {
        // Set up the session
        session = mock(Session.class);
        when(session.id()).thenReturn(SESSION_ID);

        // Set up the request
        request = mock(Request.class);
        when(request.session()).thenReturn(session);
        when(request.queryParams("username")).thenReturn(USERNAME);

        // Set up the response
        response = mock(Response.class);

        // Set up the TemplateEngine
        templateEngine = mock(TemplateEngine.class);

        // Set up the PlayerLobby
        playerLobby = mock(PlayerLobby.class);

        // Set up the route component
        CuT = new PostSignInRoute(playerLobby, templateEngine);
    }

    //
    // Tests
    //

    /**
     * Make sure that a player can sign in successfully.
     */
    @Test
    public void testValidSignIn() {
        // Make it look like we signed in
        when(playerLobby.signIn(USERNAME, SESSION_ID)).thenReturn(true);

        // Make sure we redirect
        assertThrows(spark.HaltException.class, () -> {
            CuT.handle(request, response);
        });
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Make sure that invalid sign in attempts will return a properly rendered
     * template.
     */
    @Test
    public void testInvalidSignIn() {
        // Make it look like sign in failed
        when(playerLobby.signIn(USERNAME, SESSION_ID)).thenReturn(false);

        // Prepare the template engine tester
        TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke test
        CuT.handle(request, response);

        // Analyze results
        // Model is a non-null map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        // Model contains the correct View-Model data
        testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_ATTR, "Retry Sign-in");
        testHelper.assertViewModelAttribute(GetSignInRoute.MESSAGE_ATTR, PostSignInRoute.ERROR_MESSAGE);
        // Test view name
        testHelper.assertViewName(GetSignInRoute.TEMPLATE_NAME);
    }
}
