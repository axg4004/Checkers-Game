/**
 * This module exports the ControlsToolbarMixin class constructor.
 * 
 * This component provides a mixin to imbue another class with
 * methods to create and manage (enable/disable) buttons in the
 * 'Controls' fieldset of the Game View UI.
 */
define(function(require){
  'use strict';
  
  var DISABLED = 'disabled';
  
  /**
   * Constructor function.
   */
  function ControlsToolbarMixin() {
    // internal private attributes
    var _buttonMap = {};
    var $toolbar = $('div.toolbar');

    /**
     * Add a button to the 'Controls' toolbar.
     * 
     * @param {string} id
     *    The unique HTML id for this button.
     * @param {string} label
     *    The label text for this button.
     * @param {boolean} enabled
     *    Whether this button should start enabled or not.
     * @param {string} tooltip
     *    The hover tooltip text for this button.
     * @param {function} actionMethod
     *    The event handler for this button.
     */
    this.addButton = function addButton(id, label, enabled, tooltip, actionMethod) {
      // create the button element
      var $button = $('<button/>', {
        type: 'button',
        id: id,
        text: label,
        title: tooltip
      });
      // attach click handler
      $button.on('click', makeViewEventHandler(actionMethod.bind(this)));
      // inject button into toolbar
      $toolbar.append($button);
      // store the button in the map
      _buttonMap[id] = $button;
      // disable it initially?
      if (!enabled) this.disableButton(id);
    };

    /**
     * Disable a Control button.
     * 
     * @param {string} id
     *    The unique HTML id for this button.
     */
    this.disableButton = function disableButton(id) {
      getButton(id).attr('disabled', 'disabled');
    };

    /**
     * Disable a Control button.
     * 
     * @param {string} id
     *    The unique HTML id for this button.
     */
    this.enableButton = function enableButton(id) {
      getButton(id).removeAttr(DISABLED);
    };

    // internal private methods

    function makeViewEventHandler(actionMethod) {
      return function(event) {
        // short-circuit if disabled
        if ($(this).is("[disabled]")) {
          event.preventDefault();
          return;
        }
        // invoke the action
        actionMethod(event);
      }
    }

    /**
     * Lookup a Control button.
     * 
     * @param {string} id
     *    The unique HTML id for this button.
     *
     * @return
     *    The button for this id.
     *
     * @throw
     *    Throws an error if the id is invalid.
     */
    function getButton(id) {
      if (id === null || id === undefined) {
        throw new Error("no button id");
      }
      // find state
      var $button = _buttonMap[id];
      if ($button === null || $button === undefined) {
        throw new Error("Unknown button: " + id);
      }
      //
      return $button;
    }
  };

  // export class constructor
  return ControlsToolbarMixin;
  
});
