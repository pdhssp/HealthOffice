/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.ConsumableItemCategory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author IT
 */
@Stateless
public class ConsumableItemCategoryFacade extends AbstractFacade<ConsumableItemCategory> {

    @PersistenceContext(unitName = "HOPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumableItemCategoryFacade() {
        super(ConsumableItemCategory.class);
    }
}
