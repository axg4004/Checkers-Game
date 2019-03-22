/**
 * This module exports the StableTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the view state
 * in which the player has created a non-empty Turn.  From
 * this state the user may request another move or to submit
 * the current set of moves as a single turn.
 */
define(function(require){
  'use strict';
  
  // imports
  const PlayModeConstants = require('./PlayModeConstants');
  const AjaxUtils = require('../../util/AjaxUtils');

  /**
   * Constructor function.
   * 
   * @param {PlayController} controller
   *    The Play mode controller object.
   * @param {GameView} view
   *    The Game view object.
   */
  function StableTurnState(controller, view) {
    // private attributes
    this._controller = controller;
    this._view = view;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  StableTurnState.prototype.getName = function getName() {
    return PlayModeConstants.STABLE_TURN;
  };
  
  /**
   * Hook when entering this state.
   */
  StableTurnState.prototype.onEntry = function onEntry() {
    // enable all UI controls
    this._controller.enableButton(PlayModeConstants.BACKUP_BUTTON_ID);
    this._controller.enableButton(PlayModeConstants.SUBMIT_BUTTON_ID);
    // re-enable active Piece
    this._controller.enableActivePiece();
  };

  /**
   * The player may request an additional move for a given turn.
   */
  StableTurnState.prototype.requestMove = function requestMove(pendingMove) {
    // register the requested move
    this._controller.setPendingMove(pendingMove);
    // and change the data to Pending
    this._controller.setState(PlayModeConstants.VALIDATING_MOVE);
  };

  /**
   * Backup a single move.
   */
  StableTurnState.prototype.backupMove = function backupMove() {
    this._controller.setState(PlayModeConstants.REQUESTING_BACKUP_MOVE);
  };

  /**
   * Submit the Turn to the server.
   * 
   * This action leaves the current Game view and retrieves an
   * updated Game view from the server.
   */
  StableTurnState.prototype.submitTurn = function submitTurn() {
    AjaxUtils.callServer(
        // the action takes no data
        '/submitTurn', '',
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  function handleResponse(message) {
    this._controller.displayMessage(message);
    if (message.type === 'info') {
      window.location = '/game';
    }
    // handle error message
    else {
      // There are valid error conditions, such as not completing
      // a jump sequence.
      this._controller.displayMessage(message);
    }
  }

  // export class constructor
  return StableTurnState;
  
});
