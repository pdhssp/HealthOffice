/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Make;
import gov.sp.health.entity.Modal;
import gov.sp.health.facade.MakeFacade;
import gov.sp.health.facade.ModalFacade;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class ModalController implements Serializable {

    @EJB
    private ModalFacade ejbFacade;
    @EJB
    MakeFacade makeFacade;
    @Inject
    SessionController sessionController;
    List<Modal> lstItems;
    private Modal current;
    private Make currentMake;
    private List<Modal> items = null;
    List<Make> makes = null;
    String selectText = "";

    public Make getCurrentMake() {
        return currentMake;
    }

    public void setCurrentMake(Make currentMake) {
        this.currentMake = currentMake;
    }

    public List<Make> getMakes() {
        makes = getMakeFacade().findBySQL("SELECT m FROM Make m WHERE m.retired=false ORDER BY m.name");
        return makes;

    }

    public void setMakes(List<Make> makes) {
        this.makes = makes;
    }

    public ModalFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ModalFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public MakeFacade getMakeFacade() {
        return makeFacade;
    }

    public void setMakeFacade(MakeFacade makeFacade) {
        this.makeFacade = makeFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public ModalController() {
    }

    public List<Modal> getLstItems() {
        return getFacade().findBySQL("Select d From Modal d where d.retired = false ORDER BY d.name");
    }

    public void setLstItems(List<Modal> lstItems) {
        this.lstItems = lstItems;
    }

    public Modal getCurrent() {
        if (current == null) {
            current = new Modal();
        }
        currentMake = current.getMake();
        return current;
    }

    public void setCurrent(Modal current) {
        this.current = current;
        this.currentMake = this.current.getMake();
    }

    private ModalFacade getFacade() {
        return ejbFacade;
    }

    public List<Modal> getItems() {
        items = getFacade().findBySQL("Select d From Modal d where d.retired = false ORDER BY d.name");
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

    public List searchItems() {
        recreateModel();
        if (items == null) {
            if (selectText.equals("")) {
                items = getFacade().findAll("name", true);
            } else {
                items = getFacade().findAll("name", "%" + selectText + "%", true);
                if (items.size() > 0) {
                    current = (Modal) items.get(0);
                } else {
                    current = getCurrent();
                }
            }
        }
        return items;

    }

    public Modal searchItem(String itemName, boolean createNewIfNotPresent) {
        Modal searchedItem = null;
        items = getFacade().findAll("name", itemName, true);
        if (items.size() > 0) {
            searchedItem = (Modal) items.get(0);
        } else if (createNewIfNotPresent) {
            searchedItem = new Modal();
            searchedItem.setName(itemName);
            searchedItem.setCreatedAt(Calendar.getInstance().getTime());
            searchedItem.setCreater(sessionController.loggedUser);
            getFacade().create(searchedItem);
        }
        return searchedItem;
    }

    private void recreateModel() {
        items = null;
    }

    public void prepareAdd() {
        current = new Modal();
        currentMake = new Make();
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        current.setMake(getCurrentMake());
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        recreateModel();
        getItems();
        selectText = "";
    }

    public void addDirectly() {
        JsfUtil.addSuccessMessage("1");
        try {

            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);

            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
            current = new Modal();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
        }

    }

    public void cancelSelect() {
    }

    public void deleteCurrent() {
        
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
        recreateModel();
        getItems();
        selectText = "";
        current = null;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
        searchItems();
    }

    @FacesConverter(forClass = Modal.class)
    public static class ModalControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ModalController controller = (ModalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "modalController");
            return controller.getEjbFacade().find(getKey(value));
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
            if (object instanceof Modal) {
                Modal o = (Modal) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ModalController.class.getName());
            }
        }
    }
}
