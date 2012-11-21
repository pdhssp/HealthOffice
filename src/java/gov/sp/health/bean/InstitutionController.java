/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.entity.Institution;
import gov.sp.health.entity.InstitutionContact;
import gov.sp.health.entity.InstitutionType;
import gov.sp.health.entity.PersonContact;
import gov.sp.health.facade.ContactTypeFacade;
import gov.sp.health.facade.InstitutionContactFacade;
import gov.sp.health.facade.InstitutionTypeFacade;
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
public final class InstitutionController implements Serializable {

    @EJB
    private InstitutionFacade ejbFacade;
    @EJB
    InstitutionTypeFacade institutionTypeFacade;
    @EJB
    InstitutionContactFacade insConFacade;
    @EJB
    ContactTypeFacade ctFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<Institution> lstItems;
    private Institution current;
    List<InstitutionContact> currentContacts;
    DataModel<InstitutionContact> institutionContacts;
    InstitutionContact currentContact;
    private DataModel<Institution> items = null;
    DataModel<InstitutionType> institutionTypes;
    private int selectedItemIndex;
    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;
    String selectText = "";

    public InstitutionController() {
    }

    public ContactTypeFacade getCtFacade() {
        return ctFacade;
    }

    public void setCtFacade(ContactTypeFacade ctFacade) {
        this.ctFacade = ctFacade;
    }

    public InstitutionContactFacade getInsConFacade() {
        return insConFacade;
    }

    public void setInsConFacade(InstitutionContactFacade insConFacade) {
        this.insConFacade = insConFacade;
    }

    

    public InstitutionContact getCurrentContact() {
        if (currentContact == null) {
            currentContact = new InstitutionContact();
            currentContact.setContactType(getCtFacade().findFirstBySQL("select ct from ContactType ct"));
            System.out.print("Getting new Contact" + currentContact.getContactType().getName());
        }
        return currentContact;
    }

    public void setCurrentContact(InstitutionContact currentContact) {
        this.currentContact = currentContact;
    }

    public List<InstitutionContact> getCurrentContacts() {
        if (currentContacts == null) {
            currentContacts = new ArrayList<InstitutionContact>();
        }
        return currentContacts;
    }

    public void setCurrentContacts(List<InstitutionContact> currentContacts) {
        this.currentContacts = currentContacts;
    }

    public DataModel<InstitutionContact> getInstitutionContacts() {
        institutionContacts = new ListDataModel<InstitutionContact>(getCurrentContacts());
        return institutionContacts;
    }

    public void setInstitutionContacts(DataModel<InstitutionContact> institutionContacts) {
        this.institutionContacts = institutionContacts;
    }

    
    public void addContact() {
        if (currentContact == null) {
            JsfUtil.addErrorMessage("No Contact to add");
            return;
        }
        currentContact.setInstitution(current);
        getCurrentContacts().add(currentContact);
        getInsConFacade().create(currentContact);
        currentContact = new InstitutionContact();
    }

    public void removeContact() {
        if (currentContact == null) {
            return;
        }
        if (currentContact.getId() != 0) {
            getInsConFacade().remove(currentContact);
        }
        currentContact = new InstitutionContact();
    }

    
    
    public InstitutionFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InstitutionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public InstitutionTypeFacade getInstitutionTypeFacade() {
        return institutionTypeFacade;
    }

    public void setInstitutionTypeFacade(InstitutionTypeFacade institutionTypeFacade) {
        this.institutionTypeFacade = institutionTypeFacade;
    }

    public DataModel<InstitutionType> getInstitutionTypes() {
        String temSQL;
        temSQL = "SELECT i FROM InstitutionType i WHERE i.retired = false ORDER BY i.orderNo";
        return new ListDataModel<InstitutionType>(getInstitutionTypeFacade().findBySQL(temSQL));
    }

    public void setInstitutionTypes(DataModel<InstitutionType> institutionTypes) {
        this.institutionTypes = institutionTypes;
    }

    public List<Institution> getLstItems() {
        return getFacade().findBySQL("Select d From Institution d");
    }

    public void setLstItems(List<Institution> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public Institution getCurrent() {
        if (current == null) {
            current = new Institution();
        }
        return current;
    }

    public void setCurrent(Institution current) {
        this.current = current;
         String temSql = "";
        if (current != null && current.getId() != null) {
            temSql = "select c from InstitutionContact c where c.retired = false and c.institution.id = " + current.getId();
            currentContacts = getInsConFacade().findBySQL(temSql);
            System.out.println("Getting new set of contacts " + currentContacts.size());
        } else {
            currentContacts = null;
            System.out.println("Setting new set of contacts to null");
        }
        currentContact = new InstitutionContact();
    }

    private InstitutionFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<Institution> getItems() {
        String temSql;
        if (getSelectText().equals("")) {
            items = new ListDataModel<Institution>(getFacade().findAll("name", true));
        } else {
            temSql = "SELECT i FROM Institution i where i.retired=false and LOWER(i.name) like '%" + getSelectText().toLowerCase() + "%' order by i.name";
            items = new ListDataModel<Institution>(getFacade().findBySQL(temSql));
            System.out.println(temSql);
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
                    current = (Institution) items.getRowData();
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

    public Institution searchItem(String itemName, boolean createNewIfNotPresent) {
        Institution searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (Institution) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new Institution();
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
        current = new Institution();
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
            current = new Institution();
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

    @FacesConverter(forClass = Institution.class)
    public static class InstitutionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InstitutionController controller = (InstitutionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "institutionController");
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
            if (object instanceof Institution) {
                Institution o = (Institution) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InstitutionController.class.getName());
            }
        }
    }
}
