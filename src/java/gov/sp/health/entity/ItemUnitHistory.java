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
 * @author Buddhika
 */
@Entity
public class ItemUnitHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    boolean emptyUnit;
    
    @ManyToOne
    Item item;
    String serial;
    
    @ManyToOne
    Institution institution;
    
    Boolean toIn;
    
    Boolean toOut;
    
    Double quentity;
    
    Double beforeQty;
    Double afterQty;
    
    @ManyToOne
    ItemUnit itemUnit;
   
    @ManyToOne
    Unit unit;
    
    @ManyToOne
    Location location;
    
      
    @ManyToOne
    Person person;
    
     BillItem billItem;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date historyDate;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date historyTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date historyTimeStamp;

    public boolean isEmptyUnit() {
        return emptyUnit;
    }

    public void setEmptyUnit(boolean emptyUnit) {
        this.emptyUnit = emptyUnit;
    }

    
    
    public Date getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(Date historyDate) {
        this.historyDate = historyDate;
    }

    public Date getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(Date historyTime) {
        this.historyTime = historyTime;
    }

    public Date getHistoryTimeStamp() {
        return historyTimeStamp;
    }

    public void setHistoryTimeStamp(Date historyTimeStamp) {
        this.historyTimeStamp = historyTimeStamp;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAfterQty() {
        return afterQty;
    }

    public void setAfterQty(Double afterQty) {
        this.afterQty = afterQty;
    }

    public Double getBeforeQty() {
        return beforeQty;
    }

    public void setBeforeQty(Double beforeQty) {
        this.beforeQty = beforeQty;
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

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Double getQuentity() {
        return quentity;
    }

    public void setQuentity(Double quentity) {
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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Boolean getToIn() {
        return toIn;
    }

    public void setToIn(Boolean toIn) {
        this.toIn = toIn;
    }

    public Boolean getToOut() {
        return toOut;
    }

    public void setToOut(Boolean toOut) {
        this.toOut = toOut;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public BillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }

    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemUnitHistory)) {
            return false;
        }
        ItemUnitHistory other = (ItemUnitHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.ItemUnitHistory[ id=" + id + " ]";
    }
    
}
