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
public class InstitutionCadre implements Serializable {
    private static final Long serialVersionUID = 1L;
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
    Designation designation;
    @ManyToOne
    Institution institution;
    
    
    Integer intYear;
    Integer intMonth;
    
    
    Long maleAndFemaleIn;
    Long maleIn;
    Long femaleIn;
    Long approved;
    Long vac;

    public Integer getIntYear() {
        return intYear;
    }

    public void setIntYear(Integer intYear) {
        this.intYear = intYear;
    }

    public Integer getIntMonth() {
        return intMonth;
    }

    public void setIntMonth(Integer intMonth) {
        this.intMonth = intMonth;
    }

    
    
    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
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

    public Long getApproved() {
        return approved;
    }

    public void setApproved(Long approved) {
        this.approved = approved;
        calculateCarders();
    }

    public Long getFemaleIn() {
        return femaleIn;
    }

    public void setFemaleIn(Long femaleIn) {
        this.femaleIn = femaleIn;
        calculateCarders();
    }

    public Long getMaleAndFemaleIn() {
        return maleAndFemaleIn;
    }

    public void setMaleAndFemaleIn(Long maleAndFemaleIn) {
        this.maleAndFemaleIn = maleAndFemaleIn;
        setVac(getApproved() - getMaleAndFemaleIn());
    }

    public void calculateCarders(){
        setMaleAndFemaleIn(getMaleIn() + getFemaleIn());
    }
    
    public Long getMaleIn() {
        return maleIn;
    }

    public void setMaleIn(Long maleIn) {
        this.maleIn = maleIn;
        calculateCarders();
    }

    public Long getVac() {
        return vac;
    }

    public void setVac(Long vac) {
        this.vac = vac;
        calculateCarders();
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstitutionCadre)) {
            return false;
        }
        InstitutionCadre other = (InstitutionCadre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
