/**
 * This module exports a map of functions for inspecting browser (page) information.
 */
define(function(){
    'use strict';

    return {

      /**
       * Get the value of an GET request parameter from a specified URL, which
       * defaults to the current page's URL.
       */
      getParameterByName: function (name, url) {
        if (!url) {
          url = window.location.href;
        }
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) {
          return null;
        }
        if (!results[2]) {
          return '';
        }
        return decodeURIComponent(results[2].replace(/\+/g, " "));
      }
    };
});
