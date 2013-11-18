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
import gov.sp.health.facade.InventoryItemCategoryFacade;
import gov.sp.health.entity.inventory.InventoryItemCategory;
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
public final class InventoryItemCategoryController implements Serializable {

    @EJB
    private InventoryItemCategoryFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    private List<InventoryItemCategory> lstItems;
    private InventoryItemCategory current;
    private List<InventoryItemCategory> items = null;
    private String selectText = "";

    public InventoryItemCategoryController() {
    }

    public List<InventoryItemCategory> getLstItems() {
         String temSql;
        if (getSelectText().trim().equals("")) {
            lstItems = getFacade().findAll("name", true);
        } else {
            temSql = "select ic from InventoryItemCategory ic where ic.retired=false and upper(ic.name) like '%" + getSelectText().toUpperCase() + "%'  order by ic.name";
            lstItems = getFacade().findBySQL(temSql);
        }
        if (lstItems.isEmpty()){
            setCurrent(null);
        }else{
            setCurrent(lstItems.get(0));
        }
        return lstItems;
    }

    public void setLstItems(List<InventoryItemCategory> lstItems) {
        this.lstItems = lstItems;
    }

    public InventoryItemCategory getCurrent() {
        if (current == null) {
            current = new InventoryItemCategory();
        }
        return current;
    }

    public void setCurrent(InventoryItemCategory current) {
        this.current = current;
    }

    private InventoryItemCategoryFacade getFacade() {
        return getEjbFacade();
    }

    public List<InventoryItemCategory> getItems() {
        String temSql;
        
        if (getSelectText().trim().equals("")) {
            items = getFacade().findAll("name", true);
        } else {
            temSql = "select ic from InventoryItemCategory ic where ic.retired=false and upper(ic.name) like '%" + getSelectText().toUpperCase() + "%'  order by ic.name";
            items = getFacade().findBySQL(temSql);
        }
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
        if (getItems() == null) {
            if (getSelectText().equals("")) {
                setItems(getFacade().findAll("name", true));
            } else {
                setItems(getFacade().findAll("name", "%" + getSelectText() + "%", true));
                if (getItems().size() > 0) {
                    setCurrent(getItems().get(0));
                } else {
                    setCurrent(null);
                }
            }
        }
        return getItems();

    }

    public InventoryItemCategory searchItem(String itemName, boolean createNewIfNotPresent) {
        InventoryItemCategory searchedItem = null;
        setItems(getFacade().findAll("name", itemName, true));
        if (getItems().size() > 0) {
            getItems().get(0);
            searchedItem = getItems().get(0);
        } else if (createNewIfNotPresent) {
            searchedItem = new InventoryItemCategory();
            searchedItem.setName(itemName);
            searchedItem.setCreatedAt(Calendar.getInstance().getTime());
            searchedItem.setCreater(getSessionController().loggedUser);
            getFacade().create(searchedItem);
        }
        return searchedItem;
    }

    private void recreateModel() {
        setItems(null);
    }

    public void prepareAdd() {
        setCurrent(new InventoryItemCategory());
    }

    public void saveSelected() {
        if (getSessionController().getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (getCurrent().getId()!=null && getCurrent().getId() > 0) {
            getFacade().edit(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            getCurrent().setCreatedAt(Calendar.getInstance().getTime());
            getCurrent().setCreater(getSessionController().loggedUser);
            getFacade().create(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        recreateModel();
        getItems();
        setSelectText("");
    }

    public void addDirectly() {
        JsfUtil.addSuccessMessage("1");
        try {

            getCurrent().setCreatedAt(Calendar.getInstance().getTime());
            getCurrent().setCreater(getSessionController().loggedUser);

            getFacade().create(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
            setCurrent(new InventoryItemCategory());
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
        }

    }

    public void delete() {
        System.out.println("1");
        if (getSessionController().getPrivilege().isInventoryDelete() == false) {
            JsfUtil.addErrorMessage("You are not autherized to delete any content");
            return;
        }
        System.out.println("2");
        if (getCurrent() != null) {
            System.out.println("3");
            getCurrent().setRetired(true);
            getCurrent().setRetiredAt(Calendar.getInstance().getTime());
            getCurrent().setRetirer(getSessionController().loggedUser);
            getFacade().edit(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
        }
        recreateModel();
        getItems();
        setSelectText("");
        setCurrent(null);
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
        searchItems();
    }

    public InventoryItemCategoryFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InventoryItemCategoryFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<InventoryItemCategory> items) {
        this.items = items;
    }

    @FacesConverter(forClass = InventoryItemCategory.class)
    public static class InventoryItemCategoryControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InventoryItemCategoryController controller = (InventoryItemCategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "inventoryItemCategoryController");
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
            if (object instanceof InventoryItemCategory) {
                InventoryItemCategory o = (InventoryItemCategory) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InventoryItemCategoryController.class.getName());
            }
        }
    }
}
