package com.webcheckers.ui;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpaceViewTest {

    //
    // Attributes
    //
    private Space blackSpace;
    private Space whiteSpace;
    private Piece piece;
    // The cell index to use for testing:rotating_light::rotating_light:
    private static final int CELL_INDEX = 0;

    //
    // Pre-test
    //
    @BeforeEach
    public void testSetup() {
        // Set up the mocks
        blackSpace = mock(Space.class);
        when(blackSpace.isBlack()).thenReturn(true);

        whiteSpace = mock(Space.class);
        when(whiteSpace.isBlack()).thenReturn(false);

        piece = mock(Piece.class);
    }

    //
    // Tests
    //
    /**
     * Test the constructor to make sure white spaces will never have pieces on
     * them.
     */
    @Test
    public void should_notHavePiece_when_isWhiteSpace() {
        // Create a white space with a piece
        when(whiteSpace.doesHasPiece()).thenReturn(true);
        when(whiteSpace.pieceInfo()).thenReturn(piece);
        SpaceView CuT = new SpaceView(whiteSpace, CELL_INDEX);

        // Should not have a piece
        assertNull(CuT.getPieceView());
    }

    /**
     * Test that white spaces will be reported as invalid.
     */
    @Test
    public void should_beInvalid_when_isWhiteSpace() {
        // Create a white space with a piece
        when(whiteSpace.doesHasPiece()).thenReturn(true);
        when(whiteSpace.pieceInfo()).thenReturn(piece);
        SpaceView CuT = new SpaceView(whiteSpace, CELL_INDEX);

        // Should not be valid
        assertFalse(CuT.isValid());
    }

    /**
     * Test that spaces that have pieces on them will be reported as invalid.
     */
    @Test
    public void should_beInvalid_when_hasPiece() {
        // Create a black space with a piece
        when(blackSpace.doesHasPiece()).thenReturn(true);
        when(blackSpace.pieceInfo()).thenReturn(piece);
        when(piece.getPieceView()).thenReturn(mock(PieceView.class));
        SpaceView CuT = new SpaceView(blackSpace, CELL_INDEX);

        // Should not be valid
        assertFalse(CuT.isValid());
    }

    /**
     * Test that spaces without pieces that are black will be reported as
     * valid.
     */
    @Test
    public void should_beValid_when_blackSpaceWithoutPiece() {
        // Create a black space without a piece
        when(blackSpace.doesHasPiece()).thenReturn(false);
        SpaceView CuT = new SpaceView(blackSpace, CELL_INDEX);

        // Should be valid
        assertTrue(CuT.isValid());
    }

    /**
     * Test that the cell index getter returns the correct value.
     */
    @Test
    public void should_returnIndexInRow_when_getCellIdx() {
        // Provide a value for this method so we can create a CuT
        when(blackSpace.doesHasPiece()).thenReturn(false);
        SpaceView CuT = new SpaceView(blackSpace, CELL_INDEX);

        // Should return the index value originally set
        assertEquals(CuT.getCellIdx(), CELL_INDEX);
    }
}
