/**
 * This module exports the CheckForNextTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view makes the Ajax call to the server to check whether
 * the next turn has been made in the game being spectated.
 */
define(function(require){
  'use strict';

  // imports
  const SpectatorModeConstants = require('./SpectatorModeConstants');
  const AjaxUtils = require('../../util/AjaxUtils');

  /**
   * Constructor function.
   * 
   * @param {SpectatorController} controller
   *    The Spectator mode controller object.
   */
  function CheckForNextTurnState(controller, gameState) {
    // private attributes
    this._controller = controller;
    this._gameState = gameState;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  CheckForNextTurnState.prototype.getName = function getName() {
    return SpectatorModeConstants.CHECKING_FOR_NEXT_TURN;
  };
  
  /**
   * Method when entering this state.
   */
  CheckForNextTurnState.prototype.onEntry = function onEntry() {
    // query the server if the next turn has been played
    AjaxUtils.callServer(
        // the action takes the Game ID as the message body
        '/spectator/checkTurn', this._gameState.getGameID(),
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  function handleResponse(message) {
    // is it successful?
    if (message.type === 'info') {
      // check for special case messages
      if (message.text === 'true') {
        // tell the browser to redisplay the Game View to get the updated board
        window.location = window.location + '&splat=' + new Date().getTime();
      }
      // otherwise, check to see if there is a message to display
      else {
        if (message.text !== 'false') {
          this._controller.displayMessage(message);
        }
        // and go back to the wait state
        this._controller.setState(SpectatorModeConstants.WAITING_FOR_NEXT_TURN);
      }
    }
    // handle error message
    else {
      this._controller.displayMessage(message);
      this._controller.setState(SpectatorModeConstants.WAITING_FOR_NEXT_TURN);
    }
  }

  // export class constructor
  return CheckForNextTurnState;
  
});
