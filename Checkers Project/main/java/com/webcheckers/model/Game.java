package com.webcheckers.model;

import com.webcheckers.ui.BoardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Object that holds all of the data for a specific game
 */

public class Game {
    private static Logger LOG = Logger.getLogger(Game.class.getName());
    //
    // Attributes
    //

    private int gameID;
    Player redPlayer;
    Player whitePlayer;
    Player winningPlayer;
    Player resignedPlayer;
    Player signedoutPlayer;

    Board board;
    Turn turn;
    Player asyncRequester;
    ArrayList<Move> queuedTurnMoves;
    public State state;
    public boolean madeKing;

    public enum Turn {
        WHITE, RED;
    }

    /**
     * The current state of the game. This is used when the game is over to
     * determine if the game should be deleted. Since both players must be able
     * to perform a {@code GET /game} request so they can find out how the game
     * ended, we can't remove the game until both players have performed that
     * request.
     *
     * When the game is currently in play, it will be in the {@code ACTIVE}
     * state. Once the game has ended and the first player makes a
     * {@code GET /game} request, it will transition to the {@code ENDED}
     * state. Then, once the second player makes a {@code GET /game} request,
     * the game will be deleted.
     */
    public enum State {
        ACTIVE, ENDED, ASYNC_START, ASYNC_ACCEPTED, ASYNC_DENIED, ASYNC_ACTIVE;
    }

    //
    // Constructor
    //
    public Game(Player redPlayer, Player whitePlayer, int gameID) {
        LOG.fine(String.format("Game created: (%s : %s", redPlayer, whitePlayer));
        this.gameID = gameID;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.resignedPlayer = null;
        this.turn = Turn.RED;
        this.board = new Board();
        this.queuedTurnMoves = new ArrayList<>();
        this.state = State.ACTIVE;
        this.madeKing = false;
        this.asyncRequester = null;
    }

    // used for custom configuration
    public Game(Player redPlayer, Player whitePlayer, Turn turn, Board board, int gameID) {
        LOG.fine(String.format("Custom Game created: (%s : %s", redPlayer, whitePlayer));
        this.gameID = gameID;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.resignedPlayer = null;
        this.turn = turn;
        this.board = board;
        this.queuedTurnMoves = new ArrayList<>();
        this.state = State.ACTIVE;
        this.asyncRequester = null;
        this.madeKing = false;
    }

    //
    // Methods
    //

    /**
     * Gets the red player in this Game
     * @return player object of red player
     */
    public Player getRedPlayer() {
        return this.redPlayer;
    }

    /**
     * Gets the white player in this Game
     * @return player object of white player
     */
    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    /**
     * gets the winning player name for this game
     * @return a String representing a player's name
     */
    public String getWinningPlayerName() {
        if (this.winningPlayer != null) {
            return this.winningPlayer.getName();
        }
        return null;
    }

    /**
     * Gets the state of the game
     * @return the state of the game
     */
    public State getState() {
        return this.state;
    }

    public void setStateEnded() {
        this.state = State.ENDED;
    }

    public void setStateActive() {
        this.state = State.ACTIVE;
    }

    public int getGameID() {
        return this.gameID;
    }

    /**
     * Finds out of the supplied players is the player
     * whos turn it is
     * @return player object of white player
     */
    public boolean isPlayersTurn(Player player) {
        if(redPlayer.equals(player)) {
            return turn == Turn.RED;
        } else {
            return turn == Turn.WHITE;
        }
    }

    /**
     * this function gets the other player in the game
     * @param player one of the players in the game
     * @return the other player in the game
     */
    public Player getOpponentOf(Player player) {
        if (player.equals(this.whitePlayer)) {
            return getRedPlayer();
        } else {
            return getWhitePlayer();
        }
    }


    /**
     * This method checks if the opponent player signed out
     * @return the player object representing the player that signed out
     */
    public  Player getSignedoutPlayer() {
        return this.signedoutPlayer;
    }

    /**
     * This method sets a signed out player
     * @pram the player that is to be signed out
     */
    public  void setSignedoutPlayer(Player soPlayer){
        signedoutPlayer = soPlayer;
    }

    /**
     * This method checks if the opponent player resigned
     * @return the player object representing the player that left
     */
    public Player getResigningPlayer() {
        return this.resignedPlayer;
    }

    /**
     * This function effectively lets a player leave a game but does not
     * do any checking. It just removes them from the game by setting their attribute
     * as null. This should be used by game center to let a player leave a game
     * @param leavingPlayer the player object of the leaving player
     */
    public void leaveFromGame(Player leavingPlayer) {
        resignedPlayer = leavingPlayer;
        winningPlayer = getOpponentOf(leavingPlayer);
    }

    /**
     * This function checks if the game is over
     * @return true if game is in end state
     */
    public boolean isGameOver() {
        return this.state == State.ENDED;
    }

    /**
     * Returns the number of moves in the current turn
     * @return true if there have been moves already made
     */
    public boolean hasMovesInCurrentTurn() {
        return queuedTurnMoves.size() >= 1;
    }

    public Move getLastMoveMade() {
        return getMove(queuedTurnMoves.size() - 1);
    }

    public void addMoveToCurrentTurn(Move newest) {
        queuedTurnMoves.add(newest);
        LOG.fine(String.format("Move added to current turn: %d", queuedTurnMoves.size()));
    }

    public Move getMove(int index) {
        if(hasMovesInCurrentTurn()) {
            return queuedTurnMoves.get(index);
        }
        return null;
    }


        /**
         * Gets the board state of this Game
         * @return board object of this Game
         */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Passes up the BoardView of the current Game
     * @param opposite if true, render white pieces at the bottom of the board
     * @return BoardView object
     */
    public BoardView getBoardView(boolean opposite) {
        return new BoardView(this.board, opposite);
    }

    /**
     * Gets the color of the current turn
     * @return red or white enum
     */
    public Turn getTurn() {
        return this.turn;
    }

    public void switchTurn() {
        switch(this.turn){
            case RED:
                this.turn = Turn.WHITE;
                break;
            case WHITE:
                this.turn = Turn.RED;
                break;
        }
        LOG.fine(this.turn.toString() + "'s Turn");
    }

    /**
     * Applies the current players moves to the board, changes the turn to the other player
     */
    public void applyTurnMoves() {
        for (Move move : queuedTurnMoves) {
            move.executeMove(this);
        }
        switchTurn();
        queuedTurnMoves.clear();
    }

    /**
     * Checks if there are moves left to be made in this turn
     * @return true if there are moves left to be made in this turn
     */
    public boolean movesLeft() {
        //if there is a move left, the most recent move's end position is the next move's start position
        if(queuedTurnMoves.size() > 0) {
            Move firstMove = queuedTurnMoves.get(0);
            Move lastMove = queuedTurnMoves.get(queuedTurnMoves.size() - 1);
            // if a simple move is made it can be on only move
            if(lastMove instanceof SimpleMove) return false;
            Position newStart = lastMove.getEnd();

            // This is where the actual piece object is located
            Position originalStart = firstMove.getStart();
            return JumpMove.positionHasJumpMoveAvailable(newStart, originalStart, this);
        }
        return false;
    }

    public boolean playerHasLost(Piece.PColor color) {
        // Check if the player has pieces remaining
        ArrayList<Position> remainingPieces = board.getPieceLocations(color);
        if (remainingPieces.size() == 0) {
            return true;
        }

        // Check if the player has moves remaining if it's their turn
        Piece.PColor turnColor = Piece.PColor.red;
        if (this.turn == Turn.WHITE) {
            turnColor = Piece.PColor.white;
        }
        if (turnColor == color) {
            for (Position current : remainingPieces) {
                if (JumpMove.positionHasJumpMoveAvailable(current, current, this) || SimpleMove.positionHasSimpleMoveAvailable(current, this)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function sets and then returns the winning player of the game
     */
    public void calculateWinningPlayer() {
        if (this.state == State.ACTIVE) {
            if (playerHasLost(Piece.PColor.white)) {
                this.winningPlayer = redPlayer;
            } else if (playerHasLost(Piece.PColor.red)) {
                this.winningPlayer = whitePlayer;
            }
        }
    }

    /**
     * Adds a move to the queue for the current turn
     * @param move move to be added
     */
    public void addMove(Move move) {
        queuedTurnMoves.add(move);
    }

    /**
     * Returns the color of the piece at a specific position on the board.
     *
     * @param position the position to get the piece color of
     * @return the color of the piece on the specified space, or null if there
     *      is no piece there
     */
    public Piece.PColor getPieceColor(Position position) {
        Space space = this.board.getSpace(position);
        if (space.doesHasPiece()) {
            return space.pieceInfo().pieceColor;
        } else {
            return null;
        }
    }

    /**
     * Check if a move has been made so far this simple move.
     *
     * @return true if a simple move has been made, false otherwise
     */
    public boolean hasSimpleMove() {
        return queuedTurnMoves.size() > 0 && queuedTurnMoves.get(0) instanceof SimpleMove;
    }

    /**
     * Removes the last move from the current turn queue
     * @return the removed move
     */
    public Move removeMove() {
        int size = queuedTurnMoves.size();
        if(size > 0) {
            Move move = queuedTurnMoves.get(size - 1);
            queuedTurnMoves.remove(size - 1);
            return move;
        }
        return null;
    }

    public void unmatchPlayers() {
        redPlayer.removeCurrentGame(this);
        whitePlayer.removeCurrentGame(this);
    }

    //
    // Demonstration Methods
    //

    public static Game testKingPieces(Player redPlayer, Player whitePlayer, int gameID) {
        ArrayList<Position> redPieces = new ArrayList<>(
            Arrays.asList(new Position(2, 1), new Position(6, 1)));
        ArrayList<Position> whitePieces = new ArrayList<>(Arrays.asList(new Position(1, 2), new Position(5, 0)));
        return new Game(redPlayer, whitePlayer, Turn.RED, new Board(redPieces, whitePieces), gameID);
    }

    public static Game testDoubleJump(Player redPlayer, Player whitePlayer, int gameID) {
        ArrayList<Position> redPieces = new ArrayList<>(Arrays.asList(
            new Position(7, 0),
            new Position(1, 6),
            new Position(3, 4)
        ));
        ArrayList<Position> whitePieces = new ArrayList<>(Arrays.asList(
            new Position(0, 7),
            new Position(6, 1),
            new Position(4, 1)
        ));
        return new Game(redPlayer, whitePlayer, Turn.RED, new Board(redPieces, whitePieces), gameID);
    }

    public static Game testDoubleJumpKing(Player redPlayer, Player whitePlayer, int gameID) {
        ArrayList<Position> redPieces = new ArrayList<>(Arrays.asList(
            new Position(1, 0)
        ));
        ArrayList<Position> whitePieces = new ArrayList<>(Arrays.asList(
            new Position(1, 2),
            new Position(1, 4),
            new Position(0,7)
        ));
        return new Game(redPlayer, whitePlayer, Turn.RED, new Board(redPieces, whitePieces), gameID);
    }

    public static Game testNoMoves(Player redPlayer, Player whitePlayer, int gameID) {
        ArrayList<Position> redPieces = new ArrayList<>(Arrays.asList(
            new Position(2,1)
        ));
        ArrayList<Position> whitePieces = new ArrayList<>(Arrays.asList(
            new Position(0, 1),
            new Position(0, 3),
            new Position(0, 5),
        new Position(0, 7)
        ));
        return new Game(redPlayer, whitePlayer, Turn.RED, new Board(redPieces, whitePieces), gameID);
    }

    public static Game testKingMidTurn(Player redPlayer, Player whitePlayer, int gameID) {
        ArrayList<Position> redPieces = new ArrayList<>(Arrays.asList(
            new Position(2,1)
        ));
        ArrayList<Position> whitePieces = new ArrayList<>(Arrays.asList(
            new Position(1, 4),
            new Position(1, 2)
        ));
        return new Game(redPlayer, whitePlayer, Turn.RED, new Board(redPieces, whitePieces), gameID);
    }

    public static Game testCaptureToEnd(Player redPlayer, Player whitePlayer, int gameID) {
        ArrayList<Position> redPieces = new ArrayList<>(Arrays.asList(
            new Position(2,1)
        ));
        ArrayList<Position> whitePieces = new ArrayList<>(Arrays.asList(
            new Position(1, 2)
        ));
        return new Game(redPlayer, whitePlayer, Turn.RED, new Board(redPieces, whitePieces), gameID);
    }

    public static Game testInvalidMoves(Player redPlayer, Player whitePlayer, int gameID) {
        ArrayList<Position> redPieces = new ArrayList<>(Arrays.asList(
            new Position(7,2),
            new Position(6, 1),
            new Position(4, 1)
        ));
        ArrayList<Position> whitePieces = new ArrayList<>(Arrays.asList(
            new Position(5, 4),
            new Position(4, 3),
            new Position(2, 3),
            new Position(3, 6)
        ));
        return new Game(redPlayer, whitePlayer, Turn.RED, new Board(redPieces, whitePieces), gameID);
    }


    /**
     * Request that the game transition to asynchronous mode.
     *
     * @param player The player requesting asynchronous mode
     */
    public void requestAsync(Player player) {
        switch(this.state) {
            case ACTIVE:
                this.state = State.ASYNC_START;
                this.asyncRequester = player;
                break;
        }
    }

    /**
     * Accept the request to transition to asynchronous mode.
     */
    public void acceptAsync() {
        switch(this.state) {
            case ASYNC_START:
                this.state = State.ASYNC_ACCEPTED;
                break;
        }
    }

    /**
     * Reject the request to transition to asynchronous mode.
     */
    public void rejectAsync() {
       switch(this.state) {
           case ASYNC_START:
               this.state = State.ASYNC_DENIED;
               break;
       }
    }

    /**
     * Complete the asynchronous request after displaying the results message
     * to the player.
     */
    public void asyncRequestCompleted() {
        switch(this.state) {
            case ASYNC_ACCEPTED:
                this.state = State.ASYNC_ACTIVE;
                this.asyncRequester = null;
                break;
            case ASYNC_DENIED:
                this.state = State.ACTIVE;
                this.asyncRequester = null;
                break;
        }
    }

    /**
     * Check if a given player is the one who requested asynchronous play mode
     * to start.
     *
     * @return True if the players are the same, false if they are different
     */
    public boolean isAsyncRequester(Player player) {
        return this.asyncRequester.equals(player);
    }

    /**
     * Get the turn color of a player's opponent
     */
    public Turn getOpponentTurnColor(Player player) {
        if (redPlayer.equals(player)) {
            return Turn.WHITE;
        } else {
            return Turn.RED;
        }
    }

    public ArrayList<Move> getQueuedTurnMoves() {
        return this.queuedTurnMoves;
    }
}
