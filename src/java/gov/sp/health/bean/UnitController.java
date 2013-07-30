/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Institution;
import gov.sp.health.facade.UnitFacade;
import gov.sp.health.entity.Unit;
import gov.sp.health.facade.InstitutionFacade;
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
public final class UnitController implements Serializable {

    @EJB
    private UnitFacade ejbFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<Unit> lstItems;
    private Unit current;
    private DataModel<Unit> items = null;
    DataModel<Institution> institutions;
    private int selectedItemIndex;
    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;
    String selectText = "";
    Institution institution;

    public UnitController() {
    }

    public DataModel<Institution> getInstitutions() {
        if (sessionController.privilege.getRestrictedInstitution() != null) {
            return new ListDataModel<Institution>(getInstitutionFacade().findBySQL("SELECT d FROM Institution d WHERE d.retired=false And d.id = " + sessionController.getPrivilege().getRestrictedInstitution().getId() + " ORDER BY d.name"));
        } else if (sessionController.privilege.getRestrictedUnit() != null) {
            return new ListDataModel<Institution>(getInstitutionFacade().findBySQL("SELECT d FROM Institution d WHERE d.retired=false  And d.id = " + sessionController.getPrivilege().getRestrictedUnit().getInstitution().getId() + " ORDER BY d.name"));
        } else {
            return new ListDataModel<Institution>(getInstitutionFacade().findBySQL("SELECT d FROM Institution d WHERE d.retired=false ORDER BY d.name"));
        }
    }

    public void setInstitutions(DataModel<Institution> institutions) {
        this.institutions = institutions;
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public List<Unit> getLstItems() {
        return getFacade().findBySQL("Select d From Unit d WHERE d.retired=false ORDER BY d.name");
    }
    List<Unit> userUnits;

    public List<Unit> getUserUnits() {

        String sql;
        if (getSessionController().getLoggedUser().getWebUserPerson().getInstitution() != null) {
            sql = "select u from Unit u where u.retired=false and u.institution.id = " + getSessionController().getLoggedUser().getWebUserPerson().getInstitution().getId() + " order by u.name";
        } else {
            sql = "select u from Unit u where u.retired=false order by u.name";
        }
        userUnits = getEjbFacade().findBySQL(sql);
        return userUnits;
    }
    
   

    public void setUserUnits(List<Unit> userUnits) {
        this.userUnits = userUnits;
    }

    public void setLstItems(List<Unit> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public Unit getCurrent() {
        if (current == null) {
            current = new Unit();
        }
        return current;
    }

    public void setCurrent(Unit current) {
        this.current = current;
    }

    private UnitFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<Unit> getItems() {
        if (institution == null) {
            return new ListDataModel<Unit>(getFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false ORDER BY u.name"));
        } else {
            return new ListDataModel<Unit>(getFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id=" + institution.getId() + "  ORDER BY u.name"));
        }
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
                    current = (Unit) items.getRowData();
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

    public Unit searchItem(String itemName, boolean createNewIfNotPresent) {
        Unit searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (Unit) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new Unit();
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
        current = new Unit();
        this.prepareSelectControlDisable();
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (selectedItemIndex > 0) {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setInstitution(institution);
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

    public void addDirectly() {
        JsfUtil.addSuccessMessage("1");
        try {

            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);

            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
            current = new Unit();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
        }

    }

    public void cancelSelect() {
        this.prepareSelect();
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

    public UnitFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(UnitFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = Unit.class)
    public static class UnitControllerConverter implements Converter {

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
            UnitController controller = (UnitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "unitController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            try {
                key = Long.valueOf(value);
            } catch (Exception e) {
                key = 0l;
            }
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
            if (object instanceof Unit) {
                Unit o = (Unit) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + UnitController.class.getName());
            }
        }
    }
}
