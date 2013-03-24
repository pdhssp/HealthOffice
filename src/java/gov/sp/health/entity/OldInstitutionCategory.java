/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;

/**
 *
 * @author IT
 */
@Entity
@Inheritance
public class OldInstitutionCategory extends Category implements Serializable {

    public OldInstitutionCategory() {
    }
    
}
