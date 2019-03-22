/**
 * This module exports the SpectatorModeStartState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the starting state
 * of the Spectator mode.
 * 
 * This is an exercise for the student.
 */
define(function(require){
  'use strict';
  
  // imports
  var SpectatorModeConstants = require('./SpectatorModeConstants');

  /**
   * Constructor function.
   * 
   * @param {SpectatorController} controller
   *    The Spectator mode controller object.
   */
  function SpectatorModeStartState(controller) {
    // private attributes
    this._controller = controller;
  };

  //
  // Public (external) methods
  //

  /**
   * Get the symbolic name this state.
   */
  SpectatorModeStartState.prototype.getName = function getName() {
    return SpectatorModeConstants.SPECTATOR_MODE_STARTING;
  };
  
  /**
   * Method when entering this state.
   * 
   * Build and begin the Spectator view mode.
   */
  SpectatorModeStartState.prototype.onEntry = function onEntry() {
    alert('View mode not yet implemented: SPECTATOR');
  };
  
  // export class constructor
  return SpectatorModeStartState;
  
});
