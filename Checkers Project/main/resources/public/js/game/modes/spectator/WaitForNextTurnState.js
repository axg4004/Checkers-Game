/**
 * This module exports the WaitForNextTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view is waiting between calls to the server to check whether
 * the next turn has been made in the game being spectated.
 */
define(function(require){
  'use strict';

  // imports
  const SpectatorModeConstants = require('./SpectatorModeConstants');

  /**
   * Constructor function.
   * 
   * @param {SpectatorController} controller
   *    The Spectator mode controller object.
   */
  function WaitForNextTurnState(controller) {
    // a function to change the Spectator controller's
    // state to perform the 'next turn' check.
    this._checkNextTurn = function () {
      controller.setState(SpectatorModeConstants.CHECKING_FOR_NEXT_TURN);
    }
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  WaitForNextTurnState.prototype.getName = function getName() {
    return SpectatorModeConstants.WAITING_FOR_NEXT_TURN;
  };

  /**
   * Method when entering this state.
   */
  WaitForNextTurnState.prototype.onEntry = function onEntry() {
    // wait five seconds then check my turn
    setTimeout(this._checkNextTurn, 5000);
  };

  // export class constructor
  return WaitForNextTurnState;
  
});
