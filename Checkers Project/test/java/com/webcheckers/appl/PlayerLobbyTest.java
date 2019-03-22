package com.webcheckers.appl;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link PlayerLobby} component.
 */
@Tag("Application-Tier")
public class PlayerLobbyTest {

    //
    // Constants
    //
    private static final String SID = "12345";
    private static final String IMPOSTER_SID = "00000";
    private static final String EMPTY_USERNAME = "";
    private static final String ALL_SPACE_USERNAME = "   ";
    private static final String VALID_USERNAME = "good username123";

    //
    // Attributes
    //
    private PlayerLobby playerLobby;
    private Player player;

    //
    // Pre-test
    //
    @BeforeEach
    public void setUp() {
        // Create the PlayerLobby
        GameCenter gameCenter = mock(GameCenter.class);
        playerLobby = new PlayerLobby(gameCenter);

        // Make the player mock look legit
        player = mock(Player.class);
        when(player.getName()).thenReturn(VALID_USERNAME);
    }

    //
    // Tests
    //
    /**
     * Test that an empty username should not be allowed to sign in.
     */
    @Test
    public void signIn_should_fail_when_emptyPlayerName() {
        assertFalse(playerLobby.signIn(EMPTY_USERNAME, SID));
    }

    /**
     * Test that an all-space username should not be allowed to sign in.
     */
    @Test
    public void signIn_should_fail_when_allSpaceName() {
        assertFalse(playerLobby.signIn(ALL_SPACE_USERNAME, SID));
    }

    /**
     * Test that a player who has already signed in should not be allowed to
     * sign in.
     */
    @Test
    public void signIn_should_fail_when_alreadySignedIn() {
        when(player.isSignedIn()).thenReturn(true);
        playerLobby.signIn(player);
        assertFalse(playerLobby.signIn(VALID_USERNAME, SID));
    }

    /**
     * Test that a player name that is valid which is not signed in should be
     * allowed to sign in.
     */
    @Test
    public void signIn_should_pass_when_validNameNotSignedIn() {
        // I know this looks like it's signing in a user, but since we're
        // mocking the isSignedIn method, this will only create the player.  It
        // will appear that the player is not signed in.
        when(player.isSignedIn()).thenReturn(false);
        playerLobby.signIn(player);
        assertTrue(playerLobby.signIn(VALID_USERNAME, SID));
    }
}