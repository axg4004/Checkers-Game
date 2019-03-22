/**
 * This module exports the ReplayController class constructor.
 *
 * This component controls the user experience of the Replay Game view.
 * There's a simple State model that largely toggles between waiting for
 * a user action (such as clicking the 'Next' button) to the state that
 * actually performs the action on the server, via an Ajax call.
 *
 * There is also an Exit button that the user may click to formally exit
 * from watching this specific game.
 */
define(function(require){
  'use strict';
  
  // imports
  const StatePatternMixin = require('../../util/StatePatternMixin');
  const ControlsToolbarMixin = require('../../util/ControlsToolbarMixin');
  const ReplayModeConstants = require('./ReplayModeConstants');
  
  // import REPLAY mode states
  const WaitingForUserActionState = require('./WaitingForUserActionState');
  const WaitingForServerActionResponseState = require('./WaitingForServerActionResponseState');

  /**
   * Constructor function.
   */
  function ReplayController(view, gameState) {

    // expose read-only Game data
    this.getGameID = function getGameID() {
      return gameState.getGameID();
    };

    // expose View options
    this.getOption = function getOption(optionName) {
      return gameState.getModeOption(optionName);
    };

    // Add the StatePattern mixin
    StatePatternMixin.call(this);
    // create states and a lookup map
    this.addStateDefinition(ReplayModeConstants.WAITING_FOR_USER_ACTION,
        new WaitingForUserActionState(this));
    this.addStateDefinition(ReplayModeConstants.WAITING_FOR_SERVER_RESPONSE,
        new WaitingForServerActionResponseState(this));

    // Add the ModeControls mixin
    ControlsToolbarMixin.call(this);
    // create mode control buttons
    this.addButton(ReplayModeConstants.NEXT_BUTTON_ID, 'Next', true,
        'Move to the next turn.', this.requestNextTurn);
    this.addButton(ReplayModeConstants.PREVIOUS_BUTTON_ID, 'Previous', true,
        'Move to the previous turn.', this.requestPreviousTurn);
    this.addButton(ReplayModeConstants.EXIT_BUTTON_ID, 'Exit', true,
        'Exit to the Home page.', this.exitGame);

    // Public (internal) methods

    /**
     * Start Replay mode.
     */
    this.startup = function startup() {
      // start Replay mode
      this.setState(ReplayModeConstants.WAITING_FOR_USER_ACTION);
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
   * Disable all controls on the View.
   */
  ReplayController.prototype.disableAllControls = function disableAllControls() {
    //
    this.disableButton(ReplayModeConstants.NEXT_BUTTON_ID);
    this.disableButton(ReplayModeConstants.PREVIOUS_BUTTON_ID);
    this.disableButton(ReplayModeConstants.EXIT_BUTTON_ID);
  };

  /**
   * Re-enable appropriate controls on the View.
   */
  ReplayController.prototype.enableAllControls = function enableAllControls() {
    //
    if (!this.getOption(ReplayModeConstants.HAS_NEXT_OPTION)) {
      this.disableButton(ReplayModeConstants.NEXT_BUTTON_ID);
    }
    if (!this.getOption(ReplayModeConstants.HAS_PREVIOUS_OPTION)) {
      this.disableButton(ReplayModeConstants.PREVIOUS_BUTTON_ID);
    }
    this.enableButton(ReplayModeConstants.EXIT_BUTTON_ID);
  };

  /**
   * Send the 'Next Turn' action to the WAITING_FOR_SERVER_RESPONSE state.
   */
  ReplayController.prototype.requestNextTurn = function requestNextTurn() {
    //
    this.checkInState(ReplayModeConstants.WAITING_FOR_USER_ACTION);
    this.setState(ReplayModeConstants.WAITING_FOR_SERVER_RESPONSE, "/replay/nextTurn");
  };

  /**
   * Send the 'Previous Turn' action to the WAITING_FOR_SERVER_RESPONSE state.
   */
  ReplayController.prototype.requestPreviousTurn = function requestPreviousTurn() {
    //
    this.checkInState(ReplayModeConstants.WAITING_FOR_USER_ACTION);
    this.setState(ReplayModeConstants.WAITING_FOR_SERVER_RESPONSE, "/replay/previousTurn");
  };

  /**
   * Backup a single move.  This message has state-specific behavior.
   */
  ReplayController.prototype.exitGame = function exitGame() {
    window.location = '/replay/stopWatching?gameID=' + this.getGameID();
  };

  // export class constructor
  return ReplayController;
  
});
