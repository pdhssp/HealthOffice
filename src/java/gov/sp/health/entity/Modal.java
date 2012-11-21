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
 */
@Entity
@Inheritance
public class Modal extends ItemCategory implements Serializable {

    @ManyToOne
    Make make;
    
    
    public Modal() {
        
    }

    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

    
    
}
