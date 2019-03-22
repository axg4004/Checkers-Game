/**
 * This module exports a map of constants used in the Game view code.
 */
define(function(){
    'use strict';

    /**
     * This module is a map of constant symbols to their names.
     * Used in methods to change GameView states.
     */
    return {
      START_STATE: 'START_STATE',
      EMPTY_TURN: 'EMPTY_TURN',
      PENDING_MOVE: 'PENDING_MOVE',
      STABLE_TURN: 'STABLE_TURN',
      WAIT_FOR_MY_TURN: 'WAIT_FOR_MY_TURN',
      CHECK_MY_TURN: 'CHECK_MY_TURN'
    };
});