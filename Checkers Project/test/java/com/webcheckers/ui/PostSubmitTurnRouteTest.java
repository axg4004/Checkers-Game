package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Board;
import com.webcheckers.model.Game;
import com.webcheckers.model.Game.Turn;
import com.webcheckers.model.JumpMove;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Piece.PColor;
import com.webcheckers.model.Piece.PType;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.SimpleMove;
import com.webcheckers.ui.Message.MessageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostSubmitTurnRouteTest {

  private final String GOOD_COMPARE = "{\"text\":\"Turn submitted\",\"type\":\"info\"}";
  private final String BAD_COMPARE = "{\"text\":\"Submitted turn is incomplete\",\"type\":\"error\"}";

  private PostSubmitTurnRoute CuT;

  private Player thisPlayer;
  private Player opponentPlayer;
  private Game game;
  private PlayerLobby playerLobby;
  private Gson gson;
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
    gson = new Gson();

    thisPlayer = new Player("current", "id");
    playerLobby.signIn(thisPlayer);
    opponentPlayer = new Player("opponent");
    playerLobby.signIn(opponentPlayer);

    playerLobby.startGame(thisPlayer, opponentPlayer);
    game = playerLobby.getGame(thisPlayer);
    board = game.getBoard();

    CuT = new PostSubmitTurnRoute(playerLobby);
  }

  @Test
  public void testGoodSimpleMove() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board.getSpace(new Position(i, j)).removePiece();
      }
    }
    board.getSpace(new Position(5, 0)).addPiece(new Piece(PColor.red, PType.single));
    game.addMoveToCurrentTurn(new SimpleMove(new Position(5, 0), new Position(4, 1)));

    assertEquals(GOOD_COMPARE, CuT.handle(request, response));
    assertTrue(game.getTurn() == Turn.WHITE);
    assertFalse(game.getBoard().getSpace(new Position(5, 0)).doesHasPiece());
    assertTrue(game.getBoard().getSpace(new Position(4, 1)).doesHasPiece());
  }

  @Test
  public void testGoodJumpMove() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        board.getSpace(new Position(i, j)).removePiece();
      }
    }

    board.getSpace(new Position(5, 0)).addPiece(new Piece(PColor.red, PType.single));
    board.getSpace(new Position(4, 1)).addPiece(new Piece(PColor.white, PType.single));

    game.addMoveToCurrentTurn(new JumpMove(new Position(5, 0), new Position(3, 2)));
    Object result = CuT.handle(request, response);
    assertEquals(GOOD_COMPARE, result);
    assertFalse(board.getSpace(new Position(5, 0)).doesHasPiece());
    assertTrue(board.getSpace(new Position(3, 2)).doesHasPiece());
  }

  @Test public void testSubmitMidMultiJumpFailure() {
      playerLobby = mock(PlayerLobby.class);
      Game mockGame = mock(Game.class);
      when(playerLobby.getPlayerBySessionID("id")).thenReturn(thisPlayer);
      when(playerLobby.getGame(thisPlayer)).thenReturn(mockGame);
      when(mockGame.movesLeft()).thenReturn(true);
      CuT = new PostSubmitTurnRoute(playerLobby);
      Object actual = CuT.handle(request, response);
      assertEquals(actual, BAD_COMPARE);

  }
}

