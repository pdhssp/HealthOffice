/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Designation;
import gov.sp.health.entity.Institution;
import gov.sp.health.facade.InstitutionCadreFacade;
import gov.sp.health.entity.InstitutionCadre;
import gov.sp.health.entity.ItemCategory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class InstitutionCadreController implements Serializable {

    @EJB
    private InstitutionCadreFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private InstitutionCadre current;
    private List<InstitutionCadre> items = null;
    Institution institution;
    Designation designation;
    Long caderCount;
    Long maleIn;
    Long femaleIn;
    Long totalIn;
    Long vacantCount;
    Date carderDate;
    Integer carderYear;
    Integer carderMonth;

    public Integer getCarderYear() {
        return carderYear;
    }

    public void setCarderYear(Integer carderYear) {
        this.carderYear = carderYear;
    }

    public Integer getCarderMonth() {
        return carderMonth;
    }

    public void setCarderMonth(Integer carderMonth) {
        this.carderMonth = carderMonth;
    }

    public Date getCarderDate() {
        return carderDate;
    }

    public void setCarderDate(Date carderDate) {
        this.carderDate = carderDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(carderDate);
        setCarderYear(cal.get(Calendar.YEAR));
        setCarderMonth(cal.get(Calendar.MONTH));
        recreateItems();
    }

    public Long getMaleIn() {
        return maleIn;
    }

    public void setMaleIn(Long maleIn) {
        this.maleIn = maleIn;
    }

    public Long getFemaleIn() {
        return femaleIn;
    }

    public void setFemaleIn(Long femaleIn) {
        this.femaleIn = femaleIn;
    }

    public Long getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(Long totalIn) {
        this.totalIn = totalIn;
    }

    public Long getVacantCount() {
        return vacantCount;
    }

    public void setVacantCount(Long vacantCount) {
        this.vacantCount = vacantCount;
    }

    public void addDesignationToInstitution() {
        System.out.println("Adding");
        if (getDesignation() == null) {
            JsfUtil.addErrorMessage("Please select a designation");
            return;
        }
        if (getInstitution() == null) {
            JsfUtil.addErrorMessage("Please select an institution type");
            return;
        }
        if (caderCount == null || caderCount == 0) {
            JsfUtil.addErrorMessage("Please enter the count");
            return;
        }
        System.out.println("all variables ok to add");
        InstitutionCadre itc = new InstitutionCadre();
        itc.setDesignation(getDesignation());
        itc.setInstitution(getInstitution());
        itc.setMaleIn(getMaleIn());
        itc.setFemaleIn(getFemaleIn());
        itc.setMaleAndFemaleIn(getTotalIn());
        itc.setApproved(getCaderCount());
        itc.setVac(getVacantCount());
        itc.setIntMonth(carderMonth);
        itc.setIntYear(carderYear);
        itc.setCreatedAt(Calendar.getInstance().getTime());
        itc.setCreater(sessionController.loggedUser);
        getEjbFacade().create(itc);
        JsfUtil.addSuccessMessage("Added Successfully");
        setDesignation(null);
        setCaderCount(null);

    }

    public void removeDesignationFromInstitution() {
        if (current == null) {
            JsfUtil.addErrorMessage("Nothing to delete");
            return;
        }
        current.setRetired(Boolean.TRUE);
        current.setRetiredAt(Calendar.getInstance().getTime());
        current.setRetirer(sessionController.loggedUser);
        getEjbFacade().edit(current);
    }

    public InstitutionCadreController() {
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
        recreateItems();
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Long getCaderCount() {
        return caderCount;
    }

    public void setCaderCount(Long caderCount) {
        this.caderCount = caderCount;
    }

    public InstitutionCadre getCurrent() {
        if (current == null) {
            current = new InstitutionCadre();
        }
        return current;
    }

    public void setCurrent(InstitutionCadre current) {
        this.current = current;
    }

    private InstitutionCadreFacade getFacade() {
        return ejbFacade;
    }

    public String saveAll() {
        for (InstitutionCadre ic : items) {
            if (ic != null) {
                if (ic.getId() == null || ic.getId() == 0) {
                    getFacade().create(ic);
                } else {
                    getFacade().edit(ic);
                }
                JsfUtil.addSuccessMessage("All Saved");
            } else {
                JsfUtil.addErrorMessage("Nothing to Save");
            }

        }
        recreateItems();
        return "";
    }

    public List<InstitutionCadre> getItems() {
        if (items == null) {
            String sql;
            if (getInstitution() == null) {
                return new ArrayList<InstitutionCadre>();
            }
            sql = "Select d From InstitutionCadre d where d.retired=false and d.institution.id = " + getInstitution().getId() + " and d.intYear = " + getCarderYear() + " and d.intMonth = " + getCarderMonth() + " order by d.name";
            items = getFacade().findBySQL(sql);
        }
        return items;
    }

    public void recreateItems() {
        items = null;
    }

    public InstitutionCadreFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InstitutionCadreFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = InstitutionCadre.class)
    public static class InstitutionCadreControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InstitutionCadreController controller = (InstitutionCadreController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "institutionCadreController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        /**
         *
         * @param facesContext
         * @param component
         * @param object
         * @return
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof InstitutionCadre) {
                InstitutionCadre o = (InstitutionCadre) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InstitutionCadreController.class.getName());
            }
        }
    }
}
