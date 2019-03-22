package com.webcheckers.appl;

import com.webcheckers.appl.TurnController;
import com.webcheckers.model.*;
import com.webcheckers.ui.BoardView;
import com.webcheckers.ui.Message;
import com.webcheckers.ui.Message.MessageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

@Tag("Application-Tier")
public class TurnControllerTest {

    private static final String MESSAGE_BODY_STR = "testMessage";
    private static final String MESSAGE_TYPE_STR = "info";
    private static final String SIMPLE_MOVE_JSON = "{\"start\":{\"row\":5,\"cell\":0},\"end\":{\"row\":4,\"cell\":1}}";
    private static final String JUMP_MOVE_JSON = "{\"start\":{\"row\":5,\"cell\":0},\"end\":{\"row\":3,\"cell\":2}}";
    private static final String SIMPLE_MOVE_JSON_FAIL = "{\"start\":{\"row\":5,\"cell\":0},\"end\":{\"row\":4,\"cell\":3}}";

    private static final String TEST_RED_NAME = "red";
    private static final String TEST_WHITE_NAME = "white";
    private static final String TEST_RED_ID = "1";
    private static final String TEST_WHITE_ID= "2";

    // Component Under Test
    private TurnController CuT;

    // Friendly Objects
    PlayerLobby playerLobby;

    // Mocked Objects
    Game testGame;

    @BeforeEach
    public void setup() {
        playerLobby = new PlayerLobby();
        CuT = new TurnController(playerLobby);
    }

    @AfterEach
    public void tearDown() {
        playerLobby = null;
        CuT = null;
    }

    @Test public void testConstructor() {
        Assertions.assertNotNull(CuT.builder);
        Assertions.assertNotNull(CuT.playerLobby);
    }

    @Test
    public void testMesageFromModelToUI() {
        Message testMessage = new Message(MESSAGE_BODY_STR, MessageType.info);
        String json = CuT.MessageFromModeltoUI(testMessage);
        String expectedJson = String.format("{\"text\":\"%s\",\"type\":\"%s\"}", MESSAGE_BODY_STR, MESSAGE_TYPE_STR);
        Assertions.assertEquals(expectedJson, json);
    }

    @Test
    public void testMoveUItoModelSimpleMove() {
        SimpleMove expected = new SimpleMove(new Position(5,0), new Position(4, 1));
        Move actual = CuT.MovefromUItoModel(SIMPLE_MOVE_JSON);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMoveUItoModelJumpMove() {
        SimpleMove expected = new SimpleMove(new Position(5,0), new Position(3, 2));
        Move actual = CuT.MovefromUItoModel(JUMP_MOVE_JSON);
        Assertions.assertEquals(expected, actual);
    }

    /**
    @Test
    public void handleValidationShouldPassWithNoMovesMade() {
        setupHandleValidation(false);
        when(testGame.getPieceColor(any())).thenReturn(Piece.PColor.red);
        Message actual = CuT.handleValidation(SIMPLE_MOVE_JSON, TEST_RED_ID);
        Assertions.assertEquals(actual.getType(), MessageType.info);
    }
     */

    @Test
    public void handleValidationShouldFailWithNoMovesMade() {
        setupHandleValidation(false);
        Message actual = CuT.handleValidation(SIMPLE_MOVE_JSON_FAIL, TEST_RED_ID);
        Assertions.assertEquals(actual.getType(), MessageType.error);
    }

    /*
    @Test
    public void handleValidationShouldFailMultipleSimpleMoves() {
        setupHandleValidation(true);
        Message actual = CuT.handleValidation(SIMPLE_MOVE_JSON, TEST_RED_ID);
        Assertions.assertEquals(actual.getType(), MessageType.error);
    }
    */

    private void setupHandleValidation(boolean hasMultipleMoves) {
        Player red = new Player(TEST_RED_NAME, TEST_RED_ID);
        Board testBoard = new Board();
        testGame = mock(Game.class);
        when(testGame.hasMovesInCurrentTurn()).thenReturn(false);
        when(testGame.getBoard()).thenReturn(testBoard);
        playerLobby = mock(PlayerLobby.class);
        when(playerLobby.getPlayerBySessionID(TEST_RED_ID)).thenReturn(red);
        when(playerLobby.getGame(red)).thenReturn(testGame);
        CuT = new TurnController(playerLobby);
        if (hasMultipleMoves) {
            when(testGame.hasMovesInCurrentTurn()).thenReturn(true);
            when(testGame.getLastMoveMade()).thenReturn(mock(SimpleMove.class));
        }
    }

}
