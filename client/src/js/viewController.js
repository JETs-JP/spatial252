const gmapApiKey = "AIzaSyAW2L4j7pzSsyFyR0o32KBWRLwb-IpGaRs";
/*
 * UIのオペレーションを担当するモジュール
 */
define(["async!http://maps.googleapis.com/maps/api/js?key=" + gmapApiKey],
function() {

    function ViewController(canvas, options, backendUrl) {

        var self = this;

        // サーバーのURL
        const ServiceUrl = backendUrl;
        // サーバーアクセスのタイムアウト時間
        const timeout = 120000;

        // Mapオブジェクトの実体
        var map = new google.maps.Map(canvas, options);
        // 現在地
        self.origin = map.getCenter();
        // 選択中の避難所
        self.refuge_selected;
        // 選択中の避難所までの経路情報
        self.current_direction;
        // 表示されている避難所のマーカー
        self.refuge_markers = [];
        // 通行止めになった領域
        self.disabled_polygons = [];

        // 現在地のマーカーを表示する
        // TODO: 現在地をマウスのドラッグで移動できるようにする
        self.showOrigin = function() {
            var marker = new google.maps.Marker({
                position: self.origin,
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 8,
                    fillColor: '#0088FF',
                    fillOpacity: 0.8,
                    strokeWeight: 3,
                    strokeColor: '#004499',
                    strokeOpacity: 1.0
                }
            });
            marker.setMap(map);
        }

        self.setSelectedRefuge = function(refuge) {
            self.refuge_selected = refuge;
        }

        // 現在地の近傍にある避難所をマップに描画する
        self.showNearestRefuges = function(limit, c) {
            $.ajax({
                url: ServiceUrl + "refuges?org_lat=" + self.origin.lat() + "&org_lon=" + self.origin.lng() + "&limit=" + limit,
                contentType: 'application/json; charset=utf-8',
                success: function(refuges) {
                    for (var i = 0; i < refuges.length; i++) {
                        drawRefuge(refuges[i], c);
                    }
                    fitRefuges();
                },
                error: function (jq, status, err) {
                    console.log(err);
                },
                timeout: timeout
            });
        }

        // 指定した避難所をマップに追加する
        self.addRefuge = function(id, c) {
            $.ajax({
                url: ServiceUrl + "refuges/" + id + "?org_lat=" + self.origin.lat() + "&org_lon=" + self.origin.lng(),
                contentType: 'application/json; charset=utf-8',
                success: function(refuge) {
                    drawRefuge(refuge, c);
                },
                error: function (jq, status, err) {
                    console.log(err);
                },
                timeout: timeout
            });
        }

        function drawRefuge(refuge, c) {
            var position = new google.maps.LatLng(refuge.location.lat, refuge.location.lon);
            var image;
            if (!refuge.enabled) {
                image = 'images/disabled.png';
            } else {
                image = '';
            }
            var marker = new google.maps.Marker({
                position: position,
                title: refuge.name,
                icon: image,
                animation: google.maps.Animation.DROP
            });
            marker.setMap(map);
            var infoWindow = new google.maps.InfoWindow({
                content: refuge.name + "<br><b>" + Math.round(refuge.direction.cost) + " m</b>"
            });
            infoWindow.open(map, marker);
            marker.addListener('click', (new c(refuge)).callback);
            self.refuge_markers.push(marker);
        }

        function fitRefuges() {
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(self.origin);
            for (var i = 0; i < self.refuge_markers.length; i++) {
                bounds.extend(self.refuge_markers[i].position);
            }
            map.fitBounds(bounds);
        }

        self.flushRefuges = function() {
            for (var i in self.refuge_markers) {
                self.refuge_markers[i].setMap(null);
            }
            self.refuge_markers = [];
        }

        // 避難所までの経路の描画
        self.showDirection = function() {
            $.ajax({
                url: ServiceUrl + "?org_lat=" + self.origin.lat() + "&org_lon=" + self.origin.lng()
                        + "&dst_lat=" + self.refuge_selected.location.lat + "&dst_lon=" + self.refuge_selected.location.lon,
                contentType: 'application/json; charset=utf-8',
                success: function(direction) {
                    drawDirection(direction);
                    fitDirection();
                },
                error: function (jq, status, err) {
                    console.log(err);
                },
                timeout: timeout
            });
        }

        function drawDirection(direction) {
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
        }

        function fitDirection() {
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(self.origin);
            bounds.extend(new google.maps.LatLng(
                        self.refuge_selected.location.lat,
                        self.refuge_selected.location.lon));
            map.fitBounds(bounds);
        }

        self.flushDirection = function() {
            if (self.current_direction) {
                self.current_direction.setMap(null);
                self.current_direction = null;
            }
        }

        self.showProhibitedAreas = function() {
            $.ajax({
                url: ServiceUrl + "area",
                contentType: 'application/json; charset=utf-8',
                success: function(polygons) {
                    drawProhibitedAreas(polygons);
                },
                error: function (jq, status, err) {
                    console.log(err);
                },
                timeout: timeout
            });
        }

        function drawProhibitedAreas(polygons) {
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
                self.disabled_polygons.push(area);
                area.setMap(map);
                var infoWindow = new google.maps.InfoWindow({
                    content: polygons[i].description,
                    position: points[0]
                });
                infoWindow.open(map);
            }
        }

        self.flushProhibitedAreas = function() {
            for (var i in self.disabled_polygons) {
                self.disabled_polygons[i].setMap(null);
            }
            self.disabled_polygons = [];
        }

    }

    return ViewController;
});
