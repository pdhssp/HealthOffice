/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.InstitutionTypeCadre;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Buddhika
 */
@Stateless
public class InstitutionTypeCadreFacade extends AbstractFacade<InstitutionTypeCadre> {
    @PersistenceContext(unitName = "HOPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstitutionTypeCadreFacade() {
        super(InstitutionTypeCadre.class);
    }
    
}
