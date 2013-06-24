/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
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
public class Amp extends PharmaceuticalItem implements Serializable {
    @ManyToOne
    Vmp vmp;
    
    @ManyToOne
    IssueUnit issueUnit;

    public IssueUnit getIssueUnit() {
        return issueUnit;
    }

    public void setIssueUnit(IssueUnit issueUnit) {
        this.issueUnit = issueUnit;
    }
    
    
    

    public Vmp getVmp() {
        return vmp;
    }

    public void setVmp(Vmp vmp) {
        this.vmp = vmp;
    }
    
    
}
