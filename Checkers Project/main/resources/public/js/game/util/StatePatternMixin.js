/**
 * This module exports the StatePatternMixin class constructor.
 * 
 * This component provides a mixin to imbue another class with
 * methods to manage State-based behavior.
 */
define(function(require){
  'use strict';
  
  /**
   * Constructor function.
   */
  function StatePatternMixin() {
    // internal private attributes
    let _state = null;
    const _stateMap = {};

    /**
     * Add a State definition.
     * 
     * @param {string} stateName
     *    The name of the state.
     * @param {Object} state
     *    The state behavior object.
     */
    this.addStateDefinition = function addStateDefinition(stateName, state) {
      _stateMap[stateName] = state;
    };

    /**
     * Set the state of this object.
     * 
     * It changes the state of this object and (if different) calls the onEntry
     * method of the State object.
     * 
     * @param {string} stateName
     *    The name of the state.
     */
    this.setState = function setState(stateName) {
      // extract any additional args and pass to the onEntry method
      const eventArgs = Array.prototype.slice.call(arguments, 1);
      // find the state and check if this is a change
      const newState = findState(stateName);
      if (_state === newState) return;
      // exit the current state (if known)
      // enter the next state
      setTimeout(function() {
        let logMsg = `Entering state '${newState.getName()}'`;
        logMsg += (eventArgs.length > 0) ? ` with event args: ${eventArgs}` : '';
        console.info(logMsg);
        newState.onEntry(eventArgs);
        _state = newState;
      }, 0); // delay entry until after the DOM updates
    };

    /**
     * Check whether the Component is in the require State; if not throw an exception.
     *
     * @param requiredState  the name of the expected State
     * @throws an Error if the Component is *not* in this State
     */
    this.checkInState = function checkInState(requiredState) {
      if (_state !== findState(requiredState)) {
        throw new Error(`Not in expected state, ${requiredState}, rather in the ${_state.getName()} state.`);
      }
    };

    // Private (internal) State functions

    /**
     * Delegate a method on the controller to the current State object.
     * 
     * @throw Error
     *    If the current State object does not have the specific method.
     */
    this._delegateStateMessage = function delegateStateMessage(actionSymbol, args) {
      if (_state[actionSymbol] !== undefined) {
        _state[actionSymbol].apply(_state, args);
      } else {
        throw new Error(`The '${state.getName()}' doesn't handle a '${actionSymbol}' message.`);
      }
    };
    
    function findState(stateName) {
      if (stateName === null || stateName === undefined) {
        throw new Error("no stateName");
      }
      // find state
      var newState = _stateMap[stateName];
      if (newState === null || newState === undefined) {
        throw new Error("Unknown state: " + stateName);
      }
      //
      return newState;
    }

  }

  // export class constructor
  return StatePatternMixin;
  
});
