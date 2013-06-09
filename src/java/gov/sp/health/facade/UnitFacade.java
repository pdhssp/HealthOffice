/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.Unit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author IT
 */
@Stateless
public class UnitFacade extends AbstractFacade<Unit> {
    @PersistenceContext(unitName = "HOPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UnitFacade() {
        super(Unit.class);
    }
    
    /**
     * 
     * This is a comment
     * 
     * with a second line
     * 
     * 
     * this is a thired line 
     */
    
    
    
    
}
