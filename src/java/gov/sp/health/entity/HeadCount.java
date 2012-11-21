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
 * @author buddhika
 */
@Entity
@Inheritance
public class HeadCount extends Finding implements Serializable {

    public HeadCount() {
    }


}
