/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.data.GroupFinding;
import gov.sp.health.data.CommonFunctions;
import gov.sp.health.data.TemFinding;
import gov.sp.health.entity.CitizenCharter;
import gov.sp.health.entity.CitizenCharterCategory;
import gov.sp.health.entity.Institution;
import gov.sp.health.entity.Location;
import gov.sp.health.entity.Person;
import gov.sp.health.entity.Subject;
import gov.sp.health.entity.Unit;
import gov.sp.health.facade.CitizenCharterCategoryFacade;
import gov.sp.health.facade.CitizenCharterFacade;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.facade.SubjectFacade;
import gov.sp.health.facade.UnitFacade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author buddhika
 */
@ManagedBean
@RequestScoped
public class CitizenCharterController {

    @EJB
    private CitizenCharterFacade ejbFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    PersonFacade personFacade;
    @EJB
    SubjectFacade subjectFacade;
    @EJB
    CitizenCharterCategoryFacade catFacade;
    //
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    @ManagedProperty(value = "#{imageController}")
    ImageController imageController;
    //
    //
    //
    List<CitizenCharter> lstItems;
    private CitizenCharter current;
    private DataModel<CitizenCharter> items = null;
    private DataModel<CitizenCharter> itemsIns = null;
    private DataModel<CitizenCharter> itemsUni = null;
    private DataModel<CitizenCharter> itemsLoc = null;
    private DataModel<CitizenCharter> itemsPer = null;
    private DataModel temItems = null;
    //
    //
    DataModel<Institution> institutions;
    DataModel<Unit> units;
    DataModel<Location> locations;
    DataModel<Person> persons;
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
    Unit unit;
    Location location;
    Person person;
    Institution institution;
    Subject subject;
    //
    //
    //
    Date fromDate;
    Date toDate;
    Double value;
    CitizenCharterCategory category;
    //
    private CartesianChartModel categoryModel;
    private CartesianChartModel linearModel;
    //
    List<Person> lstPersons;
    CitizenCharter[] selectedItems;

    //
    public CitizenCharter[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(CitizenCharter[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<GroupFinding> getLstCitizenCharterResponse() {
        List<GroupFinding> lst = new ArrayList<GroupFinding>();
        if (getInstitution() == null) {
            return null;
        }
        Map temMap = new HashMap();
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        String temSql;
        if (category == null) {
            temSql = "SELECT distinct  p FROM CitizenCharter h LEFT OUTER JOIN h.person p  WHERE h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.fromDate BETWEEN :fromDate AND :toDate ";
        } else {
            temSql = "SELECT distinct  p FROM CitizenCharter h LEFT OUTER JOIN h.person p  WHERE h.retired=false AND (h.category.id = " + category.getId() + " or h.category.parentCategory.id = " + category.getId() + " ) and h.institution.id=" + getInstitution().getId() + " AND h.fromDate BETWEEN :fromDate AND :toDate ";
        }
        List<Person> temPersons = getPersonFacade().findBySQL(temSql, temMap);
        System.out.println(temSql);
        System.out.println(temMap);
        System.out.println(temPersons);
        for (Person temPer : temPersons) {
            System.out.println(temPer);
            GroupFinding gp = new GroupFinding();
            gp.setName(temPer.getName());
            if (category == null) {
                temSql = "SELECT  h FROM CitizenCharter h WHERE h.retired=false AND h.person.id=" + temPer.getId() + " AND h.institution.id=" + getInstitution().getId() + " AND h.fromDate BETWEEN :fromDate AND :toDate ";
            } else {
                temSql = "SELECT h FROM CitizenCharter h WHERE h.retired=false AND h.person.id=" + temPer.getId() + " AND (h.category.id = " + category.getId() + " or h.category.parentCategory.id = " + category.getId() + " ) and h.institution.id=" + getInstitution().getId() + " AND h.fromDate BETWEEN :fromDate AND :toDate ";
            }
            List<CitizenCharter> lstCcs = getFacade().findBySQL(temSql, temMap);
            for (CitizenCharter cc : lstCcs) {
                //System.out.println(cc);
                Double tt = calculateGroupFinding(cc);
                //System.out.println(tt);
                if (tt == 0) {
                } else if (tt <= 1) {
                    gp.setGpOne(gp.getGpOne() + 1);
                } else if (tt <= 2) {
                    gp.setGpTwo(gp.getGpTwo() + 1);
                } else if (tt <= 3) {
                    gp.setGpThree(gp.getGpThree() + 1);
                } else {
                    gp.setGpFour(gp.getGpFour() + 1);
                }
            }
            lst.add(gp);
        }
        return lst;

    }

    private Double calculateGroupFinding(CitizenCharter c) {
        try {
            Double completedDuration;
            Double plannedFor = plannedDuration((CitizenCharterCategory) c.getCategory());
            long fromInt;
            long toInt;

            Calendar orFromDate = Calendar.getInstance();
            orFromDate.setTime(c.getFromDate());
            Calendar calFrom = Calendar.getInstance();

            calFrom.set(Calendar.YEAR, orFromDate.get(Calendar.YEAR));
            calFrom.set(Calendar.MONTH, orFromDate.get(Calendar.MONTH));
            calFrom.set(Calendar.DATE, orFromDate.get(Calendar.DATE));

            Calendar orFromTime = Calendar.getInstance();
            orFromTime.setTime(c.getFromTime());
            calFrom.set(Calendar.HOUR, orFromTime.get(Calendar.HOUR));
            calFrom.set(Calendar.MINUTE, orFromTime.get(Calendar.MINUTE));

            System.out.println("From Date " + c.getFromDate());
            System.out.println("From Time " + c.getFromTime());
            System.out.println("To Date " + c.getToDate());
            System.out.println("To Time " + c.getToTime());

            Calendar orToDate = Calendar.getInstance();
            orToDate.setTime(c.getToDate());
            Calendar calTo = Calendar.getInstance();

            calTo.set(Calendar.YEAR, orToDate.get(Calendar.YEAR));
            calTo.set(Calendar.MONTH, orToDate.get(Calendar.MONTH));
            calTo.set(Calendar.DATE, orToDate.get(Calendar.DATE));

            Calendar orToTime = Calendar.getInstance();
            orToTime.setTime(c.getToTime());
            calTo.set(Calendar.HOUR, orToTime.get(Calendar.HOUR));
            calTo.set(Calendar.MINUTE, orToTime.get(Calendar.MINUTE));
            System.out.println("To " + calTo);
            System.out.println("From " + calFrom);
            fromInt = calFrom.getTimeInMillis();
            toInt = calTo.getTimeInMillis();
            System.out.println("To " + toInt);
            System.out.println("From " + fromInt);
            completedDuration = (double) (toInt - fromInt) / (1000 * 60 * 60);
            System.out.println("Differance in milisounds " + completedDuration);
            System.out.println("Differance in hours " + completedDuration);
            return completedDuration / plannedFor;
        } catch (Exception e) {
            System.out.print(e);
            return 0.0;
        }
    }

    private Double plannedDuration(CitizenCharterCategory cc) {
        return getCatFacade().find(cc.getId()).getDblValue();
    }

    public List<CitizenCharter> getCitizenCharterEntered() {
        System.out.println("get Ins Ex");
        if (getInstitution() == null) {
            return null;
        }
        System.out.println("get Ins Ex");
        String temSql;
        Map temMap = new HashMap();
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        if (category == null) {
            temSql = "SELECT h FROM CitizenCharter h WHERE h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.fromDate BETWEEN :fromDate AND :toDate ";
        } else {
            temSql = "SELECT h FROM CitizenCharter h WHERE h.retired=false AND (h.category.id = " + category.getId() + " or h.category.parentCategory.id = " + category.getId() + " ) and h.institution.id=" + getInstitution().getId() + " AND h.fromDate BETWEEN :fromDate AND :toDate ";
        }
        System.out.println(temSql);
        System.out.println(getFacade().findBySQL(temSql, temMap).size() + " In Size");
        return getFacade().findBySQL(temSql, temMap);
    }

    public List<Person> getLstPersons() {
        if (getInstitution() != null) {
            return getPersonFacade().findBySQL("select l from Person l where l.retired=false and l.institution.id = " + getInstitution().getId() + " order by l.givenName");
        } else {
            return null;
        }
    }

    public void setLstPersons(List<Person> lstPersons) {
        this.lstPersons = lstPersons;
    }

    public CartesianChartModel getCategoryModel() {
        if (categoryModel == null) {
            categoryModel = new CartesianChartModel();
        }
        return categoryModel;
    }

    public void setCategoryModel(CartesianChartModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    public void setLinearModel(CartesianChartModel linearModel) {
        this.linearModel = linearModel;
    }

    public CitizenCharterCategory getCategory() {
        return category;
    }

    public void setCategory(CitizenCharterCategory category) {
        this.category = category;
    }

    public CitizenCharterCategoryFacade getCatFacade() {
        return catFacade;
    }

    public void setCatFacade(CitizenCharterCategoryFacade catFacade) {
        this.catFacade = catFacade;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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
        if (getUnit() != null) {
            return new ListDataModel(getFacade().findBySQL("select l from Location l where l.retired=false and l.unit.id = " + getUnit().getId() + " order by l.name"));
        } else {
            return null;
        }

    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public DataModel<Person> getPersons() {
        if (getInstitution() != null) {
            return new ListDataModel(getPersonFacade().findBySQL("select l from Person l where l.retired=false and l.institution.id = " + getInstitution().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setPersons(DataModel<Person> persons) {
        this.persons = persons;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    public DataModel<Location> getLocations() {
        if (getUnit() != null) {
            return new ListDataModel(getFacade().findBySQL("select l from Location l where l.retired=false and l.unit.id = " + getUnit().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setLocations(DataModel<Location> locations) {
        this.locations = locations;
    }

    public DataModel<Unit> getUnits() {
        if (getInstitution() != null) {
            return new ListDataModel(getUnitFacade().findBySQL("select l from Unit l where l.retired=false and l.institution.id = " + getInstitution().getId() + " order by l.name"));
        } else {
            return null;
        }
    }

    public void setUnits(DataModel<Unit> units) {
        this.units = units;
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
    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Location getFromLocation() {
        return location;
    }

    public void setFromLocation(Location fromLocation) {
        this.location = fromLocation;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public CitizenCharterController() {
    }

    public CitizenCharterFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(CitizenCharterFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<CitizenCharter> getLstItems() {
        return getFacade().findBySQL("Select d From CitizenCharter d WHERE d.retired=false ORDERBY i.name");
    }

    public void setLstItems(List<CitizenCharter> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public CitizenCharter getCurrent() {
        if (current == null) {
            current = new CitizenCharter();
            current.setFromDate(Calendar.getInstance().getTime());
            current.setToDate(Calendar.getInstance().getTime());
            current.setFromTime(Calendar.getInstance().getTime());
            current.setToTime(Calendar.getInstance().getTime());
            current.setInstitution(sessionController.loggedUser.getWebUserPerson().getInstitution());
            current.setPerson(sessionController.loggedUser.getWebUserPerson());
        }
        return current;
    }

    public void setCurrent(CitizenCharter current) {
        this.current = current;
    }

    private CitizenCharterFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<CitizenCharter> getItems() {
        String temSql;
        if (unit == null) {
            return null;
        }
        if (!selectText.equals("")) {
            temSql = "select f from CitizenCharter f where f.retired=false and lower(f.name) like '%" + selectText.toLowerCase() + "%' and f.unit.id = " + unit.getId() + " order by f.name";
        } else if (!selectName.equals("")) {
            temSql = "select f from CitizenCharter f where f.retired=false  and lower(f.description) like '%" + selectName.toLowerCase() + "%' and f.unit.id = " + unit.getId() + " order by f.name";
        } else {
            temSql = "select f from CitizenCharter f where f.retired=false and f.unit.id = " + unit.getId() + " order by f.name";
        }
        items = new ListDataModel(getFacade().findBySQL(temSql));
        return items;
    }

    public void createInsCitizenCharterGraph() {
        System.out.println("Getting create");
        if (getInstitution() == null) {
            return;
        }
        System.out.println("Getting create ok");
        categoryModel = new CartesianChartModel();
        ChartSeries citizenCharters;
        if (category == null) {
            System.out.println("Category Null 1");
            List<CitizenCharterCategory> cats = getCatFacade().findAll("name", true);;
            for (Iterator<CitizenCharterCategory> it = cats.iterator(); it.hasNext();) {
                CitizenCharterCategory cat = it.next();
                citizenCharters = new ChartSeries();
                category = (CitizenCharterCategory) cat;
                citizenCharters.setLabel(category.getName());
                List<TemFinding> temLst = getInsCitizenCharters();
                System.out.println("Category Not Null " + temLst.size());
                for (TemFinding temFs : temLst) {
                    System.out.println(temFs.toString());
                    citizenCharters.set(temFs.getFindingName(), temFs.getFindingValue());
                }
                categoryModel.addSeries(citizenCharters);
            }


        } else {
            System.out.println("Category Null 2");
            citizenCharters = new ChartSeries();
            citizenCharters.setLabel(category.getName());
            List<TemFinding> temLst = getInsCitizenCharters();
            System.out.println("Category Null " + temLst.size());
            for (TemFinding temFs : temLst) {
                System.out.println(temFs.toString());
                citizenCharters.set(temFs.getFindingName(), temFs.getFindingValue());
            }
            categoryModel.addSeries(citizenCharters);
        }

    }

    public void allCategories() {
        category = null;
    }

    public List<TemFinding> getInsCitizenCharters() {
        System.out.println("get Ins Ex");
        if (getInstitution() == null) {
            return null;
        }

        System.out.println("get Ins Ex");

        List<TemFinding> temFindings = new ArrayList<TemFinding>();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MMMM/yyyy");
        List<Date> months = CommonFunctions.getMonthsBetween(fromDate, toDate);
        for (Date temMonth : months) {
            Map temMap = new HashMap();
            Date firstDate = CommonFunctions.firstDateOfMonth(temMonth);
            Date lastDate = CommonFunctions.lastDateOfMonth(temMonth);
            String temSQL;
            if (category == null) {

                temSQL = "SELECT sum(h.doubleValue) FROM CitizenCharter h WHERE h.calculated = true and h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";

            } else {

                temSQL = "SELECT sum(h.doubleValue) FROM CitizenCharter h WHERE h.calculated = true and h.retired=false AND h.category.id = " + category.getId() + " and h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
            }
            temMap.put("fromDate", firstDate);
            temMap.put("toDate", lastDate);
            System.out.println("From Date" + fromDate);
            System.out.println("To Date" + toDate);
            System.out.println("SQL" + temSQL);
            TemFinding temFinding = new TemFinding(sdf.format(firstDate), getFacade().findAggregateDbl(temSQL, temMap));
            temFindings.add(temFinding);
        }
//        CitizenCharter h = new CitizenCharter();
//        h.setInstitution(institution);
        return temFindings;
    }

    public DataModel<CitizenCharter> getItemsIns() {
        if (getInstitution() == null) {
            return null;
        }
        Map temMap = new HashMap();
        String temSQL = "SELECT h FROM CitizenCharter h WHERE h.retired=false AND h.toInstitution.id=" + getInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.receivedDate  ";
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        System.out.println("SQL" + temSQL);
        return new ListDataModel<CitizenCharter>(getFacade().findBySQL(temSQL, temMap));
    }

    public DataModel<CitizenCharter> getItemsInsSub() {
        if (getInstitution() == null) {
            return null;
        }
        String temSQL;
        Map temMap = new HashMap();
        if (subject == null) {
            temSQL = "SELECT h FROM CitizenCharter h WHERE h.retired=false AND h.toInstitution.id=" + getInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        } else {
            temSQL = "SELECT h FROM CitizenCharter h WHERE h.retired=false AND h.toInstitution.id=" + getInstitution().getId() + " AND h.subject.id = " + getSubject().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        }
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", fromDate);
        return new ListDataModel<CitizenCharter>(getFacade().findBySQL(temSQL, temMap));
    }

    public void setItemsIns(DataModel<CitizenCharter> itemsIns) {
        this.itemsIns = itemsIns;
    }

    public DataModel<CitizenCharter> getItemsLoc() {
        if (location != null) {
            return new ListDataModel<CitizenCharter>(getFacade().findBySQL("SELECT i From CitizenCharter i WHERE i.retired=false AND i.location.id = " + location.getId()));
        } else {
            return null;
        }
    }

    public void setItemsLoc(DataModel<CitizenCharter> itemsLoc) {
        this.itemsLoc = itemsLoc;
    }

    public void setItemsPer(DataModel<CitizenCharter> itemsPer) {
        this.itemsPer = itemsPer;
    }

    public DataModel<CitizenCharter> getItemsUni() {
        if (unit != null) {
            return new ListDataModel<CitizenCharter>(getFacade().findBySQL("SELECT i From CitizenCharter i WHERE i.retired=false AND i.unit.id = " + unit.getId()));
        } else {
            return null;
        }
    }

    public void setItemsUni(DataModel<CitizenCharter> itemsUni) {
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
                    current = (CitizenCharter) items.getRowData();
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

    public CitizenCharter searchItem(String itemName, boolean createNewIfNotPresent) {
        CitizenCharter searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (CitizenCharter) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new CitizenCharter();
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
        return "post_new_citizenCharter";
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
        current = new CitizenCharter();
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
//        if (getCategory() == null) {
//            JsfUtil.addErrorMessage("Please select a category");
//            return "";
//        }
        getCurrent();

//        current.setInstitution(institution);
        current.setCreatedAt(Calendar.getInstance().getTime());
        current.setCreater(sessionController.loggedUser);
//        current.setFromDate(toDate);
//        current.setToDate(toDate);
//        current.setTheDate(toDate);
//        current.setDoubleValue(value);
        current.setActual(true);
        current.setCalculated(true);
        current.setCompleted(true);
        getFacade().create(current);
        current = null;
        current = getCurrent();
        JsfUtil.addSuccessMessage("CitizenCharter added successfully");
        setValue(0.0);
        return "";
    }

    public String prepareAddNew() {
        current = new CitizenCharter();
        return "post_new_citizenCharter";
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

    @FacesConverter(forClass = CitizenCharter.class)
    public static class CitizenCharterControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            try {
                CitizenCharterController controller = (CitizenCharterController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "citizenCharterController");
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

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CitizenCharter) {
                CitizenCharter o = (CitizenCharter) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + CitizenCharterController.class.getName());
            }
        }
    }
}
