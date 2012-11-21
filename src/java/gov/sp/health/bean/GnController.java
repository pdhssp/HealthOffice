/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.facade.GnFacade;
import gov.sp.health.entity.GnArea;
import gov.sp.health.entity.AppCoordinate;
import gov.sp.health.entity.Area;
import gov.sp.health.facade.AppCoordinateFacade;
import gov.sp.health.facade.AreaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class GnController implements Serializable {

    @EJB
    private AreaFacade facade;
    @EJB
    AppCoordinateFacade coordiFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private Area current;
    private List<Area> items = null;
    List<AppCoordinate> currentCoordinates;
    String coordinateText;
    String selectText = "";

    
    public GnController() {
    }

    public Area getCurrent() {
        if (current == null) {
            current = new GnArea();
        }
        return current;
    }

    
    
    public void setCurrent(Area current) {
        this.current = current;
        String temSql = "";
        if (current != null && current.getId() != null) {
            temSql = "select c from AppCoordinate c where c.retired = false and c.area.id = " + current.getId() + " order by c.id";
            currentCoordinates = getCoordiFacade().findBySQL(temSql);
        } else {
            currentCoordinates = null;
        }
    }

    public void prepareAdd(){
        current = new GnArea();
    }
    
    public AreaFacade getFacade() {
        return facade;
    }

    public void setFacade(AreaFacade facade) {
        this.facade = facade;
    }

    

    public List<Area> getItems() {
        String temSql;
        if (selectText.trim().equals("")) {
            temSql = "select p from Area p where p.retired=false and type(p) = GnArea order by p.name";
        } else {
            temSql = "select p from Area p where p.retired=false and type(p) = GnArea  and lower(p.name) like '%" + selectText.toLowerCase() + "%' order by p.name";
        }
        items =  getFacade().findBySQL(temSql);
        return items;
    }

    public static int intValue(long value) {
        int valueInt = (int) value;
        if (valueInt != value) {
            throw new IllegalArgumentException(
                    "The long value " + value + " is not within range of the int type");
        }
        return valueInt;
    }

    public AppCoordinateFacade getCoordiFacade() {
        return coordiFacade;
    }

    public void setCoordiFacade(AppCoordinateFacade coordiFacade) {
        this.coordiFacade = coordiFacade;
    }

    public String getCoordinateText() {
        return coordinateText;
    }

    public void setCoordinateText(String coordinateText) {
        this.coordinateText = coordinateText;
    }

    public List<AppCoordinate> getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setCurrentCoordinates(List<AppCoordinate> currentCoordinates) {
        this.currentCoordinates = currentCoordinates;
    }

    
    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (current == null) {
            JsfUtil.addErrorMessage("Nothing to save");
            return;
        }
        if (current.getName().trim().equals("")) {
            JsfUtil.addErrorMessage("Please enter a name to save");
            return;
        }
        if (current.getId() != null && current.getId() != 0) {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        getItems();
        selectText = "";
    }

    public void delete() {
        if (sessionController.getPrivilege().isInventoryDelete() == false) {
            JsfUtil.addErrorMessage("You are not autherized to delete any content");
            return;
        }
        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
        }
        getItems();
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

  

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = GnArea.class)
    public static class GnAreaControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GnController controller = (GnController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gnAreaController");
            return controller.facade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GnArea) {
                GnArea o = (GnArea) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + GnController.class.getName());
            }
        }
    }
}
