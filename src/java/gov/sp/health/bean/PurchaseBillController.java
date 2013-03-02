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

/**
 *
 * @author Buddhika
 */
@ManagedBean
@ViewScoped
public class PurchaseBillController implements Serializable {

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
    private InstitutionFacade institutionFacade;
    @EJB
    private UnitFacade unitFacade;
    @EJB
    private LocationFacade locationFacade;
    @EJB
    private PersonFacade personFacade;
    @EJB
    private CountryFacade countryFacade;
    @EJB
    private ManufacturerFacade manufacturerFacade;
    @EJB
    private SupplierFacade supplierFacade;
    @EJB
    private ItemUnitFacade itemUnitFacade;
    @EJB
    private ItemUnitHistoryFacade itemUnitHistoryFacade;
    /**
     * Managed Properties
     */
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    @ManagedProperty(value = "#{transferBean}")
    private TransferBean transferBean;
    /**
     * Collections
     */
    private List<Item> items;
    private List<Make> makes;
    private List<Modal> modals;
    //
    private List<BillItemEntry> billItemEntrys;
    //
    private List<Institution> fromInstitutions;
    //
    private List<Institution> toInstitutions;
    private List<Unit> toUnits;
    private List<Location> toLocations;
    private List<Person> toPersons;
    //
    private List<Country> countries;
    private List<Supplier> suppliers;
    private List<Manufacturer> manufacturers;
    //
    /*
     * Current Objects
     *
     */
    private Bill bill;
    private BillItemEntry billItemEntry;
    private BillItemEntry editBillItemEntry;
    private String itemSerial;
    private Institution toInstitution;
    //Controllers
    //
//    Institution fromInstitution;
//    Unit fromUnit;
//    Location fromLocation;
//    Person fromPerson;
//    //
//    Institution toInstitution;
//    Unit toUnit;
//    Location toLocation;
//    Person toPerson;
    /**
     * Entries
     */
    private String modalName;
    private Boolean newBill;

    /**
     *
     * Methods
     *
     */
    public List<Modal> getModals() {
        if (modals == null) {
            modals = new ArrayList<Modal>();
        }
        return modals;
    }

    public void setModals(List<Modal> modals) {
        this.modals = modals;
    }

    public void addItemToList() {
        orderBillItemEntries();
        if (getBillItemEntry() == null) {
            JsfUtil.addErrorMessage("Hothing to add");
            return;
        }
        if (getBillItemEntry().getBillItem().getItemUnit().getItem() == null) {
            JsfUtil.addErrorMessage("Please select an item");
            return;
        }
        if (getBillItemEntry().getBillItem().getQuentity() == 0) {
            JsfUtil.addErrorMessage("Please enter a quantity");
            return;
        }
        if (getBillItemEntry().getBillItem().getNetRate() == 0) {
            JsfUtil.addErrorMessage("Please enter a rate");
            return;
        }
        // TODO: Warning - Need to add logic to search and save model
        addLastBillEntryNumber(getBillItemEntry());
        getBillItemEntrys().add(getBillItemEntry());
        calculateBillValue();
        clearEntry();

    }

    private void orderBillItemEntries() {
        long l = 1l;
        for (BillItemEntry entry : getBillItemEntrys()) {
            entry.setId(l);
            l++;
        }
    }

    public void removeItemFromList() {
        if (getEditBillItemEntry() == null) {
            JsfUtil.addErrorMessage("Nothing to Delete. Please select one");
        }
        getBillItemEntrys().remove(getEditBillItemEntry());
        orderBillItemEntries();
        setEditBillItemEntry(null);
        JsfUtil.addSuccessMessage("Removed From List");
    }

    public void settleBill() {

        saveBill();
        saveNewBillItems();
        clearEntry();
        clearBill();
        JsfUtil.addSuccessMessage("Bill Settled successfully");
    }

    private void clearEntry() {
        setModalName(null);
        setBillItemEntry(new BillItemEntry());
        setBillItemEntry(null);
        setBillItemEntry(getBillItemEntry());
    }

    private void clearBill() {
        setBill(new Bill());
        setBillItemEntrys(new ArrayList<BillItemEntry>());


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

    private void saveBill() {
        Bill temBill = getBill();
        temBill.setDiscountCost(temBill.getDiscountValue());
        temBill.setDiscountValuePercent(temBill.getDiscountValue() * 100 / temBill.getNetValue());
        temBill.setDiscountCostPercent(temBill.getDiscountValuePercent());
        temBill.setGrossCost(temBill.getGrossValue());
        temBill.setNetCost(temBill.getNetValue());
        if (getBill().getId() != null && getBill().getId() > 0) {
            getBillFacade().edit(temBill);
        } else {
            temBill.setToInstitution(getToInstitution());
            temBill.setCreatedAt(Calendar.getInstance().getTime());
            temBill.setCreater(getSessionController().getLoggedUser());
            getBillFacade().create(temBill);
        }
    }

    private void saveNewBillItems() {
        for (BillItemEntry temEntry : getBillItemEntrys()) {
            settleBillItem(temEntry);
        }
    }

//    private void saveNewBillItem(BillItemEntry temEntry) {
//        BillItem temItem = temEntry.getBillItem();
//        temItem.setBill(getBill());
//        temItem.setCreatedAt(Calendar.getInstance().getTime());
//        temItem.setCreater(sessionController.loggedUser);
//        //
//        temItem.setDiscountCostPercentRate(getBill().getDiscountValuePercent());
//        temItem.setDiscountCostPercentValue(getBill().getDiscountValuePercent());
//        temItem.setDiscountCostValue(temItem.getNetValue() * getBill().getDiscountValue() / 100);
//        temItem.setDiscountCostRate(temItem.getNetRate() * getBill().getDiscountValue() / 100);
//        //
//        temItem.setDiscountRate(temItem.getNetRate() * getBill().getDiscountValuePercent() / 100);
//        temItem.setDiscountRatePercent(getBill().getDiscountValuePercent());
//        temItem.setDiscountValue(temItem.getNetValue() * getBill().getDiscountValuePercent() / 100);
//        temItem.setDiscountValuePercent(getBill().getDiscountValuePercent());
//        //
//        temItem.setGrossCostRate(temItem.getNetRate());
//        temItem.setGrossCostValue(temItem.getNetValue());
//        temItem.setGrossRate(temItem.getNetRate());
//        temItem.setGrossCostValue(temItem.getNetValue());
//        temItem.setNetCostRate(temItem.getNetRate());
//        temItem.setNetCostValue(temItem.getNetValue());
//        temItem.setPurchaseQuentity(temItem.getQuentity());
//        temItem.setFreeQuentity(0l);
//        //
//        getBillItemFacade().create(temItem);
//
//    }
    private void settleBillItem(BillItemEntry temEntry) {
        BillItem temBillItem = temEntry.getBillItem();
        ItemUnit newItemUnit = temBillItem.getItemUnit();

        newItemUnit.setBulkUnit(newItemUnit.getItem().getBulkUnit());
        newItemUnit.setCreatedAt(Calendar.getInstance().getTime());
        newItemUnit.setCreater(getSessionController().getLoggedUser());
        newItemUnit.setInstitution(getToInstitution());
        newItemUnit.setLocation(getBill().getToLocation());
        newItemUnit.setLooseUnit(newItemUnit.getItem().getLooseUnit());
        newItemUnit.setLooseUnitsPerBulkUnit(newItemUnit.getItem().getLooseUnitsPerBulkUnit());
        newItemUnit.setOwner(getBill().getToPerson());
        newItemUnit.setWarrantyExpiary(newItemUnit.getDateOfExpiary());
        newItemUnit.setSupplier(null);
        newItemUnit.setUnit(getBill().getToUnit());
        newItemUnit.setPerson(getBill().getToPerson());
        newItemUnit.setQuentity(temBillItem.getQuentity());

        ItemUnitHistory hxUnit = new ItemUnitHistory();
        ItemUnitHistory hxLoc = new ItemUnitHistory();
        ItemUnitHistory hxIns = new ItemUnitHistory();
        ItemUnitHistory hxPer = new ItemUnitHistory();


        hxIns.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getInstitution()));
        hxIns.setCreatedAt(Calendar.getInstance().getTime());
        hxIns.setCreater(getSessionController().loggedUser);
        hxIns.setInstitution(newItemUnit.getInstitution());
        hxIns.setItem(newItemUnit.getItem());
        hxIns.setQuentity(newItemUnit.getQuentity());
        hxIns.setToIn(Boolean.TRUE);
        hxIns.setToOut(Boolean.FALSE);


        hxUnit.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getUnit()));
        hxUnit.setCreatedAt(Calendar.getInstance().getTime());
        hxUnit.setCreater(getSessionController().loggedUser);
        hxUnit.setUnit(newItemUnit.getUnit());
        hxUnit.setItem(newItemUnit.getItem());
        hxUnit.setQuentity(newItemUnit.getQuentity());
        hxUnit.setToIn(Boolean.TRUE);
        hxUnit.setToOut(Boolean.FALSE);

        hxLoc.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getLocation()));
        hxLoc.setCreatedAt(Calendar.getInstance().getTime());
        hxLoc.setCreater(getSessionController().loggedUser);
        hxLoc.setLocation(newItemUnit.getLocation());
        hxLoc.setItem(newItemUnit.getItem());
        hxLoc.setQuentity(newItemUnit.getQuentity());
        hxLoc.setToIn(Boolean.TRUE);
        hxLoc.setToOut(Boolean.FALSE);

        hxPer.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getPerson()));
        hxPer.setCreatedAt(Calendar.getInstance().getTime());
        hxPer.setCreater(getSessionController().loggedUser);
        hxPer.setPerson(newItemUnit.getPerson());
        hxPer.setItem(newItemUnit.getItem());
        hxPer.setQuentity(newItemUnit.getQuentity());
        hxPer.setToIn(Boolean.TRUE);
        hxPer.setToOut(Boolean.FALSE);

        getItemUnitFacade().create(newItemUnit);

        hxIns.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getInstitution()));
        hxIns.setItemUnit(newItemUnit);
        getItemUnitHistoryFacade().create(hxIns);

        hxUnit.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getUnit()));
        hxUnit.setItemUnit(newItemUnit);
        getItemUnitHistoryFacade().create(hxUnit);

        hxLoc.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getLocation()));
        hxLoc.setItemUnit(newItemUnit);
        getItemUnitHistoryFacade().create(hxLoc);

        hxPer.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getPerson()));
        hxPer.setItemUnit(newItemUnit);
        getItemUnitHistoryFacade().create(hxPer);


        temBillItem.setBill(getBill());
        temBillItem.setCreatedAt(Calendar.getInstance().getTime());
        temBillItem.setCreater(getSessionController().loggedUser);
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

//    private void addNewToUnitStock(BillItemEntry temEntry) {
//
//        BillItem temBillItem = temEntry.getBillItem();
//        ItemUnit newItemUnit = temBillItem.getItemUnit();
//
//        newItemUnit.setBulkUnit(newItemUnit.getItem().getBulkUnit());
//        newItemUnit.setCreatedAt(Calendar.getInstance().getTime());
//        newItemUnit.setCreater(sessionController.getLoggedUser());
//        newItemUnit.setInstitution(getToInstitution());
//        newItemUnit.setLocation(getBill().getToLocation());
//        newItemUnit.setLooseUnit(newItemUnit.getItem().getLooseUnit());
//        newItemUnit.setLooseUnitsPerBulkUnit(newItemUnit.getItem().getLooseUnitsPerBulkUnit());
//        newItemUnit.setOwner(getBill().getToPerson());
//        newItemUnit.setWarrantyExpiary(newItemUnit.getDateOfExpiary());
//        newItemUnit.setSupplier(null);
//        newItemUnit.setUnit(getBill().getToUnit());
//        newItemUnit.setPerson(getBill().getToPerson());
//        newItemUnit.setQuentity(temBillItem.getQuentity());
//
//        ItemUnitHistory hxUnit = new ItemUnitHistory();
//        ItemUnitHistory hxLoc = new ItemUnitHistory();
//        ItemUnitHistory hxIns = new ItemUnitHistory();
//        ItemUnitHistory hxPer = new ItemUnitHistory();
//
//
//        hxIns.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getInstitution()));
//        hxIns.setCreatedAt(Calendar.getInstance().getTime());
//        hxIns.setCreater(sessionController.loggedUser);
//        hxIns.setInstitution(newItemUnit.getInstitution());
//        hxIns.setItem(newItemUnit.getItem());
//        hxIns.setQuentity(newItemUnit.getQuentity());
//        hxIns.setToIn(Boolean.TRUE);
//        hxIns.setToOut(Boolean.FALSE);
//
//
//        hxUnit.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getUnit()));
//        hxUnit.setCreatedAt(Calendar.getInstance().getTime());
//        hxUnit.setCreater(sessionController.loggedUser);
//        hxUnit.setUnit(newItemUnit.getUnit());
//        hxUnit.setItem(newItemUnit.getItem());
//        hxUnit.setQuentity(newItemUnit.getQuentity());
//        hxUnit.setToIn(Boolean.TRUE);
//        hxUnit.setToOut(Boolean.FALSE);
//
//        hxLoc.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getLocation()));
//        hxLoc.setCreatedAt(Calendar.getInstance().getTime());
//        hxLoc.setCreater(sessionController.loggedUser);
//        hxLoc.setLocation(newItemUnit.getLocation());
//        hxLoc.setItem(newItemUnit.getItem());
//        hxLoc.setQuentity(newItemUnit.getQuentity());
//        hxLoc.setToIn(Boolean.TRUE);
//        hxLoc.setToOut(Boolean.FALSE);
//
//        hxPer.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getPerson()));
//        hxPer.setCreatedAt(Calendar.getInstance().getTime());
//        hxPer.setCreater(sessionController.loggedUser);
//        hxPer.setPerson(newItemUnit.getPerson());
//        hxPer.setItem(newItemUnit.getItem());
//        hxPer.setQuentity(newItemUnit.getQuentity());
//        hxPer.setToIn(Boolean.TRUE);
//        hxPer.setToOut(Boolean.FALSE);
//
//        getItemUnitFacade().create(newItemUnit);
//
//        hxIns.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getInstitution()));
//        hxIns.setItemUnit(newItemUnit);
//        getItemUnitHistoryFacade().create(hxIns);
//
//        hxUnit.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getUnit()));
//        hxUnit.setItemUnit(newItemUnit);
//        getItemUnitHistoryFacade().create(hxUnit);
//
//        hxLoc.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getLocation()));
//        hxLoc.setItemUnit(newItemUnit);
//        getItemUnitHistoryFacade().create(hxLoc);
//
//        hxPer.setAfterQty(calculateStock(newItemUnit.getItem(), newItemUnit.getPerson()));
//        hxPer.setItemUnit(newItemUnit);
//        getItemUnitHistoryFacade().create(hxPer);
//
//
//    }
//    private void addToLocation() {
//    }
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
    public PurchaseBillController() {
    }

    /**
     * Getters and Setters
     */
    private void addLastBillEntryNumber(BillItemEntry entry) {
        entry.setId((long) getBillItemEntrys().size() + 1);
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

    public List<BillItemEntry> getBillItemEntrys() {
        if (billItemEntrys == null) {
            billItemEntrys = new ArrayList<BillItemEntry>();
        }
        return billItemEntrys;
    }

    public void setBillItemEntrys(List<BillItemEntry> lstBillItemEntrys) {
        this.billItemEntrys = lstBillItemEntrys;
    }

//
//        public Bill getBill() {
//        if (bill != null) {
//            JsfUtil.addErrorMessage(bill.toString());
//        } else {
//            JsfUtil.addErrorMessage("Null");
//        }
//        return bill;
//    }
//
//    public void setBill(Bill bill) {
//        this.bill = bill;
//        if (bill != null) {
//            JsfUtil.addErrorMessage(bill.toString());
//        } else {
//            JsfUtil.addErrorMessage("Null");
//        }
//    }
//
    public void prepareForNewBill() {
        setNewBill(Boolean.TRUE);
        setBill(new InInventoryBill());
        getBill().setBillDate(Calendar.getInstance().getTime());

    }

    public void prepareForOldBill() {
        setNewBill(Boolean.FALSE);
        setBill(getTransferBean().getBill());
        String temStr = "SELECT e FROM BillItem e WHERE e.retired=false AND e.bill.id = " + getBill().getId();
        List<BillItem> temLstBillItems = new ArrayList<BillItem>(getBillItemFacade().findBySQL(temStr));
        System.out.println(temLstBillItems.toString());
        long i = 1;
        for (BillItem bi : temLstBillItems) {
            BillItemEntry bie = new BillItemEntry();
            bie.setBillItem(bi);
            bie.setId(i);
            getBillItemEntrys().add(bie);
            i++;
        }
        getTransferBean().setBill(null);
    }

    public Bill getBill() {
        if (bill == null) {
            if (getTransferBean().getBill() != null) {
                prepareForOldBill();
            } else {
                prepareForNewBill();
            }
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

    public List<Item> getItems() {
        items = getItemFacade().findBySQL("SELECT i FROM Item i WHERE i.retired=false AND type(i) = InventoryItem ORDER By i.name");
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public MakeFacade getMakeFacade() {
        return makeFacade;
    }

    public void setMakeFacade(MakeFacade makeFacade) {
        this.makeFacade = makeFacade;
    }

    public List<Make> getMakes() {
        makes = getMakeFacade().findBySQL("SELECT m FROM Make m WHERE m.retired=false ORDER BY m.name");
        return makes;
    }

    public void setMakes(List<Make> makes) {
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

    public List<Institution> getToInstitutions() {
        if (getSessionController().getPrivilege().getRestrictedInstitution() == null) {
            toInstitutions = getInstitutionFacade().findBySQL("SELECT i FROM HealthInstitution i WHERE i.retired=false ORDER by i.name");
        } else {
            toInstitutions = new ArrayList<Institution>();
            toInstitutions.add(getSessionController().getPrivilege().getRestrictedInstitution());
        }
        return toInstitutions;
    }

    public void setToInstitutions(List<Institution> institutions) {
        this.toInstitutions = institutions;
    }

    public List<Institution> getFromInstitutions() {
        fromInstitutions = getInstitutionFacade().findBySQL("SELECT i FROM Supplier i WHERE i.retired=false ORDER by i.name");
        return fromInstitutions;
    }

    public void setFromInstitutions(List<Institution> institutions) {
        this.fromInstitutions = institutions;
    }

    public List<Location> getToLocations() {
        if (getBill().getToUnit() != null) {
            toLocations = getLocationFacade().findBySQL("SELECT l FROM Location l WHERE l.retired=false AND l.unit.id = " + getBill().getToUnit().getId() + " ORDER BY l.name");
        } else {
            toLocations = new ArrayList<Location>();
        }
        return toLocations;
    }

    public void setToLocations(List<Location> locations) {
        this.toLocations = locations;
    }

    public List<Unit> getToUnits() {
        if (getToInstitution() == null) {
            toUnits = new ArrayList<Unit>();
        } else {
            toUnits = getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id=" + getToInstitution().getId() + " ORDER BY u.name");
        }
        return toUnits;
    }

    public void setToUnits(List<Unit> toUnits) {
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

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    public List<Person> getToPersons() {
        if (getToInstitution() == null) {
            toPersons = new ArrayList<Person>();
        } else {
            toPersons = getPersonFacade().findBySQL("SELECT p FROM Person p WHERE p.retired=false AND p.institution.id=" + getToInstitution().getId() + " ORDER BY p.name");
        }
        return toPersons;
    }

    public void setToPersons(List<Person> toPersons) {
        this.toPersons = toPersons;
    }

    public List<Country> getCountries() {
        countries = getCountryFacade().findBySQL("SELECT c FROM Country c WHERE c.retired=false ORDER BY c.name");
        return countries;
    }

    public void setCountries(List<Country> countries) {
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

    public List<Manufacturer> getManufacturers() {
        manufacturers = getManufacturerFacade().findBySQL("SELECT m FROM Manufacturer m WHERE m.retired=false ORDER BY m.name");
        return manufacturers;
    }

    public void setManufacturers(List<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    public SupplierFacade getSupplierFacade() {
        return supplierFacade;
    }

    public void setSupplierFacade(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    public List<Supplier> getSuppliers() {
        suppliers = getSupplierFacade().findBySQL("SELECT s FROM Supplier s WHERE s.retired=false ORDER BY s.name");
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
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

    public TransferBean getTransferBean() {
        return transferBean;
    }

    public void setTransferBean(TransferBean transferBean) {
        this.transferBean = transferBean;
    }

    public Boolean getNewBill() {
        return newBill;
    }

    public void setNewBill(Boolean newBill) {
        this.newBill = newBill;
    }

    public String getItemSerial() {
        if (getBillItemEntry().getBillItem().getItemUnit().getItem() == null) {
            return "";
        }
        if (getToInstitution() == null) {
            return "";
        }
        if (getToInstitution().getCode().trim().equals("")) {
            itemSerial = getToInstitution().getCode().trim().toUpperCase();
        } else {
            itemSerial = getToInstitution().getCode().trim().toUpperCase();
        }
        if (getBillItemEntry().getBillItem().getItemUnit().getItem().getName().trim().equals("")) {
            itemSerial += "/" + getBillItemEntry().getBillItem().getItemUnit().getItem().getName().trim().toUpperCase();
        } else {
            itemSerial += "/" + getBillItemEntry().getBillItem().getItemUnit().getItem().getCode().trim().toUpperCase();

        }
        Long temNo = getItemUnitFacade().findAggregateLong("select count(iu) from ItemUnit where iu.retired = false and iu.institute.id =" + getToInstitution().getId() + " and iu.item.id =" + getBillItemEntry().getBillItem().getItemUnit().getItem().getId());
        itemSerial += "/" + temNo;
        return itemSerial;
    }

    public void setItemSerial(String itemSerial) {
        this.itemSerial = itemSerial;
    }

    public Institution getToInstitution() {
        if (toInstitution == null) {
            if (sessionController.getPrivilege().getRestrictedInstitution() != null) {
                toInstitution = sessionController.getPrivilege().getRestrictedInstitution();
            } else {
                toInstitution = sessionController.getLoggedUser().getWebUserPerson().getInstitution();
            }
        }
        return toInstitution;
    }

    public void setToInstitution(Institution toInstitution) {
        this.toInstitution = toInstitution;
    }
}
