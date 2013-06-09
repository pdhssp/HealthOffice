/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;

/**
 *
 * @author Buddhika
 */
@Entity
@Inheritance
public class Vmp extends PharmaceuticalItem implements Serializable {
    Integer LevelNo;
    String whNo;

    public Integer getLevelNo() {
        return LevelNo;
    }

    public void setLevelNo(Integer LevelNo) {
        this.LevelNo = LevelNo;
    }

    public String getWhNo() {
        return whNo;
    }

    public void setWhNo(String whNo) {
        this.whNo = whNo;
    }
    
    
}
