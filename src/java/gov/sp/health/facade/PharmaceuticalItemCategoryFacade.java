/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.PharmaceuticalItemCategory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author IT
 */
@Stateless
public class PharmaceuticalItemCategoryFacade extends AbstractFacade<PharmaceuticalItemCategory> {
    @PersistenceContext(unitName = "HOPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PharmaceuticalItemCategoryFacade() {
        super(PharmaceuticalItemCategory.class);
    }
    
}
