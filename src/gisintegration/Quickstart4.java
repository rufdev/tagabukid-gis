/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gisintegration;

import com.rameses.rcp.common.MsgBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import net.miginfocom.swing.MigLayout;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapLayerTable;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.NoToolAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ResetAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.control.JMapStatusBar;

/**
 *
 * @author rufino
 */
public class Quickstart4 extends javax.swing.JPanel {
    
    private MapContent map;
    /**
     * Creates new form Quickstart4
     */
    
    /** Name assigned to toolbar button for feature info queries. */
    public static final String TOOLBAR_INFO_BUTTON_NAME = "ToolbarInfoButton";
    /** Name assigned to toolbar button for map panning. */
    public static final String TOOLBAR_PAN_BUTTON_NAME = "ToolbarPanButton";
    /** Name assigned to toolbar button for default pointer. */
    public static final String TOOLBAR_POINTER_BUTTON_NAME = "ToolbarPointerButton";
    /** Name assigned to toolbar button for map reset. */
    public static final String TOOLBAR_RESET_BUTTON_NAME = "ToolbarResetButton";
    /** Name assigned to toolbar button for map zoom in. */
    public static final String TOOLBAR_ZOOMIN_BUTTON_NAME = "ToolbarZoomInButton";
    /** Name assigned to toolbar button for map zoom out. */
    public static final String TOOLBAR_ZOOMOUT_BUTTON_NAME = "ToolbarZoomOutButton";

    public enum Tool {
        /**
         * Simple mouse cursor, used to unselect previous cursor tool.
         */
        POINTER,

        /**
         * The feature info cursor tool
         */
        INFO,

        /**
         * The panning cursor tool.
         */
        PAN,

        /**
         * The reset map extent cursor tool.
         */
        RESET,

        /**
         * The zoom display cursor tools.
         */
        ZOOM;
    }
    

    private boolean showToolBar;
    private Set<Tool> toolSet;

    /*
     * UI elements
     */
    private JMapPane mapPane;
    private MapLayerTable mapLayerTable;
    private JToolBar toolBar;

    private boolean showStatusBar;
    private boolean showLayerTable;
    private boolean uiSet;

 
    
    public Quickstart4() throws IOException {
//        final JPanel jPanel1 = new JPanel(new FlowLayout());
         
        File roads =  new File("C:\\Users\\rufino\\Desktop\\Pagudpud shp file\\PAGUDPUD CASE1 1 2 3 Data Encoding Drawing.shp");

        FileDataStore roadstore = FileDataStoreFinder.getDataStore(roads);
        SimpleFeatureSource featureSource = roadstore.getFeatureSource();

        map = new MapContent();
        
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        FeatureLayer layer = new FeatureLayer(featureSource, style);

        map.addLayer(layer);
        
        initComponents();
        mapPane = new JMapPane(map);
        mapPane.setBackground(Color.WHITE);
        mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
        showToolBar = true;
        toolSet = EnumSet.allOf(Tool.class);
        StringBuilder sb = new StringBuilder();
        if (!toolSet.isEmpty()) {
            sb.append("[]"); // fixed size
        }
        sb.append("[grow]");
        sb.append("[min!]"); // status bar height
         JPanel panel = new JPanel(new MigLayout(
                "wrap 1, insets 0", // layout constrains: 1 component per row, no insets

                "[grow]", // column constraints: col grows when frame is resized

                sb.toString() ));
       
            toolBar = new JToolBar();
            toolBar.setOrientation(JToolBar.HORIZONTAL);
            toolBar.setFloatable(false);

            JButton btn;
            ButtonGroup cursorToolGrp = new ButtonGroup();
            
            if (toolSet.contains(Tool.POINTER)) {
                btn = new JButton(new NoToolAction(mapPane));
                btn.setName(TOOLBAR_POINTER_BUTTON_NAME);
                toolBar.add(btn);
                cursorToolGrp.add(btn);
            }

            if (toolSet.contains(Tool.ZOOM)) {
                btn = new JButton(new ZoomInAction(mapPane));
                btn.setName(TOOLBAR_ZOOMIN_BUTTON_NAME);
                toolBar.add(btn);
                cursorToolGrp.add(btn);

                btn = new JButton(new ZoomOutAction(mapPane));
                btn.setName(TOOLBAR_ZOOMOUT_BUTTON_NAME);
                toolBar.add(btn);
                cursorToolGrp.add(btn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.PAN)) {
                btn = new JButton(new PanAction(mapPane));
                btn.setName(TOOLBAR_PAN_BUTTON_NAME);
                toolBar.add(btn);
                cursorToolGrp.add(btn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.INFO)) {
                btn = new JButton(new InfoAction(mapPane));
                btn.setName(TOOLBAR_INFO_BUTTON_NAME);
                toolBar.add(btn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.RESET)) {
                btn = new JButton(new ResetAction(mapPane));
                btn.setName(TOOLBAR_RESET_BUTTON_NAME);
                toolBar.add(btn);
            }
       add(toolBar, "grow");

       mapLayerTable = new MapLayerTable(mapPane);

            /*
             * We put the map layer panel and the map pane into a JSplitPane
             * so that the user can adjust their relative sizes as needed
             * during a session. The call to setPreferredSize for the layer
             * panel has the effect of setting the initial position of the
             * JSplitPane divider
             */
        mapLayerTable.setPreferredSize(new Dimension(200, -1));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                false, 
                mapLayerTable, 
                mapPane);
        add(splitPane, "grow");
//       
        add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");
        
//        mapPane.setRenderer( new StreamingRenderer() );
//        mapPane.setMapContent( map );
//        mapPane.setVisible(true);
//        
//        jPanel1.setVisible(true);
//        this.revalidate();
//        this.repaint();
//        this.add(jPanel1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMapStatusBar1 = new org.geotools.swing.control.JMapStatusBar();
        jMapPane1 = new org.geotools.swing.JMapPane();

        javax.swing.GroupLayout jMapPane1Layout = new javax.swing.GroupLayout(jMapPane1);
        jMapPane1.setLayout(jMapPane1Layout);
        jMapPane1Layout.setHorizontalGroup(
            jMapPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 408, Short.MAX_VALUE)
        );
        jMapPane1Layout.setVerticalGroup(
            jMapPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 267, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jMapPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jMapPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.geotools.swing.JMapPane jMapPane1;
    private org.geotools.swing.control.JMapStatusBar jMapStatusBar1;
    // End of variables declaration//GEN-END:variables
}
