/**
 * This module exports a map of functions used in processing Ajax responses.
 */
define(function () {
  'use strict';

  // Private functions

  /**
   * Parse the HTTP response text into a JSON object.
   *
   * @param {string} data  the HTTP response text
   * @return {any}  the JavaScript object represented in the response text
   * @throws {Error}  when the response text is XML/HTML
   */
  function myDataFilter(data) {
    if (data[0] === '<') {
      throw new Error('HTML content detected in Ajax reponse.'
          + '  Did you accidentally perform a redirect in your Spark Route?'
          + '  Sorry, that\'s a no-no.');
    } else {
      return JSON.parse(data);
    }
  }

  /**
   * This function is a generic Ajax error handler.
   * It logs an error to the console and pops-up an alert.
   */
  function handleErrorResponse(xhr, textStatus, error) {
    // error handling
    if (xhr.status === 404) {
      throw new Error('This Ajax call has not been implemented.');
    } else if (xhr.status === 500) {
      throw new Error('This Ajax call threw an exception: ' + error);
    } else if (xhr.status === 0) {
      throw new Error('The WebCheckers server is down.');
    } else {
      throw new Error(`Unknown error (status=${xhr.status}) error: '${error}'`);
    }
  }

  var AjaxUtils = {

    /**
     * Make an Ajax call to the server.
     *
     * <p>
     *   This always uses an HTTP POST action and I would prefer to use the
     *   jQuery post function that provides a very nice Promise API, but in
     *   order to detect when student's code incorrectly sends an HTTP 302 (redirect)
     *   response instead of a JSON response I had to use the raw 'ajax' function
     *   and pass-in a "success callback" that captures the raw XHR object.
     *
     * @param actionURL  the URL for a server Ajax action
     * @param actionData  the data (as an object or a raw string) to send to the server in the body of the POST request
     * @param callback  the developer's handler for the succesful response (expecting JSON)
     * @param callbackContext  the object context within which the callback is executed (the 'this' object)
     */
    callServer: function (actionURL, actionData, callback, callbackContext) {
      var ajaxOptions = {

        // HTTP action
        method: 'POST',
        url: actionURL,

        // action data (as JSON)
        contentType: 'application/json',
        data: (typeof actionData === 'object') ? JSON.stringify(actionData) : actionData,

        // HTTP callback handlers
        beforeSend: function() {
          console.debug(`POST ${actionURL} being sent.`);
        },
        dataFilter: myDataFilter,
        success: callback.bind(callbackContext),
        error: handleErrorResponse,
        complete: function (xhr, textStatus) {
          // log Ajax call
          console.debug(`POST ${actionURL} response complete with '${textStatus}' status.`);
        }
      };

      // send the HTTP request and immediately return; the callback will be invoked asynchronously
      jQuery.ajax(ajaxOptions);
    }
  };

  return AjaxUtils;
});
