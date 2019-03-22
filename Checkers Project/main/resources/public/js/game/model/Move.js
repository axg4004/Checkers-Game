/**
 * This module exports the Move class constructor.
 * 
 * This component is an Information Expert on checkers game moves.
 */
define(function(require){
  'use strict';
  
  // imports
  const Position = require('./Position');

  /**
   * Constructor function.
   */
  function Move(start, end) {
    // validate
    if (! start instanceof Position) {
      throw new Error('start (' + start + ') is not a Position.');
    }
    if (! end instanceof Position) {
      throw new Error('end (' + end + ') is not a Position.');
    }
    //
    this.start = start;
    this.end = end;
  };

  /**
   * Create a Move object that is the reverse this move.
   */
  Move.prototype.reverse = function reverse() {
    return new Move(this.end, this.start);
  };
  
  // export class constructor
  return Move;
  
});
