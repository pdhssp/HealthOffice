/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;

/**
 *
 * @author Buddhika
 * 
 */
@Entity
@Inheritance
public class ReceiveBill extends Bill implements Serializable {

    @ManyToOne
    IssueBill issueBill;
    
    public ReceiveBill() {
    }

    
}
