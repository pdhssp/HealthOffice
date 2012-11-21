/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Bill;
import gov.sp.health.entity.Location;
import gov.sp.health.entity.Unit;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Buddhika
 */
@ManagedBean
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
