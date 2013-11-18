/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.bean.JsfUtil;
import gov.sp.health.bean.MessageProvider;
import gov.sp.health.bean.SessionController;
import gov.sp.health.facade.InventoryItemFacade;
import gov.sp.health.entity.inventory.InventoryItem;
import gov.sp.health.entity.inventory.InventoryItemCategory;
import gov.sp.health.entity.ItemCategory;
import java.io.Serializable;
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
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public final class InventoryItemController implements Serializable {

    @EJB
    private InventoryItemFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<InventoryItem> searchItems;
    private InventoryItem current;
    ItemCategory currentCat;
    private List<InventoryItem> items = null;
    String selectText = "";

    public InventoryItemController() {
    }

    public ItemCategory getCurrentCat() {
        return currentCat;
    }

    public void setCurrentCat(ItemCategory currentCat) {
        this.currentCat = currentCat;
    }

    public List<InventoryItem> getSearchItems() {
        String sql;
        if (getSelectText().trim().equals("")) {
            sql = "Select d From InventoryItem d where d.retired=false order by d.name";
        } else {
            sql = "Select d From InventoryItem d where d.retired=false and lower(d.name)like '%" + getSelectText().trim().toLowerCase() + "%' order by d.name";
        }
        searchItems = getFacade().findBySQL(sql);
        return searchItems;
    }

    public void setSearchItems(List<InventoryItem> searchItems) {
        this.searchItems = searchItems;
    }

    public InventoryItem getCurrent() {
        if (current == null) {
            current = new InventoryItem();
        }
        if (current != null) {
            currentCat = current.getCategory();
        }
        return current;
    }

    public void setCurrent(InventoryItem current) {
        this.current = current;
        if (current != null) {
            currentCat = current.getCategory();
        }
    }

    private InventoryItemFacade getFacade() {
        return ejbFacade;
    }

    public List<InventoryItem> getItems() {
        items = getFacade().findAll("name", true);
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

    public InventoryItem searchItem(String itemName, boolean createNewIfNotPresent) {
        InventoryItem searchedItem = null;
        items = getFacade().findAll("name", itemName, true);
        if (items.size() > 0) {
            searchedItem = (InventoryItem) items.get(0);
        } else if (createNewIfNotPresent) {
            searchedItem = new InventoryItem();
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
        current = new InventoryItem();
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            current.setCategory(currentCat);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setCategory(currentCat);
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        recreateModel();
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
    }

    public InventoryItemFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InventoryItemFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = InventoryItem.class)
    public static class InventoryItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InventoryItemController controller = (InventoryItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "inventoryItemController");
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

        /**
         *
         * @param facesContext
         * @param component
         * @param object
         * @return
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof InventoryItem) {
                InventoryItem o = (InventoryItem) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InventoryItemController.class.getName());
            }
        }
    }
}
