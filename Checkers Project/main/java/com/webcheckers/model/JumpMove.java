package com.webcheckers.model;

import com.webcheckers.model.Game.Turn;
import com.webcheckers.model.Piece.PColor;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * A representation of a capture move, where a player's piece jumps over an
 * opponent piece to capture it. This move will move the piece two spaces
 * vertically and two spaces horizontally.
 */
public class JumpMove extends Move {
    static Logger LOG = Logger.getLogger(JumpMove.class.getName());

    static final String INVALID_JUMP_SPACING = "Jump does not jump the correct distance.";
    static final String INVALID_JUMP_PIECE = "Please continue jumping with your current piece.";
    static final String INVALID_START_PIECE = "This space does not contain a piece.";
    static final String INVALID_LANDING_SPACE = "You cannot end a jump move on a space with a piece.";
    static final String MIDDLE_SAME_COLOR = "You cannot jump your own piece.";
    static final String MIDDLE_NO_PIECE = "You cannot jump an empty space.";
    static final String ALREADY_JUMPED = "That piece has already been jumped!";

    Position middle;

    public JumpMove(Position start, Position end) {
        this(start, calculateMiddle(start, end), end);
    }

    /**
     * Helper constructor that is useful for testing, because sometimes we want
     * to inject a mock object for the middle position.
     *
     * @param start the start position of the move
     * @param middle the position that is jumped by the move
     * @param end the end position of the move
     */
    JumpMove(Position start, Position middle, Position end) {
        super(start, end);
        this.middle = middle;
    }

    /**
     * Calculate the "middle" position, AKA the position/space that this move
     * jumps over, given the starting and ending locations of the move.
     *
     * @param start the position the move starts at
     * @param end the position the move ends at
     * @return the calculated "middle" position
     */
    private static Position calculateMiddle(Position start, Position end) {
        int middleRow = (start.getRow() + end.getRow()) / 2;
        int middleCell = (start.getCell() + end.getCell()) / 2;
        return new Position(middleRow, middleCell);
    }

    /**
     * Helper method to verify the spacing of the move
     * @param start the starting position of the move
     * @param end the ending position of the move
     * @return true if the spacing is valid for a jump move, false otherwise
     */
    private static boolean validSpacing(Position start, Position end) {
        //TODO: fix the absolute value because it still checks if off the board
        if (end.outOfBounds()) return false;
        return (Math.abs(start.getRow() - end.getRow()) == 2 &&
                Math.abs(start.getCell() - end.getCell()) == 2);
    }

    /**
     * Check that this move is valid for a given game.
     *
     * @param game the game in which we're moving the piece
     * @return true if the move is valid, false if the move is invalid
     */
    @Override
    public boolean validateMove(Game game) {
        LOG.fine("JumpMove validation invoked");
        // Init variables
        Piece movedPiece;
        Piece jumpedPiece;

        // Make sure we haven't jumped the same piece twice
        for(Move move : game.getQueuedTurnMoves()) {
            if (move instanceof JumpMove) {
                // Only perform this check on jump moves, duh
                Position theirMiddle = ((JumpMove) move).middle;
                if (theirMiddle.equals(this.middle)) {
                    this.currentMsg = ALREADY_JUMPED;
                    return false;
                }
            }
        }

        // Make sure the spacing is right
        if(!validSpacing(this.start, this.end)) {
            LOG.fine("Failed valid spacing");
            this.currentMsg = INVALID_JUMP_SPACING;
            return false;
        }

        // Make sure the starting position has a piece
        Board board = game.getBoard();
        if(board.getSpace(start).doesHasPiece()) {
            // We'll need this information later
            movedPiece = board.getSpace(start).pieceInfo();
        } else if (game.hasMovesInCurrentTurn()) {
            // Get the moved piece
            Move firstMove = game.queuedTurnMoves.get(0);
            movedPiece = board.getSpace(firstMove.start).pieceInfo();
            // Check if last turn made
            Move lastMove = game.getLastMoveMade();
            Position end = lastMove.getEnd();
            boolean endEqualsStart = this.start.equals(end);
            if (endEqualsStart) {
                this.currentMsg = MOVE_VALID;
            } else {
                this.currentMsg = INVALID_JUMP_PIECE;
                return false;
            }
        } else {
            LOG.fine("Failed starting space has piece");
            this.currentMsg = INVALID_START_PIECE;
            return false;
        }

        // Make sure the ending position doesn't have a piece
        try {
            if(!board.spaceIsValid(end)) {
                LOG.fine("Failed ending space");
                this.currentMsg = INVALID_LANDING_SPACE;
                return false;
            }
        } catch(IndexOutOfBoundsException except) {
            // This is a really lazy way of checking that the ending position
            // is out of the board's bounds, but whatever.
            this.currentMsg = OUT_OF_BOUNDS;
            return false;
        }

        // Make sure the middle position has an opponent piece
        Position realStart;
        if(game.hasMovesInCurrentTurn()) {
            realStart = game.getMove(0).start;
        } else {
            realStart = this.start;
        }
        PColor currentColor = game.getPieceColor(realStart);
        if(board.getSpace(middle).doesHasPiece()) {
            LOG.fine("Middle: " + middle.toString());
            LOG.fine("Board has piece.");
            jumpedPiece = board.getSpace(middle).pieceInfo();
            if(currentColor == jumpedPiece.pieceColor) {
                // Can only jump an opponent's piece
                LOG.fine("Failed middle position same color");
                this.currentMsg = MIDDLE_SAME_COLOR;
                return false;
            }
        } else {
            LOG.fine("Failed space has middle piece");
            this.currentMsg = MIDDLE_NO_PIECE;
            return false;
        }

        // Make sure the piece is going in the right direction
        if (!movedPiece.isKing()) {
            boolean check;
            if (currentColor == PColor.red) {
                // Red pieces should travel in the "negative" direction
                check = start.getRow() > end.getRow();
            } else {
                // White pieces should travel in the "positive" direction
                check = start.getRow() < end.getRow();
            }
            if (check) this.currentMsg = MOVE_VALID;
            else this.currentMsg = MOVE_PIECE_FORWARD;
            return check;
        } else {
            return true;
        }
    }

    /**
     * Apply this move to a given game
     *
     * @param game the game in which we're moving the piece
     * @return true if the move was successfully executed, false otherwise
     */
    @Override
    public boolean executeMove(Game game) {
        // Double-check that the move is valid
        //if(validateMove(game)) {
            LOG.fine("Executing Move " + toString());
            Board board = game.getBoard();
            // Move the piece
            board.move(start, end);

            // Remove the jumped piece
            board.getSpace(middle).removePiece();
            return true;
        /*} else {
            return false;
        }*/
    }

    /**
     * Checks a position in a given game to see if it has any jump moves
     * available.
     *
     * @param pos the position to check for moves
     * @param pieceLocation the location of the piece that is being moved this
     *                      turn
     * @param game the game that is currently running
     * @return true if the position has jump moves available, false otherwise
     */
    static boolean positionHasJumpMoveAvailable(Position pos, Position pieceLocation, Game game) {
        ArrayList<JumpMove> possibleMoves = new ArrayList<>();
        /* REFERENCE FOR THIS CODE BLOCK:
        "lower" refers to lower-numbered rows
        "higher" refers to higher-numbered rows
         */

        // This is a big-time law of Demeter violation right here
        PColor currentColor = game.getPieceColor(pieceLocation);
        boolean isKing = game.getBoard().getSpace(pieceLocation).pieceInfo().isKing();
        if(currentColor == PColor.red || isKing) {
            // Lower-left
            possibleMoves.add(new JumpMove(pos, new Position(pos.getRow() - 2, pos.getCell() - 2)));
            // Lower-right
            possibleMoves.add(new JumpMove(pos, new Position(pos.getRow() - 2, pos.getCell() + 2)));
        }
        if(currentColor == PColor.white || isKing) {
            // Upper-left
            possibleMoves.add(new JumpMove(pos, new Position(pos.getRow() + 2, pos.getCell() - 2)));
            // Upper-right
            possibleMoves.add(new JumpMove(pos, new Position(pos.getRow() + 2, pos.getCell() + 2)));
        }

        // Check all the possible spaces to see if any one of them is valid
        for(JumpMove move : possibleMoves) {
            if(move.validateMove(game)) {
                if(isMoveOk(move, game)) {
                    continue;
                }
                LOG.fine("jumpMove found to be true. Must make jump" + move.toString());
                return true;
            }
        }
        return false;
    }

    static boolean isMoveOk(Move move, Game game) {
        // Check if the move's end location has already been visited
        for(Move madeMove : game.queuedTurnMoves) {
            if (madeMove.end.equals(move.end)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks a game to see if the current player has a jump move available.
     * Used for testing in simple move to check if the player has to make a jump
     *
     * @param game the game state to check for possible jump moves
     * @return true if a jump move is available, false otherwise
     */
    static boolean jumpMoveAvailable(Game game) {
        // Convert the turn to a PColor
        Piece.PColor currentColor = Piece.PColor.red;
        if(game.getTurn() == Game.Turn.WHITE) {
            currentColor = Piece.PColor.white;
        }

        // Loop through all piece positions for that player
        for(Position pos : game.getBoard().getPieceLocations(currentColor)) {
            if(positionHasJumpMoveAvailable(pos, pos, game)) {
                return true;
            }
        }
        return false;
    }
}
