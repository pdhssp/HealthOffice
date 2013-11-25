/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.data.CommonFunctions;
import gov.sp.health.data.TemFinding;
import gov.sp.health.entity.*;
import gov.sp.health.facade.ExpenseFacade;
import gov.sp.health.facade.FinancialFindingCategoryFacade;
import gov.sp.health.facade.HeadCountFacade;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.facade.SubjectFacade;
import gov.sp.health.facade.UnitFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class ExpenseController implements Serializable {

    @EJB
    private ExpenseFacade ejbFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    PersonFacade personFacade;
    @EJB
    SubjectFacade subjectFacade;
    @EJB
    FinancialFindingCategoryFacade financeCatFacade;
    @EJB
    HeadCountFacade hcFacade;
    //
    @Inject
    SessionController sessionController;
    @Inject
    ImageController imageController;
    //
    //
    //
    List<Expense> lstItems;
    private Expense current;
    private DataModel<Expense> items = null;
    private DataModel<Expense> itemsIns = null;
    private DataModel<Expense> itemsUni = null;
    private DataModel<Expense> itemsLoc = null;
    private DataModel<Expense> itemsPer = null;
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
    Long amount;
    FinancialFindingCategory category;
    //
    private CartesianChartModel categoryModel;
    private CartesianChartModel linearModel;
    //
    Expense[] selectedItems;

    public Expense[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Expense[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public HeadCountFacade getHcFacade() {
        return hcFacade;
    }

    public void setHcFacade(HeadCountFacade hcFacade) {
        this.hcFacade = hcFacade;
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

    public FinancialFindingCategory getCategory() {
        return category;
    }

    public void setCategory(FinancialFindingCategory category) {
        this.category = category;
    }

    public FinancialFindingCategoryFacade getFinanceCatFacade() {
        return financeCatFacade;
    }

    public void setFinanceCatFacade(FinancialFindingCategoryFacade financeCatFacade) {
        this.financeCatFacade = financeCatFacade;
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
        if (sessionController.getPrivilege().getRestrictedInstitution() != null) {
            temSQL = "SELECT i FROM Institution i WHERE i.retired=false AND i.id = " + sessionController.getPrivilege().getRestrictedInstitution().getId();
        } else {
            temSQL = "SELECT i FROM Institution i WHERE i.retired=false ORDER BY i.name";
        }
//        temSQL = "SELECT i FROM Institution i WHERE i.retired=false ORDER BY i.name";
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

    public ExpenseController() {
    }

    public ExpenseFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ExpenseFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<Expense> getLstItems() {
        return getFacade().findBySQL("Select d From Expense d WHERE d.retired=false ORDERBY i.name");
    }

    public void setLstItems(List<Expense> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public Expense getCurrent() {
        if (current == null) {
            current = new Expense();
        }
        return current;
    }

    public void setCurrent(Expense current) {
        this.current = current;
    }

    private ExpenseFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<Expense> getItems() {
        String temSql;
        if (unit == null) {
            return null;
        }
        if (!selectText.equals("")) {
            temSql = "select f from Expense f where f.retired=false and lower(f.name) like '%" + selectText.toLowerCase() + "%' and f.unit.id = " + unit.getId() + " order by f.name";
        } else if (!selectName.equals("")) {
            temSql = "select f from Expense f where f.retired=false  and lower(f.description) like '%" + selectName.toLowerCase() + "%' and f.unit.id = " + unit.getId() + " order by f.name";
        } else {
            temSql = "select f from Expense f where f.retired=false and f.unit.id = " + unit.getId() + " order by f.name";
        }
        items = new ListDataModel(getFacade().findBySQL(temSql));
        return items;
    }

    public void createInsExpenseGraph() {
        System.out.println("Getting create");
        if (getInstitution() == null) {
            return;
        }
        System.out.println("Getting create ok");
        categoryModel = new CartesianChartModel();
        ChartSeries expenses;
        if (category == null) {
            System.out.println("Category Null 1");
            List<FinancialFindingCategory> cats = getFinanceCatFacade().findAll("name", true);;
            for (Iterator<FinancialFindingCategory> it = cats.iterator(); it.hasNext();) {
                FinancialFindingCategory cat = it.next();
                expenses = new ChartSeries();
                category = (FinancialFindingCategory) cat;
                expenses.setLabel(category.getName());
                List<TemFinding> temLst = getInsExpenses();
                System.out.println("Category Not Null " + temLst.size());
                for (TemFinding temFs : temLst) {
                    System.out.println(temFs.toString());
                    expenses.set(temFs.getFindingName(), temFs.getFindingValue());
                }
                categoryModel.addSeries(expenses);
            }


        } else {
            System.out.println("Category Null 2");
            expenses = new ChartSeries();
            expenses.setLabel(category.getName());
            List<TemFinding> temLst = getInsExpenses();
            System.out.println("Category Null " + temLst.size());
            for (TemFinding temFs : temLst) {
                System.out.println(temFs.toString());
                expenses.set(temFs.getFindingName(), temFs.getFindingValue());
            }
            categoryModel.addSeries(expenses);
        }

    }

    public void createInsExpenseGraphPerHead() {
        System.out.println("Getting create");
        if (getInstitution() == null) {
            return;
        }
        System.out.println("Getting create ok");
        categoryModel = new CartesianChartModel();
        ChartSeries expenses;
        if (category == null) {
            System.out.println("Category Null 1");
            List<FinancialFindingCategory> cats = getFinanceCatFacade().findAll("name", true);;
            for (Iterator<FinancialFindingCategory> it = cats.iterator(); it.hasNext();) {
                FinancialFindingCategory cat = it.next();
                expenses = new ChartSeries();
                category = (FinancialFindingCategory) cat;
                expenses.setLabel(category.getName());
                List<TemFinding> temLst = getInsExpensesPerHead();
                System.out.println("Category Not Null " + temLst.size());
                for (TemFinding temFs : temLst) {
                    System.out.println(temFs.toString());
                    expenses.set(temFs.getFindingName(), temFs.getFindingValue());
                }
                categoryModel.addSeries(expenses);
            }


        } else {
            System.out.println("Category Null 2");
            expenses = new ChartSeries();
            expenses.setLabel(category.getName());
            List<TemFinding> temLst = getInsExpensesPerHead();
            System.out.println("Category Null " + temLst.size());
            for (TemFinding temFs : temLst) {
                System.out.println(temFs.toString());
                expenses.set(temFs.getFindingName(), temFs.getFindingValue());
            }
            categoryModel.addSeries(expenses);
        }

    }

    public void allCategories() {
        category = null;
    }

    public List<TemFinding> getInsExpenses() {
        System.out.println("get Ins Ex");
        if (getInstitution() == null) {
            return null;
        }

        System.out.println("get Ins Ex");

        List<TemFinding> temFindings = new ArrayList<TemFinding>();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MMM yy");
        List<Date> months = CommonFunctions.getMonthsBetween(fromDate, toDate);
        for (Date temMonth : months) {
            Map temMap = new HashMap();
            Date firstDate = CommonFunctions.firstDateOfMonth(temMonth);
            Date lastDate = CommonFunctions.lastDateOfMonth(temMonth);
            String temSQL;
            if (category == null) {
                temSQL = "SELECT sum(h.doubleValue) FROM Expense h WHERE h.calculated = true and h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
            } else {
                temSQL = "SELECT sum(h.doubleValue) FROM Expense h WHERE h.calculated = true and h.retired=false AND (h.category.id = " + category.getId() + " or h.category.parentCategory.id = " + category.getId() + ") and h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
            }
            temMap.put("fromDate", firstDate);
            temMap.put("toDate", lastDate);
            System.out.println("From Date" + fromDate);
            System.out.println("To Date" + toDate);
            System.out.println("SQL" + temSQL);
            TemFinding temFinding = new TemFinding(sdf.format(firstDate), getFacade().findAggregateDbl(temSQL, temMap));
            temFindings.add(temFinding);
        }
//        Expense h = new Expense();
//        h.setInstitution(institution);
        return temFindings;
    }

    public List<Expense> getExpensesEntered() {
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
            temSql = "SELECT h FROM Expense h WHERE h.actual = true and h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
        } else {
            temSql = "SELECT h FROM Expense h WHERE h.actual = true and h.retired=false AND (h.category.id = " + category.getId() + " or h.category.parentCategory.id = " + category.getId() + " ) and h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
        }
        System.out.println(temSql);
        return getFacade().findBySQL(temSql, temMap);
    }


    public List<Expense> getExpensesCalculated() {
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
            temSql = "SELECT h FROM Expense h WHERE h.calculated = true and h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
        } else {
            temSql = "SELECT h FROM Expense h WHERE h.calculated = true and h.retired=false AND (h.category.id = " + category.getId() + " or h.category.parentCategory.id = " + category.getId() + " ) and  h.theDate BETWEEN :fromDate AND :toDate ";
        }
        return getFacade().findBySQL(temSql, temMap);
    }
    
    
    public List<TemFinding> getInsExpensesPerHead() {
        System.out.println("get Ins Ex");
        if (getInstitution() == null) {
            return null;
        }

        System.out.println("get Ins Ex");

        List<TemFinding> temFindings = new ArrayList<TemFinding>();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MMM yy");
        List<Date> months = CommonFunctions.getMonthsBetween(fromDate, toDate);
        for (Date temMonth : months) {
            Map temMap = new HashMap();
            Date firstDate = CommonFunctions.firstDateOfMonth(temMonth);
            Date lastDate = CommonFunctions.lastDateOfMonth(temMonth);
            String temSQL;
            if (category == null) {

                temSQL = "SELECT sum(h.doubleValue) FROM Expense h WHERE h.calculated = true and h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";

            } else {

                temSQL = "SELECT sum(h.doubleValue) FROM Expense h WHERE h.calculated = true and h.retired=false AND h.category.id = " + category.getId() + " and h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
            }
            temMap.put("fromDate", firstDate);
            temMap.put("toDate", lastDate);
            System.out.println("From Date" + fromDate);
            System.out.println("To Date" + toDate);
            System.out.println("SQL" + temSQL);
            Double dblExpense;
            Double headCount;
            dblExpense = getFacade().findAggregateDbl(temSQL, temMap);
            temSQL = "SELECT avg(h.doubleValue) FROM HeadCount h WHERE h.retired=false AND h.institution.id=" + getInstitution().getId() + " AND h.theDate BETWEEN :fromDate AND :toDate ";
            headCount = getHcFacade().findAggregateDbl(temSQL, temMap);
            TemFinding temFinding;
//            System.out.println("Head Count is " + headCount);
//            System.out.println("Expense is " + dblExpense);
//            System.out.println("Head Count is " + dblExpense / headCount);
            if (headCount == null || dblExpense == null || headCount == 0) {
                temFinding = new TemFinding(sdf.format(firstDate), null);
            } else {
                temFinding = new TemFinding(sdf.format(firstDate), dblExpense / headCount);
            }
            temFindings.add(temFinding);
        }
//        Expense h = new Expense();
//        h.setInstitution(institution);
        return temFindings;
    }

    public DataModel<Expense> getItemsIns() {
        if (getInstitution() == null) {
            return null;
        }
        Map temMap = new HashMap();
        String temSQL = "SELECT h FROM Expense h WHERE h.retired=false AND h.toInstitution.id=" + getInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.receivedDate  ";
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        System.out.println("SQL" + temSQL);
        return new ListDataModel<Expense>(getFacade().findBySQL(temSQL, temMap));
    }

    public DataModel<Expense> getItemsInsSub() {
        if (getInstitution() == null) {
            return null;
        }
        String temSQL;
        Map temMap = new HashMap();
        if (subject == null) {
            temSQL = "SELECT h FROM Expense h WHERE h.retired=false AND h.toInstitution.id=" + getInstitution().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        } else {
            temSQL = "SELECT h FROM Expense h WHERE h.retired=false AND h.toInstitution.id=" + getInstitution().getId() + " AND h.subject.id = " + getSubject().getId() + " AND h.receivedDate BETWEEN :fromDate AND :toDate ORDER BY h.toPerson.givenName  ";
        }
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", fromDate);
        return new ListDataModel<Expense>(getFacade().findBySQL(temSQL, temMap));
    }

    public void setItemsIns(DataModel<Expense> itemsIns) {
        this.itemsIns = itemsIns;
    }

    public DataModel<Expense> getItemsLoc() {
        if (location != null) {
            return new ListDataModel<Expense>(getFacade().findBySQL("SELECT i From Expense i WHERE i.retired=false AND i.location.id = " + location.getId()));
        } else {
            return null;
        }
    }

    public void setItemsLoc(DataModel<Expense> itemsLoc) {
        this.itemsLoc = itemsLoc;
    }

    public void setItemsPer(DataModel<Expense> itemsPer) {
        this.itemsPer = itemsPer;
    }

    public DataModel<Expense> getItemsUni() {
        if (unit != null) {
            return new ListDataModel<Expense>(getFacade().findBySQL("SELECT i From Expense i WHERE i.retired=false AND i.unit.id = " + unit.getId()));
        } else {
            return null;
        }
    }

    public void setItemsUni(DataModel<Expense> itemsUni) {
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
                    current = (Expense) items.getRowData();
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

    public Expense searchItem(String itemName, boolean createNewIfNotPresent) {
        Expense searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (Expense) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new Expense();
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
        return "post_new_expense";
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
        current = new Expense();
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

    public String removeSelected() {
        if (selectedItems != null && selectedItems.length != 0) {
            for (Expense ex : selectedItems) {
                removeDerives(ex);
                ex.setRetired(true);
                ex.setRetiredAt(Calendar.getInstance().getTime());
                ex.setRetirer(sessionController.loggedUser);
                getFacade().edit(ex);
            }
        }
        return "";
    }

    private void removeDerives(Expense ex) {
        String temSql = "select e from Expense e where e.derivedFrom.id = " + ex.getId();
        List<Expense> temExs = getFacade().findBySQL(temSql);
        for (Expense temEx : temExs) {
            temEx.setRetired(true);
            temEx.setRetiredAt(Calendar.getInstance().getTime());
            temEx.setRetirer(sessionController.loggedUser);
            getFacade().edit(temEx);
        }
    }

    public String addDirectly() {
//        if (institution == null) {
//            JsfUtil.addErrorMessage("Please select an institution");
//            return "";
//        }
        if (getCategory() == null) {
            JsfUtil.addErrorMessage("Please select a category");
            return "";
        }
        getCurrent();
        Expense originalExpense;
//        try {
            if (fromDate == toDate) {
                current.setInstitution(sessionController.getLoggedUser().getWebUserPerson().getInstitution());
                current.setCreatedAt(Calendar.getInstance().getTime());
                current.setCreater(sessionController.loggedUser);
                current.setFromDate(fromDate);
                current.setToDate(toDate);
                current.setTheDate(toDate);
                current.setDoubleValue(value);
                current.setLongValue(amount);
                current.setActual(true);
                current.setCalculated(true);
                current.setCategory(category);
                getFacade().create(current);
            } else {
                current.setInstitution(sessionController.getLoggedUser().getWebUserPerson().getInstitution());
                current.setCreatedAt(Calendar.getInstance().getTime());
                current.setCreater(sessionController.loggedUser);
                current.setFromDate(fromDate);
                current.setToDate(toDate);
                current.setTheDate(toDate);
                current.setDoubleValue(value);
                current.setLongValue(amount);
                current.setActual(true);
                current.setCategory(category);
                current.setCalculated(false);
                getFacade().create(current);
                originalExpense = current;
                List<Date> allDays = CommonFunctions.datesBetween(fromDate, toDate);
                for (Date temDate : allDays) {
                    Expense temEx = new Expense();
                    temEx.setInstitution(sessionController.getLoggedUser().getWebUserPerson().getInstitution());
                    temEx.setCreatedAt(Calendar.getInstance().getTime());
                    temEx.setCreater(sessionController.loggedUser);
                    temEx.setFromDate(fromDate);
                    temEx.setToDate(toDate);
                    temEx.setActual(false);
                    temEx.setCalculated(true);
                    temEx.setTheDate(temDate);
                    temEx.setCategory(category);
                    temEx.setDerivedFrom(originalExpense);
                    System.out.println("Writing for the data " + temDate);
                    try {
                        temEx.setDoubleValue(value / (allDays.size()));
                        temEx.setLongValue(amount / (allDays.size()));
                    } catch (Exception e) {
                    }
                    getFacade().create(temEx);
                }
            }
            current = new Expense();
            JsfUtil.addSuccessMessage("Expense added successfully");
            setValue(0.0);
            setAmount(0l);
            return "";

//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, "Error" + e.getMessage());
//            return "";
//        }

    }

    public String prepareAddNew() {
        current = new Expense();
        return "post_new_expense";
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

    @FacesConverter(forClass = Expense.class)
    public static class ExpenseControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            try {
                ExpenseController controller = (ExpenseController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "expenseController");
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
            if (object instanceof Expense) {
                Expense o = (Expense) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ExpenseController.class.getName());
            }
        }
    }
}
