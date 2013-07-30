/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.InInventoryBill;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author IT
 */
@Stateless
public class InInventoryBillFacade extends AbstractFacade<InInventoryBill> {
    @PersistenceContext(unitName = "HOPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InInventoryBillFacade() {
        super(InInventoryBill.class);
    }
    
    
    
    
    
    
}
