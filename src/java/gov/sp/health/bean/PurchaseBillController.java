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
public class PurchaseBillController  implements Serializable {

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
    @ManagedProperty(value = "#{transferBean}")
    TransferBean transferBean;
    /**
     * Collections
     */
    DataModel<Item> items;
    DataModel<Make> makes;
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
    BillItemEntry billItemEntry;
    BillItemEntry editBillItemEntry;
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
    String modalName;
    Boolean newBill;

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
        // TODO: Warning - Need to add logic to search and save model        
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
        newItemUnit.setCreater(sessionController.getLoggedUser());
        newItemUnit.setInstitution(getBill().getToInstitution());
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
        hxIns.setCreater(sessionController.loggedUser);
        hxIns.setInstitution(newItemUnit.getInstitution());
        hxIns.setItem(newItemUnit.getItem());
        hxIns.setQuentity(newItemUnit.getQuentity());
        hxIns.setToIn(Boolean.TRUE);
        hxIns.setToOut(Boolean.FALSE);


        hxUnit.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getUnit()));
        hxUnit.setCreatedAt(Calendar.getInstance().getTime());
        hxUnit.setCreater(sessionController.loggedUser);
        hxUnit.setUnit(newItemUnit.getUnit());
        hxUnit.setItem(newItemUnit.getItem());
        hxUnit.setQuentity(newItemUnit.getQuentity());
        hxUnit.setToIn(Boolean.TRUE);
        hxUnit.setToOut(Boolean.FALSE);

        hxLoc.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getLocation()));
        hxLoc.setCreatedAt(Calendar.getInstance().getTime());
        hxLoc.setCreater(sessionController.loggedUser);
        hxLoc.setLocation(newItemUnit.getLocation());
        hxLoc.setItem(newItemUnit.getItem());
        hxLoc.setQuentity(newItemUnit.getQuentity());
        hxLoc.setToIn(Boolean.TRUE);
        hxLoc.setToOut(Boolean.FALSE);

        hxPer.setBeforeQty(calculateStock(newItemUnit.getItem(), newItemUnit.getPerson()));
        hxPer.setCreatedAt(Calendar.getInstance().getTime());
        hxPer.setCreater(sessionController.loggedUser);
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

//    private void addNewToUnitStock(BillItemEntry temEntry) {
//
//        BillItem temBillItem = temEntry.getBillItem();
//        ItemUnit newItemUnit = temBillItem.getItemUnit();
//
//        newItemUnit.setBulkUnit(newItemUnit.getItem().getBulkUnit());
//        newItemUnit.setCreatedAt(Calendar.getInstance().getTime());
//        newItemUnit.setCreater(sessionController.getLoggedUser());
//        newItemUnit.setInstitution(getBill().getToInstitution());
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
        bill = new InInventoryBill();
        bill.setBillDate(Calendar.getInstance().getTime());

    }

    public void prepareForOldBill() {
        setNewBill(Boolean.FALSE);
        bill = getTransferBean().getBill();
        String temStr = "SELECT e FROM BillItem e WHERE e.retired=false AND e.bill.id = " + bill.getId();
        List<BillItem> temLstBillItems = new ArrayList<BillItem>(getBillItemFacade().findBySQL(temStr));
        System.out.println(temLstBillItems.toString());
        long i = 1;
        for (BillItem bi:temLstBillItems){
            BillItemEntry bie = new BillItemEntry();
            bie.setBillItem(bi);
            bie.setId(i);
            getLstBillItemEntrys().add(bie);
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
        JsfUtil.addSuccessMessage(bill.toString());
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
        return new ListDataModel<Item>(getItemFacade().findBySQL("SELECT i FROM Item i WHERE i.retired=false AND type(i) = InventoryItem ORDER By i.name"));
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
        return new ListDataModel<Unit>(getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id = " + getBill().getFromInstitution().getId()));
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
}
