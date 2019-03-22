package com.webcheckers.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import spark.Request;
import spark.Response;
import spark.Session;

@Tag("UI-Tier")
public class PostCheckTurnRouteTest {

    private static final String TEST_ID = "1";
    private static final String IS_PLAYERS_TURN_JSON = "{\"text\":\"true\",\"type\":\"info\"}";
    private static final String NOT_PLAYERS_TURN_JSON = "{\"text\":\"false\",\"type\":\"info\"}";


    // Component Under Test
    private PostCheckTurnRoute CuT;

    // Cannot Mock final class
    private Gson gson;

    // Mocked objects
    private PlayerLobby playerLobby;
    private Player player;
    private Game game;
    private Session session;
    private Request request;
    private Response response;

    @BeforeEach
    public void setup(){
        playerLobby = mock(PlayerLobby.class);
        gson = new Gson();
        session = mock(Session.class);
        response = mock(Response.class);
        request = mock(Request.class);
        player = mock(Player.class);
        game = mock(Game.class);
        when(request.session()).thenReturn(session);
        when(session.id()).thenReturn(TEST_ID);
        when(playerLobby.getPlayerBySessionID(TEST_ID)).thenReturn(player);
        when(playerLobby.getGame(player)).thenReturn(game);
        when(game.getState()).thenReturn(Game.State.ACTIVE);
        CuT = new PostCheckTurnRoute(playerLobby, gson);
    }

    @AfterEach
    public void tearDown() {
        playerLobby = null;
        CuT = null;
    }

    @Test
    public void testConstructor() {
        Assertions.assertNotNull(CuT.playerLobby);
        Assertions.assertSame(playerLobby, CuT.playerLobby);
        Assertions.assertNotNull(CuT.gson);
    }

    @Test
    public void testHandleReturnTrue() {
        when(game.isPlayersTurn(player)).thenReturn(true);
        Object result = CuT.handle(request, response);
        String JSONResult = (String)result;
        Assertions.assertEquals(IS_PLAYERS_TURN_JSON, JSONResult);
    }

    @Test
    public void testHandleShouldReturnFalse() {
        when(game.isPlayersTurn(player)).thenReturn(false);
        Object result = CuT.handle(request, response);
        String JSONResult = (String)result;
        Assertions.assertEquals(NOT_PLAYERS_TURN_JSON, JSONResult);
    }
}
