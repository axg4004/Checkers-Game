package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Model-Tier")
public class JumpMoveTest {

    //
    // Constants
    //

    //
    // Attributes
    //
    private Position start;
    private Position middle;
    private Position validEnd;
    private Position invalidEnd;
    private Game game;
    private Board board;

    //
    // Helper methods
    //
    private Position makeMockPosition(int row, int col) {
        Position mockPosition = mock(Position.class);
        when(mockPosition.getRow()).thenReturn(row);
        when(mockPosition.getCell()).thenReturn(col);
        return mockPosition;
    }

    private Space makeEmptySpace() {
        Space mockSpace = mock(Space.class);
        when(mockSpace.doesHasPiece()).thenReturn(false);
        when(mockSpace.isValid()).thenReturn(true);
        return mockSpace;
    }

    private Space makeFullSpace(Piece.PColor color) {
        Space mockSpace = mock(Space.class);
        when(mockSpace.doesHasPiece()).thenReturn(true);
        when(mockSpace.pieceInfo()).thenReturn(new Piece(color, Piece.PType.single));
        return mockSpace;
    }

    private Space makeRedSpace() {
        return makeFullSpace(Piece.PColor.red);
    }

    private Space makeWhiteSpace() {
        return makeFullSpace(Piece.PColor.white);
    }

    private void addEmptySpace(Board board, Position position) {
        Space emptySpace = makeEmptySpace();
        when(board.getSpace(position)).thenReturn(emptySpace);
        when(board.spaceIsValid(position)).thenReturn(true);
    }

    //
    // Setup
    //
    @BeforeEach
    public void setUp() {
        // Set up positions
        start = makeMockPosition(0, 0);
        middle = makeMockPosition(1, 1);
        validEnd = makeMockPosition(2, 2);
        invalidEnd = makeMockPosition(4, 4);

        // Create game
        game = mock(Game.class);
        board = mock(Board.class);
        when(game.getBoard()).thenReturn(board);
    }

    //
    // Tests
    //

    /**
     * Makes sure the constructor correctly calculates the position of the
     * piece that is being jumped.
     */
    @Test
    public void testMiddleCalculation() {
        JumpMove CuT = new JumpMove(start, validEnd);
        assertEquals(CuT.middle.row, 1, "Row of jumped position calculated incorrectly");
        assertEquals(CuT.middle.cell, 1, "Column of jumped position calculated incorrectly");
    }

    /**
     * Makes sure that a JumpMove with incorrect spacing does not pass the
     * validation check.
     */
    @Test
    public void testInvalidSpacingValidation() {
        JumpMove CuT = new JumpMove(start, invalidEnd);
        assertFalse(CuT.validateMove(game), "An invalidly-spaced move was returned as valid");
    }

    /**
     * Makes sure that the space the jump starts on actually has a piece.
     */
    @Test
    public void testNoPieceOnStartSpace() {
        // Set up an empty board
        addEmptySpace(board, start);

        JumpMove CuT = new JumpMove(start, validEnd);

        assertFalse(CuT.validateMove(game));
    }

    /**
     * Make sure that a jump move is not considered valid if the move will end
     * on an invalid space. This would only happen if the space was already
     * occupied.
     */
    @Test
    public void testEndSpaceIsInvalid() {
        // Add a space to the start
        Space startSpace = makeRedSpace();
        when(board.getSpace(start)).thenReturn(startSpace);

        // Add a space that appears to have a piece on it to the destination on
        // the board - the color doesn't matter, it just needs to be full
        Space endSpace = makeRedSpace();
        when(board.getSpace(validEnd)).thenReturn(endSpace);

        JumpMove CuT = new JumpMove(start, validEnd);

        assertFalse(CuT.validateMove(game), "A move that would end on a full or white space was returned as valid");
    }

    /**
     * Make sure that a jump move that would end out of the bounds of the
     * checkers board is not considered valid, but also that the exception is
     * properly handled.
     */
    @Test
    public void testOutOfBoundsEndPosition() {
        // Set up the start position
        Space startSpace = makeRedSpace();
        when(board.getSpace(start)).thenReturn(startSpace);

        // Set up the OOB position
        Position outOfBounds = makeMockPosition(-2, -2);
        when(board.spaceIsValid(outOfBounds)).thenThrow(new IndexOutOfBoundsException());

        JumpMove CuT = new JumpMove(start, outOfBounds);

        assertFalse(CuT.validateMove(game), "A move that would end outside of the board was returned as valid");
    }

    /**
     * Make sure that a jump move that doesn't actually jump over a piece is
     * not considered valid.
     */
    @Test
    public void testNoPieceInMiddleSpace() {
        // Make sure the destination space looks valid
        when(board.spaceIsValid(validEnd)).thenReturn(true);

        // Set up the start space
        Space startSpace = makeRedSpace();
        when(board.getSpace(start)).thenReturn(startSpace);

        // Set up the middle space
        addEmptySpace(board, middle);

        // There are no queued moves
        when(game.hasMovesInCurrentTurn()).thenReturn(false);

        JumpMove CuT = new JumpMove(start, middle, validEnd);

        assertFalse(CuT.validateMove(game), "A jump move that jumps over an empty space was returned as valid");
    }

    /**
     * Make sure that a jump move that jumps one of your own pieces is not
     * considered valid.
     */
    @Test
    public void testOwnPieceInMiddleSpace() {
        // Make sure the destination space looks valid
        when(board.spaceIsValid(validEnd)).thenReturn(true);

        // Make it look like white's turn
        when(game.getTurn()).thenReturn(Game.Turn.WHITE);

        // Add white piece at the start
        Space startSpace = makeWhiteSpace();
        when(board.getSpace(start)).thenReturn(startSpace);
        when(game.getPieceColor(start)).thenReturn(Piece.PColor.white);
        // Add white piece in the middle
        Space middleSpace = makeWhiteSpace();
        when(board.getSpace(middle)).thenReturn(middleSpace);

        JumpMove CuT = new JumpMove(start, middle, validEnd);

        assertFalse(CuT.validateMove(game), "A jump move that jumps over the player's own piece was returned as valid");
    }

    @Test
    public void testValidJumpMove() {
        // Make sure the destination space looks valid
        when(board.spaceIsValid(validEnd)).thenReturn(true);

        // Add a white space to the start
        Space startSpace = makeWhiteSpace();
        when(board.getSpace(start)).thenReturn(startSpace);

        // Add a red piece to the middle
        Space middleSpace = makeRedSpace();
        when(board.getSpace(middle)).thenReturn(middleSpace);

        JumpMove CuT = new JumpMove(start, middle, validEnd);

        assertTrue(CuT.validateMove(game));
    }

    /**
     * Make sure that backwards red moves do not count as valid.
     */
    @Test
    public void testBackwardsRedJumpMove() {
        // Make sure the destination space looks valid
        when(board.spaceIsValid(validEnd)).thenReturn(true);

        // Make it look like red's turn
        when(game.getTurn()).thenReturn(Game.Turn.RED);

        // Add a red space to the start
        Space startSpace = makeRedSpace();
        when(board.getSpace(start)).thenReturn(startSpace);
        when(game.getPieceColor(start)).thenReturn(Piece.PColor.red);

        // Add a white space to the middle
        Space middleSpace = makeWhiteSpace();
        when(board.getSpace(middle)).thenReturn(middleSpace);

        JumpMove CuT = new JumpMove(start, middle, validEnd);

        assertFalse(CuT.validateMove(game));
    }

    /**
     * Make sure that backwards white moves do not count as valid. The use of
     * the previously-defined positions is a little weird here, and I'm sorry.
     */
    @Test
    public void testBackwardsWhiteJumpMove() {
        // Make sure the destination space looks valid
        when(board.spaceIsValid(validEnd)).thenReturn(true);

        // Add a white space to the start
        Space startSpace = makeWhiteSpace();
        when(board.getSpace(validEnd)).thenReturn(startSpace);

        // Add a red space to the middle
        Space middleSpace = makeRedSpace();
        when(board.getSpace(middle)).thenReturn(middleSpace);

        JumpMove CuT = new JumpMove(validEnd, middle, start);

        assertFalse(CuT.validateMove(game));
    }

    /**
     * Make sure invalid moves aren't executed.
     */
    /**
    @Test
    public void testExecuteInvalidMove() {
        JumpMove CuT = new JumpMove(start, invalidEnd);
        when(board.getSpace(CuT.middle)).thenReturn(mock(Space.class));
        assertFalse(CuT.executeMove(game));
    }
    */

    /**
     * Make sure valid moves get executed properly.
     */
    @Test
    public void testExecuteValidMove() {
        // Make sure the destination space looks valid
        when(board.spaceIsValid(validEnd)).thenReturn(true);

        // Add a white space to the start
        Space startSpace = makeWhiteSpace();
        when(board.getSpace(start)).thenReturn(startSpace);

        // Add a red piece to the middle
        Space middleSpace = makeRedSpace();
        when(board.getSpace(middle)).thenReturn(middleSpace);

        JumpMove CuT = new JumpMove(start, middle, validEnd);

        assertTrue(CuT.executeMove(game));
    }

    /**
     * Make sure that a default starting board does not have any valid jump
     * moves for the red player.
     */

    /*
    @Test
    public void testDefaultBoardRedNoJumpMoves() {
        Game mockGame = mock(Game.class);
        Board realBoard = new Board();
        when(mockGame.getBoard()).thenReturn(realBoard);

        assertFalse(JumpMove.jumpMoveAvailable(mockGame));
    }
    */

    /**
     * Make sure that a default starting board does not have any valid jump
     * moves for the white player.
     */
    @Test
    public void testDefaultBoardWhiteNoJumpMoves() {
        Game realGame = new Game(mock(Player.class), mock(Player.class), 0);

        assertFalse(JumpMove.jumpMoveAvailable(game));
    }

    /**
    @Test
    public void testValidateMoveWithMultiMoveTurn() {
        when(board.spaceIsValid(validEnd)).thenReturn(true);

        // Add a white space to the start
        Space startSpace = makeWhiteSpace();
        when(board.getSpace(start)).thenReturn(startSpace);
        when(startSpace.doesHasPiece()).thenReturn(false);

        // Add a red piece to the middle
        Space middleSpace = makeRedSpace();
        when(board.getSpace(middle)).thenReturn(middleSpace);

        Move mockPreviousMove = mock(JumpMove.class);
        when(mockPreviousMove.getEnd()).thenReturn(start);
        when(game.hasMovesInCurrentTurn()).thenReturn(true);
        when(game.getLastMoveMade()).thenReturn(mockPreviousMove);

        JumpMove CuT = new JumpMove(start, middle, validEnd);
        boolean actual = CuT.validateMove(game);
        assertTrue(actual);
    }

    @Test
    public void testValidateMoveWithMultiMoveTurnShouldFail() {
        Game mockGame = mock(Game.class);
        Board realBoard = new Board();
        Move mockPreviousMove = mock(JumpMove.class);
        when(mockPreviousMove.getEnd()).thenReturn(validEnd);
        when(mockGame.getBoard()).thenReturn(realBoard);
        when(mockGame.hasMovesInCurrentTurn()).thenReturn(true);
        when(mockGame.getLastMoveMade()).thenReturn(mockPreviousMove);

        JumpMove CuT = new JumpMove(start, validEnd);
        assertFalse(CuT.validateMove(mockGame));
    }
    */
}
