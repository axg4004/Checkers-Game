/**
 * This module exports the PieceMoveEvent class constructor.
 * 
 * The event object for when a piece is moved on the board.
 */
define(function(require){
  'use strict';

  /**
   * Constructor function.
   * 
   * @param {any} a jQuery object holding the Piece element
   * @param {Move} the specific move for this event
   */
  function PieceMoveEvent($piece, move) {
    // private attributes
    this.$piece = $piece;
    this.move = move;
  };

  // export class constructor
  return PieceMoveEvent;
  
});
