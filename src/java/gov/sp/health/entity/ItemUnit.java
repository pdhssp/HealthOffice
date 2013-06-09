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
public class ItemUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    @ManyToOne
    Item item;
    String serial;
    
    @ManyToOne
    Institution institution;
    
    
    
    
    @ManyToOne
    Unit unit;
    
    @ManyToOne
    Location location;
    
    @ManyToOne
    Person person;
    
    
    @ManyToOne
    Country country;
    
    @ManyToOne
    Manufacturer manufacturer;
    
    @ManyToOne
    Supplier supplier;
    
    @ManyToOne
    Person owner;
    
    @ManyToOne
    Make make;
    
    @ManyToOne
    Modal modal;
    
    String modalNo;
    
    
    @ManyToOne
    MeasurementUnit bulkUnit;
    
    @ManyToOne
    MeasurementUnit looseUnit;
    
    double looseUnitsPerBulkUnit;
    
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date dateOfManufacture;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date dateOfExpiary;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date warrantyExpiary ;
    
    double purchaseRate;
    double purchaseCostRate;
    double retailSaleRate;
    double retailSaleCostRate;
    double wholesaleRate;
    double wholesaleCostRate;
    
    double purchaseValue;
    double purchaseCostValue;
    double retailSaleValue;
    double retailSaleCostValue;
    double wholesaleValue;
    double wholesaleCostValue;
    
    
    
    
    double quentity;

    public String getModalNo() {
        return modalNo;
    }

    public void setModalNo(String modalNo) {
        this.modalNo = modalNo;
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

    public double getLooseUnitsPerBulkUnit() {
        return looseUnitsPerBulkUnit;
    }

    public void setLooseUnitsPerBulkUnit(double looseUnitsPerBulkUnit) {
        this.looseUnitsPerBulkUnit = looseUnitsPerBulkUnit;
    }

    
    
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getDateOfExpiary() {
        return dateOfExpiary;
    }

    public void setDateOfExpiary(Date dateOfExpiary) {
        this.dateOfExpiary = dateOfExpiary;
    }

    public Date getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(Date dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Modal getModal() {
        return modal;
    }

    public void setModal(Modal modal) {
        this.modal = modal;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public double getQuentity() {
        return quentity;
    }

    public void setQuentity(double quentity) {
        this.quentity = quentity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public double getPurchaseCostRate() {
        return purchaseCostRate;
    }

    public void setPurchaseCostRate(double purchaseCostRate) {
        this.purchaseCostRate = purchaseCostRate;
    }

    public double getPurchaseCostValue() {
        return purchaseCostValue;
    }

    public void setPurchaseCostValue(double purchaseCostValue) {
        this.purchaseCostValue = purchaseCostValue;
    }

    public double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public double getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(double purchaseValue) {
        this.purchaseValue = purchaseValue;
    }

    public double getRetailSaleCostRate() {
        return retailSaleCostRate;
    }

    public void setRetailSaleCostRate(double retailSaleCostRate) {
        this.retailSaleCostRate = retailSaleCostRate;
    }

    public double getRetailSaleCostValue() {
        return retailSaleCostValue;
    }

    public void setRetailSaleCostValue(double retailSaleCostValue) {
        this.retailSaleCostValue = retailSaleCostValue;
    }

    public double getRetailSaleRate() {
        return retailSaleRate;
    }

    public void setRetailSaleRate(double retailSaleRate) {
        this.retailSaleRate = retailSaleRate;
    }

    public double getRetailSaleValue() {
        return retailSaleValue;
    }

    public void setRetailSaleValue(double retailSaleValue) {
        this.retailSaleValue = retailSaleValue;
    }

    public Date getWarrantyExpiary() {
        return warrantyExpiary;
    }

    public void setWarrantyExpiary(Date warrantyExpiary) {
        this.warrantyExpiary = warrantyExpiary;
    }

    public double getWholesaleCostRate() {
        return wholesaleCostRate;
    }

    public void setWholesaleCostRate(double wholesaleCostRate) {
        this.wholesaleCostRate = wholesaleCostRate;
    }

    public double getWholesaleCostValue() {
        return wholesaleCostValue;
    }

    public void setWholesaleCostValue(double wholesaleCostValue) {
        this.wholesaleCostValue = wholesaleCostValue;
    }

    public double getWholesaleRate() {
        return wholesaleRate;
    }

    public void setWholesaleRate(double wholesaleRate) {
        this.wholesaleRate = wholesaleRate;
    }

    public double getWholesaleValue() {
        return wholesaleValue;
    }

    public void setWholesaleValue(double wholesaleValue) {
        this.wholesaleValue = wholesaleValue;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemUnit)) {
            return false;
        }
        ItemUnit other = (ItemUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (getItem()==null){
            return "No item";
        }else{
            return getItem().toString();
        }
        
    }
    
}
