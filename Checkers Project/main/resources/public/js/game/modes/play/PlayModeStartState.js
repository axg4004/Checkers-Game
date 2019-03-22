/**
 * This module exports the PlayModeStartState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the Play mode starting state.
 * Its job is to initialize the Play game controls and then decide the
 * next state based upon whether the it's the current player's turn or not.
 */
define(function(require){
  'use strict';
  
  // imports
  const PlayModeConstants = require('./PlayModeConstants');

  /**
   * Constructor function.
   * 
   * @param {PlayController} controller
   *    The Play mode controller object.
   * @param {GameView} view
   *    The Game view object.
   * @param {GameState} gameState
   *    The state of the game.
   */
  function PlayModeStartState(controller, view, gameState) {
    // private attributes
    this._controller = controller;
    this._view = view;
    this._gameState = gameState;
  };

  //
  // Public (external) methods
  //

  /**
   * Get the symbolic name this state.
   */
  PlayModeStartState.prototype.getName = function getName() {
    return PlayModeConstants.PLAY_MODE_STARTING;
  };
  
  /**
   * Method when entering this state.
   */
  PlayModeStartState.prototype.onEntry = function onEntry() {
    // validate game state for Play mode (currentPlayer must be == to one of the platers)
    var currentPlayer = this._gameState.getCurrentPlayer();
    if ( currentPlayer !== this._gameState.getRedPlayer()
      && currentPlayer !== this._gameState.getWhitePlayer() ) {
      alert('PlayModeStartState invalid state variables.');
      return;
    }
    // 1) initialize the main View content
    this._initializeView();
    // 2) decide which state to transition to next
    if (this._gameState.isMyTurn()) {
      console.debug("It's your turn.");
      this._controller.setState(PlayModeConstants.EMPTY_TURN);
    } else {
      console.debug("It's not your turn.");
      this._controller.setState(PlayModeConstants.WAIT_FOR_MY_TURN);
    }
  };
  
  //
  // Private (external) methods
  //

  /**
   * Initializes the content in the game-info fieldset.
   */
  PlayModeStartState.prototype._initializeView = function _initializeView() {
    // Create helper text
    var opponentPlayer = this._gameState.getOpponentPlayer();
    var helperText = 'You are playing a game of checkers with ' + opponentPlayer + '. <br/><br/>';
    if (this._gameState.isMyTurn()) {
      helperText += "It's your turn.  Move your piece and click the Submit link.\n";
      helperText += "If you want to erase your move click the Reset link.";
    } else {
      helperText += "It's " + opponentPlayer + " turn.  The page will refresh periodically\n";
      helperText += "and you will be informed when it is your turn.";
    }
    this._view.setHelperText(helperText);
    
    // Adjust current player's indicator
    if (this._gameState.isPlayerRed()) {
      this._view.setRedPlayersName('You');
    }
    if (this._gameState.isPlayerWhite()) {
      this._view.setWhitePlayersName('You');
    }
  };
  
  // export class constructor
  return PlayModeStartState;
  
});
