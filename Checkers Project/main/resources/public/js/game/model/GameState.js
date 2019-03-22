/**
 * This module exports the GameState class constructor.
 * 
 * This Value Object holds a snapshot of the state of the checkers game.
 * Information about who the players are, the current player and whose
 * turn is it.
 */
define(function(require){
  'use strict';

  // imports
  const BrowserUtils = require('../util/BrowserUtils');

  /**
   * Constructor function.
   */
  function GameState(gameData) {

    // public (internal) methods

    this.getGameID = function getGameID() {
      return BrowserUtils.getParameterByName('gameID');
    };

    this.getViewMode = function getViewMode() {
      return gameData.viewMode;
    };

    this.getModeOptions = function getModeOptions() {
      return gameData.modeOptions;
    };

    this.getModeOption = function getModeOption(optionName) {
      return gameData.modeOptions[optionName];
    };

    this.getCurrentPlayer = function getCurrentPlayer() {
      return gameData.currentPlayer;
    };

    this.getOpponentPlayer = function getOpponentPlayer() {
      return (gameData.currentPlayer === gameData.redPlayer)
         ? gameData.whitePlayer : gameData.redPlayer;
    };

    this.getRedPlayer = function getRedPlayer() {
      return gameData.redPlayer;
    };

    this.getWhitePlayer = function getWhitePlayer() {
      return gameData.whitePlayer;
    };
    
    this.isRedsTurn = function isRedsTurn() {
      return gameData.activeColor === 'RED';
    };
    
    this.isPlayerRed = function isPlayerRed() {
      return gameData.redPlayer === gameData.currentPlayer;
    };

    this.isPlayerWhite = function isPlayerWhite() {
      return gameData.whitePlayer === gameData.currentPlayer;
    };

  };

  //
  // Public (external) methods
  //

  /**
   * Queries whether it's the current player's turn.
   */
  GameState.prototype.isMyTurn = function isMyTurn() {
    return (this.isPlayerRed() && this.isRedsTurn())
    || (this.isPlayerWhite() && !this.isRedsTurn());
  };
  
  // export class constructor
  return GameState;
  
});
