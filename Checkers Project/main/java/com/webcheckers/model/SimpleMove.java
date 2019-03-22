package com.webcheckers.model;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SimpleMove extends Move {
    private static final Logger LOG = Logger.getLogger(SimpleMove.class.getName());

    static final String INVALID_LANDING_SPACE = "You cannot end a simple move on a space with a piece on it.";
    static final String JUMP_MOVE_AVAILABLE = "You must jump a piece";


    public SimpleMove(Position start, Position end) {
        super(start, end);
    }

    /**
     * This is an override so that when a SimpleMove is
     * created, it can be executed with different logic than that
     * of other moves.
     */
    @Override
    public boolean executeMove(Game game) {
        Board board = game.getBoard();
        if (validateMove(game)) {
            board.move(start, end);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks if the selected space is results in a valid
     * move
     * <p>
     * NOTE: since this is only used for move validation, the client will
     * only send us requests if the piece is already inside the board
     *
     * @param game the game on which we're applying the moves
     * @return true if it is a valid move
     */
    @Override
    public boolean validateMove(Game game) {
        // First, make sure there aren't any JumpMoves available
        if(JumpMove.jumpMoveAvailable(game)) {
            this.currentMsg = JUMP_MOVE_AVAILABLE;
            return false;
        }
        // Get the real start position with the piece
        Position realStart;
        try {
            realStart = game.getMove(0).start;
        } catch (NullPointerException e) {
            realStart = this.start;
        }
        //check if there is a piece on it
        Board board = game.getBoard();
        if (board.spaceIsValid(end)) {
            // red is at the top of the board in the model and moving "forward" is going down
            Piece.PColor currentColor = game.getPieceColor(realStart);
            boolean whiteErrorCheck = false;
            boolean redErrorCheck = false;
            boolean isKing = game.getBoard().getSpace(realStart).pieceInfo().isKing();
            // get the possible starting points given this ending space
            int left = end.getCell() - 1;
            int right = end.getCell() + 1;
            int top = end.getRow() - 1;
            int bot = end.getRow() + 1;
            // create new positions for easy comparison
            Position upperLeft = new Position(top, left);
            Position upperRight = new Position(top, right);
            Position bottomLeft = new Position(bot, left);
            Position bottomRight = new Position(bot, right);

            if (currentColor == Piece.PColor.white || isKing) {
                whiteErrorCheck = upperLeft.equals(start) || upperRight.equals(start);
            }
            if (currentColor == Piece.PColor.red || isKing) {
                redErrorCheck = bottomLeft.equals(start) || bottomRight.equals(start);
            }
            if (whiteErrorCheck || redErrorCheck)
                this.currentMsg = MOVE_VALID;
            else
                this.currentMsg = MOVE_PIECE_FORWARD;

            return whiteErrorCheck || redErrorCheck;
        } else {
            this.currentMsg = INVALID_LANDING_SPACE;
            return false;
        }
    }

    /**
     * This method runs a general check if the move is a simple move. Not checking if
     * there is a piece on the space. This means it just checks the 4 diagnoally adjacent
     * spaces to see if any of them is the end space.
     *
     * @param move the move to check
     * @return true if the start pos matches any of the expected start positions for the space
     */
    public static boolean isSimpleMove(Move move) {
        Position start = move.getStart();
        Position end = move.getEnd();
        // get the possible starting points given this ending space
        int left = end.getCell() - 1;
        int right = end.getCell() + 1;
        int top = end.getRow() + 1;
        int bot = end.getRow() - 1;
        // create new positions for easy comparison
        Position upperLeft = new Position(top, left);
        Position upperRight = new Position(top, right);
        Position bottomLeft = new Position(bot, left);
        Position bottomRight = new Position(bot, right);
        return upperLeft.equals(start) || upperRight.equals(start) || bottomLeft.equals(start) || bottomRight.equals(start);
    }

    /**
     * Checks if a given position in a game has any simple moves available.
     *
     * @param pos the position to check for possible moves
     * @param game the game to check for possible moves
     * @return true if there is at least one simple move possible from the
     *      position, false otherwise
     */
    public static boolean positionHasSimpleMoveAvailable(Position pos, Game game) {
        // Get the possible ending points given this starting space
        int left = pos.getCell() - 1;
        int right = pos.getCell() + 1;
        int top = pos.getRow() + 1;
        int bot = pos.getRow() - 1;

        // Get piece at starting space
        Piece piece = game.getBoard().getSpace(pos).pieceInfo();
        if (piece == null) {
            // There must be a piece on the starting space
            return false;
        }

        // Create new SimpleMoves for easy comparison
        ArrayList<SimpleMove> possibleMoves = new ArrayList<>();
        if (piece.pieceColor == Piece.PColor.white || piece.isKing()) {
            possibleMoves.add(new SimpleMove(pos, new Position(top, left)));
            possibleMoves.add(new SimpleMove(pos, new Position(top, right)));
        }

        if (piece.pieceColor == Piece.PColor.red || piece.isKing()) {
            possibleMoves.add(new SimpleMove(pos, new Position(bot, left)));
            possibleMoves.add(new SimpleMove(pos, new Position(bot, right)));
        }

        // Check possible moves
        for (SimpleMove current : possibleMoves) {
            if (current.validateMove(game)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("SimpleMove{(%d, %d) -> (%d, %d)}",
                start.getRow(), start.getCell(), end.getRow(), end.getCell());
    }
}
