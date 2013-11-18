/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.facade.ConsumableItemCategoryFacade;
import gov.sp.health.entity.ConsumableItemCategory;
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
public class ConsumableItemCategoryController implements Serializable {

    @EJB
    private ConsumableItemCategoryFacade ejbFacade;
    @Inject
    private SessionController sessionController;
    private List<ConsumableItemCategory> lstItems;
    private ConsumableItemCategory current;
    private List<ConsumableItemCategory> items = null;
    private String selectText = "";

    public ConsumableItemCategoryController() {
    }

    public List<ConsumableItemCategory> getLstItems() {
        String temSql;
        if (getSelectText().trim().equals("")) {
            lstItems = getFacade().findAll("name", true);
        } else {
            temSql = "select ic from ConsumableItemCategory ic where ic.retired=false and upper(ic.name) like '%" + getSelectText().toUpperCase() + "%'  order by ic.name";
            lstItems = getFacade().findBySQL(temSql);
        }
        if (lstItems.isEmpty()) {
            setCurrent(null);
        } else {
            setCurrent(lstItems.get(0));
        }
        return lstItems;
    }

    public void setLstItems(List<ConsumableItemCategory> lstItems) {
        this.lstItems = lstItems;
    }

    public ConsumableItemCategory getCurrent() {
        if (current == null) {
            current = new ConsumableItemCategory();
        }
        return current;
    }

    public void setCurrent(ConsumableItemCategory current) {
        this.current = current;
    }

    private ConsumableItemCategoryFacade getFacade() {
        return getEjbFacade();
    }

    public List<ConsumableItemCategory> getItems() {
        String temSql;

        if (getSelectText().trim().equals("")) {
            items = getFacade().findAll("name", true);
        } else {
            temSql = "select ic from ConsumableItemCategory ic where ic.retired=false and upper(ic.name) like '%" + getSelectText().toUpperCase() + "%'  order by ic.name";
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

    public ConsumableItemCategory searchItem(String itemName, boolean createNewIfNotPresent) {
        ConsumableItemCategory searchedItem = null;
        setItems(getFacade().findAll("name", itemName, true));
        if (getItems().size() > 0) {
            getItems().get(0);
            searchedItem = getItems().get(0);
        } else if (createNewIfNotPresent) {
            searchedItem = new ConsumableItemCategory();
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
        setCurrent(new ConsumableItemCategory());
    }

    public void saveSelected() {
        if (getSessionController().getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
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
            setCurrent(new ConsumableItemCategory());
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

    public ConsumableItemCategoryFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ConsumableItemCategoryFacade ejbFacade) {
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
    public void setItems(List<ConsumableItemCategory> items) {
        this.items = items;
    }

    @FacesConverter(forClass = ConsumableItemCategory.class)
    public static class ConsumableItemCategoryControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsumableItemCategoryController controller = (ConsumableItemCategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consumableItemCategoryController");
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
            if (object instanceof ConsumableItemCategory) {
                ConsumableItemCategory o = (ConsumableItemCategory) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ConsumableItemCategoryController.class.getName());
            }
        }
    }
}
