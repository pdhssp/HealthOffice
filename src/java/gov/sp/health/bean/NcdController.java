/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.facade.PersonFacade;
import java.io.Serializable;
import javax.ejb.EJB;

import javax.faces.bean.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author buddhika
 */
@Named
@SessionScoped
public class NcdController implements Serializable{

    @EJB
    PersonFacade personFacade;
    
    /**
     * Creates a new instance of NcdController
     */
    public NcdController() {
    }
}
