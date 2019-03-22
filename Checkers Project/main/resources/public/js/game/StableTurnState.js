/**
 * This module exports the StableTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the view state
 * in which the player has created a non-empty Turn.  From
 * this state the user may request another move or to submit
 * the current set of moves as a single turn.
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
  var StableTurnState = function(view) {
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
  StableTurnState.prototype.getName = function getName() {
    return GameConstants.STABLE_TURN;
  }
  
  /**
   * Hook when entering this state.
   */
  StableTurnState.prototype.onEntry = function onEntry() {
    // enable all UI controls
    this.getView().enableAllControls();
    // re-enable active Piece
    this.getView().enableActivePiece();
  }

  /**
   * The player may request an additional move for a given turn.
   */
  StableTurnState.prototype.requestMove = function requestMove(pendingMove) {
    // register the requested move
    this.getView().setPendingMove(pendingMove);
    // and change the data to Pending
    this.getView().setState(GameConstants.PENDING_MOVE);
  };

  /**
   * Reset the whole turn.
   */
  StableTurnState.prototype.resetTurn = function resetTurn() {
    var self = this;
    var view = this.getView();
    jQuery.post('/backupMove', '',
            function(message, textStatus, jqXHR) {
      handleUnmoveResponse(view, message);
      // call this recursively, while there are still moves to be undone
      if (view.isTurnActive()) {
        self.resetTurn();
      }
    },
    'json');
  }

  /**
   * Backup a single move.
   */
  StableTurnState.prototype.backupMove = function backupMove() {
    var view = this.getView();
    jQuery.post('/backupMove', '',
            function(message, textStatus, jqXHR) {
      handleUnmoveResponse(view, message);
    },
    'json');
  }

  /**
   * Submit the Turn to the server.
   * 
   * This action leaves the current Game view and retrieves an
   * updated Game view from the server.
   */
  StableTurnState.prototype.submitTurn = function submitTurn() {
    jQuery('#gameForm').submit();
  }

  //
  // Private (external) functions
  //

  /**
   * Handle the Ajax response on the '/backupMove' action.
   */
  function handleUnmoveResponse(view, message) {
    view.displayMessage(message);
    if (message.type === 'info') {
      view.popMove();
      view.setState(getNextState(view));
    }
    // handle error message
    else {
      // FIXME: unrecoverable right now
    }
  }
  
  /**
   * Get the next Game view state based upon whether there
   * is still an active turn or not.
   */
  function getNextState(view) {
    return view.isTurnActive() ? GameConstants.STABLE_TURN : GameConstants.EMPTY_TURN;
  }
  
  // export class constructor
  return StableTurnState;
  
});
