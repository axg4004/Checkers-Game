/**
 * This module exports a map of constants used in the PLAY mode.
 */
define(function(){
    'use strict';

    /**
     * This module is a map of constant symbols to their names.
     * Used in methods to change GameView Play mode states.
     */
    return {
      
      //
      // States
      //
      
      PLAY_MODE_STARTING: 'Play Mode Starting Up'
      ,EMPTY_TURN: 'Empty Turn'
      ,VALIDATING_MOVE: 'Validating Move on the Server'
      ,STABLE_TURN: 'Stable Turn'
      ,REQUESTING_BACKUP_MOVE: 'Requesting Backup Move on the Server'
      ,WAIT_FOR_MY_TURN: 'Waiting for My Turn'
      ,CHECK_MY_TURN: 'Checking for My Turn on the Server'

      //
      // Buttons
      //

      ,BACKUP_BUTTON_ID: 'backupBtn'
      ,BACKUP_BUTTON_TOOLTIP: 'Remove the last move with your current turn.'
      ,SUBMIT_BUTTON_ID: 'submitBtn'
      ,SUBMIT_BUTTON_TOOLTIP: 'Commit your current turn to the server.'
      ,RESIGN_BUTTON_ID: 'resignBtn'
      ,RESIGN_BUTTON_TOOLTIP: 'Resign from the game.'

    };
});