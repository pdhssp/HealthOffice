/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author IT
 */
@Entity
public class ItemUnitLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    ItemUnit itemUnit;
    @ManyToOne
    Location location;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date fromDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date fromTime;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date toDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date toTime;
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
    //    
    //Adding Properties
    @ManyToOne
    WebUser adder;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date addedAt;
    @OneToOne
    ItemUnitLocation previousUnitLocation;
    @ManyToOne
    Bill addedBill;
//Removing Properties
    @ManyToOne
    WebUser remover;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date removedAt;
    @OneToOne
    ItemUnitLocation nextUnitLocation;
    @ManyToOne
    Bill removedBill;    
    //
    double quentity;
    
    
    public ItemUnitLocation() {
    }

    public double getQuentity() {
        return quentity;
    }

    public void setQuentity(double quentity) {
        this.quentity = quentity;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Bill getAddedBill() {
        return addedBill;
    }

    public void setAddedBill(Bill addedBill) {
        this.addedBill = addedBill;
    }

    public WebUser getAdder() {
        return adder;
    }

    public void setAdder(WebUser adder) {
        this.adder = adder;
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemUnitLocation getNextUnitLocation() {
        return nextUnitLocation;
    }

    public void setNextUnitLocation(ItemUnitLocation nextUnitLocation) {
        this.nextUnitLocation = nextUnitLocation;
    }

    public ItemUnitLocation getPreviousUnitLocation() {
        return previousUnitLocation;
    }

    public void setPreviousUnitLocation(ItemUnitLocation previousUnitLocation) {
        this.previousUnitLocation = previousUnitLocation;
    }

    public Date getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Date removedAt) {
        this.removedAt = removedAt;
    }

    public Bill getRemovedBill() {
        return removedBill;
    }

    public void setRemovedBill(Bill removedBill) {
        this.removedBill = removedBill;
    }

    public WebUser getRemover() {
        return remover;
    }

    public void setRemover(WebUser remover) {
        this.remover = remover;
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

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemUnitLocation)) {
            return false;
        }
        ItemUnitLocation other = (ItemUnitLocation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.ItemUnitLocation[ id=" + id + " ]";
    }
}
