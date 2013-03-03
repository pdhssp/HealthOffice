/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Buddhika
 */
@Entity
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
//Main Properties
    String name;
    String description;
    //Created Properties
    @ManyToOne
    WebUser creater;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date createdAt;
    //Retairing properties
    boolean retired;
    @ManyToOne
    WebUser retirer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date retiredAt;
    String retireComments;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date billDate;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date billTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date billTimeStamp;
    double grossValue;
    double discountValue;
    double netValue;
    double discountValuePercent;
    double grossCost;
    double discountCost;
    double netCost;
    double discountCostPercent;
    boolean cancelled;
    @ManyToOne
    Bill cancelledBill;
    boolean returned;
    @ManyToOne
    Bill returnedBill;

    @ManyToOne
    Institution fromInstitution;
    @ManyToOne
    Institution toInstitution;
    @ManyToOne
    Unit fromUnit;
    @ManyToOne
    Unit toUnit;
    @ManyToOne
    Location fromLocation;
    @ManyToOne
    Location toLocation;
    @ManyToOne
    Person fromPerson;
    @ManyToOne
    Person toPerson;

    
    @OneToMany(mappedBy = "bill")
    List<BillItem> billItems;

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
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
    
    
    
    
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Bill getCancelledBill() {
        return cancelledBill;
    }

    public void setCancelledBill(Bill cancelledBill) {
        this.cancelledBill = cancelledBill;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public WebUser getCreater() {
        return creater;
    }

    public void setCreater(WebUser creater) {
        this.creater = creater;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountCost() {
        return discountCost;
    }

    public void setDiscountCost(double discountCost) {
        this.discountCost = discountCost;
    }

    public double getDiscountCostPercent() {
        return discountCostPercent;
    }

    public void setDiscountCostPercent(double discountCostPercent) {
        this.discountCostPercent = discountCostPercent;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getDiscountValuePercent() {
        return discountValuePercent;
    }

    public void setDiscountValuePercent(double discountValuePercent) {
        this.discountValuePercent = discountValuePercent;
    }

    public double getGrossCost() {
        return grossCost;
    }

    public void setGrossCost(double grossCost) {
        this.grossCost = grossCost;
    }

    public double getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(double grossValue) {
        this.grossValue = grossValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNetCost() {
        return netCost;
    }

    public void setNetCost(double netCost) {
        this.netCost = netCost;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    public String getRetireComments() {
        return retireComments;
    }

    public void setRetireComments(String retireComments) {
        this.retireComments = retireComments;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public Date getRetiredAt() {
        return retiredAt;
    }

    public void setRetiredAt(Date retiredAt) {
        this.retiredAt = retiredAt;
    }

    public WebUser getRetirer() {
        return retirer;
    }

    public void setRetirer(WebUser retirer) {
        this.retirer = retirer;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public Bill getReturnedBill() {
        return returnedBill;
    }

    public void setReturnedBill(Bill returnedBill) {
        this.returnedBill = returnedBill;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Date getBillTime() {
        return billTime;
    }

    public void setBillTime(Date billTime) {
        this.billTime = billTime;
    }

    public Date getBillTimeStamp() {
        return billTimeStamp;
    }

    public void setBillTimeStamp(Date billTimeStamp) {
        this.billTimeStamp = billTimeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Bill)) {
            return false;
        }
        Bill other = (Bill) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Bill (" + id + ")";
    }
}
