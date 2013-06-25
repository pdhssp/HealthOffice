/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.OldInstitutionCategory;
import gov.sp.health.facade.OldInstitutionCategoryFacade;
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
public class OldInstitutionCategoryController {

    @EJB
    private OldInstitutionCategoryFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<OldInstitutionCategory> items;
    private OldInstitutionCategory current;
    
    public OldInstitutionCategoryController() {
    }

    public List<OldInstitutionCategory> getItems() {
        if (items == null) {
            items = new ArrayList<OldInstitutionCategory>();
        }
        items = getFacade().findAll("name", true);
        return items;
    }

    public void setItems(List<OldInstitutionCategory> items) {
        this.items = items;
    }

    public OldInstitutionCategory getCurrent() {
        if (current == null) {
            current = new OldInstitutionCategory();
        }
        return current;
    }

    public void setCurrent(OldInstitutionCategory current) {
        this.current = current;
    }

    private OldInstitutionCategoryFacade getFacade() {
        return ejbFacade;
    }

    private void recreateModel() {
        items = null;
    }

    public void prepareAdd() {
        System.out.println("Current before prepeare add is " + current);
        current = new OldInstitutionCategory();
        current.setCreater(sessionController.loggedUser);
        System.out.println("Current after prepare added by " + current.getCreater());
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
        recreateModel();
        getItems();
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
        current = new OldInstitutionCategory();
    }

    public OldInstitutionCategoryFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(OldInstitutionCategoryFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = OldInstitutionCategory.class)
    public static class OldInstitutionCategoryControllerConverter implements Converter {

        /**
         *
         * @param facesContext
         * @param component
         * @param value
         * @return
         */
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OldInstitutionCategoryController controller = (OldInstitutionCategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "oldInstitutionCategoryController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof OldInstitutionCategory) {
                OldInstitutionCategory o = (OldInstitutionCategory) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + OldInstitutionCategoryController.class.getName());
            }
        }
    }
}
