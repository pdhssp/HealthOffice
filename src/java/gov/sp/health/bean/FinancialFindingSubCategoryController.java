/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.FinancialFindingCategory;
import gov.sp.health.facade.FinancialFindingCategoryFacade;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author buddhika
 */
@Named
@RequestScoped
public class FinancialFindingSubCategoryController {

    @EJB
    FinancialFindingCategoryFacade cccFacade;
    FinancialFindingCategory cat;
    FinancialFindingCategory subCat;
    String txtName;
    List<FinancialFindingCategory> cats;
    List<FinancialFindingCategory> subCats;
    long removeId;

    /**
     * Creates a new instance of FinancialFindingSubCategoryController
     */
    public FinancialFindingSubCategoryController() {
    }

    public long getRemoveId() {
        return removeId;
    }

    public void setRemoveId(long removeId) {
        this.removeId = removeId;
    }

    public String getTxtName() {
        return txtName;
    }

    public List<FinancialFindingCategory> getCats() {
        return getCccFacade().findBySQL("select c from FinancialFindingCategory c where c.parentCategory is null and c.retired = false order by c.name   ");
    }

    public void setCats(List<FinancialFindingCategory> cats) {
        this.cats = cats;
    }

    public List<FinancialFindingCategory> getSubCats() {
        if (cat == null) {
            return null;
        }
        return getCccFacade().findBySQL("select c from FinancialFindingCategory c where c.parentCategory.id = " + cat.getId() + " and c.retired = false order by c.name   ");
    }

    public void setSubCats(List<FinancialFindingCategory> subCats) {
        this.subCats = subCats;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public void removeSubCat() {
        if (subCat == null) {
            JsfUtil.addErrorMessage("Nothing to Remove");
            return;
        }
        subCat.setRetired(true);
        JsfUtil.addSuccessMessage("Removed");
        subCat = null;
    }

    public void saveSubCat() {
        if (cat == null) {
            JsfUtil.addErrorMessage("Please select a category");
            return;
        }
        subCat = new FinancialFindingCategory();
        subCat.setParentCategory(cat);
        subCat.setName(txtName);
        cccFacade.create(subCat);
        subCat = new FinancialFindingCategory();
        JsfUtil.addSuccessMessage("Saved");
    }

    public FinancialFindingCategoryFacade getCccFacade() {
        return cccFacade;
    }

    public void setCccFacade(FinancialFindingCategoryFacade cccFacade) {
        this.cccFacade = cccFacade;
    }

    public FinancialFindingCategory getCat() {
        return cat;
    }

    public void setCat(FinancialFindingCategory cat) {
        this.cat = cat;
    }

    public FinancialFindingCategory getSubCat() {
        return subCat;
    }

    public void setSubCat(FinancialFindingCategory subCat) {
        this.subCat = subCat;
    }

}
