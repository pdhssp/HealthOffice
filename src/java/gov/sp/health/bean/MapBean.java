/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;  
  
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  
import javax.faces.event.ActionEvent;  
  
import org.primefaces.event.map.OverlaySelectEvent;  
import org.primefaces.model.map.DefaultMapModel;  
import org.primefaces.model.map.LatLng;  
import org.primefaces.model.map.MapModel;  
import org.primefaces.model.map.Polygon; 
/**
 *
 * @author Buddhika
 */
@ManagedBean
@RequestScoped
public class MapBean {

    /**
     * Creates a new instance of MapBean
     */
    
    
    
    private MapModel polygonModel;  
      
    public MapBean() {  
        polygonModel = new DefaultMapModel();  
          
        //Shared coordinates  
        LatLng coord1 = new LatLng(36.879466, 30.667648);  
        LatLng coord2 = new LatLng(36.883707, 30.689216);  
        LatLng coord3 = new LatLng(36.879703, 30.706707);  
  
        //Polygon  
        Polygon polygon = new Polygon();  
        polygon.getPaths().add(coord1);  
        polygon.getPaths().add(coord2);  
        polygon.getPaths().add(coord3);  
  
        polygon.setStrokeColor("#FF9900");  
        polygon.setFillColor("#FF9900");  
        polygon.setStrokeOpacity(0.7);  
        polygon.setFillOpacity(0.7);  
          
        
        
        polygonModel.addOverlay(polygon);  
        
        
        
         //Shared coordinates  
        coord1 = new LatLng(36.877466, 30.666648);  
        coord2 = new LatLng(36.884707, 30.688216);  
        coord3 = new LatLng(36.873703, 30.705707);  
  
        //Polygon  
        polygon = new Polygon();  
        polygon.getPaths().add(coord1);  
        polygon.getPaths().add(coord2);  
        polygon.getPaths().add(coord3);  
  
        polygon.setStrokeColor("#FF9700");  
        polygon.setFillColor("#FF9600");  
        polygon.setStrokeOpacity(0.8);  
        polygon.setFillOpacity(0.9);  
        
        polygonModel.addOverlay(polygon);  
    }  
  
    public MapModel getPolygonModel() {  
        return polygonModel;  
    }  
  
    public void onPolygonSelect(OverlaySelectEvent event) {  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Polygon Selected", null));  
    }  
  
    public void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
}
