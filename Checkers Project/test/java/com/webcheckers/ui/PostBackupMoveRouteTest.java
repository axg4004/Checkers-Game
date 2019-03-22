package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.JumpMove;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Piece.PColor;
import com.webcheckers.model.Piece.PType;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.SimpleMove;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostBackupMoveRouteTest {

  private static String GOOD_SIMPLE_COMPARE = "{\"text\":\"Simple move undone.\",\"type\":\"info\"}";
  private static String GOOD_JUMP_COMPARE = "{\"text\":\"Jump move undone.\",\"type\":\"info\"}";
  private static String ERROR_COMPARE = "{\"text\":\"No moves to undo.\",\"type\":\"error\"}";



  private PostBackupMoveRoute CuT;

  private Player thisPlayer;
  private Player opponentPlayer;
  private Game game;
  private PlayerLobby playerLobby;
  private Board board;

  private Request request;
  private Response response;
  private Session session;

  @BeforeEach
  void setup() {
    response = mock(Response.class);
    request = mock(Request.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);
    when(session.id()).thenReturn("id");


    playerLobby = new PlayerLobby();

    thisPlayer = new Player("current", "id");
    playerLobby.signIn(thisPlayer);
    opponentPlayer = new Player("opponent");
    playerLobby.signIn(opponentPlayer);

    playerLobby.startGame(thisPlayer, opponentPlayer);
    game = playerLobby.getGame(thisPlayer);
    board = game.getBoard();

    CuT = new PostBackupMoveRoute(playerLobby);

  }

  @Test
  public void TestBackupSimpleMove() {
    game.addMove(new SimpleMove(new Position(5, 0), new Position(4, 1)));

    assertEquals(GOOD_SIMPLE_COMPARE, CuT.handle(request, response));
    assertNull(game.removeMove());
  }

  @Test
  public void TestBackupJumpMove() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board.getSpace(new Position(i, j)).removePiece();
      }
    }

    board.getSpace(new Position(5, 0)).addPiece(new Piece(PColor.red, PType.single));
    board.getSpace(new Position(4, 1)).addPiece(new Piece(PColor.white, PType.single));

    game.addMove(new JumpMove(new Position(5, 0), new Position(3, 2)));

    assertEquals(GOOD_JUMP_COMPARE, CuT.handle(request, response));
    assertNull(game.removeMove());
  }

  @Test
  public void TestBackupNoMove() {
    assertEquals(ERROR_COMPARE, CuT.handle(request, response));
    assertNull(game.removeMove());
  }

}
