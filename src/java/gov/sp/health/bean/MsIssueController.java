/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.*;
import gov.sp.health.facade.BillFacade;
import gov.sp.health.facade.BillItemFacade;
import gov.sp.health.facade.CountryFacade;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.ItemFacade;
import gov.sp.health.facade.ItemUnitFacade;
import gov.sp.health.facade.ItemUnitHistoryFacade;
import gov.sp.health.facade.LocationFacade;
import gov.sp.health.facade.MakeFacade;
import gov.sp.health.facade.ManufacturerFacade;
import gov.sp.health.facade.ModalFacade;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.facade.SupplierFacade;
import gov.sp.health.facade.UnitFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Buddhika
 */
@ManagedBean
@ViewScoped
public class MsIssueController implements Serializable {

    /**
     *
     * Enterprise Java Beans
     *
     *
     */
    @EJB
    private ItemFacade itemFacade;
    @EJB
    private ModalFacade modalFacade;
    @EJB
    private MakeFacade makeFacade;
    @EJB
    private BillFacade billFacade;
    @EJB
    private BillItemFacade billItemFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    LocationFacade locationFacade;
    @EJB
    PersonFacade personFacade;
    @EJB
    CountryFacade countryFacade;
    @EJB
    ManufacturerFacade manufacturerFacade;
    @EJB
    SupplierFacade supplierFacade;
    @EJB
    ItemUnitFacade itemUnitFacade;
    @EJB
    ItemUnitHistoryFacade itemUnitHistoryFacade;
    /**
     * Managed Properties
     */
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    /**
     * Collections
     */
    DataModel<Item> items;
    DataModel<Make> makes;
    DataModel<ItemUnit> itemUnits;
    //
    DataModel<BillItemEntry> billItemEntrys;
    List<BillItemEntry> lstBillItemEntrys;
    //
    DataModel<Institution> fromInstitutions;
    DataModel<Unit> fromUnits;
    DataModel<Location> fromLocations;
    DataModel<Person> fromPersons;
    //
    DataModel<Institution> toInstitutions;
    DataModel<Unit> toUnits;
    DataModel<Location> toLocations;
    DataModel<Person> toPersons;
    //
    DataModel<Country> countries;
    DataModel<Supplier> suppliers;
    DataModel<Manufacturer> manufacturers;
    //
    /*
     * Current Objects
     *
     */
    Bill bill;
    BillItem billItem;
    BillItemEntry billItemEntry;
    BillItemEntry editBillItemEntry;
    Institution institution;
    Unit unit;
    ItemUnit itemUnit;
    ItemUnit editItemUnit;
    /**
     * Entries
     */
    String modalName;
    String itemSearchText;
    List<ItemUnit> selectedItems;

    public String getItemSearchText() {
        return itemSearchText;
    }

    public void setItemSearchText(String itemSearchText) {
        this.itemSearchText = itemSearchText;
    }

    public List<ItemUnit> getSelectedItems() {
        if (getUnit() == null) {
            return null;
        }
        String temSql;
        
        return getItemUnitFacade().findBySQL("SELECT i FROM ItemUnit i WHERE i.retired=false ORDER By i.name");

    }

    public List<ItemUnit> selectedItems(String searchText) {
        if (getUnit() == null) {
            return null;
        }
        String temSql;
        System.out.println(searchText);
        return getItemUnitFacade().findBySQL("SELECT i FROM ItemUnit i WHERE i.retired=false AND i.unit.id = "
                + getUnit().getId() + " ORDER By i.name");

    }    
    
    public void setSelectedItems(List<ItemUnit> selectedItems) {
        this.selectedItems = selectedItems;
    }

    /**
     *
     * Methods
     *
     */
    public void addItemToList() {
        orderBillItemEntries();
        if (billItemEntry == null) {
            JsfUtil.addErrorMessage("Hothing to add");
            return;
        }
        /* TODO: Warning : CorrectLogic Here */

        addLastBillEntryNumber(billItemEntry);
        getLstBillItemEntrys().add(billItemEntry);
        calculateBillValue();
        clearEntry();

    }

    private void orderBillItemEntries() {
        long l = 1l;
        for (BillItemEntry entry : lstBillItemEntrys) {
            entry.setId(l);
            l++;
        }
    }

    public void removeItemFromList() {
        if (editBillItemEntry == null) {
            JsfUtil.addErrorMessage("Nothing to Delete. Please select one");
        }
        getLstBillItemEntrys().remove(editBillItemEntry);
        orderBillItemEntries();
        editBillItemEntry = null;
        JsfUtil.addSuccessMessage("Removed From List");
    }

    public void settleBill() {
        saveNewBill();
        saveNewBillItems();
        clearEntry();
        clearBill();
        JsfUtil.addSuccessMessage("Bill Settled successfully");
    }

    private void clearEntry() {
        modalName = null;
        billItemEntry = new BillItemEntry();
        billItemEntry = null;
        billItemEntry = getBillItemEntry();
    }

    private void clearBill() {
        bill = new Bill();
        lstBillItemEntrys = new ArrayList<BillItemEntry>();


    }

    public Double calculateStock(Item item) {
        if (item != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + "");
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(Item item, Institution institution) {
        if (item != null && institution != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.institution.id = " + institution.getId());
        } else {
            return 0.0;
        }
    }

    public Double calculateStock(Item item, Location location) {
        if (item != item && location != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.location.id = " + location.getId());
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(Item item, Unit unit) {
        if (item != null && unit != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.unit.id = " + unit.getId());
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(Item item, Person person) {
        if (item != null && person != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.person.id = " + person.getId());
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(String strJQL) {
        System.out.println(strJQL);
        System.out.println(getBillFacade().toString());
        System.out.println(getBillFacade().findAll().toString());
        return getBillFacade().findAggregateDbl(strJQL);
    }

    private void saveNewBill() {
        Bill temBill = getBill();
        temBill.setCreatedAt(Calendar.getInstance().getTime());
        temBill.setCreater(sessionController.getLoggedUser());
        temBill.setDiscountCost(temBill.getDiscountValue());
        temBill.setDiscountValuePercent(temBill.getDiscountValue() * 100 / temBill.getNetValue());
        temBill.setDiscountCostPercent(temBill.getDiscountValuePercent());
        temBill.setGrossCost(temBill.getGrossValue());
        temBill.setNetCost(temBill.getNetValue());
        getBillFacade().create(temBill);
    }

    private void saveNewBillItems() {
        for (BillItemEntry temEntry : lstBillItemEntrys) {
            settleBillItem(temEntry);
        }
    }

    private void settleBillItem(BillItemEntry temEntry) {
        BillItem temBillItem = temEntry.getBillItem();
        ItemUnit goingOutItemUnit = temBillItem.getItemUnit();




        ItemUnitHistory hxUnit = new ItemUnitHistory();
        ItemUnitHistory hxLoc = new ItemUnitHistory();
        ItemUnitHistory hxIns = new ItemUnitHistory();
        ItemUnitHistory hxPer = new ItemUnitHistory();


        hxIns.setBeforeQty(calculateStock(goingOutItemUnit.getItem(), goingOutItemUnit.getInstitution()));
        hxIns.setCreatedAt(Calendar.getInstance().getTime());
        hxIns.setCreater(sessionController.loggedUser);
        hxIns.setInstitution(goingOutItemUnit.getInstitution());
        hxIns.setItem(goingOutItemUnit.getItem());
        hxIns.setQuentity(goingOutItemUnit.getQuentity());
        hxIns.setToIn(Boolean.FALSE);
        hxIns.setToOut(Boolean.TRUE);


        hxUnit.setBeforeQty(calculateStock(goingOutItemUnit.getItem(), goingOutItemUnit.getUnit()));
        hxUnit.setCreatedAt(Calendar.getInstance().getTime());
        hxUnit.setCreater(sessionController.loggedUser);
        hxUnit.setUnit(goingOutItemUnit.getUnit());
        hxUnit.setItem(goingOutItemUnit.getItem());
        hxUnit.setQuentity(goingOutItemUnit.getQuentity());
        hxUnit.setToIn(Boolean.FALSE);
        hxUnit.setToOut(Boolean.TRUE);

        hxLoc.setBeforeQty(calculateStock(goingOutItemUnit.getItem(), goingOutItemUnit.getLocation()));
        hxLoc.setCreatedAt(Calendar.getInstance().getTime());
        hxLoc.setCreater(sessionController.loggedUser);
        hxLoc.setLocation(goingOutItemUnit.getLocation());
        hxLoc.setItem(goingOutItemUnit.getItem());
        hxLoc.setQuentity(goingOutItemUnit.getQuentity());
        hxLoc.setToIn(Boolean.FALSE);
        hxLoc.setToOut(Boolean.TRUE);

        hxPer.setBeforeQty(calculateStock(goingOutItemUnit.getItem(), goingOutItemUnit.getPerson()));
        hxPer.setCreatedAt(Calendar.getInstance().getTime());
        hxPer.setCreater(sessionController.loggedUser);
        hxPer.setPerson(goingOutItemUnit.getPerson());
        hxPer.setItem(goingOutItemUnit.getItem());
        hxPer.setQuentity(goingOutItemUnit.getQuentity());
        hxPer.setToIn(Boolean.FALSE);
        hxPer.setToOut(Boolean.TRUE);

        System.out.println("Before Stock" + calculateStock(goingOutItemUnit.getItem(), getBill().getFromInstitution()));
        System.out.println("Before Save" + goingOutItemUnit);

        goingOutItemUnit.setInstitution(null);
        goingOutItemUnit.setLocation(null);
        goingOutItemUnit.setOwner(null);
        goingOutItemUnit.setUnit(null);
        goingOutItemUnit.setPerson(null);
        goingOutItemUnit.setQuentity(temBillItem.getQuentity());
        getItemUnitFacade().edit(goingOutItemUnit);

        System.out.println("After Save" + goingOutItemUnit);
        System.out.println("After Stock" + calculateStock(goingOutItemUnit.getItem(), goingOutItemUnit.getInstitution()));

        hxIns.setAfterQty(calculateStock(goingOutItemUnit.getItem(), getBill().getFromInstitution()));

        hxIns.setItemUnit(goingOutItemUnit);
        getItemUnitHistoryFacade().create(hxIns);

        hxUnit.setAfterQty(calculateStock(goingOutItemUnit.getItem(), getBill().getFromUnit()));
        hxUnit.setItemUnit(goingOutItemUnit);
        getItemUnitHistoryFacade().create(hxUnit);

        hxLoc.setAfterQty(calculateStock(goingOutItemUnit.getItem(), getBill().getFromLocation()));
        hxLoc.setItemUnit(goingOutItemUnit);
        getItemUnitHistoryFacade().create(hxLoc);

        hxPer.setAfterQty(calculateStock(goingOutItemUnit.getItem(), getBill().getFromPerson()));
        hxPer.setItemUnit(goingOutItemUnit);
        getItemUnitHistoryFacade().create(hxPer);


        temBillItem.setBill(getBill());
        temBillItem.setCreatedAt(Calendar.getInstance().getTime());
        temBillItem.setCreater(sessionController.loggedUser);
        //
        temBillItem.setDiscountCostPercentRate(getBill().getDiscountValuePercent());
        temBillItem.setDiscountCostPercentValue(getBill().getDiscountValuePercent());
        temBillItem.setDiscountCostValue(temBillItem.getNetValue() * getBill().getDiscountValue() / 100);
        temBillItem.setDiscountCostRate(temBillItem.getNetRate() * getBill().getDiscountValue() / 100);
        //
        temBillItem.setDiscountRate(temBillItem.getNetRate() * getBill().getDiscountValuePercent() / 100);
        temBillItem.setDiscountRatePercent(getBill().getDiscountValuePercent());
        temBillItem.setDiscountValue(temBillItem.getNetValue() * getBill().getDiscountValuePercent() / 100);
        temBillItem.setDiscountValuePercent(getBill().getDiscountValuePercent());
        //
        temBillItem.setGrossCostRate(temBillItem.getNetRate());
        temBillItem.setGrossCostValue(temBillItem.getNetValue());
        temBillItem.setGrossRate(temBillItem.getNetRate());
        temBillItem.setGrossCostValue(temBillItem.getNetValue());
        temBillItem.setNetCostRate(temBillItem.getNetRate());
        temBillItem.setNetCostValue(temBillItem.getNetValue());
        temBillItem.setPurchaseQuentity(temBillItem.getQuentity());
        temBillItem.setFreeQuentity(0l);
        //
        getBillItemFacade().create(temBillItem);
        //
        hxIns.setBillItem(temBillItem);
        hxIns.setHistoryDate(getBill().getBillDate());
        hxIns.setHistoryTimeStamp(Calendar.getInstance().getTime());

        hxUnit.setBillItem(temBillItem);
        hxUnit.setHistoryDate(getBill().getBillDate());
        hxUnit.setHistoryTimeStamp(Calendar.getInstance().getTime());

        hxLoc.setBillItem(temBillItem);
        hxLoc.setHistoryDate(getBill().getBillDate());
        hxLoc.setHistoryTimeStamp(Calendar.getInstance().getTime());

        hxPer.setBillItem(temBillItem);
        hxPer.setHistoryDate(getBill().getBillDate());
        hxPer.setHistoryTimeStamp(Calendar.getInstance().getTime());

        getItemUnitHistoryFacade().edit(hxIns);
        getItemUnitHistoryFacade().edit(hxUnit);
        getItemUnitHistoryFacade().edit(hxLoc);
        getItemUnitHistoryFacade().edit(hxPer);


    }

    public void calculateItemValue() {
        getBillItemEntry().getBillItem().setNetValue(getBillItemEntry().getBillItem().getNetRate() * getBillItemEntry().getBillItem().getQuentity());
    }

    public void calculateBillValue() {
        double netBillValue = 0l;
        double grossBillValue = 0l;
        double discountBillValue = 0l;
        for (BillItemEntry temEntry : getBillItemEntrys()) {
            netBillValue += temEntry.getBillItem().getNetValue();
            grossBillValue += temEntry.getBillItem().getGrossValue();
            discountBillValue += temEntry.getBillItem().getDiscountValue();
        }
        getBill().setNetValue(netBillValue - getBill().getDiscountValue());
        getBill().setGrossValue(netBillValue);
    }

    /**
     * Creates a new instance of PurchaseBillController
     */
    public MsIssueController() {
    }

    /**
     * Getters and Setters
     */
    private void addLastBillEntryNumber(BillItemEntry entry) {
        entry.setId((long) getLstBillItemEntrys().size() + 1);
    }

    public BillItemEntry getBillItemEntry() {
        if (billItemEntry == null) {
            billItemEntry = new BillItemEntry();
            billItemEntry.setBillItem(new BillItem());
            billItemEntry.getBillItem().setItemUnit(new ItemUnit());
            addLastBillEntryNumber(billItemEntry);
        }
        return billItemEntry;
    }

    public void setBillItemEntry(BillItemEntry billItemEntry) {
        this.billItemEntry = billItemEntry;
    }

    public DataModel<BillItemEntry> getBillItemEntrys() {
        return new ListDataModel<BillItemEntry>(getLstBillItemEntrys());
    }

    public void setBillItemEntrys(DataModel<BillItemEntry> billItemEntrys) {
        this.billItemEntrys = billItemEntrys;
    }

    public List<BillItemEntry> getLstBillItemEntrys() {
        if (lstBillItemEntrys == null) {
            lstBillItemEntrys = new ArrayList<BillItemEntry>();
        }
        return lstBillItemEntrys;
    }

    public void setLstBillItemEntrys(List<BillItemEntry> lstBillItemEntrys) {
        this.lstBillItemEntrys = lstBillItemEntrys;
    }

    public Bill getBill() {
        if (bill == null) {
            bill = new OutInventoryBill();
            bill.setBillDate(Calendar.getInstance().getTime());
        }
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }

    public BillItemFacade getBillItemFacade() {
        return billItemFacade;
    }

    public void setBillItemFacade(BillItemFacade billItemFacade) {
        this.billItemFacade = billItemFacade;
    }

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    public DataModel<Item> getItems() {
        if (getUnit() == null) {
            return null;
        }
        return new ListDataModel<Item>(getItemFacade().findBySQL("SELECT i FROM Item i "));
    }

    public void setItems(DataModel<Item> items) {
        this.items = items;
    }

    public MakeFacade getMakeFacade() {
        return makeFacade;
    }

    public void setMakeFacade(MakeFacade makeFacade) {
        this.makeFacade = makeFacade;
    }

    public DataModel<Make> getMakes() {
        return new ListDataModel(getMakeFacade().findBySQL("SELECT m FROM Make m WHERE m.retired=false ORDER BY m.name"));
    }

    public void setMakes(DataModel<Make> makes) {
        this.makes = makes;
    }

    public ModalFacade getModalFacade() {
        return modalFacade;
    }

    public void setModalFacade(ModalFacade modalFacade) {
        this.modalFacade = modalFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }

    public BillItemEntry getEditBillItemEntry() {
        return editBillItemEntry;
    }

    public void setEditBillItemEntry(BillItemEntry editBillItemEntry) {
        this.editBillItemEntry = editBillItemEntry;
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public DataModel<Institution> getToInstitutions() {
        return new ListDataModel<Institution>(getInstitutionFacade().findBySQL("SELECT i FROM Institution i WHERE i.retired=false ORDER by i.name"));
    }

    public void setToInstitutions(DataModel<Institution> institutions) {
        this.toInstitutions = institutions;
    }

    public DataModel<Institution> getFromInstitutions() {
        return new ListDataModel<Institution>(getInstitutionFacade().findBySQL("SELECT i FROM Institution i WHERE i.retired=false ORDER by i.name"));
    }

    public void setFromInstitutions(DataModel<Institution> institutions) {
        this.fromInstitutions = institutions;
    }

    public DataModel<Unit> getFromUnits() {
        if (getInstitution() == null) {
            return null;
        }
        return new ListDataModel<Unit>(getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id = " + getInstitution().getId()));
    }

    public void setFromUnits(DataModel<Unit> fromUnits) {
        this.fromUnits = fromUnits;
    }

    public DataModel<Location> getFromLocations() {
        if (getBill().getFromUnit() != null) {
            return new ListDataModel<Location>(getLocationFacade().findBySQL("SELECT l FROM Location l WHERE l.retired=false AND l.unit.id = " + getBill().getFromUnit().getId() + " ORDER BY l.name"));
        }
        return null;
    }

    public void setFromLocations(DataModel<Location> locations) {
        this.fromLocations = locations;
    }

    public DataModel<Location> getToLocations() {
//        System.out.println("Getting ToLocations");
        if (getBill().getToUnit() != null) {
            System.out.println("Got Null while getting toLocations");
            return new ListDataModel<Location>(getLocationFacade().findBySQL("SELECT l FROM Location l WHERE l.retired=false AND l.unit.id = " + getBill().getToUnit().getId() + " ORDER BY l.name"));
        }
//        System.out.println("Got Null while getting toLocations");
        return null;
    }

    public void setToLocations(DataModel<Location> locations) {
        this.toLocations = locations;
    }

    public DataModel<Unit> getToUnits() {
        if (getBill().getToInstitution() != null) {
            return new ListDataModel<Unit>(getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id=" + getBill().getToInstitution().getId() + " ORDER BY u.name"));
        }
        return null;
    }

    public void setToUnits(DataModel<Unit> toUnits) {
        this.toUnits = toUnits;
    }

    public UnitFacade getUnitFacade() {
        return unitFacade;
    }

    public void setUnitFacade(UnitFacade unitFacade) {
        this.unitFacade = unitFacade;
    }

    public LocationFacade getLocationFacade() {
        return locationFacade;
    }

    public void setLocationFacade(LocationFacade locationFacade) {
        this.locationFacade = locationFacade;
    }

    public DataModel<Person> getFromPersons() {
        if (getBill().getFromInstitution() != null) {
            return new ListDataModel<Person>(getPersonFacade().findBySQL("SELECT p FROM Person p WHERE p.retired=false AND p.institution.id=" + getBill().getFromInstitution().getId() + " ORDER BY p.name"));
        }
        return null;
    }

    public void setFromPersons(DataModel<Person> fromPersons) {
        this.fromPersons = fromPersons;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    public DataModel<Person> getToPersons() {
        if (getBill().getToInstitution() != null) {
            return new ListDataModel<Person>(getPersonFacade().findBySQL("SELECT p FROM Person p WHERE p.retired=false AND p.institution.id=" + getBill().getToInstitution().getId() + " ORDER BY p.name"));
        }
        return null;
    }

    public void setToPersons(DataModel<Person> toPersons) {
        this.toPersons = toPersons;
    }

    public DataModel<Country> getCountries() {
        return new ListDataModel<Country>(getCountryFacade().findBySQL("SELECT c FROM Country c WHERE c.retired=false ORDER BY c.name"));
    }

    public void setCountries(DataModel<Country> countries) {
        this.countries = countries;
    }

    public CountryFacade getCountryFacade() {
        return countryFacade;
    }

    public void setCountryFacade(CountryFacade countryFacade) {
        this.countryFacade = countryFacade;
    }

    public ManufacturerFacade getManufacturerFacade() {
        return manufacturerFacade;
    }

    public void setManufacturerFacade(ManufacturerFacade manufacturerFacade) {
        this.manufacturerFacade = manufacturerFacade;
    }

    public DataModel<Manufacturer> getManufacturers() {
        return new ListDataModel<Manufacturer>(getManufacturerFacade().findBySQL("SELECT m FROM Manufacturer m WHERE m.retired=false ORDER BY m.name"));
    }

    public void setManufacturers(DataModel<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    public SupplierFacade getSupplierFacade() {
        return supplierFacade;
    }

    public void setSupplierFacade(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    public DataModel<Supplier> getSuppliers() {
        return new ListDataModel<Supplier>(getSupplierFacade().findBySQL("SELECT s FROM Supplier s WHERE s.retired=false ORDER BY s.name"));
    }

    public void setSuppliers(DataModel<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public ItemUnitFacade getItemUnitFacade() {
        return itemUnitFacade;
    }

    public void setItemUnitFacade(ItemUnitFacade itemUnitFacade) {
        this.itemUnitFacade = itemUnitFacade;
    }

    public ItemUnitHistoryFacade getItemUnitHistoryFacade() {
        return itemUnitHistoryFacade;
    }

    public void setItemUnitHistoryFacade(ItemUnitHistoryFacade itemUnitHistoryFacade) {
        this.itemUnitHistoryFacade = itemUnitHistoryFacade;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public DataModel<ItemUnit> getItemUnits() {
        if (getUnit() == null) {
            return null;
        }
        return new ListDataModel<ItemUnit>(getItemUnitFacade().findBySQL("SELECT i FROM ItemUnit i WHERE i.retired=false AND i.unit.id = "
                + getUnit().getId() + " ORDER By i.name"));
    }

    public void setItemUnits(DataModel<ItemUnit> itemUnits) {
        this.itemUnits = itemUnits;
    }

    public ItemUnit getEditItemUnit() {
        return editItemUnit;
    }

    public void setEditItemUnit(ItemUnit editItemUnit) {
        this.editItemUnit = editItemUnit;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

    public BillItem getBillItem() {
        if (billItem == null) {
            billItem = new BillItem();
        }
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }
}
