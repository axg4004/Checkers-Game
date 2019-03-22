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
public class GetSignInRouteTest {
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
    private GetSignInRoute CuT;

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

        // Set up the response
        response = mock(Response.class);

        // Set up the TemplateEngine
        templateEngine = mock(TemplateEngine.class);

        // Set up the PlayerLobby
        playerLobby = mock(PlayerLobby.class);

        // Set up the route component
        CuT = new GetSignInRoute(playerLobby, templateEngine);
    }

    //
    // Tests
    //

    /**
     * Check that a user which is already signed in will be redirected back to
     * the home page.
     */
    @Test
    public void testSignedInPlayerRedirect() {
        // Make it look like we're signed in
        when(playerLobby.getPlayerNameBySessionID(SESSION_ID)).thenReturn(USERNAME);

        // Make sure we redirect
        assertThrows(spark.HaltException.class, () -> {
            CuT.handle(request, response);
        });
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Check that a user which is not yet signed in will be able to sign in.
     */
    @Test
    public void testSigningIn() {
        // Make it look like we're not signed in
        when(playerLobby.getPlayerNameBySessionID(SESSION_ID)).thenReturn(null);

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
        testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_ATTR, "Sign-in");
        testHelper.assertViewModelAttribute(GetSignInRoute.MESSAGE_ATTR, "Please enter your username to sign in.");
        // Test view name
        testHelper.assertViewName(GetSignInRoute.TEMPLATE_NAME);
    }
}
