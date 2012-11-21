/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.CitizenCharterCategory;
import gov.sp.health.facade.CitizenCharterCategoryFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author buddhika
 */
@ManagedBean
@RequestScoped
public class CitizenCharterCategoryController {

    @EJB
    private CitizenCharterCategoryFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<CitizenCharterCategory> items;
    private CitizenCharterCategory current;
    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;
    String selectText = "";

    public CitizenCharterCategoryController() {
    }

    public List<CitizenCharterCategory> getItems() {
        if (items == null) {
            items = new ArrayList<CitizenCharterCategory>();
        }
        String temSql;
        if (selectText.trim().equals("")) {
            items = getFacade().findAll("name", true);
        } else {
            temSql = "select ic from CitizenCharterCategory ic where ic.retired=false and upper(ic.name) like '%" + selectText.toUpperCase() + "%'  order by ic.name";
            items = getFacade().findBySQL(temSql);
        }
        return items;
    }

    public void setItems(List<CitizenCharterCategory> items) {
        this.items = items;
    }

    public CitizenCharterCategory getCurrent() {
        if (current == null) {
            current = new CitizenCharterCategory();
        }
        return current;
    }

    public void setCurrent(CitizenCharterCategory current) {
        this.current = current;
    }

    private CitizenCharterCategoryFacade getFacade() {
        return ejbFacade;
    }

    private void recreateModel() {
        items = null;
    }

    public void prepareSelect() {
        this.prepareModifyControlDisable();
    }

    public void prepareEdit() {
        if (current != null) {
//            selectedItemIndex = intValue(current.getId());
            this.prepareSelectControlDisable();
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToEdit"));
        }
    }

    public void prepareAdd() {
//        selectedItemIndex = -1;
        System.out.println("Current before prepeare add is " + current);
        current = new CitizenCharterCategory();
        current.setCreater(sessionController.loggedUser);
        System.out.println("Current after prepare added by " + current.getCreater());
        this.prepareSelectControlDisable();
    }

       public void saveSelected() {
        System.out.println("Current after save add is " + current);
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        //System.out.println("Current after save add is " + current.getCreater().getName());
        if (current.getId() == null || current.getId() == 0) {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        } else {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        }
        this.prepareSelect();
        recreateModel();
        getItems();
        selectText = "";
    }

    public void cancelSelect() {
        this.prepareSelect();
    }

    public void delete() {
        System.out.println("1");
        if (sessionController.getPrivilege().isInventoryDelete() == false) {
            JsfUtil.addErrorMessage("You are not autherized to delete any content");
            return;
        }
        System.out.println("2");
        if (current != null) {
            System.out.println("3");
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
        }
        recreateModel();
        getItems();
        selectText = "";
        current = null;
        this.prepareSelect();
    }

    public boolean isModifyControlDisable() {
        return modifyControlDisable;
    }

    public void setModifyControlDisable(boolean modifyControlDisable) {
        this.modifyControlDisable = modifyControlDisable;
    }

    public boolean isSelectControlDisable() {
        return selectControlDisable;
    }

    public void setSelectControlDisable(boolean selectControlDisable) {
        this.selectControlDisable = selectControlDisable;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public void prepareSelectControlDisable() {
        selectControlDisable = true;
        modifyControlDisable = false;
    }

    public void prepareModifyControlDisable() {
        selectControlDisable = false;
        modifyControlDisable = true;
    }

    public CitizenCharterCategoryFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(CitizenCharterCategoryFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = CitizenCharterCategory.class)
    public static class CitizenCharterCategoryControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CitizenCharterCategoryController controller = (CitizenCharterCategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "citizenCharterCategoryController");
            return controller.ejbFacade.find(getKey(value));
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
            if (object instanceof CitizenCharterCategory) {
                CitizenCharterCategory o = (CitizenCharterCategory) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + CitizenCharterCategoryController.class.getName());
            }
        }
    }
}
