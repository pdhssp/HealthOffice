/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.*;
import gov.sp.health.facade.BillFacade;
import gov.sp.health.facade.BillItemFacade;
import gov.sp.health.facade.CountryFacade;
import gov.sp.health.facade.InInventoryBillFacade;
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
import java.util.*;
import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Buddhika
 */
@Named
@ViewScoped
public class InventoryReportController  implements Serializable {

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
    @EJB
    InInventoryBillFacade inInventoryBillFacade;
    /**
     * Managed Properties
     */
    @Inject
    SessionController sessionController;
    @Inject
    TransferBean transferBean;
    /**
     * Collections
     */
    DataModel<Item> items;
    DataModel<Make> makes;
    //
    DataModel<LedgerEntry> unitLedgureEntrys;
    List<LedgerEntry> lstUnitLedgureEntrys;
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
    DataModel<Institution> institutions;
    DataModel<Unit> units;
    DataModel<Location> locations;
    DataModel<Person> persons;
    //
    DataModel<Country> countries;
    DataModel<Supplier> suppliers;
    DataModel<Manufacturer> manufacturers;
    //
    /*
     * Current Objects
     *
     */
    Date fromDate;
    Date toDate;
    LedgerEntry ledgureEntry;
    Institution institution;
    Unit unit;
    Location location;
    Person person;
    Item item;
    //
    Institution fromInstitution;
    Unit fromUnit;
    Location fromLocation;
    Person fromPerson;
    //
    Institution toInstitution;
    Unit toUnit;
    Location toLocation;
    Person toPerson;
    //
    DataModel<InInventoryBill> inInventoryBills;
    Bill bill;

    /**
     *
     * Methods
     *
     */
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

    private boolean canProceed() {
//        if (getEntryDate() == null) {
//            return false;
//        }
//        if (getItemUnit() == null) {
//            return false;
//        }
//        if (getBillItem() == null) {
//            return false;
//        }
//        if (getBill() == null) {
//            return false;
//        }
//        if (getInstitution() != null) {
//            entryType = LedgerEntry.EntryType.INSTITUTION;
//            return true;
//        } else {
//            if (getUnit() != null) {
//                entryType = LedgerEntry.EntryType.UNIT;
//                return true;
//            } else {
//                if (getLocation() != null) {
//                    entryType = LedgerEntry.EntryType.LOCATION;
//                    return true;
//                } else {
//                    if (getPerson() != null) {
//                        entryType = LedgerEntry.EntryType.PERSON;
//                        return true;
//                    } else {
//                        entryType = LedgerEntry.EntryType.OTHER;
//                        return false;
//                    }
//                }
//            }
//        }
        return false;

    }

    /**
     * Creates a new instance of PurchaseBillController
     */
    public InventoryReportController() {
    }

    /**
     * Getters and Setters
     */
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
        return new ListDataModel<Item>(getItemFacade().findBySQL("SELECT i FROM Item i WHERE i.retired=false ORDER By i.name"));
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

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public DataModel<Institution> getToInstitutions() {
        return new ListDataModel<Institution>(getInstitutionFacade().findBySQL("SELECT i FROM Institution i WHERE i.retired=false ORDER by i.name"));
    }

    public DataModel<Institution> getInstitutions() {
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
        if (getInstitution() != null) {
            return new ListDataModel<Unit>(getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id = " + getFromInstitution().getId()));
        }
        return null;
    }

    public void setFromUnits(DataModel<Unit> fromUnits) {
        this.fromUnits = fromUnits;
    }

    public DataModel<Location> getFromLocations() {
        if (getFromUnit() != null) {
            return new ListDataModel<Location>(getLocationFacade().findBySQL("SELECT l FROM Location l WHERE l.retired=false AND l.unit.id = " + getFromUnit().getId() + " ORDER BY l.name"));
        }
        return null;
    }

    public void setFromLocations(DataModel<Location> locations) {
        this.fromLocations = locations;
    }

    public DataModel<Location> getToLocations() {
        if (getToUnit() != null) {
            return new ListDataModel<Location>(getLocationFacade().findBySQL("SELECT l FROM Location l WHERE l.retired=false AND l.unit.id = " + getToUnit().getId() + " ORDER BY l.name"));
        }
        return null;
    }

    public DataModel<Location> getLocations() {
        if (getToUnit() != null) {
            return new ListDataModel<Location>(getLocationFacade().findBySQL("SELECT l FROM Location l WHERE l.retired=false AND l.unit.id = " + getUnit().getId() + " ORDER BY l.name"));
        }
        return null;
    }

    public void setToLocations(DataModel<Location> locations) {
        this.toLocations = locations;
    }

    public DataModel<Unit> getToUnits() {
        if (getToInstitution() != null) {
            return new ListDataModel<Unit>(getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id=" + getToInstitution().getId() + " ORDER BY u.name"));
        }
        return null;
    }

    public DataModel<Unit> getUnits() {
        if (getInstitution() != null) {
            return new ListDataModel<Unit>(getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id=" + getInstitution().getId() + " ORDER BY u.name"));
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
        if (getFromInstitution() != null) {
            return new ListDataModel<Person>(getPersonFacade().findBySQL("SELECT p FROM Person p WHERE p.retired=false AND p.institution.id=" + getFromInstitution().getId() + " ORDER BY p.name"));
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
        if (getToInstitution() != null) {
            return new ListDataModel<Person>(getPersonFacade().findBySQL("SELECT p FROM Person p WHERE p.retired=false AND p.institution.id=" + getToInstitution().getId() + " ORDER BY p.name"));
        }
        return null;
    }

    public DataModel<Person> getPersons() {
        if (getInstitution() != null) {
            return new ListDataModel<Person>(getPersonFacade().findBySQL("SELECT p FROM Person p WHERE p.retired=false AND p.institution.id=" + getInstitution().getId() + " ORDER BY p.name"));
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

    public LedgerEntry getLedgureEntry() {
        return ledgureEntry;
    }

    public void setLedgureEntry(LedgerEntry ledgureEntry) {
        this.ledgureEntry = ledgureEntry;
    }

    public void fillGrid() {
        if (getUnit() == null) {
            JsfUtil.addErrorMessage("Please select a Unit");
            unitLedgureEntrys = null;
            return;
        }
        if (getItem() == null) {
            JsfUtil.addErrorMessage("Please select an Item");
            unitLedgureEntrys = null;
            return;
        }
        Map temMap = new HashMap();
        String temSQL = "SELECT h FROM ItemUnitHistory h WHERE h.retired=false AND h.unit.id=" + getUnit().getId() + " AND h.itemUnit.item.id=" + getItem().getId() + " AND h.historyDate BETWEEN :fromDate AND :toDate ORDER BY h.historyTimeStamp  ";
//        temSQL = "SELECT h FROM ItemUnitHistory h WHERE h.retired=false AND h.unit.id=" + getUnit().getId() + " AND h.itemUnit.item.id=" + getItem().getId() + " ORDER BY h.historyTimeStamp  ";
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);

        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        System.out.println("SQL" + temSQL);

        List<ItemUnitHistory> lstTemHx = getItemUnitHistoryFacade().findBySQL(temSQL, temMap);
        System.out.println("lstTemHx is " + lstTemHx);

        List<LedgerEntry> lstTemLedgerEntrys = new ArrayList<LedgerEntry>();


        for (ItemUnitHistory hx : lstTemHx) {
            LedgerEntry en = new LedgerEntry();
            System.out.println("en is " + en);
            en.setAfterStock(hx.getAfterQty());
            en.setItemUnitHistory(hx);
            en.setBillItem(hx.getBillItem());
            en.setBill(hx.getBillItem().getBill());
            if (en.getBill() instanceof InBill) {
                en.setInQty(hx.getBillItem().getQuentity());
                en.setOutQty(null);
            } else {
                en.setOutQty(hx.getBillItem().getQuentity());
                en.setInQty(null);
            }
            lstTemLedgerEntrys.add(en);
        }
        unitLedgureEntrys = new ListDataModel<LedgerEntry>(lstTemLedgerEntrys);

    }

    public DataModel<InInventoryBill> getInInventoryBills() {
        return inInventoryBills;
    }

    public DataModel<LedgerEntry> getUnitLedgureEntrys() {
        return unitLedgureEntrys;
    }

    public String viewBill() {
        transferBean.setBill(getBill());
        return "inventory_purchase";
    }

    public void fillInInventoryBills() {
        if (getUnit() == null) {
            inInventoryBills = null;
            return;
        }
        Map temMap = new HashMap();
        String temSQL = "SELECT b FROM InInventoryBill b WHERE b.retired=false AND b.toUnit.id=" + getUnit().getId() + " AND b.billDate BETWEEN :fromDate AND :toDate ";
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);

        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        System.out.println("SQL" + temSQL);

        List<InInventoryBill> lstTemBill = getInInventoryBillFacade().findBySQL(temSQL, temMap);
        inInventoryBills = new ListDataModel<InInventoryBill>(lstTemBill);
    }

    public void setInInventoryBills(DataModel<InInventoryBill> inInventoryBills) {
        this.inInventoryBills = inInventoryBills;
    }

    public void setUnitLedgureEntrys(DataModel<LedgerEntry> ledgureEntrys) {
        this.unitLedgureEntrys = ledgureEntrys;
    }

    public List<LedgerEntry> getLstUnitLedgureEntrys() {
        return lstUnitLedgureEntrys;
    }

    public void setLstUnitLedgureEntrys(List<LedgerEntry> lstUnitLedgureEntrys) {
        this.lstUnitLedgureEntrys = lstUnitLedgureEntrys;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Institution getFromInstitution() {
        return fromInstitution;
    }

    public void setFromInstitution(Institution fromInstitution) {
        this.fromInstitution = fromInstitution;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Person getFromPerson() {
        return fromPerson;
    }

    public void setFromPerson(Person fromPerson) {
        this.fromPerson = fromPerson;
    }

    public Unit getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(Unit fromUnit) {
        this.fromUnit = fromUnit;
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

    public Person getToPerson() {
        return toPerson;
    }

    public void setToPerson(Person toPerson) {
        this.toPerson = toPerson;
    }

    public Unit getToUnit() {
        return toUnit;
    }

    public void setToUnit(Unit toUnit) {
        this.toUnit = toUnit;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public TransferBean getTransferBean() {
        return transferBean;
    }

    public void setTransferBean(TransferBean transferBean) {
        this.transferBean = transferBean;
    }

    public Bill getBill() {
        if (bill != null) {
            JsfUtil.addErrorMessage(bill.toString());
        } else {
            JsfUtil.addErrorMessage("Null");
        }
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
        if (bill != null) {
            JsfUtil.addErrorMessage(bill.toString());
        } else {
            JsfUtil.addErrorMessage("Null");
        }
    }

    public InInventoryBillFacade getInInventoryBillFacade() {
        return inInventoryBillFacade;
    }

    public void setInInventoryBillFacade(InInventoryBillFacade inInventoryBillFacade) {
        this.inInventoryBillFacade = inInventoryBillFacade;
    }
}
