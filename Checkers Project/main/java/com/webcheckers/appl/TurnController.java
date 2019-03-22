package com.webcheckers.appl;

import com.webcheckers.model.*;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.webcheckers.ui.Message;
import com.webcheckers.ui.Message.MessageType;
import java.util.logging.Logger;

public class TurnController {
    private static final Logger LOG = Logger.getLogger(TurnController.class.getName());

    static final String JUMP_MOVE_ERROR_MSG = "You must jump exactly one opponent piece.";
    static final String SIMPLE_MOVE_ERROR_MSG = "You must move a piece to an empty adjacent space.";
    static final String TOO_MANY_SIMPLE_MOVES_ERROR_MSG = "You may only make one simple move per turn.";
    static final String VALID_MOVE = "Valid move!";
    static final String GENERIC_MOVE_ERR = "GENERIC MOVE ERROR";

    // Private attributes
    GsonBuilder builder;
    PlayerLobby playerLobby;

    public TurnController(PlayerLobby playerLobby) {
        builder = new GsonBuilder();
        this.playerLobby = playerLobby;
    }

    /**
     * Return the message in a json format
     * @param message the message to be translated
     * @return a string continaing a message
     */
    public String MessageFromModeltoUI(Message message) {
        Gson gson = this.builder.create();
        return gson.toJson(message, Message.class);
    }

    /**
     * Takes a JSON object that holds the information for a move
     * and creates a model-tier value object to represent with the same information
     * @param json the json-formatted move
     * @return a Move instance
     */
    Move MovefromUItoModel(String json) {
        LOG.fine(json);
        Gson gson = this.builder.create();
        Move translatedMove = gson.fromJson(json, Move.class);

        // Do checking for move type in order to return correct type
        if(SimpleMove.isSimpleMove(translatedMove)) {
            return new SimpleMove(translatedMove.getStart(), translatedMove.getEnd());
        } else {
            return new JumpMove(translatedMove.getStart(), translatedMove.getEnd());
        }
    }

    /**
     * Starts the validation in the model tier for the given move.
     * @param moveToBeValidated the move in JSON format
     * @param sessionID the session ID that will help find the game
     * @return
     */
    public Message handleValidation(String moveToBeValidated, String sessionID) {
        Player playerMakingMove = playerLobby.getPlayerBySessionID(sessionID);
        Game currentGame = playerLobby.getGame(playerMakingMove);
        Move currentMove = MovefromUItoModel(moveToBeValidated);
        boolean movesMade = currentGame.hasMovesInCurrentTurn();
        boolean result = currentMove.validateMove(currentGame);
        // If we have made a king, the move is over
        if(currentGame.madeKing) {
            return new Message("You have created a king piece, so your turn is over. Please submit!", MessageType.error);
        }
        // test if move is valid
        if(result) {
            if (movesMade) {
                Move lastMove = currentGame.getLastMoveMade();
                if (currentMove instanceof SimpleMove || lastMove instanceof SimpleMove) {
                    return new Message(TOO_MANY_SIMPLE_MOVES_ERROR_MSG, MessageType.error);
                }
            }
            currentGame.addMoveToCurrentTurn(currentMove);
            makeKingPiece(currentGame);
            return new Message(VALID_MOVE);
        } else {
            // differentiate between different errors move types
            String msg = currentMove.getCurrentMsg();
            return new Message(msg, MessageType.error);
        }
    }

    /**
     * Removes the last move from the current turn in a given game
     * @param game game that a move will be removed from
     * @return the move that was removed
     */
    public Move backupMove(Game game) {
        // If a piece was kinged, un-king it
        if(game.madeKing) {
            Position pieceLocation = game.getMove(0).getStart();
            Piece movedPiece = game.getBoard().getSpace(pieceLocation).pieceInfo();
            if (movedPiece.isKing()) {
                movedPiece.pieceType = Piece.PType.single;
                game.madeKing = false;
            }
        }
        return game.removeMove();
    }

    private void makeKingPiece(Game game) {
        // Get the original start and final ending space
        Position end = game.getLastMoveMade().getEnd();
        Position start = game.getMove(0).getStart();

        Piece movedPiece = game.getBoard().getSpace(start).pieceInfo();

        // Don't do anything if it's already a king piece
        if (!movedPiece.isKing()) {
            // Red pieces turn into kings when they end on the 0 row
            if (movedPiece.isRed() && end.getRow() == 0) {
                movedPiece.makeKing();
                game.madeKing = true;
            } else if (!movedPiece.isRed() && end.getRow() == 7) {
                movedPiece.makeKing();
                game.madeKing = true;
            }
        }
    }
}
