package com.webcheckers.ui;

import static spark.Spark.halt;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.TurnController;
import com.webcheckers.model.Game;
import com.webcheckers.model.JumpMove;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.SimpleMove;
import com.webcheckers.ui.Message.MessageType;
import java.awt.geom.RectangularShape;
import java.util.Objects;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public class PostBackupMoveRoute implements Route {

  //
  // Constants
  //
  private static final String SUCCESS_MESSAGE_SIMPLE = "Simple move undone.";
  private static final String SUCCESS_MESSAGE_JUMP = "Jump move undone.";
  private static final String ERROR_MESSAGE = "No moves to undo.";

  //
  // Attributes
  //
  private PlayerLobby playerLobby;

  //
  // Constructor
  //
  public PostBackupMoveRoute(PlayerLobby playerLobby) {
    Objects.requireNonNull(playerLobby, "playerLobby cannot be null");
    this.playerLobby = playerLobby;
  }

  //
  // Methods
  //
  @Override
  public Object handle(Request request, Response response) {
    final Session httpSession = request.session();
    final String sessionID = httpSession.id();


    Player thisPlayer = playerLobby.getPlayerBySessionID(sessionID);

    if(thisPlayer == null) {
      response.redirect("/");
      halt();
      return null;
    }

    Game game = playerLobby.getGame(thisPlayer);
    TurnController turnController = new TurnController(playerLobby);

    Move move = turnController.backupMove(game);

    if(move == null) {
      return turnController.MessageFromModeltoUI(new Message(ERROR_MESSAGE, MessageType.error));
    }
    if(move instanceof SimpleMove) {
      return turnController.MessageFromModeltoUI(new Message(SUCCESS_MESSAGE_SIMPLE, MessageType.info));
    }
    if(move instanceof JumpMove) {
      return turnController.MessageFromModeltoUI(new Message(SUCCESS_MESSAGE_JUMP, MessageType.info));
    }
    return turnController.MessageFromModeltoUI(new Message("Unknown error", MessageType.error));
  }

}
