/**
 * This module exports the EmptyTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the view state
 * in which the player has not yet made a move or have backed-up
 * all preceding moves.
 */
define(function(require){
  'use strict';
  
  // imports
  const PlayModeConstants = require('./PlayModeConstants');

  /**
   * Constructor function.
   * 
   * @param {PlayController} controller
   *    The Play mode controller object.
   */
  var EmptyTurnState = function(controller) {
    // private attributes
    this._controller = controller;
    this._first = true;
  };

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  EmptyTurnState.prototype.getName = function getName() {
    return PlayModeConstants.EMPTY_TURN;
  }
  
  /**
   * Method when entering this state.
   */
  EmptyTurnState.prototype.onEntry = function onEntry() {
    // beep the first time here
    if (this._first) {
      // beep to let the player know it's their turn
      beep();
      // initialize game board
      this._controller.initializePlayMode();
      // 
      this._first = false;
    }
    // disable UI controls
    this._controller.disableButton(PlayModeConstants.BACKUP_BUTTON_ID);
    this._controller.disableButton(PlayModeConstants.SUBMIT_BUTTON_ID);
    this._controller.enableButton(PlayModeConstants.RESIGN_BUTTON_ID);
    // re-enable all of my Pieces
    this._controller.enableAllMyPieces();
  }

  /**
   * The player starts her turn by request an initial move.
   */
  EmptyTurnState.prototype.requestMove = function requestMove(pendingMove) {
    // register the requested move
    this._controller.setPendingMove(pendingMove);
    // and change the data to Pending
    this._controller.setState(PlayModeConstants.VALIDATING_MOVE);
  };

  /**
   * Beep to alert the player that it's their turn
   */
  function beep() {
    var sound = document.getElementById("audio");
    sound.play();
  }
  
  // export class constructor
  return EmptyTurnState;
  
});
