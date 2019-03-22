/**
 * This module exports the WaitForTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view is waiting between calls to the server to check whether
 * it's the current player's turn.
 */
define(function(require){
  'use strict';

  // imports
  var GameConstants = require('GameConstants');

  /**
   * Constructor function.
   * 
   * @param {GameView} view
   *    The Game view object.
   */
  function WaitForTurnState(view) {
    // public (internal) methods
    this.getView = function() {
      return view;
    };
  };

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  WaitForTurnState.prototype.getName = function getName() {
    return GameConstants.WAIT_FOR_MY_TURN;
  }
  
  /**
   * Method when entering this state.
   */
  WaitForTurnState.prototype.onEntry = function onEntry() {
    var view = this.getView();
    // wait five seconds then check my turn
    setTimeout(function() { view.setState(GameConstants.CHECK_MY_TURN); }, 5000);
  }

  // export class constructor
  return WaitForTurnState;
  
});
