/**
 * This module exports the WaitingForServerActionResponseState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view makes the Ajax call to the server to perform an action
 * on the TurnIterator for the game being "replayed".
 */
define(function(require){
  'use strict';

  // imports
  const ReplayModeConstants = require('./ReplayModeConstants');
  const AjaxUtils = require('../../util/AjaxUtils');

  /**
   * Constructor function.
   *
   * @param {ReplayController} controller
   *    The Replay mode controller object.
   */
  function WaitingForServerActionResponseState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  WaitingForServerActionResponseState.prototype.getName = function getName() {
    return ReplayModeConstants.WAITING_FOR_SERVER_RESPONSE;
  };
  
  /**
   * Method when entering this state.
   */
  WaitingForServerActionResponseState.prototype.onEntry = function onEntry(args) {
    // disable all of the View controls
    this._controller.disableAllControls();
    //
    const actionURL = args[0];
    // query the server if the next turn has been played
    AjaxUtils.callServer(
        // the action takes the Game ID as the message body
        actionURL, this._controller.getGameID(),
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
        this._controller.setState(ReplayModeConstants.WAITING_FOR_USER_ACTION);
      }
    }
    // handle error message
    else {
      this._controller.displayMessage(message);
      this._controller.setState(ReplayModeConstants.WAITING_FOR_USER_ACTION);
    }
  }

  // export class constructor
  return WaitingForServerActionResponseState;
  
});
