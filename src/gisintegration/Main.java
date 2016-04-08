/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package gisintegration;

/**
 *
 * @author rufino
 */

import java.io.File;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {
    
    
    @Override
    public void start(Stage primaryStage) {
        StringBuilder sb = new StringBuilder();
//        sb.append("<html>");
//        sb.append("<head>");
//        sb.append("<script>");
//        sb.append("function sayHello() {");
//        sb.append("alert('say hello');");
//        sb.append("}");
//        sb.append("</script>");
//        sb.append("</head>");
//        sb.append("<body>");
//        sb.append("<h1>What Can JavaScript Do?</h1>");
//        sb.append("<p id=\"demo\">JavaScript can change HTML content.</p>");
//        sb.append("<input type=\"button\" value=\"Hello Javascript\" onclick=\"sayHello()\">");
//        //sb.append("onclick=\"document.getElementById('demo').innerHTML = 'Hello JavaScript!'\">");
//        //sb.append("Click Me!</button>");
//        sb.append("</body>");
//        sb.append("</html>");
        sb.append("<!doctype html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<link rel=\"stylesheet\" href=\"http://10.30.0.85:8090/geoserver/openlayers3/ol.css\" type=\"text/css\"/>");
        sb.append("<script src=\"http://10.30.0.85:8090/geoserver/openlayers3/ol.js\" type=\"text/javascript\"></script>");
        sb.append("<title>TEST</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div>${entity.fullpin}</div>");
        sb.append("<div id=\"map\"></div>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("var fil = \"INTERSECTS(the_geom, querySingle('bukid:BrgyBdry','the_geom','testPIN=''059-12-011-08-099'''))\";");
        sb.append("var vector = new ol.layer.Vector({");
        sb.append("source: new ol.source.Vector({");
        sb.append("format: new ol.format.GeoJSON(),");
        sb.append("url: 'http://10.30.0.85:8090/geoserver/bukid/wfs?service=WFS&version=1.0.0&request=GetFeature&typeName=bukid:BrgyBdry&outputFormat=application/json&srsname=EPSG:3857&CQL_FILTER=' + fil,");
        sb.append("strategy: ol.loadingstrategy.tile(ol.tilegrid.createXYZ({");
        sb.append("maxZoom: 19");
        sb.append("}))");
        sb.append("}),");
        sb.append("style: new ol.style.Style({");
        sb.append("stroke: new ol.style.Stroke({");
        sb.append("color: 'rgba(0, 0, 255, 1.0)',");
        sb.append("width: 2");
        sb.append("})");
        sb.append("})");
        sb.append("});");
        sb.append("var map = new ol.Map({");
        sb.append("layers: [vector],");
        sb.append("target: 'map',");
        sb.append("view: new ol.View({");
        sb.append("center: [13910469.0295, 903791.4224],");
        sb.append("zoom: 9");
        sb.append("})");
        sb.append("});");
        sb.append("alert(\"TEST\");");
        sb.append("vector.getSource().on('change', function(evt) {");
        sb.append("extent = vector.getSource().getExtent();");
        sb.append("map.getView().fit(extent, map.getSize());");
        sb.append("});");
        sb.append("</script>");
        sb.append("</body>");
        sb.append("</html>");
        
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.loadContent(sb.toString());
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>(){
            @Override
            public void handle(WebEvent<String> event) {
                System.out.println("Alert Message > " + event.getData());
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(browser);
        
        Scene scene = new Scene(root, 700, 550);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
