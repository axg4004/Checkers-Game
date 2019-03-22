/**
 * This module exports the GameView class constructor.
 * 
 * This component manages the Client-side behavior of the Game view.
 */
define(function(require){
  'use strict';
  
  // imports
  const BoardController = require('./BoardController');
  const GameState = require('./model/GameState');
  
  // MVP imports
  const PlayController = require('./modes/play/PlayController');

  // Project Enhancement imports
  const ReplayController = require('./modes/replay/ReplayController');
  const SpectatorController = require('./modes/spectator/SpectatorController');
  // more?

  //
  // Constants
  //

  var PLAY_MODE = 'PLAY';
  var SPECTATOR_MODE = 'SPECTATOR';
  var REPLAY_MODE = 'REPLAY';

  //
  // Constructor
  //

  /**
   * Constructor function.
   */
  function GameView(gameData) {
    // private data
    this._gameState = new GameState(gameData);
    this._boardController = new BoardController(this._gameState);
  }

  //
  // Public (external) methods
  //

  GameView.prototype.startup = function startup() {
    // initialize the Info fieldset with the player's names
    this.setRedPlayersName(this._gameState.getRedPlayer());
    this.setWhitePlayersName(this._gameState.getWhitePlayer());
    this.setTurnFlasher();
    
    // launch the Mode controller
    switch (this._gameState.getViewMode()) {
    case PLAY_MODE:
      console.debug('Play mode');
      this._modeController = new PlayController(this, this._boardController, this._gameState);
      break;
    case SPECTATOR_MODE:
      console.debug('Spectator mode');
      this._modeController = new SpectatorController(this, this._gameState);
      break;
    case REPLAY_MODE:
      console.debug('Replay mode');
      this._modeController = new ReplayController(this, this._gameState);
      break;
    default:
      alert('Unknown view module: ' + this._gameState.getViewMode());
      return;
    }
    this._modeController.startup();
  };

  GameView.prototype.setHelperText = function setHelperText(helpTextHTML) {
    jQuery("p#help_text").html(helpTextHTML);
  };

  GameView.prototype.setRedPlayersName = function setRedPlayersName(name) {
    jQuery("fieldset#game-info table[data-color='RED'] td.name").text(name);
  };

  GameView.prototype.setWhitePlayersName = function setWhitePlayersName(name) {
    jQuery("fieldset#game-info table[data-color='WHITE'] td.name").text(name);
  };

  GameView.prototype.setTurnFlasher = function setTurnFlasher() {
    var activeColor = this._gameState.isRedsTurn() ? 'RED' : 'WHITE';
    var inactiveColor = this._gameState.isRedsTurn() ? 'WHITE' : 'RED';
    jQuery("fieldset#game-info table[data-color='" + activeColor + "']").addClass('isMyTurn');
    jQuery("fieldset#game-info table[data-color='" + inactiveColor + "']").removeClass('isMyTurn');
  };

  GameView.prototype.displayMessage = function displayMessage(message) {
    jQuery('#message').attr('class', message.type).html(message.text).slideDown(400);
  };

  // export class constructor
  return GameView;
  
});

