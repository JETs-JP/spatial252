/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/*
 * Your dashboard ViewModel code goes here
 */
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'viewController', 'ojs/ojbutton', 'ojs/ojdialog', 'ojs/ojoffcanvas'],
function(oj, ko, $, app, view) {

    function DashboardViewModel() {

        var self = this;
        // Header Config
        self.headerConfig = {'viewName': 'header', 'viewModelFactory': app.getHeaderModel()};

        const backendUrl = "http://localhost:8080/directions/";

        self.sseEventSource = new EventSource(backendUrl + "sseconnect", {withCredentials: false});

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
            self.goHome();
        };

        function initialize() {
            // View Controller
            var map_canvas = document.getElementById("map_canvas");
            var map_state = {
                zoom: 16,
                center: new google.maps.LatLng(35.659719, 139.699056),
                scaleControl: true
            };
            this.vc = new view(map_canvas, map_state, backendUrl);
            // SSE Event Listeners
            self.sseEventSource.addEventListener("disable_area", function(e) {
                $("#directionDialog").ojDialog({cancelBehavior: "none"}).ojDialog("open");
            }, false);
            self.sseEventSource.addEventListener("disable_refuge", function(e) {
                $(".reason").text(e.data);
                $("#disableRefugeDialog").ojDialog({cancelBehavior: "none"}).ojDialog("open");
            }, false);
            self.sseEventSource.addEventListener("add_refuge", function(e) {
                vc.addRefuge(e.data, c);
            }, false);
        }

        self.navigate = function() {
            closeDrawer();
            closeDialogs();
            vc.flushDirection();
            vc.showProhibitedAreas();
            vc.showDirection();
        };

        self.goHome = function() {
            closeDrawer();
            closeDialogs();
            vc.flushRefuges();
            vc.flushDirection();
            vc.flushProhibitedAreas();
            vc.showOrigin();
            vc.showNearestRefuges(3, c);
            vc.showProhibitedAreas();
        }

        var c = function markerCallback(refuge) {

            this.callback = function callback() {
                vc.setSelectedRefuge(refuge);
                var url = "images/" + refuge.id + ".jpg";
                $("#image-box").children("img").attr({'src': url});
                $(".refuge_name").text(shorten(refuge.name));
                $(".refuge_distance").text(Math.round(refuge.direction.cost) + " m");
                $(".refuge_congestion").text(refuge.congestion + " 人");
                $(".refuge_food").text(getStockIcon(refuge.food));
                $(".refuge_blanket").text(getStockIcon(refuge.blanket));
                $(".refuge_accessible").text(getBoolIcon(refuge.accessible));
                $(".refuge_milk").text(getStockIcon(refuge.milk));
                $(".refuge_babyFood").text(getStockIcon(refuge.babyFood));
                $(".refuge_nursingRoom").text(getBoolIcon(refuge.nursingRoom));
                $(".refuge_sanitaryGoods").text(getStockIcon(refuge.sanitaryGoods));
                $(".refuge_napkin").text(getStockIcon(refuge.napkin));
                $(".refuge_pet").text(getBoolIcon(refuge.pet));
                $(".refuge_bath").text(getBoolIcon(refuge.bath));
                $(".refuge_multilingual").text(getBoolIcon(refuge.multilingual));
                openDrawer();
            }

            function getBoolIcon(bool) {
                if (bool) {
                    return "可";
                }
                return "不可";
            }

            function getStockIcon(degree) {
                if (degree === 3) {
                    return "◎";
                } else if (degree === 2) {
                    return "△";
                }
                return "×";
            }

            function shorten(str) {
                if (str.length > 16) {
                    return str.substring(0, 16) + "…";
                }
                return str;
            }

        }

        function closeDialogs() {
            $("#directionDialog").ojDialog("close");
            $("#disableRefugeDialog").ojDialog("close");
            $("#addRefugeDialog").ojDialog("close");
        }

        var drawer = {
            "selector": "#bottomDrawer",
            "edge": "bottom",
            "autoDismiss": "none",
            "displayMode": "overlay",
            "modality": "modeless",
        }

        function openDrawer() {
            oj.OffcanvasUtils.open(drawer);
        }

        function closeDrawer() {
            return oj.OffcanvasUtils.close(drawer);
        }

    }

    /*
     * Returns a constructor for the ViewModel so that the ViewModel is constrcuted
     * each time the view is displayed.  Return an instance of the ViewModel if
     * only one instance of the ViewModel is needed.
     */
    return new DashboardViewModel();

}
);
