/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Entity
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    boolean demographyAdd;
    boolean demographyEdit;
    boolean demographyDelete;
    boolean demographyView;
    boolean inventoryAdd;
    boolean inventoryEdit;
    boolean inventoryDelete;
    boolean inventoryView;
    boolean msAdd;
    boolean msEdit;
    boolean msDelete;
    boolean msView;
    boolean caderAdd;
    boolean caderEdit;
    boolean caderDelete;
    boolean caderView;
    boolean libraryAdd;
    boolean libraryEdit;
    boolean libraryDelete;
    boolean libraryView;
    boolean vehicleAdd;
    boolean vehicleEdit;
    boolean vehicleDelete;
    boolean vehicleView;
    boolean activateAccounts;
    boolean dectivateAccounts;
    boolean deleteAccounts;
    boolean manageAccounts;
    boolean bmeAdd;
    boolean bmeEdit;
    boolean bmeDelete;
    boolean bmeView;
    boolean financeAdd;
    boolean financeEdit;
    boolean financeDelete;
    boolean financeView;
    boolean postView;
    boolean postAdd;
    boolean postDelete;
    boolean postEdit;
    @ManyToOne
    Institution restrictedInstitution;
    @ManyToOne
    Unit restrictedUnit;
    @ManyToOne
    Area restrictedArea;

    public Area getRestrictedArea() {
        return restrictedArea;
    }

    public void setRestrictedArea(Area restrictedArea) {
        this.restrictedArea = restrictedArea;
    }
    //Main Properties
    @Column(unique = true)
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
    //Activation properties
    boolean activated;
    @ManyToOne
    WebUser activator;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date activatedAt;
    String activateComments;
    @OneToOne
    WebUser webUser;
    @OneToOne
    WebUserRole webUserRole;

    public boolean isActivateAccounts() {
        return activateAccounts;
    }

    public void setActivateAccounts(boolean activateAccounts) {
        this.activateAccounts = activateAccounts;
    }

    public String getActivateComments() {
        return activateComments;
    }

    public void setActivateComments(String activateComments) {
        this.activateComments = activateComments;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Date getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(Date activatedAt) {
        this.activatedAt = activatedAt;
    }

    public WebUser getActivator() {
        return activator;
    }

    public void setActivator(WebUser activator) {
        this.activator = activator;
    }

    public boolean isBmeAdd() {
        return bmeAdd;
    }

    public void setBmeAdd(boolean bmeAdd) {
        this.bmeAdd = bmeAdd;
    }

    public boolean isBmeDelete() {
        return bmeDelete;
    }

    public void setBmeDelete(boolean bmeDelete) {
        this.bmeDelete = bmeDelete;
    }

    public boolean isBmeEdit() {
        return bmeEdit;
    }

    public void setBmeEdit(boolean bmeEdit) {
        this.bmeEdit = bmeEdit;
    }

    public boolean isBmeView() {
        return bmeView;
    }

    public void setBmeView(boolean bmeView) {
        this.bmeView = bmeView;
    }

    public boolean isCaderAdd() {
        return caderAdd;
    }

    public void setCaderAdd(boolean caderAdd) {
        this.caderAdd = caderAdd;
    }

    public boolean isCaderDelete() {
        return caderDelete;
    }

    public void setCaderDelete(boolean caderDelete) {
        this.caderDelete = caderDelete;
    }

    public boolean isCaderEdit() {
        return caderEdit;
    }

    public void setCaderEdit(boolean caderEdit) {
        this.caderEdit = caderEdit;
    }

    public boolean isCaderView() {
        return caderView;
    }

    public void setCaderView(boolean caderView) {
        this.caderView = caderView;
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

    public boolean isDectivateAccounts() {
        return dectivateAccounts;
    }

    public void setDectivateAccounts(boolean dectivateAccounts) {
        this.dectivateAccounts = dectivateAccounts;
    }

    public boolean isDeleteAccounts() {
        return deleteAccounts;
    }

    public void setDeleteAccounts(boolean deleteAccounts) {
        this.deleteAccounts = deleteAccounts;
    }

    public boolean isDemographyAdd() {
        return demographyAdd;
    }

    public void setDemographyAdd(boolean demographyAdd) {
        this.demographyAdd = demographyAdd;
    }

    public boolean isDemographyDelete() {
        return demographyDelete;
    }

    public void setDemographyDelete(boolean demographyDelete) {
        this.demographyDelete = demographyDelete;
    }

    public boolean isDemographyEdit() {
        return demographyEdit;
    }

    public void setDemographyEdit(boolean demographyEdit) {
        this.demographyEdit = demographyEdit;
    }

    public boolean isDemographyView() {
        return demographyView;
    }

    public void setDemographyView(boolean demographyView) {
        this.demographyView = demographyView;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinanceAdd() {
        return financeAdd;
    }

    public void setFinanceAdd(boolean financeAdd) {
        this.financeAdd = financeAdd;
    }

    public boolean isFinanceDelete() {
        return financeDelete;
    }

    public void setFinanceDelete(boolean financeDelete) {
        this.financeDelete = financeDelete;
    }

    public boolean isFinanceEdit() {
        return financeEdit;
    }

    public void setFinanceEdit(boolean financeEdit) {
        this.financeEdit = financeEdit;
    }

    public boolean isFinanceView() {
        return financeView;
    }

    public void setFinanceView(boolean financeView) {
        this.financeView = financeView;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isInventoryAdd() {
        return inventoryAdd;
    }

    public void setInventoryAdd(boolean inventoryAdd) {
        this.inventoryAdd = inventoryAdd;
    }

    public boolean isInventoryDelete() {
        return inventoryDelete;
    }

    public void setInventoryDelete(boolean inventoryDelete) {
        this.inventoryDelete = inventoryDelete;
    }

    public boolean isInventoryEdit() {
        return inventoryEdit;
    }

    public void setInventoryEdit(boolean inventoryEdit) {
        this.inventoryEdit = inventoryEdit;
    }

    public boolean isInventoryView() {
        return inventoryView;
    }

    public void setInventoryView(boolean inventoryView) {
        this.inventoryView = inventoryView;
    }

    public boolean isLibraryAdd() {
        return libraryAdd;
    }

    public void setLibraryAdd(boolean libraryAdd) {
        this.libraryAdd = libraryAdd;
    }

    public boolean isLibraryDelete() {
        return libraryDelete;
    }

    public void setLibraryDelete(boolean libraryDelete) {
        this.libraryDelete = libraryDelete;
    }

    public boolean isLibraryEdit() {
        return libraryEdit;
    }

    public void setLibraryEdit(boolean libraryEdit) {
        this.libraryEdit = libraryEdit;
    }

    public boolean isLibraryView() {
        return libraryView;
    }

    public void setLibraryView(boolean libraryView) {
        this.libraryView = libraryView;
    }

    public boolean isManageAccounts() {
        return manageAccounts;
    }

    public void setManageAccounts(boolean manageAccounts) {
        this.manageAccounts = manageAccounts;
    }

    public boolean isMsAdd() {
        return msAdd;
    }

    public void setMsAdd(boolean msAdd) {
        this.msAdd = msAdd;
    }

    public boolean isMsDelete() {
        return msDelete;
    }

    public void setMsDelete(boolean msDelete) {
        this.msDelete = msDelete;
    }

    public boolean isMsEdit() {
        return msEdit;
    }

    public void setMsEdit(boolean msEdit) {
        this.msEdit = msEdit;
    }

    public boolean isMsView() {
        return msView;
    }

    public void setMsView(boolean msView) {
        this.msView = msView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Institution getRestrictedInstitution() {
        return restrictedInstitution;
    }

    public void setRestrictedInstitution(Institution restrictedInstitution) {
        this.restrictedInstitution = restrictedInstitution;
    }

    public Unit getRestrictedUnit() {
        return restrictedUnit;
    }

    public void setRestrictedUnit(Unit restrictedUnit) {
        this.restrictedUnit = restrictedUnit;
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

    public boolean isVehicleAdd() {
        return vehicleAdd;
    }

    public void setVehicleAdd(boolean vehicleAdd) {
        this.vehicleAdd = vehicleAdd;
    }

    public boolean isVehicleDelete() {
        return vehicleDelete;
    }

    public void setVehicleDelete(boolean vehicleDelete) {
        this.vehicleDelete = vehicleDelete;
    }

    public boolean isVehicleEdit() {
        return vehicleEdit;
    }

    public void setVehicleEdit(boolean vehicleEdit) {
        this.vehicleEdit = vehicleEdit;
    }

    public boolean isVehicleView() {
        return vehicleView;
    }

    public void setVehicleView(boolean vehicleView) {
        this.vehicleView = vehicleView;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public WebUserRole getWebUserRole() {
        return webUserRole;
    }

    public void setWebUserRole(WebUserRole webUserRole) {
        this.webUserRole = webUserRole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Privilege)) {
            return false;
        }
        Privilege other = (Privilege) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.Privilage[ id=" + id + " ]";
    }
}
