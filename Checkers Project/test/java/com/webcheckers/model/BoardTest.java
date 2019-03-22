package com.webcheckers.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")

public class BoardTest {

    Board board = new Board();

    @Test
    public void testConstructor() {
        assertTrue(board.boardArray[7][0].isBlack(), "Bottom-left space should be black");
        assertTrue(board.boardArray[0][7].isBlack(), "Top-right space should be black");
    }

    @Test
    public void testMoveFromEmptySpace() {
        Position oldPos = new Position(4,0);
        Position newPos = new Position(3,0);
        board.move(oldPos, newPos);
        assertFalse(board.boardArray[3][0].doesHasPiece());
    }

    /*
    @Test
    public void testBadMoveWhiteSpace() {
        Position oldPos = new Position(5,0);
        Position newPos = new Position(4,0);
        board.move(oldPos, newPos);
        assertFalse(board.boardArray[4][0].doesHasPiece());
        assertTrue(board.boardArray[5][0].doesHasPiece());
    }

    @Test
    public void testBadMoveTakenSpace() {
        Position oldPos = new Position(5,2);
        Position newPos = new Position(6,3);
        board.move(oldPos, newPos);
        assertTrue(board.boardArray[5][2].doesHasPiece());
    }

    @Test
    public void testGoodMove() {
        Position oldPos = new Position(5,4);
        Position newPos = new Position(4,5);
        board.move(oldPos, newPos);
        assertTrue(board.boardArray[4][5].doesHasPiece());
        assertFalse(board.boardArray[5][4].doesHasPiece());
    }
    */
}
