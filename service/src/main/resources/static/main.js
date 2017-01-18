var directionsDisplay;
var directionsService;
var map;

function initialize() {
    directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: false});
    directionsService = new google.maps.DirectionsService();
    var mapOptions = {
        zoom: 18,
        center: new google.maps.LatLng(35.659719, 139.699056),
        scaleControl: true,
    }
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    directionsDisplay.setMap(map);
    getDirection();
}

function getDirection() {
    $.ajax({
        url: "http://" + location.host + "/directions/refuges?org_lat=35.6595953&org_lon=139.6987158&limit=6",
        contentType: 'application/json; charset=utf-8',
        success: function(result) {
                     var bounds = new google.maps.LatLngBounds();
                     for (var i in result) {
                         var path = [];
                         for (var j in result[i].direction.wayPoints) {
                             path.push(new google.maps.LatLng(
                                         result[i].direction.wayPoints[j].lat,
                                         result[i].direction.wayPoints[j].lon));
                         }
                         var line = new google.maps.Polyline({
                             path: path,
                             strokeColor: '#0000FF',
                             strokeOpacity: 0.5,
                             strokeWeight: 8,
                         });
                         line.setMap(map);
                         var marker = new google.maps.Marker({
                             position: path[path.length - 1],
                             title: result[i].name,
                         });
                         bounds.extend(path[path.length - 1]);
                         var infoWindow = new google.maps.InfoWindow({
                             content: result[i].name + "<br>" + result[i].address,
                         });
                         marker.setMap(map);
                         infoWindow.open(map, marker);
                     }
                     map.fitBounds (bounds);
                 },
        error: function(jq, status, err) {},
        timeout: 120000,
    });
}
