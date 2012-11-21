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
public class Ampp extends PharmaceuticalItem implements Serializable {
    @ManyToOne
    Vmpp vmpp;
    @ManyToOne
    Amp amp;
    @ManyToOne
    PackUnit packUnit;
    Double issueUnitsPerPack;
    

    public Vmpp getVmpp() {
        return vmpp;
    }

    public void setVmpp(Vmpp vmpp) {
        this.vmpp = vmpp;
    }

    public Amp getAmp() {
        return amp;
    }

    public void setAmp(Amp amp) {
        this.amp = amp;
    }

    public Double getIssueUnitsPerPack() {
        return issueUnitsPerPack;
    }

    public void setIssueUnitsPerPack(Double issueUnitsPerPack) {
        this.issueUnitsPerPack = issueUnitsPerPack;
    }

    public PackUnit getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(PackUnit packUnit) {
        this.packUnit = packUnit;
    }
    
    
}
