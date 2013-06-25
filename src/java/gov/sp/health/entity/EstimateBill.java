/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Temporal;

/**
 *
 * @author buddhika
 */
@Entity
@Inheritance
public class EstimateBill extends Bill implements Serializable {

    @Temporal(javax.persistence.TemporalType.DATE)
    Date fromDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date toDate;
    Boolean annualEstimate;
    Boolean supplimentary;
    Boolean recorrection;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Boolean getAnnualEstimate() {
        return annualEstimate;
    }

    public void setAnnualEstimate(Boolean annualEstimate) {
        this.annualEstimate = annualEstimate;
    }

    public Boolean getSupplimentary() {
        return supplimentary;
    }

    public void setSupplimentary(Boolean supplimentary) {
        this.supplimentary = supplimentary;
    }

    public Boolean getRecorrection() {
        return recorrection;
    }

    public void setRecorrection(Boolean recorrection) {
        this.recorrection = recorrection;
    }
    
    
    
    
}
