/**
 * This module exports the ReplayModeStartState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the starting state
 * of the Replay mode.
 * 
 * This is an exercise for the student.
 */
define(function(require){
  'use strict';
  
  // imports
  var ReplayModeConstants = require('./ReplayModeConstants');

  /**
   * Constructor function.
   * 
   * @param {ReplayController} controller
   *    The Replay mode controller object.
   */
  function ReplayModeStartState(controller) {
    // private attributes
    this._controller = controller;
  };

  //
  // Public (external) methods
  //

  /**
   * Get the symbolic name this state.
   */
  ReplayModeStartState.prototype.getName = function getName() {
    return ReplayModeConstants.REPLAY_MODE_STARTING;
  };
  
  /**
   * Method when entering this state.
   * 
   * Build and begin the Replay view mode.
   */
  ReplayModeStartState.prototype.onEntry = function onEntry() {
    alert('View mode not yet implemented: REPLAY');
  };
  
  // export class constructor
  return ReplayModeStartState;
  
});
