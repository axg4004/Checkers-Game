/**
 * This module exports the CheckMyTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view makes the Ajax call to the server to check whether
 * it's the current player's turn.
 */
define(function (require) {
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
  function CheckMyTurnState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  CheckMyTurnState.prototype.getName = function getName() {
    return PlayModeConstants.CHECK_MY_TURN;
  };
  
  /**
   * Method when entering this state.
   */
  CheckMyTurnState.prototype.onEntry = function onEntry() {
    this._controller.disableButton(PlayModeConstants.RESIGN_BUTTON_ID);
    // query the server if it's my turn
    AjaxUtils.callServer(
        // the action takes no data
        '/checkTurn', '',
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  function handleResponse(message) {
    if (message.type === 'info') {
      if (message.text === 'true') {
        // tell the browser to redisplay the Game View to get the updated board
        window.location = '/game';
      } else {
        this._controller.setState(PlayModeConstants.WAIT_FOR_MY_TURN);
      }
    }
    // handle error message
    else {
      this._controller.displayMessage(message);
      this._controller.setState(PlayModeConstants.WAIT_FOR_MY_TURN);
    }
  }

  // export class constructor
  return CheckMyTurnState;
  
});
