/**
 * This module exports the EmptyTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the view state
 * in which the player has not yet made a move or have backed-up
 * all preceding moves.
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
  var EmptyTurnState = function(view) {
    this._first = true;
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
  EmptyTurnState.prototype.getName = function getName() {
    return GameConstants.EMPTY_TURN;
  }
  
  /**
   * Method when entering this state.
   */
  EmptyTurnState.prototype.onEntry = function onEntry() {
    // beep the first time here
    if (this._first) {
      this.getView().beep();
      this._first = false;
    }
    // disable all UI controls
    this.getView().disableAllControls();
    // re-enable all of my Pieces
    this.getView().enableAllMyPieces();
  }

  /**
   * The player starts her turn by request an initial move.
   */
  EmptyTurnState.prototype.requestMove = function requestMove(pendingMove) {
    // register the requested move
    this.getView().setPendingMove(pendingMove);
    // and change the data to Pending
    this.getView().setState(GameConstants.PENDING_MOVE);
  };

  // export class constructor
  return EmptyTurnState;
  
});
