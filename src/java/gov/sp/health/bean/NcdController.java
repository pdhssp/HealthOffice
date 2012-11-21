/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.facade.PersonFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author buddhika
 */
@ManagedBean
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
