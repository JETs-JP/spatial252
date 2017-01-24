/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/*
 * Your dashboard ViewModel code goes here
 */
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'serviceClient', 'viewController', 'ojs/ojbutton', 'ojs/ojdialog', 'ojs/ojoffcanvas'],
function(oj, ko, $, app, service, view) {

    function DashboardViewModel() {

        var self = this;
        // Header Config
        self.headerConfig = {'viewName': 'header', 'viewModelFactory': app.getHeaderModel()};

        function initialize() {
            // View Controller
            var map_canvas = document.getElementById("map_canvas");
            var map_state = {
                zoom: 16,
                center: new google.maps.LatLng(35.659719, 139.699056),
                scaleControl: true
            };
            this.vc = new view(map_canvas, map_state);
            // Service Client
            this.sc = new service();
            this.sc.eventSource.onmessage = function(e) {
                vc.openDialog();
            };
        }

        self.navi = function() {
            vc.closeDrawer();
            vc.closeDialog();
            var dfd = new $.Deferred;
            sc.getDirection(vc.origin, vc.refuge_selected, dfd);
            dfd.then(function(result) {
                vc.hideDirection();
                vc.showDirection(result);
            });
        };

        // Below are a subset of the ViewModel methods invoked by the ojModule binding
        // Please reference the ojModule jsDoc for additionaly available methods.

        /**
         * Optional ViewModel method invoked when this ViewModel is about to be
         * used for the View transition.  The application can put data fetch logic
         * here that can return a Promise which will delay the handleAttached function
         * call below until the Promise is resolved.
         * @param {Object} info - An object with the following key-value pairs:
         * @param {Node} info.element - DOM element or where the binding is attached. This may be a 'virtual' element (comment node).
         * @param {Function} info.valueAccessor - The binding's value accessor.
         * @return {Promise|undefined} - If the callback returns a Promise, the next phase (attaching DOM) will be delayed until
         * the promise is resolved
         */
        self.handleActivated = function(info) {
            // Implement if needed
        };

        /**
        * Optional ViewModel method invoked after the View is inserted into the
        * document DOM.  The application can put logic that requires the DOM being
        * attached here.
        * @param {Object} info - An object with the following key-value pairs:
        * @param {Node} info.element - DOM element or where the binding is attached. This may be a 'virtual' element (comment node).
        * @param {Function} info.valueAccessor - The binding's value accessor.
        * @param {boolean} info.fromCache - A boolean indicating whether the module was retrieved from cache.
        */
        self.handleAttached = function(info) {
            // Implement if needed
        };

        /**
        * Optional ViewModel method invoked after the bindings are applied on this View. 
        * If the current View is retrieved from cache, the bindings will not be re-applied
        * and this callback will not be invoked.
        * @param {Object} info - An object with the following key-value pairs:
        * @param {Node} info.element - DOM element or where the binding is attached. This may be a 'virtual' element (comment node).
        * @param {Function} info.valueAccessor - The binding's value accessor.
        */
        self.handleBindingsApplied = function(info) {
            initialize();

            var rdfd = new $.Deferred;
            sc.getRefuges(vc.origin, 3, rdfd);
            rdfd.then(function(result) {
                vc.showRefuges(result);
            });
            var pdfd = new $.Deferred;
            sc.getDisabledPolygons(pdfd);
            pdfd.then(function(result) {
                console.log(result);
                vc.showDisabledPolygons(result);
            });
            vc.showOrigin();
        };

        /*
        * Optional ViewModel method invoked after the View is removed from the
        * document DOM.
        * @param {Object} info - An object with the following key-value pairs:
        * @param {Node} info.element - DOM element or where the binding is attached. This may be a 'virtual' element (comment node).
        * @param {Function} info.valueAccessor - The binding's value accessor.
        * @param {Array} info.cachedNodes - An Array containing cached nodes for the View if the cache is enabled.
        */
        self.handleDetached = function(info) {
            // Implement if needed
        };
    }

    /*
     * Returns a constructor for the ViewModel so that the ViewModel is constrcuted
     * each time the view is displayed.  Return an instance of the ViewModel if
     * only one instance of the ViewModel is needed.
     */
    return new DashboardViewModel();

}
);
