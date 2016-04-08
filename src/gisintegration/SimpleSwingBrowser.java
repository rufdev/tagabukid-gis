/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package gisintegration;

import com.rameses.rcp.common.DocViewModel;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.PropertySupport;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.concurrent.Worker.State.FAILED;
import org.apache.commons.io.FileUtils;

public class SimpleSwingBrowser extends JPanel implements UIControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean refreshed;
    private int stretchWidth;
    private int stretchHeight;
    private DocViewModel docModel;
    
    private JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;
    
    private final JPanel panel = new JPanel(new BorderLayout());
    private final JLabel lblStatus = new JLabel();
    
    
    private final JButton btnGo = new JButton("Go");
    private final JTextField txtURL = new JTextField();
    private final JProgressBar progressBar = new JProgressBar();
    
    public SimpleSwingBrowser() {
        initComponents();
    }
    
    
    private void initComponents() {
        createScene();
        
//        ActionListener al = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                loadURL(txtURL.getText());
//            }
//        };
//
//        btnGo.addActionListener(al);
//        txtURL.addActionListener(al);
        
        progressBar.setPreferredSize(new Dimension(150, 18));
        progressBar.setStringPainted(true);
        
//        JPanel topBar = new JPanel(new BorderLayout(5, 0));
//        topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
//        topBar.add(txtURL, BorderLayout.CENTER);
//        topBar.add(btnGo, BorderLayout.EAST);
        
        JPanel statusBar = new JPanel(new BorderLayout(5, 0));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(lblStatus, BorderLayout.CENTER);
        statusBar.add(progressBar, BorderLayout.EAST);
        
        //panel.add(topBar, BorderLayout.NORTH);
        panel.add(jfxPanel, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.SOUTH);
        
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        
//        setPreferredSize(new Dimension(1024, 600));
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        pack();
        
    }
    
    private void createScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                
                WebView view = new WebView();
                engine = view.getEngine();
                
//                engine.titleProperty().addListener(new ChangeListener<String>() {
//                    @Override
//                    public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                SimpleSwingBrowser.this.setTitle(newValue);
//                            }
//                        });
//                    }
//                });
                
                engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(final WebEvent<String> event) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                lblStatus.setText(event.getData());
                            }
                        });
                    }
                });
                
//                engine.locationProperty().addListener(new ChangeListener<String>() {
//                    @Override
//                    public void changed(ObservableValue<? extends String> ov, String oldValue, final String newValue) {
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                txtURL.setText(newValue);
//                            }
//                        });
//                    }
//                });
                
                engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setValue(newValue.intValue());
                            }
                        });
                    }
                });
                
                engine.getLoadWorker()
                        .exceptionProperty()
                        .addListener(new ChangeListener<Throwable>() {
                            
                            public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                                if (engine.getLoadWorker().getState() == FAILED) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override public void run() {
                                            JOptionPane.showMessageDialog(
                                                    panel,
                                                    (value != null) ?
                                                            engine.getLocation() + "\n" + value.getMessage() :
                                                            engine.getLocation() + "\nUnexpected error.",
                                                    "Loading error...",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                                }
                            }
                        });
                
                jfxPanel.setScene(new Scene(view));
            }
        });
    }
    
    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String tmp = toURL(url);
                
                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }
                engine.load(url);
            }
        });
    }
    
    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }
    
    
    
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                SimpleSwingBrowser browser = new SimpleSwingBrowser();
//                browser.setVisible(true);
//                browser.loadURL("http://oracle.com");
//           }
//       });
//    }
    
    
    
    
    public void load() {
    }
    
    
    public void refresh() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimpleSwingBrowser browser = new SimpleSwingBrowser();
                browser.setVisible(true);
                refreshBrowser();
            }
        });
        
    }
    
    private void refreshBrowser(){
        DocViewModel newModel = null;
                try
                {
                    Object value = UIControlUtil.getBeanValue(this);
                    
                    if ((value instanceof DocViewModel))
                    {
                        newModel = (DocViewModel)value;
                        value = newModel.getValue();
                    }
                    URL url = null;
                    if (value == null)
                    {
                        txtURL.setText("");
                        txtURL.setCaretPosition(0);
                    }
                    else if ((value instanceof URL))
                    {
                        url = (URL)value;
                    }
                    else if (value.toString().startsWith("http://"))
                    {
                        url = new URL(value.toString());
                    }
                    else
                    {
                        txtURL.setText(value.toString());
                        txtURL.setCaretPosition(0);
                    }
                    this.docModel = newModel;
                    
                    loadURL(value.toString());
                }
                catch (Exception ex)
                {
                    MsgBox.err(ex);
                }
    }
    
    
    public Binding getBinding() {
        return this.binding; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public void setBinding(Binding binding) {
        this.binding = binding; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public String[] getDepends() {
        return this.depends = depends; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public int getIndex() {
        return this.index = index; //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setPropertyInfo(PropertySupport.PropertyInfo info) {
        
    }
    
    
    public int getStretchWidth() {
        return this.stretchWidth; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public void setStretchWidth(int stretchWidth) {
        this.stretchWidth = stretchWidth; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public int getStretchHeight() {
        return this.stretchHeight; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public void setStretchHeight(int stretchHeight) {
        this.stretchHeight = stretchHeight; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o); //To change body of generated methods, choose Tools | Templates.
    }
}


