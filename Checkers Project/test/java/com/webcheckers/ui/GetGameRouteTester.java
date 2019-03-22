package com.webcheckers.ui;

import com.webcheckers.appl.AsyncServices;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.*;

@Tag("UI-Tier")
public class GetGameRouteTester {

    //
    // Constants
    //
    private static final String SESSION_ID = "12345";
    private static final String GUEST_SESSION_ID = "54321";
    private static final String OPPONENT_USERNAME = "other";
    private static final String MY_USERNAME = "jimmy";
    private static final String WINNER_ATTR_VAL_NO_WINNER = "NO_WINNER";

    //
    // Attributes
    //
    private Session session;
    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private TemplateEngine templateEngine;
    private GetGameRoute CuT;
    private Player thisPlayer;
    private Player otherPlayer;
    private Game game;
    private AsyncServices asyncServices;

    //
    // Setup
    //
    @BeforeEach
    public void setUp() {
        // Set up session
        session = mock(Session.class);
        when(session.id()).thenReturn(SESSION_ID);

        // Set up request
        request = mock(Request.class);
        when(request.session()).thenReturn(session);

        // Set up response
        response = mock(Response.class);

        // Set up template engine
        templateEngine = mock(TemplateEngine.class);

        // Set up the user making the request
        thisPlayer = mock(Player.class);
        when(thisPlayer.getName()).thenReturn(MY_USERNAME);

        // Set up the other user, i.e. the opponent
        otherPlayer = mock(Player.class);

        // Set up player lobby
        playerLobby = mock(PlayerLobby.class);
        when(playerLobby.getPlayerBySessionID(SESSION_ID)).thenReturn(thisPlayer);

        // Set up game
        game = mock(Game.class);
        when(game.getTurn()).thenReturn(Game.Turn.RED);
        when(game.getState()).thenReturn(Game.State.ACTIVE);
        when(game.getWinningPlayerName()).thenReturn(MY_USERNAME);

        // Set up async services
        asyncServices = mock(AsyncServices.class);

        // Set up the route component
        CuT = new GetGameRoute(playerLobby, templateEngine, asyncServices);
    }

    //
    // Tests
    //

    /**
     * Make sure that a user can start a game with another logged-in user when
     * the other user is not currently in a game.
     */
    @Test
    public void testStartGameOpponentNotInGame() {
        // Make it look like we're starting the game
        when(request.queryParams("username")).thenReturn(OPPONENT_USERNAME);

        // Return the other player when requested
        when(playerLobby.getPlayer(OPPONENT_USERNAME)).thenReturn(otherPlayer);

        // Make it look like neither player is in a game
        when(playerLobby.getGame(thisPlayer)).thenReturn(null);
        when(playerLobby.getGame(otherPlayer)).thenReturn(null);

        // There's no winner
        when(game.getWinningPlayerName()).thenReturn(null);

        // Start the game
        when(playerLobby.startGame(thisPlayer, otherPlayer)).thenReturn(game);

        // Set correct player positions
        when(game.getWhitePlayer()).thenReturn(otherPlayer);
        when(game.getRedPlayer()).thenReturn(thisPlayer);

        // Set up template engine tester
        TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Set up the expected board view
        BoardView expected = mock(BoardView.class);
        when(playerLobby.getBoardView(thisPlayer)).thenReturn(expected);
        when(game.getBoardView(false)).thenReturn(expected);

        // Invoke test
        CuT.handle(request, response);

        // Analyze results
        // Model is a non-null map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        // Model contains the correct View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.WINNER_ATTR, WINNER_ATTR_VAL_NO_WINNER);
        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_ATTR, otherPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_ATTR, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_PLAYER_ATTR, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD_VIEW_ATTR, expected);
        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_ATTR, GetGameRoute.View.PLAY);
        testHelper.assertViewModelAttributeIsAbsent(GetGameRoute.SIGNED_IN_PLAYERS);
        testHelper.assertViewModelAttributeIsAbsent(GetGameRoute.IS_SIGNED_IN);
        testHelper.assertViewModelAttributeIsAbsent(GetGameRoute.MESSAGE_ATTR);
        // Test view name
        testHelper.assertViewName(GetGameRoute.TEMPLATE_NAME);
    }

    /**
     * Make sure that the player will be properly pulled into the game when
     * another user has started a game with them.
     */
    @Test
    public void testOtherPlayerStartedGame() {
        // Make it look like someone else has already started the game
        when(request.queryParams("username")).thenReturn(null);
        when(playerLobby.getOpponent(thisPlayer)).thenReturn(otherPlayer);

        // Make it look like neither player is in a game
        when(playerLobby.getGame(thisPlayer)).thenReturn(null);
        when(playerLobby.getGame(otherPlayer)).thenReturn(null);

        // Get the already-started game
        when(playerLobby.getGame(thisPlayer)).thenReturn(game);

        // Set correct player positions
        when(game.getWhitePlayer()).thenReturn(thisPlayer);
        when(game.getRedPlayer()).thenReturn(otherPlayer);

        // Nobody has won
        when(game.getWinningPlayerName()).thenReturn(null);

        // Set up template engine tester
        TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Set up the expected board view
        BoardView expected = mock(BoardView.class);
        when(playerLobby.getBoardView(thisPlayer)).thenReturn(expected);
        when(game.getBoardView(true)).thenReturn(expected);

        // Invoke test
        CuT.handle(request, response);

        // Analyze results
        // Model is a non-null map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        // Model contains the correct View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.WINNER_ATTR, WINNER_ATTR_VAL_NO_WINNER);
        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetGameRoute.TITLE);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_ATTR, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_ATTR, otherPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_PLAYER_ATTR, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD_VIEW_ATTR, expected);
        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_ATTR, GetGameRoute.View.PLAY);
        testHelper.assertViewModelAttributeIsAbsent(GetGameRoute.SIGNED_IN_PLAYERS);
        testHelper.assertViewModelAttributeIsAbsent(GetGameRoute.IS_SIGNED_IN);
        testHelper.assertViewModelAttributeIsAbsent(GetGameRoute.MESSAGE_ATTR);
        // Test view name
        testHelper.assertViewName(GetGameRoute.TEMPLATE_NAME);
    }

    /**
     * Make sure that a player who is not signed in will be redirected to the
     * home page.
     */
    @Test
    public void testPlayerNotSignedIn() {
        // Set up the guest session and request
        Session guestSession = mock(Session.class);
        when(guestSession.id()).thenReturn(GUEST_SESSION_ID);
        Request guestRequest = mock(Request.class);
        when(guestRequest.session()).thenReturn(guestSession);

        // Make sure we redirect
        assertThrows(spark.HaltException.class, () -> {
            CuT.handle(guestRequest, response);
        });
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Make sure that the game properly displays when a player has won the
     * game.
     */
    @Test
    public void testPlayerWon() {
        // Make it look like we're in a game
        when(playerLobby.getGame(thisPlayer)).thenReturn(game);

        // Make it look like I won
        when(game.getWinningPlayerName()).thenReturn(MY_USERNAME);

        // Set up template engine tester
        TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        // Analyze results
        // Model is a non-null map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        // Model contains the correct View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.WINNER_ATTR, MY_USERNAME);
        // Test view name
        testHelper.assertViewName(GetGameRoute.TEMPLATE_NAME);
    }

    /**
     * Make sure that the game properly displays when a player has resigned
     * from the game.
     */
    @Test
    public void testPlayerResigned() {
        // Make it look like we're in a game
        when(playerLobby.getGame(thisPlayer)).thenReturn(game);

        // Make it look like my opponent resigned
        when(game.getResigningPlayer()).thenReturn(otherPlayer);
        when(game.getWinningPlayerName()).thenReturn(MY_USERNAME);

        // Set up template engine tester
        TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        // Analyze results
        // Model is a non-null map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        // Model contains the correct View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.WINNER_ATTR, MY_USERNAME);
        // Test view name
        testHelper.assertViewName(GetGameRoute.TEMPLATE_NAME);
    }

    /**
     * Make sure the opponentNames function filters properly.
     */
    @Test
    public void testFilterOpponentNames() {
        // Create the input hashmap
        HashMap<String, Game.State> input = new HashMap<>();

        // Add opponent names
        input.put("Sally", Game.State.ACTIVE);
        input.put("Joe", Game.State.ACTIVE);
        input.put("Bob", Game.State.ACTIVE);
        input.put("Martha", Game.State.ASYNC_START);
        input.put("Amy", Game.State.ENDED);

        assertEquals(GetGameRoute.opponentNames(input, Game.State.ACTIVE), "Joe, Sally, Bob");
    }

    /**
     * Make sure the home page is rendered if the player doesn't select a
     * username or game ID when they make a GET /game request.
     */
    @Test
    public void testRenderHomePageNoUsername() {
        // Null out the two query parameters
        when(request.queryParams("username")).thenReturn(null);
        when(request.queryParams("id")).thenReturn(null);

        // Return the other player when requested
        when(playerLobby.getPlayer(OPPONENT_USERNAME)).thenReturn(otherPlayer);

        // Make it look like we aren't in a game
        when(playerLobby.getGame(thisPlayer)).thenReturn(null);

        // Set up template engine tester
        TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Spoof the signed-in players list
        ArrayList<String> onlinePlayers = new ArrayList<>();
        onlinePlayers.add("Joe");
        onlinePlayers.add("Sue");
        when(playerLobby.getSignedInPlayers()).thenReturn(onlinePlayers);

        // Invoke test
        CuT.handle(request, response);

        // Analyze results
        // Model is a non-null map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        // Model contains the correct View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.SIGNED_IN_PLAYERS, onlinePlayers);
        testHelper.assertViewModelAttribute(GetGameRoute.IS_SIGNED_IN, true);
        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        // Test view name
        testHelper.assertViewName(GetHomeRoute.TEMPLATE_NAME);
    }
}
