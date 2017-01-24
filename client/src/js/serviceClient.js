/*
 * サーバーサイドとのやりとりを担当するモジュール
 */
define([],
function() {

    function ServiceClient() {

        var self = this;
        // サーバーサイドのURL
        var ServiceUrl = "http://localhost:8080/directions/";
        // サーバーアクセスのタイムアウト時間
        const timeout = 120000;

        self.getRefuges = function getRefuges(origin, limit, deferred) {
            $.ajax({
                url: ServiceUrl + "refuges?org_lat=" + origin.lat() + "&org_lon=" + origin.lng() + "&limit=" + limit,
                contentType: 'application/json; charset=utf-8',
                success: function(result) {
                    deferred.resolve(result);
                },
                error: function (jq, status, err) {
                    console.log(err);
                },
                timeout: timeout
            });
        }

        self.getDirection = function getDirection(origin, destination, deferred) {
            $.ajax({
                url: ServiceUrl + "?org_lat=" + origin.lat() + "&org_lon=" + origin.lng()
                        + "&dst_lat=" + destination.location.lat + "&dst_lon=" + destination.location.lon,
                contentType: 'application/json; charset=utf-8',
                success: function(result) {
                    deferred.resolve(result);
                },
                error: function (jq, status, err) {
                    console.log(err);
                },
                timeout: timeout
            });
        }

        self.getDisabledPolygons = function getDisabledPolygons(deferred) {
            $.ajax({
                url: ServiceUrl + "area",
                contentType: 'application/json; charset=utf-8',
                success: function(result) {
                    deferred.resolve(result);
                },
                error: function (jq, status, err) {
                    console.log(err);
                },
                timeout: timeout
            });
        }

        self.eventSource = new EventSource(ServiceUrl + "sseconnect", {withCredentials: false});

    }

    return ServiceClient;
});
