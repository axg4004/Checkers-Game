package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Game;
import com.webcheckers.model.Game.State;
import com.webcheckers.model.Player;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("Application-Tier")
public class GameCenterTest {

    private GameCenter CuT;
    private Player whitePlayer;
    private Player redPlayer;
    private Player thirdPlayer;

    @BeforeEach
    public void setup() {
        // Build GameCenter
        CuT = new GameCenter();
        // create test players for the game
        whitePlayer = new Player("whitePlayerName", "1");
        redPlayer = new Player("redPlayerName", "2");
        thirdPlayer = new Player("thirdPlayerName", "3");
    }
    @AfterEach
    public void destroy() {
        // Reset for next test
        CuT = null;
        whitePlayer = null;
        redPlayer = null;
    }

    @Test
    public void testGameCenterConstructor() {
        assertNotNull(CuT.activeGames);
        assertNotNull(CuT.opponents);
    }

    @Test
    public void testStartGame() {
        Game testGame = CuT.startGame(redPlayer, whitePlayer);
        int prev_active_size = CuT.activeGames.size();
        int prev_num_opp = CuT.opponents.size();
        // check if the list and hashmap updated with number of player and games
        assertEquals(prev_active_size, CuT.activeGames.size());
        assertEquals(prev_num_opp, CuT.opponents.size());
        assertEquals(1, CuT.gameID);
        assertEquals(0, CuT.readGameID(redPlayer));
        // check if game was returned
        assertNotNull(testGame);
    }

    @Test
    public void testBuildPlayerLists() {
      Game testGame = CuT.startGame(redPlayer, whitePlayer);
      Game testGame2 = CuT.startGame(redPlayer, thirdPlayer);
      ArrayList<String> redNames = redPlayer.getCurrentOpponentNames();
      ArrayList<Integer> redIDs = redPlayer.getCurrentGameIDs();
      assertEquals(redNames.get(0), whitePlayer.getName());
      assertEquals(redNames.get(1), thirdPlayer.getName());
      assertEquals(redIDs.get(0), (Integer)testGame.getGameID());
      assertEquals(redIDs.get(1), (Integer)testGame2.getGameID());
  }

    @Test
    public void testGetOpponentWithNonexistentPlayer() {
        assertNull(CuT.getOpponent(redPlayer));
    }
    @Test
    public void testGetOpponentWithExistingPlayer() {
        // must start a game for players to be added to hashmap
        CuT.startGame(redPlayer,whitePlayer);
        assertEquals(whitePlayer, CuT.getOpponent(redPlayer));
    }

    @Test
    public void testGetGameWithNonexistentPlayer() {
        // should return null because no game found with that player
        assertNull(CuT.getGame(whitePlayer));
    }

    @Test
    public void testGetGameWithExistingWhitePlayer() {
        Game expected = CuT.startGame(redPlayer, whitePlayer);

        // Switch the white player's view to the game
        whitePlayer.changeGame(0);

        Game actual = CuT.getGame(whitePlayer);
        assertSame(expected, actual);
    }

    /**
     * Make sure that the getGame method will return null when a player is
     * viewing the home page.
     */
    @Test
    public void testGetGameOnHomePage() {
        CuT.startGame(redPlayer, whitePlayer);
        assertNull(CuT.getGame(whitePlayer));
    }

    @Test
    public void testGetGameWithExistingRedPlayer() {
        Game expected = CuT.startGame(redPlayer, whitePlayer);
        Game actual = CuT.getGame(redPlayer);
        assertSame(expected, actual);
    }

    @Test
    public void testGetBoardViewWithExistingWhitePlayer() {
        // Start game
        CuT.startGame(redPlayer, whitePlayer);

        // Switch the white player to the game
        whitePlayer.changeGame(0); // 0 is the first game ID assigned

        assertNotNull(CuT.getBoardView(whitePlayer));
    }

    @Test
    public void testGetBoardViewWithExistingRedPlayer() {
        CuT.startGame(redPlayer, whitePlayer);
        assertNotNull(CuT.getBoardView(redPlayer));
    }

    @Test
    public void testResignFromGameWhenActive() {
        // create datastructures to use within GameCenter
        HashMap<String, Player> testHashMap = new HashMap<>();
        testHashMap.put(redPlayer.getName(), whitePlayer);
        testHashMap.put(whitePlayer.getName(), redPlayer);
        ArrayList<Game> testGames = new ArrayList<>();
        Game game = mock(Game.class);
        when(game.getState()).thenReturn(State.ACTIVE);
        testGames.add(game);

        CuT = new GameCenter(testHashMap, testGames);
        CuT.resignFromGame(game, redPlayer);
        verify(game, times(1)).leaveFromGame(redPlayer);
        // when in active game, it just sets state to null, next call it is wiped
        assertEquals(1, testGames.size());
        assertEquals(2, testHashMap.size());
    }

    @Test
    public void testResignFromGameWhenEnded() {
        // create datastructures to use within GameCenter
        HashMap<String, Player> testHashMap = new HashMap<>();
        testHashMap.put(redPlayer.getName(), whitePlayer);
        testHashMap.put(whitePlayer.getName(), redPlayer);
        ArrayList<Game> testGames = new ArrayList<>();
        Game game = mock(Game.class);
        when(game.getState()).thenReturn(State.ENDED);
        when(game.getRedPlayer()).thenReturn(redPlayer);
        when(game.getWhitePlayer()).thenReturn(whitePlayer);
        testGames.add(game);

        CuT = new GameCenter(testHashMap, testGames);
        CuT.resignFromGame(game, redPlayer);
        verify(game, times(1)).leaveFromGame(redPlayer);
        // when in active game, it just sets state to null, next call it is wiped
        assertEquals(0, testGames.size());
        assertEquals(0, testHashMap.size());
    }

    /**
     * Helper method to create a mock game with a specific game ID.
     */
    private Game makeGame(int id) {
        Game game = mock(Game.class);
        when(game.getGameID()).thenReturn(id);
        return game;
    }

    /**
     * Make sure all game objects a player is in are properly retrieved.
     */
    @Test
    public void testGetAllGames() {
        // Set up the fake games
        ArrayList<Game> testGames = new ArrayList<>();
        ArrayList<Integer> gameIDs = new ArrayList<>();
        testGames.add(makeGame(1));
        gameIDs.add(1);
        testGames.add(makeGame(2));
        gameIDs.add(2);
        testGames.add(makeGame(3));
        gameIDs.add(3);

        // Set up the fake player
        Player player = mock(Player.class);
        when(player.getCurrentGameIDs()).thenReturn(gameIDs);

        // Set up Component under Test
        CuT = new GameCenter(new HashMap<>(), testGames);

        // Invoke test
        ArrayList<Game> results = CuT.getAllGames(player);

        assertIterableEquals(testGames, results);
    }
}
