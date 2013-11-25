/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.*;
import gov.sp.health.facade.TransferFacade;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.LocationFacade;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.facade.SubjectFacade;
import gov.sp.health.facade.UnitFacade;
import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class TransferController implements Serializable {

    @EJB
    private TransferFacade ejbFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    PersonFacade personFacade;
    @EJB
    SubjectFacade subjectFacade;
    @Inject
    SessionController sessionController;
    @Inject
    ImageController imageController;
    //
    //
    //
    List<Transfer> lstItems;
    private Transfer current;
    private DataModel<Transfer> items = null;
    private DataModel<Transfer> itemsIns = null;
    private DataModel<Transfer> itemsUni = null;
    private DataModel<Transfer> itemsLoc = null;
    private DataModel<Transfer> itemsPer = null;
    private DataModel temItems = null;
    //
    //
    DataModel<Institution> institutions;
    DataModel<Unit> toUnits;
    DataModel<Unit> fromUnits;
    DataModel<Location> toLocations;
    DataModel<Location> fromLocations;
    DataModel<Person> fromPersons;
    DataModel<Person> toPersons;
    DataModel<Subject> subjects;
    //
    //
    //
    private int selectedItemIndex;
    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;
    String selectText = "";
    String selectName = "";
    //
    //
    //
    Unit fromUnit;
    Unit toUnit;
    Location fromLocation;
    Location toLocation;
    Person fromPerson;
    Person toPerson;
    Institution fromInstitution;
    Institution toInstitution;
    Subject subject;
    //
    //
    //
    Date fromDate;
    Date toDate;

    public DataModel getTemItems() {
        return new ListDataModel(getFacade().findAll());
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setTemItems(DataModel temItems) {
        this.temItems = temItems;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectText = "";
        this.selectName = selectName;
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public SubjectFacade getSubjectFacade() {
        return subjectFacade;
    }

    public void setSubjectFacade(SubjectFacade subjectFacade) {
        this.subjectFacade = subjectFacade;
    }

    public DataModel<Subject> getSubjects() {
        return new ListDataModel<Subject>(getSubjectFacade().findBySQL("Select s from Subject s where s.retired=false order by s.name"));
    }

    public void setSubjects(DataModel<Subject> subjects) {
        this.subjects = subjects;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public UnitFacade getUnitFacade() {
        return unitFacade;
    }

    public void setUnitFacade(UnitFacade unitFacade) {
        this.unitFacade = unitFacade;
    }

    public DataModel<Location> getFromLocations() {
        if (getFromUnit() != null) {
            return new ListDataModel(getFacade().findBySQL("select l from Location l where l.retired=false and l.unit.id = " + getFromUnit().getId() + " order by l.name"));
        } else {
            return null;
        }

    }

    public void setFromLocations(DataModel<Location> fromLocations) {
        this.fromLocations = fromLocations;
    }

    public Person getFromPerson() {
        return fromPerson;
    }

    public void setFromPerson(Person fromPerson) {
        this.fromPerson = fromPerson;
    }

    public DataModel<Person> getFromPersons() {
        if (getFromInstitution() != null) {
            return new ListDataModel(getPersonFacade().findBySQL("select l from Person l where l.retired=false and l.institution.id = " + getFromInstitution().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setFromPersons(DataModel<Person> fromPersons) {
        this.fromPersons = fromPersons;
    }

    public DataModel<Unit> getFromUnits() {
        if (getFromInstitution() != null) {
            return new ListDataModel(getUnitFacade().findBySQL("select l from Unit l where l.retired=false and l.institution.id = " + getFromInstitution().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setFromUnits(DataModel<Unit> fromUnits) {
        this.fromUnits = fromUnits;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    public Institution getToInstitution() {
        return toInstitution;
    }

    public void setToInstitution(Institution toInstitution) {
        this.toInstitution = toInstitution;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

    public DataModel<Location> getToLocations() {
        if (getToUnit() != null) {
            return new ListDataModel(getFacade().findBySQL("select l from Location l where l.retired=false and l.unit.id = " + getToUnit().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setToLocations(DataModel<Location> toLocations) {
        this.toLocations = toLocations;
    }

    public DataModel<Person> getToPersons() {
        if (getToInstitution() != null) {
            return new ListDataModel(getPersonFacade().findBySQL("select l from Person l where l.retired=false and l.institution.id = " + getToInstitution().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setToPersons(DataModel<Person> toPersons) {
        this.toPersons = toPersons;
    }

    public Unit getToUnit() {
        return toUnit;
    }

    public void setToUnit(Unit toUnit) {
        this.toUnit = toUnit;
    }

    public DataModel<Unit> getToUnits() {
        if (getToInstitution() != null) {
            return new ListDataModel(getUnitFacade().findBySQL("select l from Unit l where l.retired=false and l.institution.id = " + getToInstitution().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setToUnits(DataModel<Unit> toUnits) {
        this.toUnits = toUnits;
    }

    public DataModel<Institution> getInstitutions() {
        String temSQL;
//        if (sessionController.getPrivilege().getRestrictedInstitution() != null) {
//            temSQL = "SELECT i FROM Institution i WHERE i.retired=false AND i.id = " + sessionController.getPrivilege().getRestrictedInstitution().getId();
//        } else {
//            temSQL = "SELECT i FROM Institution i WHERE i.retired=false ORDER BY i.name";
//        }
        temSQL = "SELECT i FROM Institution i WHERE i.retired=false ORDER BY i.name";
        return new ListDataModel<Institution>(getInstitutionFacade().findBySQL(temSQL));
    }

    public void setInstitutions(DataModel<Institution> institutions) {
        this.institutions = institutions;
    }

//    public DataModel<Unit> getUnits() {
//        String temSql;
//        if (sessionController.getPrivilege().getRestrictedUnit() != null) {
//            temSql = "SELECT u from Unit u where u.id = " + sessionController.getPrivilege().getRestrictedUnit().getId();
//        } else if (getFromInstitution() != null) {
//            temSql = "select u from Unit u where u.retired=false and u.institution.id = " + getFromInstitution().getId() + " order by u.name";
//        } else {
//            return null;
//        }
//        return new ListDataModel<Unit>(getUnitFacade().findBySQL(temSql));
//    }
    public Institution getFromInstitution() {
        return fromInstitution;
    }

    public void setFromInstitution(Institution fromInstitution) {
        this.fromInstitution = fromInstitution;
    }

    public Person getToPerson() {
        return toPerson;
    }

    public void setToPerson(Person toPerson) {
        this.toPerson = toPerson;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Unit getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(Unit fromUnit) {
        this.fromUnit = fromUnit;
    }

    public TransferController() {
    }

    public TransferFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(TransferFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<Transfer> getLstItems() {
        return getFacade().findBySQL("Select d From Transfer d WHERE d.retired=false ORDERBY i.name");
    }

    public void setLstItems(List<Transfer> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public Transfer getCurrent() {
        if (current == null) {
            current = new Transfer();
        }
        return current;
    }

    public void setCurrent(Transfer current) {
        this.current = current;
    }

    private TransferFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<Transfer> getItems() {
        String temSql;
        if (fromUnit == null) {
            return null;
        }
        if (!selectText.equals("")) {
            temSql = "select f from Transfer f where f.retired=false and lower(f.name) like '%" + selectText.toLowerCase() + "%' and f.unit.id = " + fromUnit.getId() + " order by f.name";
        } else if (!selectName.equals("")) {
            temSql = "select f from Transfer f where f.retired=false  and lower(f.description) like '%" + selectName.toLowerCase() + "%' and f.unit.id = " + fromUnit.getId() + " order by f.name";
        } else {
            temSql = "select f from Transfer f where f.retired=false and f.unit.id = " + fromUnit.getId() + " order by f.name";
        }
        items = new ListDataModel(getFacade().findBySQL(temSql));
        return items;
    }

    public DataModel<Transfer> getItemsIns() {
        if (getToInstitution() == null) {
            return null;
        }
        Map temMap = new HashMap();
        String temSQL = "SELECT h FROM Transfer h WHERE h.retired=false AND h.toInstitution.id=" + getToInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.receivedDate  ";
        temSQL = "SELECT h FROM Transfer h";
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        System.out.println("SQL" + temSQL);
        //return new ListDataModel<Transfer>(getFacade().findBySQL(temSQL, temMap));
        return new ListDataModel<Transfer>(getFacade().findBySQL(temSQL));
    }

    public DataModel<Transfer> getItemsInsSub() {
        if (getToInstitution() == null) {
            return null;
        }
        String temSQL;
        Map temMap = new HashMap();
        if (subject == null) {
            temSQL = "SELECT h FROM Transfer h WHERE h.retired=false AND h.toInstitution.id=" + getToInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        } else {
            temSQL = "SELECT h FROM Transfer h WHERE h.retired=false AND h.toInstitution.id=" + getToInstitution().getId() + " AND h.subject.id = " + getSubject().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        }
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", fromDate);
        return new ListDataModel<Transfer>(getFacade().findBySQL(temSQL, temMap));
    }

    public void setItemsIns(DataModel<Transfer> itemsIns) {
        this.itemsIns = itemsIns;
    }

    public DataModel<Transfer> getItemsLoc() {
        if (fromLocation != null) {
            return new ListDataModel<Transfer>(getFacade().findBySQL("SELECT i From Transfer i WHERE i.retired=false AND i.location.id = " + fromLocation.getId()));
        } else {
            return null;
        }
    }

    public void setItemsLoc(DataModel<Transfer> itemsLoc) {
        this.itemsLoc = itemsLoc;
    }

    public void setItemsPer(DataModel<Transfer> itemsPer) {
        this.itemsPer = itemsPer;
    }

    public DataModel<Transfer> getItemsUni() {
        if (fromUnit != null) {
            return new ListDataModel<Transfer>(getFacade().findBySQL("SELECT i From Transfer i WHERE i.retired=false AND i.unit.id = " + fromUnit.getId()));
        } else {
            return null;
        }
    }

    public void setItemsUni(DataModel<Transfer> itemsUni) {
        this.itemsUni = itemsUni;
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
                    current = (Transfer) items.getRowData();
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

    public Transfer searchItem(String itemName, boolean createNewIfNotPresent) {
        Transfer searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (Transfer) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new Transfer();
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

    public String toEdit() {
        current = getItemsIns().getRowData();
        return "post_new_transfer";
    }

    public String toView() {
//        imageController.setTransfer(current);
        return "post_view_transfer";
    }

    public ImageController getImageController() {
        return imageController;
    }

    public void setImageController(ImageController imageController) {
        this.imageController = imageController;
    }

    public void toDelete() {
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
        current = new Transfer();
        this.prepareSelectControlDisable();
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (current.getId() != 0) {
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

    public String addDirectly() {
        //try {
        current.setFromInstitution(fromInstitution);
        System.out.println("Transfer. Add Direct. 01");
        current.setToInstitution(toInstitution);
        System.out.println("Transfer. Add Direct. 02");
        current.setCreatedAt(Calendar.getInstance().getTime());
        current.setFromTime(Calendar.getInstance().getTime());
        System.out.println("Transfer. Add Direct. 03");
        current.setCreater(sessionController.loggedUser);
        System.out.println("Transfer. Add Direct. 04");
        getFacade().create(current);
        System.out.println("Transfer. Add Direct. 05");
        JsfUtil.addSuccessMessage("savedNewSuccessfully");
        System.out.println("Transfer. Add Direct. 06");
        setFromDate(current.getToDate());
        System.out.println("Transfer. Add Direct. 07");
        setToDate(current.getToDate());
        System.out.println("Transfer. Add Direct. 08");
        current = new Transfer();
        System.out.println("Transfer. Add Direct. 09");
        return "post_institution_received";
//
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, "Error" + e.getMessage());
//            return "";
//        }

    }

    public String prepareAddNew() {
        current = new Transfer();
        return "post_new_transfer";
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
        this.selectName = "";
    }

    public void prepareSelectControlDisable() {
        selectControlDisable = true;
        modifyControlDisable = false;
    }

    public void prepareModifyControlDisable() {
        selectControlDisable = false;
        modifyControlDisable = true;
    }

    @FacesConverter(forClass = Transfer.class)
    public static class TransferControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            try {
                TransferController controller = (TransferController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "transferController");
                return controller.getEjbFacade().find(getKey(value));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, "Error");
                return null;
            }

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
            if (object instanceof Transfer) {
                Transfer o = (Transfer) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + TransferController.class.getName());
            }
        }
    }
}
