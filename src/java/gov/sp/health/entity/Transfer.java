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
public class Transfer extends Encounter implements Serializable {

    @Temporal(javax.persistence.TemporalType.DATE)
    Date fromDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date fromTime;
    Boolean initiated;
    @ManyToOne
    Institution fromInstitution;
    @ManyToOne
    Unit fromUnit;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date toDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date toTime;
    Boolean completed;
    @ManyToOne
    Institution toInstitution;
    @ManyToOne
    Unit toUnit;
    @ManyToOne
    Staff fromStaff;
    @ManyToOne
    Staff toStaff;

    public Staff getFromStaff() {
        return fromStaff;
    }

    public void setFromStaff(Staff fromStaff) {
        this.fromStaff = fromStaff;
    }

    public Staff getToStaff() {
        return toStaff;
    }

    public void setToStaff(Staff toStaff) {
        this.toStaff = toStaff;
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

    public Institution getFromInstitution() {
        return fromInstitution;
    }

    public void setFromInstitution(Institution fromInstitution) {
        this.fromInstitution = fromInstitution;
    }

    public Unit getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(Unit fromUnit) {
        this.fromUnit = fromUnit;
    }

    public Boolean getInitiated() {
        return initiated;
    }

    public void setInitiated(Boolean initiated) {
        this.initiated = initiated;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
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

    public Institution getToInstitution() {
        return toInstitution;
    }

    public void setToInstitution(Institution toInstitution) {
        this.toInstitution = toInstitution;
    }

    public Unit getToUnit() {
        return toUnit;
    }

    public void setToUnit(Unit toUnit) {
        this.toUnit = toUnit;
    }
}
