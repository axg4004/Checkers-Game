package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.ui.Message.MessageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import spark.Request;
import spark.Response;
import spark.Session;

@Tag("UI-Tier")
public class PostResignGameRouteTest {

    private static final String MOCK_SESSION_ID1 = "1";
    private static final String MOCK_SESSION_ID2 = "2";

    // Component Under Test
    private PostResignGameRoute CuT;

    // Attributes used for testing
    Request mockRequest;
    Response mockResponse;
    Session mockSession;
    PlayerLobby playerLobby;
    Gson gson = new Gson();

    @BeforeEach
    private void setup() {
        // mocked objects
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        mockSession = mock(Session.class);
        when(mockRequest.session()).thenReturn(mockSession);
        when(mockSession.id()).thenReturn(MOCK_SESSION_ID1);
        // friendly objects
        playerLobby = new PlayerLobby();
    }

    @AfterEach
    private void tearDown() {
        mockRequest = null;
        mockResponse = null;
        mockSession = null;
        playerLobby = null;
    }

    @Test
    public void testConstructor() {
        CuT = new PostResignGameRoute(playerLobby, gson);
        assertNotNull(playerLobby);
        assertNotNull(gson);
    }

    @Test
    public void testHandleCorrectlyResignsPlayers() {
        playerLobby.signIn("redPlayer", MOCK_SESSION_ID1);
        playerLobby.signIn("redWhite", MOCK_SESSION_ID2);
        Player red = playerLobby.getPlayerBySessionID(MOCK_SESSION_ID1);
        Player white = playerLobby.getPlayerBySessionID(MOCK_SESSION_ID2);
        playerLobby.startGame(red, white);

        CuT = new PostResignGameRoute(playerLobby, gson);
        String expected = gson.toJson(new Message(PostResignGameRoute.RESIGNATION_MESSAGE, MessageType.info));
        Object actual = CuT.handle(mockRequest, mockResponse);
        assertEquals(expected, actual);

    }

}
