/**
 * This module exports the WaitingForUserActionState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view is waiting for the user to perform an action, such as
 * click the 'Next' or 'Previous' buttons.
 */
define(function(require){
  'use strict';

  // imports
  const ReplayModeConstants = require('./ReplayModeConstants');

  /**
   * Constructor function.
   * 
   * @param {ReplayController} controller
   *    The Replay mode controller object.
   */
  function WaitingForUserActionState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  WaitingForUserActionState.prototype.getName = function getName() {
    return ReplayModeConstants.WAITING_FOR_USER_ACTION;
  };
  
  /**
   * Method when entering this state.
   */
  WaitingForUserActionState.prototype.onEntry = function onEntry() {
    // re-enable all of the View controls
    this._controller.enableAllControls();
  };

  // export class constructor
  return WaitingForUserActionState;
  
});
