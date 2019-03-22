package com.webcheckers.model;

import com.webcheckers.model.Game.Turn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Model-Tier")
public class SimpleMoveTest {

    private static final int START_ROW = 1;
    private static final int START_COL = 2;
    private static final int END_ROW_WHITE= 2;
    private static final int END_COL_WHITE= 3;
    private static final int END_ROW_RED= 0;
    private static final int END_COL_RED= 3;
    private static final String TEST_NAME_RED = "red";
    private static final String TEST_NAME_WHITE= "white";

    // Compoenent under test
    private SimpleMove CuT;

    // Friendly Objects
    private Position testStart;
    private Position testEnd;

    // Mocked Objects for testing
    private Game testGame;
    private Board testBoard;

    @BeforeEach
    public void setup() {
       // CuT = new SimpleMove(testStart, testEnd);
       testBoard = mock(Board.class);
       testGame = mock(Game.class);
       // when(testGame.getBoard()).thenReturn(testBoard);
    }

    @AfterEach
    public void tearDown() {
       // CuT = null;
       // testStart = null;
       // testEnd = null;
       // testGame = null;
    }

    @Test
    public void testExecuteMoveSuccess() {
        // setup
        testStart = new Position(START_ROW, START_COL);
        testEnd = new Position(END_ROW_RED, END_COL_RED);
        testGame = new Game(new Player(TEST_NAME_RED), new Player(TEST_NAME_WHITE), Turn.RED, testBoard, 1);
        when(testBoard.spaceIsValid(testEnd)).thenReturn(true);
        when(testBoard.getSpace(testStart)).thenReturn(new Space(Space.SpColor.black, new Piece(Piece.PColor.red, Piece.PType.single)));

        // Initialize CuT and start testing
        CuT = new SimpleMove(testStart, testEnd);
        Turn previousTurn = testGame.getTurn();
        boolean result = CuT.executeMove(testGame);
        Assertions.assertTrue(result);
        // Assertions.assertNotEquals(testGame.getTurn(), previousTurn);  This shouldn't actually change
    }

    @Test
    public void testExecuteMoveFailure() {

        // Setup
        testStart = new Position(START_ROW, START_COL);
        testEnd = new Position(START_ROW, START_COL);
        when(testGame.getBoard()).thenReturn(testBoard);

        // Iniitialze cut and start testing
        CuT = new SimpleMove(testStart, testEnd);
        boolean result = CuT.executeMove(testGame);
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldFailValidateMoveEndSpaceFilled() {
        testStart = new Position(START_ROW, START_COL);
        testEnd = new Position(START_ROW, START_COL);
        when(testGame.getBoard()).thenReturn(testBoard);
        when(testBoard.spaceIsValid(testEnd)).thenReturn(false);
        // Initialize CuT and start testing
        CuT = new SimpleMove(testStart, testEnd);
        Assertions.assertFalse(CuT.validateMove(testGame));
    }

    /**
    @Test
    public void shouldPassWhitePlayerValidateMove() {
        //TODO: test upper left of white
        // set valid starting spot
        testStart = new Position(START_ROW, START_COL);
        // set valid ending spot using white's movement (upper right)
        testEnd = new Position(END_ROW_WHITE, END_COL_WHITE);
        when(testBoard.spaceIsValid(testEnd)).thenReturn(true);
        when(testGame.getBoard()).thenReturn(testBoard);
        when(testGame.getTurn()).thenReturn(Turn.WHITE);
        when(testGame.getPieceColor(testStart)).thenReturn(Piece.PColor.white);
        // create a new move to update these values
        CuT = new SimpleMove(testStart, testEnd);
        Assertions.assertTrue(CuT.validateMove(testGame));
    }

    @Test
    public void shouldFailWhitePlayerValidateMove() {
        // setup
        testStart = new Position(START_ROW, START_COL);
        testEnd = new Position(END_ROW_RED, END_COL_RED);
        when(testGame.getPieceColor(testStart)).thenReturn(Piece.PColor.white);
        when(testBoard.spaceIsValid(testEnd)).thenReturn(true);
        when(testGame.getBoard()).thenReturn(testBoard);
        when(testGame.getTurn()).thenReturn(Turn.WHITE);

        // Initialize CuT and start testing
        CuT = new SimpleMove(testStart, testEnd);
        Assertions.assertFalse(CuT.validateMove(testGame));
    }

    @Test
    public void shouldPassRedPlayerValidateMove() {
        // setup
        //TODO: test left side of the move
        testStart = new Position(START_ROW, START_COL);
        testEnd = new Position(END_ROW_RED, END_COL_RED);
        when(testBoard.spaceIsValid(testEnd)).thenReturn(true);
        when(testGame.getBoard()).thenReturn(testBoard);
        when(testGame.getTurn()).thenReturn(Turn.RED);

        // Initiialize CuT and run test
        CuT = new SimpleMove(testStart, testEnd);
        Assertions.assertTrue(CuT.validateMove(testGame));
    }
    @Test
    public void shouldFailRedPlayerValidateMove() {
        testStart = new Position(START_ROW, START_COL);
        testEnd = new Position(END_ROW_WHITE, END_COL_WHITE);
        when(testBoard.spaceIsValid(testEnd)).thenReturn(true);
        when(testGame.getBoard()).thenReturn(testBoard);
        when(testGame.getTurn()).thenReturn(Turn.RED);

        CuT = new SimpleMove(testStart, testEnd);
        Assertions.assertFalse(CuT.validateMove(testGame));
    }

    @Test
    public void shouldFailWhenJumpMove() {
        testStart = new Position(5, 0);
        testEnd = new Position(3, 2);
        when(testBoard.spaceIsValid(testEnd)).thenReturn(true);
        when(testGame.getBoard()).thenReturn(testBoard);
        when(testGame.getTurn()).thenReturn(Turn.RED);

        CuT = new SimpleMove(testStart, testEnd);
        Assertions.assertFalse(CuT.validateMove(testGame));

    }
    */
}
