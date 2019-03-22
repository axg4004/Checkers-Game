package com.webcheckers.model;

import com.webcheckers.model.Piece.PColor;
import java.lang.UnsupportedOperationException;
public class Move {

    static final String GENERIC_VALIDATION_ERROR_MESSAGE = "Generic move validation not supported.";
    static final String GENERIC_EXECUTION_ERROR_MESSAGE = "Generic move execution not supported.";
    static final String MOVE_PIECE_FORWARD = "You must move non-kinged pieces forward";
    static final String MOVE_NOT_VALIDATED = "Move has not been validated";
    static final String OUT_OF_BOUNDS = "Please place the piece inside the board.";
    static final String MOVE_VALID = "Valid Move!";


    Position start;
    Position end;
    String currentMsg;

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
        this.currentMsg = MOVE_NOT_VALIDATED;
    }

    public Position getEnd() {
        return end;
    }

    public Position getStart() {
        return start;
    }

    public String getCurrentMsg(){
        return this.currentMsg;
    }

    /**
     * This method will not be used because a move must be a specific subclass of Move itsef.
     * If a plain Move is used then we throw the UnsupportedOperationException because
     * a generic move doesn't exist
     * @param game the game in which we're moving the piece
     * @throws UnsupportedOperationException
     */
    public boolean validateMove(Game game) throws UnsupportedOperationException{
        throw new UnsupportedOperationException(GENERIC_VALIDATION_ERROR_MESSAGE);
    }

    /**
     * This method will not be used because a move must be a specific subclass of Move itsef.
     * If a plain Move is used then we throw the UnsupportedOperationException because
     * a generic move doesn't exist
     * @param game the game in which we're moving the piece
     * @return true if move was made
     * @throws UnsupportedOperationException
     */
    public boolean executeMove(Game game) throws UnsupportedOperationException{
        throw new UnsupportedOperationException(GENERIC_EXECUTION_ERROR_MESSAGE);
    }

    /**
     * This function checks if the given move will result in a "kinging"
     * of piece for a given color. Because red pieces are located at the top
     * of the board (rows 0 - 2 inclusive) we can check if a red piece has
     * reached king status by seeing if it has reached row 0. Conversely, white
     * will be a King when reaching row 7
     * @param colorCheckedForKing the color of the piece we're checking
     * @return true if this move results in a kinging
     */
    public boolean isKingMove(Piece.PColor colorCheckedForKing) {
        if(colorCheckedForKing == PColor.red) {
            return end.getRow() == 7;
        } else {
            return end.getRow() == 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Move)) {
            return false;
        } else {
            Move other = (Move)obj;
            return this.start.equals(other.start) && this.end.equals(other.end);
        }
    }

    @Override
    public String toString() {
        return String.format("Move{(%d, %d) -> (%d, %d)}",
            start.getRow(), start.getCell(), end.getRow(), end.getCell());
    }
}
