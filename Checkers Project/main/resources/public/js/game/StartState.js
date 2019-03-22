/**
 * This module exports the StartState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the starting state.
 * Its job is to decide the next state based upon whether the
 * it's the current player's turn or not.
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
  function StartState(view) {

    // public (internal) methods
    this.getView = function() {
      return view;
    };
  };

  //
  // Public (external) methods
  //

  /**
   * Method when exiting this state.
   */
  StartState.prototype.getName = function getName() {
    return GameConstants.START_STATE;
  };
  
  /**
   * Method when entering this state.
   */
  StartState.prototype.onEntry = function onEntry() {
    // 1) initialize the main View content
    this._initializeView();
    // 2) decide which state to transition to next
    if (this.getView().isMyTurn()) {
      console.debug("It's your turn.");
      this.getView().setState(GameConstants.EMPTY_TURN);
    } else {
      console.debug("It's not your turn.");
      this.getView().setState(GameConstants.WAIT_FOR_MY_TURN);
    }
  };
  
  //
  // Private (external) methods
  //

  /**
   * Initializes the content in the game-info fieldset.
   */
  StartState.prototype._initializeView = function _initializeView() {
    console.debug('initializeView');
    //
    var view = this.getView();
    var redPlayer, whitePlayer;
    var redsTurn;
    if (view.isPlayerRed()) {
      redPlayer = 'You';
      whitePlayer = view.getOpponentName();
      redsTurn = view.isMyTurn();
    } else {
      whitePlayer = 'You';
      redPlayer = view.getOpponentName();
      redsTurn = !view.isMyTurn();
    }
    // update the view 
    jQuery("fieldset#game-info table[data-color='RED'] td.name").text(redPlayer);
    if (redsTurn) {
      jQuery("fieldset#game-info table[data-color='RED']").addClass('isMyTurn');
    }
    jQuery("fieldset#game-info table[data-color='WHITE'] td.name").text(whitePlayer);
    if (!redsTurn) {
      jQuery("fieldset#game-info table[data-color='WHITE']").addClass('isMyTurn');
    }
  };
  
  // export class constructor
  return StartState;
  
});
