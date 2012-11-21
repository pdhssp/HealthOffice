/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Buddhika
 */
@Entity
public class BillItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
//Main Properties
    String name;
    String code;
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
    
    @ManyToOne
    Bill bill;
    @ManyToOne
    ItemUnit itemUnit;
    
    @ManyToOne
    Item item;
    
    
    double grossRate;
    double discountRate;
    double netRate;
    double discountRatePercent;
    double grossCostRate;
    double discountCostRate;
    double netCostRate;
    double discountCostPercentRate;

    double quentity;
    double freeQuentity;
    double purchaseQuentity;

    double grossValue;
    double discountValue;
    double netValue;
    double discountValuePercent;
    double grossCostValue;
    double discountCostValue;
    double netCostValue;
    double discountCostPercentValue;
    
    
    boolean cancelled;
    @ManyToOne
    BillItem cancelledBillItem;
    boolean returned;
    @ManyToOne
    BillItem returnedBillItem;

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public BillItem getCancelledBillItem() {
        return cancelledBillItem;
    }

    public void setCancelledBillItem(BillItem cancelledBillItem) {
        this.cancelledBillItem = cancelledBillItem;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public double getDiscountCostPercentRate() {
        return discountCostPercentRate;
    }

    public void setDiscountCostPercentRate(double discountCostPercentRate) {
        this.discountCostPercentRate = discountCostPercentRate;
    }

    public double getDiscountCostPercentValue() {
        return discountCostPercentValue;
    }

    public void setDiscountCostPercentValue(double discountCostPercentValue) {
        this.discountCostPercentValue = discountCostPercentValue;
    }

    public double getDiscountCostRate() {
        return discountCostRate;
    }

    public void setDiscountCostRate(double discountCostRate) {
        this.discountCostRate = discountCostRate;
    }

    public double getDiscountCostValue() {
        return discountCostValue;
    }

    public void setDiscountCostValue(double discountCostValue) {
        this.discountCostValue = discountCostValue;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRatePercent() {
        return discountRatePercent;
    }

    public void setDiscountRatePercent(double discountRatePercent) {
        this.discountRatePercent = discountRatePercent;
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

    public double getGrossCostRate() {
        return grossCostRate;
    }

    public void setGrossCostRate(double grossCostRate) {
        this.grossCostRate = grossCostRate;
    }

    public double getGrossCostValue() {
        return grossCostValue;
    }

    public void setGrossCostValue(double grossCostValue) {
        this.grossCostValue = grossCostValue;
    }

    public double getGrossRate() {
        return grossRate;
    }

    public void setGrossRate(double grossRate) {
        this.grossRate = grossRate;
    }

    public double getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(double grossValue) {
        this.grossValue = grossValue;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNetCostRate() {
        return netCostRate;
    }

    public void setNetCostRate(double netCostRate) {
        this.netCostRate = netCostRate;
    }

    public double getNetCostValue() {
        return netCostValue;
    }

    public void setNetCostValue(double netCostValue) {
        this.netCostValue = netCostValue;
    }

    public double getNetRate() {
        return netRate;
    }

    public void setNetRate(double netRate) {
        this.netRate = netRate;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    public double getQuentity() {
        return quentity;
    }

    public void setQuentity(double quentity) {
        this.quentity = quentity;
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

    public BillItem getReturnedBillItem() {
        return returnedBillItem;
    }

    public void setReturnedBillItem(BillItem returnedBillItem) {
        this.returnedBillItem = returnedBillItem;
    }

    public double getFreeQuentity() {
        return freeQuentity;
    }

    public void setFreeQuentity(double freeQuentity) {
        this.freeQuentity = freeQuentity;
    }

    public double getPurchaseQuentity() {
        return purchaseQuentity;
    }

    public void setPurchaseQuentity(double purchaseQuentity) {
        this.purchaseQuentity = purchaseQuentity;
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
        if (!(object instanceof BillItem)) {
            return false;
        }
        BillItem other = (BillItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.BillItem[ id=" + id + " ]";
    }
    
}
