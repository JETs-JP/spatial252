const gmapApiKey = "AIzaSyAW2L4j7pzSsyFyR0o32KBWRLwb-IpGaRs";
/*
 * UIのオペレーションを担当するモジュール
 */
define(["async!http://maps.googleapis.com/maps/api/js?key=" + gmapApiKey],
function() {

    function NavigationController(canvas, options, backendUrl) {

        var self = this;

        // サーバーのURL
        const backend = backendUrl;
        // サーバーアクセスのタイムアウト時間
        const timeout = 120000;
        // Mapオブジェクトの実体
        const map = new google.maps.Map(canvas, options);

        // 現在地
        var origin = map.getCenter();
        // 選択中の目的地（避難所）
        var destination;
        // 選択中の避難所までの経路情報
        var displayedDirection;
        // 表示されている避難所のマーカー
        var displayedMarkers = [];
        // 通行止めになった領域
        var displayedProhibitedAreas = [];

        // 現在地のマーカーを表示する
        self.showOrigin = function() {
            var marker = new google.maps.Marker({
                position: origin,
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

        self.setDestination = function(refuge) {
            destination = refuge;
        }

        // 現在地の近傍にある避難所をマップに描画する
        self.showNearestRefuges = function(limit, c) {
            $.ajax({
                url: backend + "refuges/actions/search/nearest?org_lat=" + origin.lat() + "&org_lng=" + origin.lng() + "&limit=" + limit,
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
                url: backend + "refuges/" + id + "?org_lat=" + origin.lat() + "&org_lng=" + origin.lng(),
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
            var position = new google.maps.LatLng(refuge.location.lat, refuge.location.lng);
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
            displayedMarkers.push(marker);
        }

        function fitRefuges() {
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(origin);
            for (var i = 0; i < displayedMarkers.length; i++) {
                bounds.extend(displayedMarkers[i].position);
            }
            map.fitBounds(bounds);
        }

        self.flushRefuges = function() {
            for (var i in displayedMarkers) {
                displayedMarkers[i].setMap(null);
            }
            displayedMarkers = [];
        }

        // 避難所までの経路の描画
        self.showDirection = function() {
            $.ajax({
                url: backend + "directions/actions/get/between?org_lat=" + origin.lat() + "&org_lng=" + origin.lng()
                        + "&dst_lat=" + destination.location.lat + "&dst_lng=" + destination.location.lng,
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
                        direction.wayPoints[i].lng));
            }
            displayedDirection = new google.maps.Polyline({
                path: path,
                strokeColor: '#0000FF',
                strokeOpacity: 0.5,
                strokeWeight: 8
            });
            displayedDirection.setMap(map);
        }

        function fitDirection() {
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(origin);
            bounds.extend(new google.maps.LatLng(
                        destination.location.lat,
                        destination.location.lng));
            map.fitBounds(bounds);
        }

        self.flushDirection = function() {
            if (displayedDirection) {
                displayedDirection.setMap(null);
                displayedDirection = null;
            }
        }

        self.showProhibitedAreas = function() {
            $.ajax({
                url: backend + "prohibitedareas",
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
                    points.push({lat: polygons[i].coordinates[j].lat, lng: polygons[i].coordinates[j].lng});
                }
                var area = new google.maps.Polygon({
                    paths: points,
                    strokeColor: '#FF0000',
                    strokeOpacity: 0.8,
                    strokeWeight: 2,
                    fillColor: '#FF0000',
                    fillOpacity: 0.35
                });
                displayedProhibitedAreas.push(area);
                area.setMap(map);
                var infoWindow = new google.maps.InfoWindow({
                    content: polygons[i].description,
                    position: points[0]
                });
                infoWindow.open(map);
            }
        }

        self.flushProhibitedAreas = function() {
            for (var i in displayedProhibitedAreas) {
                displayedProhibitedAreas[i].setMap(null);
            }
            displayedProhibitedAreas = [];
        }

    }

    return NavigationController;
});
