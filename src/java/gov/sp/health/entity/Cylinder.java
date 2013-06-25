/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import gov.sp.health.data.CylinderType;
import gov.sp.health.data.Gas;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/**
 *
 * @author buddhika
 */
@Entity
@Inheritance
public class Cylinder extends Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Enumerated(EnumType.STRING)
    Gas gas;
    @Enumerated(EnumType.STRING)
    CylinderType cylinderType;
    Double gasVolume;

    public Double getGasVolume() {
        return gasVolume;
    }

    public void setGasVolume(Double gasVolume) {
        this.gasVolume = gasVolume;
    }

    
    
    public Gas getGas() {
        return gas;
    }

    public void setGas(Gas gas) {
        this.gas = gas;
    }

    public CylinderType getCylinderType() {
        return cylinderType;
    }

    public void setCylinderType(CylinderType cylinderType) {
        this.cylinderType = cylinderType;
    }
}
