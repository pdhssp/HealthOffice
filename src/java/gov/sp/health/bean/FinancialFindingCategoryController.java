/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.facade.FinancialFindingCategoryFacade;
import gov.sp.health.entity.FinancialFindingCategory;
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
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class FinancialFindingCategoryController implements Serializable {

    @EJB
    private FinancialFindingCategoryFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<FinancialFindingCategory> lstItems;
    private FinancialFindingCategory current;
    private DataModel<FinancialFindingCategory> items = null;
//    private int selectedItemIndex;
    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;
    String selectText = "";

    public FinancialFindingCategoryController() {
    }

    public List<FinancialFindingCategory> getLstItems() {
        String temSql;
        if (selectText.trim().equals("")) {
            lstItems = getFacade().findAll("name", true);
        } else {
            temSql = "select ic from FinancialFindingCategory ic where ic.retired=false and upper(ic.name) like '%" + selectText.toUpperCase() + "%'  order by ic.name";
            lstItems = getFacade().findBySQL(temSql);
        }
        if (lstItems.isEmpty()){
            current = null;
        }else{
            current = lstItems.get(0);
        }
        return lstItems;
    }

    public void setLstItems(List<FinancialFindingCategory> lstItems) {
        this.lstItems = lstItems;
    }

//    public int getSelectedItemIndex() {
//        return selectedItemIndex;
//    }
//
//    public void setSelectedItemIndex(int selectedItemIndex) {
//        this.selectedItemIndex = selectedItemIndex;
//    }
    public FinancialFindingCategory getCurrent() {
        if (current == null) {
            current = new FinancialFindingCategory();
        }
        return current;
    }

    public void setCurrent(FinancialFindingCategory current) {
        this.current = current;
    }

    private FinancialFindingCategoryFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<FinancialFindingCategory> getItems() {
        String temSql;
        if (selectText.trim().equals("")) {
            items = new ListDataModel(getFacade().findAll("name", true));
        } else {
            temSql = "select ic from FinancialFindingCategory ic where ic.retired=false and upper(ic.name) like '%" + selectText.toUpperCase() + "%'  order by ic.name";
            items = new ListDataModel<FinancialFindingCategory>(getFacade().findBySQL(temSql));
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
                    current =  items.getRowData();
//                    Long temLong = current.getId();
//                    selectedItemIndex = intValue(temLong);
                } else {
                    current = null;
//                    selectedItemIndex = -1;
                }
            }
        }
        return items;

    }

    public FinancialFindingCategory searchItem(String itemName, boolean createNewIfNotPresent) {
        FinancialFindingCategory searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (FinancialFindingCategory) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new FinancialFindingCategory();
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
//            selectedItemIndex = intValue(current.getId());
            this.prepareSelectControlDisable();
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToEdit"));
        }
    }

    public void prepareAdd() {
//        selectedItemIndex = -1;
        System.out.println("Current before prepeare add is " + current);
        current = new FinancialFindingCategory();
        System.out.println("Current after prepare add is " + current);
        this.prepareSelectControlDisable();
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        System.out.println("Current after save add is " + current);
        if (current.getId() != null && current.getId() != 0) {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        this.prepareSelect();
        recreateModel();
        getItems();
        selectText = "";
//        selectedItemIndex = intValue(current.getId());
    }

    public void addDirectly() {
        JsfUtil.addSuccessMessage("1");
        try {

            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);

            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
            current = new FinancialFindingCategory();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
        }

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
//        selectedItemIndex = -1;
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

    public FinancialFindingCategoryFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(FinancialFindingCategoryFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = FinancialFindingCategory.class)
    public static class FinancialFindingCategoryControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FinancialFindingCategoryController controller = (FinancialFindingCategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "financialFindingCategoryController");
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
            if (object instanceof FinancialFindingCategory) {
                FinancialFindingCategory o = (FinancialFindingCategory) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + FinancialFindingCategoryController.class.getName());
            }
        }
    }
}
