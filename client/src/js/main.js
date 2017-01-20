/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
'use strict';

requirejs.config({
    baseUrl: 'js',
    // Path mappings for the logical module names
    paths:
    //injector:mainReleasePaths
    {
        'knockout': 'libs/knockout/knockout-3.4.0.debug',
        'jquery': 'libs/jquery/jquery-3.1.0',
        'jqueryui-amd': 'libs/jquery/jqueryui-amd-1.12.0',
        'promise': 'libs/es6-promise/es6-promise',
        'hammerjs': 'libs/hammer/hammer-2.0.8',
        'ojdnd': 'libs/dnd-polyfill/dnd-polyfill-1.0.0',
        'ojs': 'libs/oj/v2.1.0/debug',
        'ojL10n': 'libs/oj/v2.1.0/ojL10n',
        'ojtranslations': 'libs/oj/v2.1.0/resources',
        'text': 'libs/require/text',
        'signals': 'libs/js-signals/signals',
        'async': 'libs/require-plugins/src/async'
    }
    //endinjector
    ,
    // Shim configurations for modules that do not expose AMD
    shim:
    {
        'jquery': {
            exports: ['jQuery', '$']
        }
    }
}
);

/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require(['ojs/ojcore', 'knockout', 'appController', 'ojs/ojknockout', 'ojs/ojmodule', 'ojs/ojrouter', 'ojs/ojnavigationlist'],
function(oj, ko, app) {
    $(function() {
        function init() {
            oj.Router.sync().then(
                function () {
                    ko.applyBindings(app, document.getElementById('globalBody'));
                },
                function (error) {
                    oj.Logger.error('Error in root start: ' + error.message);
                }
            );
        }
        // If running in a hybrid (e.g. Cordova) environment, we need to wait for the deviceready 
        // event before executing any code that might interact with Cordova APIs or plugins.
        if ($(document.body).hasClass('oj-hybrid')) {
            document.addEventListener("deviceready", init);
        } else {
            init();
        }
    });
}
);