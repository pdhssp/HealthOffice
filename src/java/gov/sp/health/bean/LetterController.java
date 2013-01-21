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
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.LetterFacade;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.facade.SubjectFacade;
import gov.sp.health.facade.UnitFacade;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public final class LetterController implements Serializable {

    @EJB
    private LetterFacade ejbFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    PersonFacade personFacade;
    @EJB
    SubjectFacade subjectFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    @ManagedProperty(value = "#{imageController}")
    ImageController imageController;
    //
    //
    //
    Letter[] lettersToMark;
    List<Letter> lstItems;
    private Letter current;
    private DataModel<Letter> items = null;
    private DataModel<Letter> itemsIns = null;
    private DataModel<Letter> itemsUni = null;
    private DataModel<Letter> itemsLoc = null;
    private DataModel<Letter> itemsPer = null;
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

    public Letter[] getLettersToMark() {
        return lettersToMark;
    }

    public void setLettersToMark(Letter[] lettersToMark) {
        this.lettersToMark = lettersToMark;
    }

    public void markAsSent() {
        if (lettersToMark == null || lettersToMark.length <= 0) {
            return;
        }
        for (Letter markLetter : lettersToMark) {
            markLetter.setSent(true);
            markLetter.setSentDate(Calendar.getInstance().getTime());
            markLetter.setSentTime(Calendar.getInstance().getTime());
            getFacade().edit(markLetter);
        }
        JsfUtil.addSuccessMessage("Letters marked as Collected");
    }

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
            return new ListDataModel(getPersonFacade().findBySQL("select l from Person l where l.retired=false and l.institution.id = " + getToInstitution().getId() + " order by l.givenName"));
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

    public LetterController() {
    }

    public LetterFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(LetterFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<Letter> getLstItems() {
        return getFacade().findBySQL("Select d From Letter d WHERE d.retired=false ORDERBY i.name");
    }

    public void setLstItems(List<Letter> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public Letter getCurrent() {
        if (current == null) {
            current = new Letter();
        }
        return current;
    }

    public void setCurrent(Letter current) {
        this.current = current;
    }

    private LetterFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<Letter> getItems() {
        String temSql;
        if (fromUnit == null) {
            return null;
        }
        if (!selectText.equals("")) {
            temSql = "select f from Letter f where f.retired=false and lower(f.name) like '%" + selectText.toLowerCase() + "%' and f.unit.id = " + fromUnit.getId() + " order by f.name";
        } else if (!selectName.equals("")) {
            temSql = "select f from Letter f where f.retired=false  and lower(f.description) like '%" + selectName.toLowerCase() + "%' and f.unit.id = " + fromUnit.getId() + " order by f.name";
        } else {
            temSql = "select f from Letter f where f.retired=false and f.unit.id = " + fromUnit.getId() + " order by f.name";
        }
        items = new ListDataModel(getFacade().findBySQL(temSql));
        return items;
    }

    public List<Letter> getMyLetters() {
        Map temMap = new HashMap();
        String temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.toPerson.id=" + sessionController.getLoggedUser().getWebUserPerson().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.receivedDate  ";
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        System.out.println("SQL" + temSQL);
        return getFacade().findBySQL(temSQL, temMap);
    }

    public List<Letter> getMyLettersToCollect() {
        String temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.toPerson.id=" + sessionController.getLoggedUser().getWebUserPerson().getId() + " AND (h.sent=false or h.sent is null) ORDER BY h.receivedDate  ";
        return getFacade().findBySQL(temSQL);
    }

    public DataModel<Letter> getItemsIns() {
        if (getToInstitution() == null) {
            return null;
        }
        Map temMap = new HashMap();
        String temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.toInstitution.id=" + getSessionController().getPrivilege().getRestrictedInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.receivedDate  ";
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        System.out.println("SQL" + temSQL);
        return new ListDataModel<Letter>(getFacade().findBySQL(temSQL, temMap));
    }

    public DataModel<Letter> getItemsInsSub() {
        setToInstitution(getSessionController().getPrivilege().getRestrictedInstitution());
        if (getToInstitution() == null) {
            return null;
        }
        String temSQL;
        Map temMap = new HashMap();
        if (subject == null) {
            temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.toInstitution.id=" + getToInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        } else {
            temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.toInstitution.id=" + getToInstitution().getId() + " AND h.subject.id = " + getSubject().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        }
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", fromDate);
        System.out.println(temSQL);
        return new ListDataModel<Letter>(getFacade().findBySQL(temSQL, temMap));
    }

    public DataModel<Letter> getItemsInsSubToPrint() {
        setToInstitution(getSessionController().getPrivilege().getRestrictedInstitution());
        if (getToInstitution() == null) {
            return null;
        }
        String temSQL;
        Map temMap = new HashMap();
        if (subject == null) {
            temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.printed != true AND h.toInstitution.id=" + getToInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        } else {
            temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.printed != true AND h.toInstitution.id=" + getToInstitution().getId() + " AND h.subject.id = " + getSubject().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        }
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(fromDate);
        } catch (Exception e) {
            
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", c.getTime());
        System.out.println(temSQL);
        return new ListDataModel<Letter>(getFacade().findBySQL(temSQL, temMap));
    }

    public void markPrintInsSub() {
        String temSQL;
        setToInstitution(getSessionController().getPrivilege().getRestrictedInstitution());
        if (getToInstitution() == null) {
            return;
        }
        Map temMap = new HashMap();
        if (subject == null) {
            temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.toInstitution.id=" + getSessionController().getPrivilege().getRestrictedInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        } else {
            temSQL = "SELECT h FROM Letter h WHERE h.retired=false AND h.toInstitution.id=" + getSessionController().getPrivilege().getRestrictedInstitution().getId() + " AND h.subject.id = " + getSubject().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        }

        System.out.println(temSQL);

        temMap.put("fromDate", fromDate);
        temMap.put("toDate", fromDate);

        List<Letter> markLetters = getFacade().findBySQL(temSQL, temMap);

        for (Letter markLetter : markLetters) {
            markLetter.setPrinted(true);
            getFacade().edit(markLetter);
        }
    }

    public void setItemsIns(DataModel<Letter> itemsIns) {
        this.itemsIns = itemsIns;
    }

    public DataModel<Letter> getItemsLoc() {
        if (fromLocation != null) {
            return new ListDataModel<Letter>(getFacade().findBySQL("SELECT i From Letter i WHERE i.retired=false AND i.location.id = " + fromLocation.getId()));
        } else {
            return null;
        }
    }

    public void setItemsLoc(DataModel<Letter> itemsLoc) {
        this.itemsLoc = itemsLoc;
    }

    public void setItemsPer(DataModel<Letter> itemsPer) {
        this.itemsPer = itemsPer;
    }

    public DataModel<Letter> getItemsUni() {
        if (fromUnit != null) {
            return new ListDataModel<Letter>(getFacade().findBySQL("SELECT i From Letter i WHERE i.retired=false AND i.unit.id = " + fromUnit.getId()));
        } else {
            return null;
        }
    }

    public void setItemsUni(DataModel<Letter> itemsUni) {
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
                    current = (Letter) items.getRowData();
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

    public Letter searchItem(String itemName, boolean createNewIfNotPresent) {
        Letter searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (Letter) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new Letter();
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
        return "post_new_letter";
    }

    public String toView() {
        imageController.setLetter(current);
        return "post_view_letter";
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
        current = new Letter();
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
        try {
            current.setPrinted(false);
            current.setFromInstitution(fromInstitution);
            current.setToInstitution(toInstitution);
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            
            JsfUtil.addSuccessMessage("savedNewSuccessfully");

            setFromDate(current.getReceivedDate());
            setToDate(current.getReceivedDate());
            current = new Letter();

            return "post_institution_received";

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error" + e.getMessage());
            return "";
        }

    }

    public String prepareAddNew() {
        current = new Letter();
        return "post_new_letter";
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

    @FacesConverter(forClass = Letter.class)
    public static class LetterControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            try {
                LetterController controller = (LetterController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "letterController");
                return controller.ejbFacade.find(getKey(value));
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
            if (object instanceof Letter) {
                Letter o = (Letter) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + LetterController.class.getName());
            }
        }
    }
}
