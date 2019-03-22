/**
 * This module exports the SpectatorController class constructor.
 * 
 * This component controls the user experience of the Spectator's Game view.
 * There's a simple State model that largely toggles between waiting to
 * check for a game turn to the state that actually performs the check on
 * the server, via an Ajax call.
 *
 * There is also an Exit button that the user may click to formally exit
 * from watching this specific game.
 */
define(function(require){
  'use strict';
  
  // imports
  const StatePatternMixin = require('../../util/StatePatternMixin');
  const ControlsToolbarMixin = require('../../util/ControlsToolbarMixin');
  const SpectatorModeConstants = require('./SpectatorModeConstants');

  // import SPECTATOR mode states
  const WaitForNextTurnState = require('./WaitForNextTurnState');
  const CheckForNextTurnState = require('./CheckForNextTurnState');

  /**
   * Constructor function.
   */
  function SpectatorController(view, gameState) {

    // expose read-only Game data
    this.getGameID = function () {
      return gameState.getGameID();
    };

    // Add the StatePattern mixin
    StatePatternMixin.call(this);
    // create states and a lookup map
    this.addStateDefinition(SpectatorModeConstants.WAITING_FOR_NEXT_TURN,
        new WaitForNextTurnState(this));
    this.addStateDefinition(SpectatorModeConstants.CHECKING_FOR_NEXT_TURN,
        new CheckForNextTurnState(this, gameState));
    
    // Add the ModeControls mixin
    ControlsToolbarMixin.call(this);
    // create mode control buttons
    this.addButton(SpectatorModeConstants.EXIT_BUTTON_ID, 'Exit', true,
        'Exit to the Home page.', this.exitGame);

    // Public (internal) methods

    /**
     * Start Spectator mode.
     */
    this.startup = function startup() {
      // start Spectator mode
      this.setState(SpectatorModeConstants.WAITING_FOR_NEXT_TURN);
    };

    /**
     * Display a message to the Game View.
     */
    this.displayMessage = function displayMessage(message) {
      // delegate to the GameView component.
      view.displayMessage(message);
    };

  }

  //
  // Public (external) methods
  //

  /**
   * Backup a single move.  This message has state-specific behavior.
   */
  SpectatorController.prototype.exitGame = function exitGame() {
    window.location = '/spectator/stopWatching?gameID=' + this.getGameID();
  };

  // export class constructor
  return SpectatorController;
  
});
