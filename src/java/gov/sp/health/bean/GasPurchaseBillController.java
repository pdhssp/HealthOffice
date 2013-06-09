/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.*;
import gov.sp.health.facade.BillFacade;
import gov.sp.health.facade.BillItemFacade;
import gov.sp.health.facade.CountryFacade;
import gov.sp.health.facade.CylinderFacade;
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
public class GasPurchaseBillController implements Serializable {

    /**
     *
     * Enterprise Java Beans
     *
     *
     */
    @EJB
    private ItemFacade itemFacade;
    @EJB
    private CylinderFacade cylinderFacade;
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
    private List<Cylinder> items;
                //
    private List<BillItem> billItems;
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
    private BillItem billItem;
    private BillItem editBillItem;
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
     * @return
     */
    public CylinderFacade getCylinderFacade() {
        return cylinderFacade;

    }

    public void setCylinderFacade(CylinderFacade cylinderFacade) {
        this.cylinderFacade = cylinderFacade;
    }

   
    /**
     *
     */
    public void addItemToList() {
        orderBillItemEntries();
        if (getBillItem() == null) {
            JsfUtil.addErrorMessage("Hothing to add");
            return;
        }
        if (getBillItem().getItem() == null) {
            JsfUtil.addErrorMessage("Please select an item");
            return;
        }
        if (getBillItem().getQuentity() == 0) {
            JsfUtil.addErrorMessage("Please enter a quantity");
            return;
        }
        if (getBillItem().getNetRate() == 0) {
            JsfUtil.addErrorMessage("Please enter a rate");
            return;
        }
        // TODO: Warning - Need to add logic to search and save model
        System.out.println("going to ad last bill number");
        addLastBillEntryNumber(getBillItem());
        System.out.println("before adding bill items. Size is " + getBillItems().size());

        getBillItems().add(getBillItem());

        System.out.println("before adding bill items. Size is " + getBillItems().size());
        calculateBillValue();

        clearEntry();

    }

    private void orderBillItemEntries() {
        long l = 1l;
        for (BillItem entry : getBillItems()) {
            entry.setBillSerial(l);
            l++;
        }
    }

    /**
     *
     */
    public void removeItemFromList() {
        if (getEditBillItem() == null) {
            JsfUtil.addErrorMessage("Nothing to Delete. Please select one");
        }
        getBillItems().remove(getEditBillItem());
        orderBillItemEntries();
        setEditBillItem(null);
        JsfUtil.addSuccessMessage("Removed From List");
    }

    /**
     *
     */
    public void settleBill() {

        saveBill();
        saveNewBillItems();
        clearEntry();
        clearBill();
        JsfUtil.addSuccessMessage("Bill Settled successfully");
    }

    private void clearEntry() {
        setModalName(null);
        setBillItem(null);
        setBillItem(getBillItem());
    }

    private void clearBill() {
        setBill(new Bill());
        setBillItems(new ArrayList<BillItem>());


    }

    /**
     *
     * @param item
     * @return
     */
    public Double calculateStock(Cylinder item) {
        if (item != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + "");
        } else {
            return 0.00;
        }
    }

    /**
     *
     * @param item
     * @param institution
     * @return
     */
    public Double calculateStock(Cylinder item, Institution institution) {
        if (item != null && institution != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.institution.id = " + institution.getId());
        } else {
            return 0.0;
        }
    }

    /**
     *
     * @param item
     * @param location
     * @return
     */
    public Double calculateStock(Cylinder item, Location location) {
        if (item != item && location != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.location.id = " + location.getId());
        } else {
            return 0.00;
        }
    }

    /**
     *
     * @param item
     * @param unit
     * @return
     */
    public Double calculateStock(Cylinder item, Unit unit) {
        if (item != null && unit != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.unit.id = " + unit.getId());
        } else {
            return 0.00;
        }
    }

    /**
     *
     * @param item
     * @param person
     * @return
     */
    public Double calculateStock(Cylinder item, Person person) {
        if (item != null && person != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.person.id = " + person.getId());
        } else {
            return 0.00;
        }
    }

    /**
     *
     * @param strJQL
     * @return
     */
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
        for (BillItem temEntry : getBillItems()) {
            if (temEntry != null) {
                settleBillItem(temEntry);
            }
        }
    }

    private ItemUnit getExistingItemUnit(Institution exIns, Unit exUnit, Person exPerson, Cylinder exItem, String exSerial, Make exMake, Location exLoc) {
        ItemUnit myIu;
        String sql;

        
        String strIns;
        String strUnit;
        String strPer;
        String strLoc;
        String strItem;
        String strMake;
        String strSerial;

        if (exIns == null) {
            strIns = " (iu.institution.id = 0 or iu.institution.id = null )" ;
        } else {
            strIns = " iu.institution.id = " + exIns.getId() + " " ;
        }
        if (exUnit == null) {
            strUnit = " (iu.unit.id = 0 or iu.unit.id = null )" ;
        } else {
            strUnit = " iu.unit.id = " + exUnit.getId() + " " ;
        }
        if (exPerson == null) {
            strPer = " (iu.person.id = 0 or iu.person.id = null )" ;
        } else {
            strPer = " iu.person.id = " + exPerson.getId() + " " ;
        }
        if (exItem == null) {
            strItem = " (iu.item.id = 0 or iu.item.id = null )" ;
        } else {
            strItem = " iu.item.id = " + exItem.getId() + " " ;
        }
        if (exMake == null) {
            strMake = " (iu.make.id = 0 or iu.make.id = null )" ;
        } else {
            strMake = " iu.make.id = " + exMake.getId() + " " ;
        }
        if (exLoc == null) {
            strLoc = " (iu.location.id = 0 or iu.location.id = null )" ;
        } else {
            strLoc = " iu.location.id = " + exLoc.getId() + " " ;
        }
        if (exSerial == null) {
            strSerial = " (iu.name = '' or iu.name = null )" ;
        }else{
            strSerial = " iu.name = '" + exSerial + "' " ;
        }
        sql = "Select iu from ItemUnit iu where  " + strItem + " and " + strSerial + " and " + strIns + " and " + strUnit + " and " + strPer + " and " + strMake + " and  " + strLoc;
        System.out.println(sql);

        myIu = getItemUnitFacade().findFirstBySQL(sql);

        if (myIu == null) {
            System.out.println("my IU is null");
            myIu = new ItemUnit();
            myIu.setInstitution(exIns);
            myIu.setUnit(exUnit);
            myIu.setPerson(exPerson);
            myIu.setItem(exItem);
            myIu.setMake(exMake);
            myIu.setName(exSerial);
            myIu.setLocation(exLoc);
            myIu.setCreatedAt(Calendar.getInstance().getTime());
            myIu.setCreater(getSessionController().getLoggedUser());
            getItemUnitFacade().create(myIu);
        }
        return myIu;

    }

    private void settleBillItem(BillItem temEntry) {

        BillItem temBillItem = temEntry;
        ItemUnit newItemUnit = getExistingItemUnit(toInstitution, getBill().getToUnit(), getBill().getToPerson(), (Cylinder)temBillItem.getItem(), temBillItem.getName(), null, null);

        newItemUnit.setOwner(getBill().getToPerson());
        newItemUnit.setWarrantyExpiary(newItemUnit.getDateOfExpiary());
        newItemUnit.setSupplier(null);
        newItemUnit.setUnit(getBill().getToUnit());
        newItemUnit.setQuentity(newItemUnit.getQuentity() + temBillItem.getQuentity());



        System.out.println("Saving Bill Cylinder " + temBillItem.getQuentity());
        ItemUnitHistory hxUnit = new ItemUnitHistory();
        ItemUnitHistory hxLoc = new ItemUnitHistory();
        ItemUnitHistory hxIns = new ItemUnitHistory();
        ItemUnitHistory hxPer = new ItemUnitHistory();


        hxIns.setBeforeQty( calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getInstitution()));
        hxIns.setCreatedAt(Calendar.getInstance().getTime());
        hxIns.setCreater(getSessionController().loggedUser);
        hxIns.setInstitution(newItemUnit.getInstitution());
        hxIns.setItem(newItemUnit.getItem());
        hxIns.setQuentity(newItemUnit.getQuentity());
        hxIns.setToIn(Boolean.TRUE);
        hxIns.setToOut(Boolean.FALSE);


        hxUnit.setBeforeQty(calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getUnit()));
        hxUnit.setCreatedAt(Calendar.getInstance().getTime());
        hxUnit.setCreater(getSessionController().loggedUser);
        hxUnit.setUnit(newItemUnit.getUnit());
        hxUnit.setItem(newItemUnit.getItem());
        hxUnit.setQuentity(newItemUnit.getQuentity());
        hxUnit.setToIn(Boolean.TRUE);
        hxUnit.setToOut(Boolean.FALSE);

        hxLoc.setBeforeQty(calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getLocation()));
        hxLoc.setCreatedAt(Calendar.getInstance().getTime());
        hxLoc.setCreater(getSessionController().loggedUser);
        hxLoc.setLocation(newItemUnit.getLocation());
        hxLoc.setItem(newItemUnit.getItem());
        hxLoc.setQuentity(newItemUnit.getQuentity());
        hxLoc.setToIn(Boolean.TRUE);
        hxLoc.setToOut(Boolean.FALSE);

        hxPer.setBeforeQty(calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getPerson()));
        hxPer.setCreatedAt(Calendar.getInstance().getTime());
        hxPer.setCreater(getSessionController().loggedUser);
        hxPer.setPerson(newItemUnit.getPerson());
        hxPer.setItem(newItemUnit.getItem());
        hxPer.setQuentity(newItemUnit.getQuentity());
        hxPer.setToIn(Boolean.TRUE);
        hxPer.setToOut(Boolean.FALSE);

        getItemUnitFacade().edit(newItemUnit);

        hxIns.setAfterQty(calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getInstitution()));
        hxIns.setItemUnit(newItemUnit);
        getItemUnitHistoryFacade().create(hxIns);

        hxUnit.setAfterQty(calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getUnit()));
        hxUnit.setItemUnit(newItemUnit);
        getItemUnitHistoryFacade().create(hxUnit);

        hxLoc.setAfterQty(calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getLocation()));
        hxLoc.setItemUnit(newItemUnit);
        getItemUnitHistoryFacade().create(hxLoc);

        hxPer.setAfterQty(calculateStock((Cylinder)newItemUnit.getItem(), newItemUnit.getPerson()));
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
        if (temBillItem.getId() == null || temBillItem.getId() == 0) {
            getBillItemFacade().create(temBillItem);
        } else {
            getBillItemFacade().edit(temBillItem);
        }
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
    /**
     *
     */
    public void calculateItemValue() {
        getBillItem().setNetValue(getBillItem().getNetRate() * getBillItem().getQuentity());
    }

    /**
     *
     */
    public void calculateBillValue() {
        double netBillValue = 0l;
        double grossBillValue = 0l;
        double discountBillValue = 0l;
        for (BillItem temEntry : getBillItems()) {
            netBillValue += temEntry.getNetValue();
            grossBillValue += temEntry.getGrossValue();
            discountBillValue += temEntry.getDiscountValue();
        }
        getBill().setNetValue(netBillValue - getBill().getDiscountValue());
        getBill().setGrossValue(netBillValue);
    }

    /**
     * Creates a new instance of PurchaseBillController
     */
    public GasPurchaseBillController() {
    }

    /**
     * Getters and Setters
     */
    private void addLastBillEntryNumber(BillItem entry) {
        entry.setBillSerial((long) getBillItems().size() + 1);
    }

    /**
     *
     * @return
     */
    public BillItem getBillItem() {
        if (billItem == null) {
            billItem = new BillItem();
            addLastBillEntryNumber(billItem);
        }
        return billItem;
    }

    /**
     *
     * @param billItemEntry
     */
    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }

    /**
     *
     * @return
     */
    public List<BillItem> getBillItems() {
        System.out.println("getting bill items");
        if (billItems == null) {
            System.out.println("bill items are null");
            billItems = new ArrayList<BillItem>();
        }
        return billItems;
    }

    /**
     *
     * @param lstBillItemEntrys
     */
    public void setBillItems(List<BillItem> lstBillItems) {
        this.billItems = lstBillItems;
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
    /**
     *
     */
    public void prepareForNewBill() {
        setNewBill(Boolean.TRUE);
        setBill(new InInventoryBill());
        getBill().setBillDate(Calendar.getInstance().getTime());

    }

    /**
     *
     */
    public void prepareForOldBill() {
        setNewBill(Boolean.FALSE);
        setBill(getTransferBean().getBill());
        String temStr = "SELECT e FROM BillItem e WHERE e.retired=false AND e.bill.id = " + getBill().getId();
        List<BillItem> temLstBillItems = new ArrayList<BillItem>(getBillItemFacade().findBySQL(temStr));
        System.out.println(temLstBillItems.toString());
        long i = 1;
        for (BillItem bi : temLstBillItems) {
            BillItem bie = new BillItem();
            bie.setId(i);
            getBillItems().add(bie);
            i++;
        }
        getTransferBean().setBill(null);
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @param bill
     */
    public void setBill(Bill bill) {
        this.bill = bill;
    }

    /**
     *
     * @return
     */
    public BillFacade getBillFacade() {
        return billFacade;
    }

    /**
     *
     * @param billFacade
     */
    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }

    /**
     *
     * @return
     */
    public BillItemFacade getBillItemFacade() {
        return billItemFacade;
    }

    /**
     *
     * @param billItemFacade
     */
    public void setBillItemFacade(BillItemFacade billItemFacade) {
        this.billItemFacade = billItemFacade;
    }

    /**
     *
     * @return
     */
    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    /**
     *
     * @param itemFacade
     */
    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    /**
     *
     * @return
     */
    public List<Cylinder> getItems() {
        items = getCylinderFacade().findBySQL("SELECT i FROM Cylinder i WHERE i.retired=false ORDER By i.name");
        return items;
    }

    /**
     *
     * @param items
     */
    public void setItems(List<Cylinder> items) {
        this.items = items;
    }

    /**
     *
     * @return
     */
    public SessionController getSessionController() {
        return sessionController;
    }

    /**
     *
     * @param sessionController
     */
    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    /**
     *
     * @return
     */
    public String getModalName() {
        return modalName;
    }

    /**
     *
     * @param modalName
     */
    public void setModalName(String modalName) {
        this.modalName = modalName;
    }

    /**
     *
     * @return
     */
    public BillItem getEditBillItem() {
        return editBillItem;
    }

    /**
     *
     * @param editBillItemEntry
     */
    public void setEditBillItem(BillItem editBillItemEntry) {
        this.editBillItem = editBillItemEntry;
    }

    /**
     *
     * @return
     */
    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    /**
     *
     * @param institutionFacade
     */
    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    /**
     *
     * @return
     */
    public List<Institution> getToInstitutions() {
        if (getSessionController().getPrivilege().getRestrictedInstitution() == null) {
            toInstitutions = getInstitutionFacade().findBySQL("SELECT i FROM HealthInstitution i WHERE i.retired=false ORDER by i.name");
        } else {
            toInstitutions = new ArrayList<Institution>();
            toInstitutions.add(getSessionController().getPrivilege().getRestrictedInstitution());
        }
        return toInstitutions;
    }

    /**
     *
     * @param institutions
     */
    public void setToInstitutions(List<Institution> institutions) {
        this.toInstitutions = institutions;
    }

    /**
     *
     * @return
     */
    public List<Institution> getFromInstitutions() {
        fromInstitutions = getInstitutionFacade().findBySQL("SELECT i FROM Supplier i WHERE i.retired=false ORDER by i.name");
        return fromInstitutions;
    }

    /**
     *
     * @param institutions
     */
    public void setFromInstitutions(List<Institution> institutions) {
        this.fromInstitutions = institutions;
    }

    /**
     *
     * @return
     */
    public List<Location> getToLocations() {
        if (getBill().getToUnit() != null) {
            toLocations = getLocationFacade().findBySQL("SELECT l FROM Location l WHERE l.retired=false AND l.unit.id = " + getBill().getToUnit().getId() + " ORDER BY l.name");
        } else {
            toLocations = new ArrayList<Location>();
        }
        return toLocations;
    }

    /**
     *
     * @param locations
     */
    public void setToLocations(List<Location> locations) {
        this.toLocations = locations;
    }

    /**
     *
     * @return
     */
    public List<Unit> getToUnits() {
        if (getToInstitution() == null) {
            toUnits = new ArrayList<Unit>();
        } else {
            toUnits = getUnitFacade().findBySQL("SELECT u FROM Unit u WHERE u.retired=false AND u.institution.id=" + getToInstitution().getId() + " ORDER BY u.name");
        }
        return toUnits;
    }

    /**
     *
     * @param toUnits
     */
    public void setToUnits(List<Unit> toUnits) {
        this.toUnits = toUnits;
    }

    /**
     *
     * @return
     */
    public UnitFacade getUnitFacade() {
        return unitFacade;
    }

    /**
     *
     * @param unitFacade
     */
    public void setUnitFacade(UnitFacade unitFacade) {
        this.unitFacade = unitFacade;
    }

    /**
     *
     * @return
     */
    public LocationFacade getLocationFacade() {
        return locationFacade;
    }

    /**
     *
     * @param locationFacade
     */
    public void setLocationFacade(LocationFacade locationFacade) {
        this.locationFacade = locationFacade;
    }

    /**
     *
     * @return
     */
    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    /**
     *
     * @param personFacade
     */
    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    /**
     *
     * @return
     */
    public List<Person> getToPersons() {
        if (getToInstitution() == null) {
            toPersons = new ArrayList<Person>();
        } else {
            toPersons = getPersonFacade().findBySQL("SELECT p FROM Person p WHERE p.retired=false AND p.institution.id=" + getToInstitution().getId() + " ORDER BY p.name");
        }
        return toPersons;
    }

    /**
     *
     * @param toPersons
     */
    public void setToPersons(List<Person> toPersons) {
        this.toPersons = toPersons;
    }

   
    /**
     *
     * @return
     */
    public ManufacturerFacade getManufacturerFacade() {
        return manufacturerFacade;
    }

    /**
     *
     * @param manufacturerFacade
     */
    public void setManufacturerFacade(ManufacturerFacade manufacturerFacade) {
        this.manufacturerFacade = manufacturerFacade;
    }

    /**
     *
     * @return
     */
    public List<Manufacturer> getManufacturers() {
        manufacturers = getManufacturerFacade().findBySQL("SELECT m FROM Manufacturer m WHERE m.retired=false ORDER BY m.name");
        return manufacturers;
    }

    /**
     *
     * @param manufacturers
     */
    public void setManufacturers(List<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    /**
     *
     * @return
     */
    public SupplierFacade getSupplierFacade() {
        return supplierFacade;
    }

    /**
     *
     * @param supplierFacade
     */
    public void setSupplierFacade(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    /**
     *
     * @return
     */
    public List<Supplier> getSuppliers() {
        suppliers = getSupplierFacade().findBySQL("SELECT s FROM Supplier s WHERE s.retired=false ORDER BY s.name");
        return suppliers;
    }

    /**
     *
     * @param suppliers
     */
    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    /**
     *
     * @return
     */
    public ItemUnitFacade getItemUnitFacade() {
        return itemUnitFacade;
    }

    /**
     *
     * @param itemUnitFacade
     */
    public void setItemUnitFacade(ItemUnitFacade itemUnitFacade) {
        this.itemUnitFacade = itemUnitFacade;
    }

    /**
     *
     * @return
     */
    public ItemUnitHistoryFacade getItemUnitHistoryFacade() {
        return itemUnitHistoryFacade;
    }

    /**
     *
     * @param itemUnitHistoryFacade
     */
    public void setItemUnitHistoryFacade(ItemUnitHistoryFacade itemUnitHistoryFacade) {
        this.itemUnitHistoryFacade = itemUnitHistoryFacade;
    }

    /**
     *
     * @return
     */
    public TransferBean getTransferBean() {
        return transferBean;
    }

    /**
     *
     * @param transferBean
     */
    public void setTransferBean(TransferBean transferBean) {
        this.transferBean = transferBean;
    }

    /**
     *
     * @return
     */
    public Boolean getNewBill() {
        return newBill;
    }

    /**
     *
     * @param newBill
     */
    public void setNewBill(Boolean newBill) {
        this.newBill = newBill;
    }

    /**
     *
     * @return
     */
    public String getItemSerial() {
        if (getBillItem().getItem() == null) {
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
        if (getBillItem().getItem().getName().trim().equals("")) {
            itemSerial += "/" + getBillItem().getItem().getName().trim().toUpperCase();
        } else {
            itemSerial += "/" + getBillItem().getItem().getCode().trim().toUpperCase();

        }
        Long temNo = getItemUnitFacade().findAggregateLong("select count(iu) from ItemUnit where iu.retired = false and iu.institute.id =" + getToInstitution().getId() + " and iu.item.id =" + getBillItem().getItem().getId());
        itemSerial += "/" + temNo;
        return itemSerial;
    }

    /**
     *
     * @param itemSerial
     */
    public void setItemSerial(String itemSerial) {
        this.itemSerial = itemSerial;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @param toInstitution
     */
    public void setToInstitution(Institution toInstitution) {
        this.toInstitution = toInstitution;
    }
}
