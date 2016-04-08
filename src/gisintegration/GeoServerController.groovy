import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import gisintegration.*;
import com.rameses.io.*;

class GeoServerController { 
    
    @Script("Template")
    def template;
            
    @Binding
    def binding;
            
    def htmlModel;
    def entity = [:];
    void initPreview(){
        if (!entity.fullpin) 
        MsgBox.alert("No Map Data");
        //entity.fullpin = '059-12-004-08-026';
        
        if (entity.fullpin) {
        def renderhtml = template.render( "html/sampleol", [entity:entity] );
        def dir = new File( System.getProperty('user.dir') + '/tagabukidgeoetracs/openlayrs' );
        if ( !dir.exists() ) dir.mkdirs();

        File f = new File(dir.path + '/' + "faasOL.html" );
        if ( f.exists() ) f.delete();

        f.createNewFile();
        FileWriter fileWriter = new FileWriter(f);
        fileWriter.write(renderhtml);
        fileWriter.flush();
        fileWriter.close();
        htmlModel = f.toURI().toURL().toExternalForm();
        }
    }
} 