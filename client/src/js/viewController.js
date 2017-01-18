/*
 * UIのオペレーションを担当するモジュール
 */
define(["async!http://maps.googleapis.com/maps/api/js?key=AIzaSyAW2L4j7pzSsyFyR0o32KBWRLwb-IpGaRs"],
function() {

    function ViewController(canvas, origin, options) {

        var self = this;
        // Mapオブジェクトの実体
        var map = new google.maps.Map(canvas, options);
        // 現在地
        var origin = origin;

        var refuge_selected;

        // 指定した避難所をマップに描画する
        self.showRefuges = function showRefuges(refuges) {
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(origin);
            for (var i in refuges) {
                var position =
                    new google.maps.LatLng(refuges[i].location.lat, refuges[i].location.lon);
                var marker = new google.maps.Marker({
                    position: position,
                    title: refuges[i].name
                });
                attachMarkerCallback(marker, refuges[i]);
                bounds.extend(position);
                var infoWindow = new google.maps.InfoWindow({
                    content: refuges[i].name + "<br>" + refuges[i].address
                });
                marker.setMap(map);
                infoWindow.open(map, marker);
            }
            map.fitBounds(bounds);
        }

        // 指定した経路をマップに描画する
        self.showDirection= function showDirection(direction) {
            var path = [];
            for (var i in direction.wayPoints) {
                path.push(new google.maps.LatLng(
                        direction.wayPoints[i].lat,
                        direction.wayPoints[i].lon));
            }
            var line = new google.maps.Polyline({
                path: path,
                strokeColor: '#0000FF',
                strokeOpacity: 0.5,
                strokeWeight: 8
            });
            line.setMap(map);
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(new google.maps.LatLng(
                        direction.wayPoints[0].lat,
                        direction.wayPoints[0].lon));
            bounds.extend(new google.maps.LatLng(
                        direction.wayPoints[path.length - 1].lat,
                        direction.wayPoints[path.length - 1].lon));
            map.fitBounds(bounds);
        }

        function attachMarkerCallback(marker, refuge) {
            marker.addListener('click', function() {
                self.refuge_selected = refuge;
                $(".refuge_name").text(refuge.name);
                self.openDrawer();
            });
        }

        var drawer = {
            "selector": "#bottomDrawer",
            "edge": "bottom",
            "autoDismiss": "none",
            "displayMode": "overlay",
            "modality": "modeless",
        }

        self.openDrawer = function() {
            return oj.OffcanvasUtils.open(drawer);
        };

        self.closeDrawer = function() {
            return oj.OffcanvasUtils.close(drawer);
        };

        self.getSelectedRefuges  = function() {
            self.refuge_selected;
        };
    }

    return ViewController;
});
