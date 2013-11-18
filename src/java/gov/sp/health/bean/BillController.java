/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Bill;
import gov.sp.health.entity.BillItem;
import gov.sp.health.entity.Institution;
import gov.sp.health.entity.Person;
import gov.sp.health.facade.BillFacade;
import gov.sp.health.facade.BillItemFacade;
import gov.sp.health.facade.InstitutionFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 *
 * @author Buddhika
 */
@Named
@RequestScoped
public class BillController  implements Serializable {

    @EJB
    BillFacade billFacade;
    @EJB
    BillItemFacade billItemFacade;
    @EJB
    InstitutionFacade institutionFacade;
    Bill bill;
    List<BillItem> billItems;
    List<BillItem> returnItems;
    Institution supplierIns;
    Institution receiverIns;
    Person sender;
    Person receiver;
    Person checker;
    DataModel<BillItem> dmBillItems;
    DataModel<BillItem> dmReturnItems;

    /**
     * Creates a new instance of BillController
     */
    public BillController() {
    }

    public void saveBill() {
        if (bill == null) {
            JsfUtil.addErrorMessage("Nothing to Save");
            return;
        }
        if (billItems == null) {
            JsfUtil.addErrorMessage("Nothing to Save");
            return;
        }
        bill.setCreatedAt(Calendar.getInstance().getTime());

        if (bill.getId() != 0) {
            getBillFacade().edit(bill);
        } else {
            getBillFacade().create(bill);
        }
        for (BillItem temBillItem : billItems) {
            temBillItem.setBill(bill);
            if (temBillItem.getId() != 0) {
                getBillItemFacade().edit(temBillItem);
            } else {
                getBillItemFacade().create(temBillItem);
            }
        }



    }

    public void cancelBill() {
    }

    public void newBill() {
        bill = new Bill();
        billItems = new ArrayList<BillItem>();
        returnItems = new ArrayList<BillItem>();

    }

    public void returnBill() {
    }

    public BillItemFacade getBillItemFacade() {
        return billItemFacade;
    }

    public void setBillItemFacade(BillItemFacade billItemFacade) {
        this.billItemFacade = billItemFacade;
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public Bill getBill() {
        if (bill == null) {
            bill = new Bill();
        }
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<BillItem> getBillItems() {
        if (billItems == null) {
            billItems = new ArrayList<BillItem>();
        }
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    public Person getChecker() {
        return checker;
    }

    public void setChecker(Person checker) {
        this.checker = checker;
    }

    public Person getReceiver() {
        return receiver;
    }

    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }

    public Institution getReceiverIns() {
        return receiverIns;
    }

    public void setReceiverIns(Institution receiverIns) {
        this.receiverIns = receiverIns;
    }

    public List<BillItem> getReturnItems() {
        return returnItems;
    }

    public void setReturnItems(List<BillItem> returnItems) {
        this.returnItems = returnItems;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Institution getSupplierIns() {
        return supplierIns;
    }

    public void setSupplierIns(Institution supplierIns) {
        this.supplierIns = supplierIns;
    }

    public DataModel<BillItem> getDmBillItems() {
        return new ListDataModel<BillItem>(getBillItems());
    }

    public void setDmBillItems(DataModel<BillItem> dmBillItems) {
        this.dmBillItems = dmBillItems;
    }

    public DataModel<BillItem> getDmReturnItems() {
        return new ListDataModel<BillItem>(getReturnItems());
    }

    public void setDmReturnItems(DataModel<BillItem> dmReturnItems) {
        this.dmReturnItems = dmReturnItems;
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }
}
