/**
 * This module exports the CheckMyTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view makes the Ajax call to the server to check whether
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
  function CheckMyTurnState(view) {
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
  CheckMyTurnState.prototype.getName = function getName() {
    return GameConstants.CHECK_MY_TURN;
  }
  
  /**
   * Method when entering this state.
   */
  CheckMyTurnState.prototype.onEntry = function onEntry() {
    var view = this.getView();
    // query the server if it's my turn
    jQuery.post('/checkTurn', '',
            function(isMyTurn, textStatus, jqXHR) {
      handleResponse(view, isMyTurn);
    },
    'json');
  }

  //
  // Private (external) functions
  //

  /**
   * Handle the Ajax response on the '/checkTurn' action.
   */
  function handleResponse(view, isMyTurn) {
    console.info('handleResponse isMyTurn=' + isMyTurn);
    if (isMyTurn) {
      window.location.replace('/game');
    } else {
      view.setState(GameConstants.WAIT_FOR_MY_TURN)
    }
  }

  // export class constructor
  return CheckMyTurnState;
  
});
