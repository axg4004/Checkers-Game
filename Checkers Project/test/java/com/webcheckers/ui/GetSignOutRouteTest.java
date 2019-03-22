package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.Spark;
import spark.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;


@Tag("UI-Tier")
public class GetSignOutRouteTest {


  private static final String SESSION_ID = "12345";
  private static final String OTHER_ID = "54321";
  private static final String MY_USERNAME = "jimmy";
  private static final String OTHER_USERNAME = "other";

  private Request request;
  private Response response;
  private Session session;
  private PlayerLobby playerLobby;
  private TemplateEngine templateEngine;
  private GetSignOutRoute CuT;
  private Player thisPlayer;
  private Player otherPlayer;

  //
  // Setup
  //
  @BeforeEach
  public void setup() {
    // Set up session
    session = mock(Session.class);
    when(session.id()).thenReturn(SESSION_ID);

    // Set up request
    request = mock(Request.class);
    when(request.session()).thenReturn(session);

    // Set up response
    response = mock(Response.class);

    // Set up template engine
    templateEngine = mock(TemplateEngine.class);

    // Set up the user making the request
    thisPlayer = new Player(MY_USERNAME, SESSION_ID);

    // Set up the other user, i.e. the opponent
    otherPlayer = new Player(OTHER_USERNAME, OTHER_ID);

    playerLobby = new PlayerLobby();
    playerLobby.signIn(thisPlayer);

    // Set up the route component
    CuT = new GetSignOutRoute(playerLobby);
  }

  @Test
  public void testSignOutSuccess() {
    playerLobby.signIn(thisPlayer);
    playerLobby.signIn(otherPlayer);

    TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    // Make sure we redirect
    assertThrows(spark.HaltException.class, () -> {
          CuT.handle(request, response);
    });

    //Check that the player has been properly removed from the application
    assertEquals(null, playerLobby.getPlayerBySessionID(SESSION_ID));
    assertEquals(1, playerLobby.getSignedInPlayers().size());
    assertNotNull(playerLobby.getPlayer(OTHER_USERNAME));
  }

}
