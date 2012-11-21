/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.CitizenCharterCategory;
import gov.sp.health.entity.FinancialFindingCategory;
import gov.sp.health.facade.CitizenCharterCategoryFacade;
import gov.sp.health.facade.CitizenCharterFacade;
import gov.sp.health.facade.ExpenseFacade;
import gov.sp.health.facade.FinancialFindingCategoryFacade;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author buddhika
 */
@ManagedBean
@RequestScoped
public class TemBean {
@EJB
        ExpenseFacade expenseFacade;
@EJB
        CitizenCharterCategoryFacade cccFacade;
@EJB
        FinancialFindingCategoryFacade ffFacade;


    
    
    String temTxt;
    
    public void saveAsExpense(){
        FinancialFindingCategory ffc = new FinancialFindingCategory();
        ffc.setName(temTxt);
        ffFacade.create(ffc);
        JsfUtil.addSuccessMessage("Saved FFC");
        temTxt = "";
    }
    

    public FinancialFindingCategoryFacade getFfFacade() {
        return ffFacade;
    }

    public void setFfFacade(FinancialFindingCategoryFacade ffFacade) {
        this.ffFacade = ffFacade;
    }
    
    
    
    public void saveAsCCC(){
        CitizenCharterCategory ccc = new CitizenCharterCategory();
        ccc.setName(temTxt);
        cccFacade.create(ccc);
        JsfUtil.addSuccessMessage("Saved CCC");
        temTxt="";
    }

    public ExpenseFacade getExpenseFacade() {
        return expenseFacade;
    }

    public void setExpenseFacade(ExpenseFacade expenseFacade) {
        this.expenseFacade = expenseFacade;
    }

    public CitizenCharterCategoryFacade getCccFacade() {
        return cccFacade;
    }

    public void setCccFacade(CitizenCharterCategoryFacade cccFacade) {
        this.cccFacade = cccFacade;
    }

    public String getTemTxt() {
        return temTxt;
    }

    public void setTemTxt(String temTxt) {
        this.temTxt = temTxt;
    }

    
    
    
    /**
     * Creates a new instance of TemBean
     */
    public TemBean() {
    }
    
    public String toIndex(){
        return "index";
    }
    
}
