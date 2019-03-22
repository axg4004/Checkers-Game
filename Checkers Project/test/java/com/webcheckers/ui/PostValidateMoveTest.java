package com.webcheckers.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.TurnController;
import com.webcheckers.appl.TurnControllerTest;
import com.webcheckers.ui.Message.MessageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

@Tag("UI-Tier")
public class PostValidateMoveTest {

    // Constants
    private static final String MSG_BODY = "test";
    private static final String JSON_MOVE_PASS = "{\"start\":{\"row\":5,\"cell\":0},\"end\":{\"row\":4,\"cell\":1}}";
    private static final String JSON_SERVER_RES = String.format("{\"text\":\"%s\",\"type\":\"info\"}", MSG_BODY);
    private static final String TEST_ID = "1";

    // Component Under Test
    PostValidateMoveRoute CuT;

    // Mocked Objects
    Request request;
    Response response;
    PlayerLobby playerLobby;
    TurnController turnController;

    @Test
    public void testConstructor() {
        playerLobby = mock(PlayerLobby.class);
        turnController = mock(TurnController.class);
        CuT = new PostValidateMoveRoute(playerLobby, turnController);

        Assertions.assertEquals(playerLobby, CuT.playerLobby);
    }

    @Test
    public void testHandle() {
        Message msg = new Message(MSG_BODY, MessageType.info);
        Session session = mock(Session.class);
        response = mock(Response.class);
        request = mock(Request.class);
        turnController = mock(TurnController.class);
        playerLobby = mock(PlayerLobby.class);
        when(request.session()).thenReturn(session);
        when(request.session().id()).thenReturn(TEST_ID);
        when(request.body()).thenReturn(JSON_MOVE_PASS);
        when(turnController.handleValidation(JSON_MOVE_PASS, TEST_ID)).thenReturn(msg);
        when(turnController.MessageFromModeltoUI(msg)).thenReturn(MSG_BODY);

        CuT = new PostValidateMoveRoute(playerLobby, turnController);
        Object serv_response = CuT.handle(request, response);
        Assertions.assertEquals(MSG_BODY, serv_response);
    }


}
