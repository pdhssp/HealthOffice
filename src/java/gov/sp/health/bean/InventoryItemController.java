/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.facade.InventoryItemFacade;
import gov.sp.health.entity.InventoryItem;
import gov.sp.health.entity.InventoryItemCategory;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class InventoryItemController  implements Serializable {

    @EJB
    private InventoryItemFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<InventoryItem> lstItems;
    private InventoryItem current;
    ItemCategory currentCat;
    private DataModel<InventoryItem>  items = null;
    private int selectedItemIndex;
    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;
    String selectText = "";

    public InventoryItemController() {
    }

    public ItemCategory getCurrentCat() {
        return currentCat;
    }

    public void setCurrentCat(ItemCategory currentCat) {
        this.currentCat = currentCat;
    }

    
    
    public List<InventoryItem> getLstItems() {
        return getFacade().findBySQL("Select d From InventoryItem d where d.retired=false order by i.name");
    }

    public void setLstItems(List<InventoryItem> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public InventoryItem getCurrent() {
        if (current != null) {
            currentCat = current.getCategory();
        }
        return current;
    }

    public void setCurrent(InventoryItem current) {
        this.current = current;
        if (current != null){
            currentCat = current.getCategory();
        }
    }

    private InventoryItemFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<InventoryItem>  getItems() {
        items = new ListDataModel(getFacade().findAll("name", true));
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

    public DataModel searchItems() {
        recreateModel();
        if (items == null) {
            if (selectText.equals("")) {
                items = new ListDataModel(getFacade().findAll("name", true));
            } else {
                items = new ListDataModel(getFacade().findAll("name", "%" + selectText + "%",
                        true));
                if (items.getRowCount() > 0) {
                    items.setRowIndex(0);
                    current = (InventoryItem) items.getRowData();
                    Long temLong = current.getId();
                    selectedItemIndex = intValue(temLong);
                } else {
                    current = null;
                    selectedItemIndex = -1;
                }
            }
        }
        return items;

    }

    public InventoryItem searchItem(String itemName, boolean createNewIfNotPresent) {
        InventoryItem searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (InventoryItem) items.getRowData();
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

    public void prepareSelect() {
        this.prepareModifyControlDisable();
    }

    public void prepareEdit() {
        if (current != null) {
            selectedItemIndex = intValue(current.getId());
            this.prepareSelectControlDisable();
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToEdit"));
        }
    }

    public void prepareAdd() {
        selectedItemIndex = -1;
        current = new InventoryItem();
        this.prepareSelectControlDisable();
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit()==false){
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }            
        if (selectedItemIndex > 0) {
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
        this.prepareSelect();
        recreateModel();
        getItems();
        selectText = "";
        selectedItemIndex = intValue(current.getId());
    }

       public void cancelSelect() {
        this.prepareSelect();
    }

    public void delete() {
        if (sessionController.getPrivilege().isInventoryDelete()==false){
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
        selectedItemIndex = -1;
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
        searchItems();
    }

    public void prepareSelectControlDisable() {
        selectControlDisable = true;
        modifyControlDisable = false;
    }

    public void prepareModifyControlDisable() {
        selectControlDisable = false;
        modifyControlDisable = true;
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
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

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
