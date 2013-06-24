/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.util.Date;

/**
 *
 * @author Buddhika
 */
public class LedgerEntry {

    Date entryDate;
    ItemUnit itemUnit;
    ItemUnitHistory itemUnitHistory;
    BillItem billItem;
    Bill bill;
    String comments;
    Double inQty;
    Double outQty;
    Double monthInQty;
    Double monthOutQty;
    Double yearInQty;
    Double yearOutQty;
    Double beforeStock;
    Double afterStock;
    Institution institution;
    Unit unit;
    Location location;
    Person person;

    public Double getAfterStock() {
        return afterStock;
    }

    public void setAfterStock(Double afterStock) {
        this.afterStock = afterStock;
    }

    public Double getBeforeStock() {
        return beforeStock;
    }

    public void setBeforeStock(Double beforeStock) {
        this.beforeStock = beforeStock;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public BillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Double getInQty() {
        return inQty;
    }

    public void setInQty(Double inQty) {
        this.inQty = inQty;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

    public ItemUnitHistory getItemUnitHistory() {
        return itemUnitHistory;
    }

    public void setItemUnitHistory(ItemUnitHistory itemUnitHistory) {
        this.itemUnitHistory = itemUnitHistory;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getMonthInQty() {
        return monthInQty;
    }

    public void setMonthInQty(Double monthInQty) {
        this.monthInQty = monthInQty;
    }

    public Double getMonthOutQty() {
        return monthOutQty;
    }

    public void setMonthOutQty(Double monthOutQty) {
        this.monthOutQty = monthOutQty;
    }

    public Double getOutQty() {
        return outQty;
    }

    public void setOutQty(Double outQty) {
        this.outQty = outQty;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Double getYearInQty() {
        return yearInQty;
    }

    public void setYearInQty(Double yearInQty) {
        this.yearInQty = yearInQty;
    }

    public Double getYearOutQty() {
        return yearOutQty;
    }

    public void setYearOutQty(Double yearOutQty) {
        this.yearOutQty = yearOutQty;
    }


    
    
    

}
