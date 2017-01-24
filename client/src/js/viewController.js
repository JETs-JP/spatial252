/*
 * UIのオペレーションを担当するモジュール
 */
define(["async!http://maps.googleapis.com/maps/api/js?key=AIzaSyAW2L4j7pzSsyFyR0o32KBWRLwb-IpGaRs"],
function() {

    function ViewController(canvas, options) {

        var self = this;
        // Mapオブジェクトの実体
        var map = new google.maps.Map(canvas, options);
        // 現在地
        self.origin = map.getCenter();

        self.refuge_selected;

        self.current_direction;

        self.showOrigin = function showOrigin() {
            var marker = new google.maps.Marker({
                position: self.origin,
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 8,
                    fillColor: '#0088FF',
                    fillOpacity: 0.5,
                    strokeWeight: 3,
                    strokeColor: '#004499',
                    strokeOpacity: 1.0
                }
            });
            marker.setMap(map);
        }

        // 指定した避難所をマップに描画する
        self.showRefuges = function showRefuges(refuges) {
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(self.origin);
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
                    content: refuges[i].name + "<br><b>" + Math.round(refuges[i].distance) + " m</b>"
                });
                marker.setMap(map);
                infoWindow.open(map, marker);
            }
            map.fitBounds(bounds);
        }

        // 指定した経路をマップに描画する
        self.showDirection = function showDirection(direction) {
            var path = [];
            for (var i in direction.wayPoints) {
                path.push(new google.maps.LatLng(
                        direction.wayPoints[i].lat,
                        direction.wayPoints[i].lon));
            }
            self.current_direction = new google.maps.Polyline({
                path: path,
                strokeColor: '#0000FF',
                strokeOpacity: 0.5,
                strokeWeight: 8
            });
            self.current_direction.setMap(map);
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(new google.maps.LatLng(
                        direction.wayPoints[0].lat,
                        direction.wayPoints[0].lon));
            bounds.extend(new google.maps.LatLng(
                        direction.wayPoints[path.length - 1].lat,
                        direction.wayPoints[path.length - 1].lon));
            map.fitBounds(bounds);
        }

        self.hideDirection = function hideDirection() {
            if (self.current_direction) {
                self.current_direction.setMap(null);
                self.current_direction = null;
            }
        }

        function attachMarkerCallback(marker, refuge) {
            marker.addListener('click', function() {
                self.refuge_selected = refuge;
                $(".refuge_name").text(refuge.name);
                $(".refuge_distance").text(Math.round(refuge.distance) + " m");
                self.openDrawer();
            });
        }

        self.showDisabledPolygons = function showDisabledPolygons(polygons) {
            for (var i in polygons) {
                var points = [];
                for (var j in polygons[i].coordinates) {
                    points.push({lat: polygons[i].coordinates[j].lat, lng: polygons[i].coordinates[j].lon});
                }
                var area = new google.maps.Polygon({
                    paths: points,
                    strokeColor: '#FF0000',
                    strokeOpacity: 0.8,
                    strokeWeight: 2,
                    fillColor: '#FF0000',
                    fillOpacity: 0.35
                });
                area.setMap(map);
                var infoWindow = new google.maps.InfoWindow({
                    content: polygons[i].description,
                    position: points[0]
                });
                infoWindow.open(map);
            }
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
        }

        self.closeDrawer = function() {
            return oj.OffcanvasUtils.close(drawer);
        }

        self.openDialog = function() {
            $("#directionDialog").ojDialog({cancelBehavior: "none"}).ojDialog("open");
        }

        self.closeDialog = function() {
            $("#directionDialog").ojDialog("close");
        }

    }

    return ViewController;
});
