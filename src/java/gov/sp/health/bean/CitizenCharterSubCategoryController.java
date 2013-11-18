/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.CitizenCharterCategory;
import gov.sp.health.facade.CitizenCharterCategoryFacade;
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
public class CitizenCharterSubCategoryController {

    @EJB
    CitizenCharterCategoryFacade cccFacade;
    CitizenCharterCategory cat;
    CitizenCharterCategory subCat;
    String txtName;
    List<CitizenCharterCategory> cats;
    List<CitizenCharterCategory> subCats;
    long removeId;

    /**
     * Creates a new instance of CitizenCharterSubCategoryController
     */
    public CitizenCharterSubCategoryController() {
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

    public List<CitizenCharterCategory> getCats() {
        return getCccFacade().findBySQL("select c from CitizenCharterCategory c where c.parentCategory is null and c.retired = false order by c.name   ");
    }

    public void setCats(List<CitizenCharterCategory> cats) {
        this.cats = cats;
    }

    public List<CitizenCharterCategory> getSubCats() {
        if (cat == null) {
            return null;
        }
        return getCccFacade().findBySQL("select c from CitizenCharterCategory c where c.parentCategory.id = " + cat.getId() + " and c.retired = false order by c.name   ");
    }

    public void setSubCats(List<CitizenCharterCategory> subCats) {
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
        subCat = new CitizenCharterCategory();
        subCat.setParentCategory(cat);
        subCat.setName(txtName);
        cccFacade.create(subCat);
        subCat = new CitizenCharterCategory();
        JsfUtil.addSuccessMessage("Saved");
    }

    public CitizenCharterCategoryFacade getCccFacade() {
        return cccFacade;
    }

    public void setCccFacade(CitizenCharterCategoryFacade cccFacade) {
        this.cccFacade = cccFacade;
    }

    public CitizenCharterCategory getCat() {
        return cat;
    }

    public void setCat(CitizenCharterCategory cat) {
        this.cat = cat;
    }

    public CitizenCharterCategory getSubCat() {
        return subCat;
    }

    public void setSubCat(CitizenCharterCategory subCat) {
        this.subCat = subCat;
    }

}
