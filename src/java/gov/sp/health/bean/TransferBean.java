/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Bill;
import gov.sp.health.entity.Location;
import gov.sp.health.entity.Unit;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Buddhika
 */
@Named
@SessionScoped
public class TransferBean  implements Serializable {

    Bill bill;
    Unit unit;
    Location location;
    
    
    /**
     * Creates a new instance of TransferBean
     */
    public TransferBean() {
    }

    public Bill getBill() {

        return bill;
    }

    public void setBill(Bill bill) {

        this.bill = bill;
    }
    
    
    
}
