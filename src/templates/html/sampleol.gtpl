<!doctype html>
<html lang="en">
    <head>
        <link rel="stylesheet" href="http://localhost:8090/geoserver/openlayers3/ol.css" type="text/css">
        <script src="http://localhost:8090/geoserver/openlayers3/ol.js" type="text/javascript"></script>
        <title>GeoETRACS</title>
    </head>
    <body>
        <div id="map"></div>
        <script type="text/javascript">
            
            var getText = function(feature,resolution) {
                var type = 'Normal';
                var text = feature.get('testPIN');
                
                if (resolution > 1200) {
                    text = '';
                } else if (type == 'hide') {
                    text = '';
                } else if (type == 'shorten') {
                    text = text.trunc(12);
                } else if (type == 'wrap') {
                    text = stringDivider(text, 16, '\\n');
                }
                
                return text;
            };
            var defaultStyle = new ol.style.Style({
                fill: new ol.style.Fill({
                    color: [250,250,250,1]
                }),
                stroke: new ol.style.Stroke({
                    color: [220,220,220,1],
                    width: 1
                })
            });
            
            var styleFunction = function () {
                // get the incomeLevel from the feature properties
                return function(feature, resolution) {
                    var pin = feature.get('testPIN');
                    if (feature.get('testPIN') != "${entity.fullpin}")
                        return [defaultStyle];
                    
                    
                    
                    var selectedStyle = new ol.style.Style({
                        fill: new ol.style.Fill({
                            color: [64,196,64,1]
                        }),
                        stroke: new ol.style.Stroke({
                            color: [220,220,220,1],
                            width: 1
                        }),
                        text: new ol.style.Text({
                            textAlign: 'Center',
                            textBaseline: 'Middle',
                            text: getText(feature,resolution),
                            fill: new ol.style.Fill({color: 'blue'}),
                            stroke: new ol.style.Stroke({color: '#ffffff', width: 3}),
                            offsetX: 0,
                            offsetY: 0,
                            rotation: 0
                        })
                    });
                    
                    return [selectedStyle]
                }
            }
            
            var fil = "INTERSECTS(the_geom, querySingle('bukid:BrgyBdry','the_geom','testPIN=''${entity.fullpin}'''))";
            var tile = new ol.layer.Tile({
                source: new ol.source.MapQuest({layer: 'sat'})
            });
            var vector = new ol.layer.Vector({
                source: new ol.source.Vector({
                    format: new ol.format.GeoJSON(),
                    url: 'http://localhost:8090/geoserver/bukid/wfs?service=WFS&version=1.0.0&request=GetFeature&typeName=bukid:BrgyBdry&outputFormat=application/json&srsname=EPSG:3857&CQL_FILTER=' + fil,
                    strategy: ol.loadingstrategy.tile(ol.tilegrid.createXYZ({
                        maxZoom: 19
                    }))
                }),
                style: styleFunction()
            });
            var projection = new ol.proj.Projection({
                code: 'EPSG:3857',
                units: 'm',
            });
            var map = new ol.Map({
                layers: [vector],
                target: 'map',
                view: new ol.View({
                    center: [13910469.0295, 903791.4224],
                    zoom: 9
                })
            });
            
            vector.getSource().on('change', function(evt) {
                extent = vector.getSource().getExtent();
                map.getView().fit(extent, map.getSize());
            });
            
            
            
        </script>
    </body>
</html>