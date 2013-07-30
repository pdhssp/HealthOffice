/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author buddhika
 */
@Entity
@Inheritance
public class EstimateItem extends BillItem implements Serializable {

    @Temporal(javax.persistence.TemporalType.DATE)
    Date expectedFirstDeliveryDate;
    
    Double consolidatedQuantity;
    Double forcastedMonthlyConsumptionForEstimatedYear;
    Double forcastedMonthlyConsumptionForTheNextToEstimatedYear;

    
    public Date getExpectedFirstDeliveryDate() {
        return expectedFirstDeliveryDate;
    }

    public void setExpectedFirstDeliveryDate(Date expectedFirstDeliveryDate) {
        this.expectedFirstDeliveryDate = expectedFirstDeliveryDate;
    }


    public Double getConsolidatedQuantity() {
        return consolidatedQuantity;
    }

    public void setConsolidatedQuantity(Double consolidatedQuantity) {
        this.consolidatedQuantity = consolidatedQuantity;
    }

    public Double getForcastedMonthlyConsumptionForEstimatedYear() {
        return forcastedMonthlyConsumptionForEstimatedYear;
    }

    public void setForcastedMonthlyConsumptionForEstimatedYear(Double forcastedMonthlyConsumptionForEstimatedYear) {
        this.forcastedMonthlyConsumptionForEstimatedYear = forcastedMonthlyConsumptionForEstimatedYear;
    }

    public Double getForcastedMonthlyConsumptionForTheNextToEstimatedYear() {
        return forcastedMonthlyConsumptionForTheNextToEstimatedYear;
    }

    public void setForcastedMonthlyConsumptionForTheNextToEstimatedYear(Double forcastedMonthlyConsumptionForTheNextToEstimatedYear) {
        this.forcastedMonthlyConsumptionForTheNextToEstimatedYear = forcastedMonthlyConsumptionForTheNextToEstimatedYear;
    }
    
    
    
}
