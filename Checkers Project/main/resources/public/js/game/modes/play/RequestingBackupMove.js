/**
 * This module exports the RequestingBackupMove class constructor.
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
   */
  var RequestingBackupMove = function(controller) {
    // private attributes
    this._controller = controller;
  };

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  RequestingBackupMove.prototype.getName = function getName() {
    return PlayModeConstants.REQUESTING_BACKUP_MOVE;
  }
  
  /**
   * Hook when entering this state.
   */
  RequestingBackupMove.prototype.onEntry = function onEntry() {
    // 1) disable UI controls
    this._controller.disableButton(PlayModeConstants.BACKUP_BUTTON_ID);
    this._controller.disableButton(PlayModeConstants.SUBMIT_BUTTON_ID);
    this._controller.disableButton(PlayModeConstants.RESIGN_BUTTON_ID);
    
    // 2) disable all Pieces
    this._controller.disableAllMyPieces();
    
    // 3) ask the server to backup from the most recent move
    AjaxUtils.callServer(
        // the action takes no data
        '/backupMove', '',
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  function handleResponse(message) {
    this._controller.displayMessage(message);
    if (message.type === 'info') {
      this._controller.popMove();
      var isTurnActive = this._controller.isTurnActive();
      this._controller.setState(isTurnActive ? PlayModeConstants.STABLE_TURN : PlayModeConstants.EMPTY_TURN);
    }
    // handle error message
    else {
      this._controller.displayMessage(message);
      this._controller.setState(PlayModeConstants.STABLE_TURN);
    }
  }

  // export class constructor
  return RequestingBackupMove;
  
});
