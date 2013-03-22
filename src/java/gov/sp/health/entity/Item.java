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
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    ItemCategory category;
    @ManyToOne
    Category itemClass;
    Double itemQuantity;
    @ManyToOne
    MeasurementUnit bulkUnit;
    @ManyToOne
    MeasurementUnit looseUnit;
    @ManyToOne
    MeasurementUnit itemUnit;
    double looseUnitsPerBulkUnit;
    private String itemPrefix;


        private String sname;
    private String tname;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    
    public Double getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Category getItemClass() {
        return itemClass;
    }

    public void setItemClass(Category itemClass) {
        this.itemClass = itemClass;
    }

    public MeasurementUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(MeasurementUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

    public double getLooseUnitsPerBulkUnit() {
        return looseUnitsPerBulkUnit;
    }

    public void setLooseUnitsPerBulkUnit(double looseUnitsPerBulkUnit) {
        this.looseUnitsPerBulkUnit = looseUnitsPerBulkUnit;
    }

    public MeasurementUnit getBulkUnit() {
        return bulkUnit;
    }

    public void setBulkUnit(MeasurementUnit bulkUnit) {
        this.bulkUnit = bulkUnit;
    }

    public MeasurementUnit getLooseUnit() {
        return looseUnit;
    }

    public void setLooseUnit(MeasurementUnit looseUnit) {
        this.looseUnit = looseUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (getCode().equals("")) {
            return getName();
        } else {
            return name + "(" + getName() + ")";
        }
    }

    public String getItemPrefix() {
        return itemPrefix;
    }

    public void setItemPrefix(String itemPrefix) {
        this.itemPrefix = itemPrefix;
    }
}
