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
import gov.sp.health.entity.InstitutionType;
import gov.sp.health.facade.InstitutionTypeCadreFacade;
import gov.sp.health.entity.InstitutionTypeCadre;
import gov.sp.health.entity.ItemCategory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
public final class InstitutionTypeCadreController implements Serializable {

    @EJB
    private InstitutionTypeCadreFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private InstitutionTypeCadre current;
    private List<InstitutionTypeCadre> items = null;
    InstitutionType institutionType;
    Designation designation;
    Long caderCount;

    public void fillInsTypeCarder(){
        
    }
    
    public void addDesignationToInstitutionType() {
        System.out.println("Adding");
        if (getDesignation() == null) {
            JsfUtil.addErrorMessage("Please select a designation");
            return;
        }
        if (getInstitutionType() == null) {
            JsfUtil.addErrorMessage("Please select an institution type");
            return;
        }
        if (caderCount == null || caderCount == 0) {
            JsfUtil.addErrorMessage("Please enter the count");
            return;
        }
        System.out.println("all variables ok to add");
        InstitutionTypeCadre itc = new InstitutionTypeCadre();
        itc.setDesignation(getDesignation());
        itc.setInstitutionType(getInstitutionType());
        itc.setCadreCount(getCaderCount());
        itc.setCreatedAt(Calendar.getInstance().getTime());
        itc.setCreater(sessionController.loggedUser);
        getEjbFacade().create(itc);
        JsfUtil.addSuccessMessage("Added Successfully");
        setDesignation(null);
        setCaderCount(null);

    }

    public void removeDesignationFromInstitutionType() {
        if (current == null) {
            JsfUtil.addErrorMessage("Nothing to delete");
            return;
        }
        current.setRetired(Boolean.TRUE);
        current.setRetiredAt(Calendar.getInstance().getTime());
        current.setRetirer(sessionController.loggedUser);
        getEjbFacade().edit(current);
    }

    public InstitutionTypeCadreController() {
    }

    public InstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
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

      public InstitutionTypeCadre getCurrent() {
        if (current == null) {
            current = new InstitutionTypeCadre();
        }
        return current;
    }

    public void setCurrent(InstitutionTypeCadre current) {
        this.current = current;
    }

    private InstitutionTypeCadreFacade getFacade() {
        return ejbFacade;
    }

    public List<InstitutionTypeCadre> getItems() {
        String sql;
        if(getInstitutionType()==null){
            return new ArrayList<InstitutionTypeCadre>();
        }
        sql = "Select d From InstitutionTypeCadre d where d.retired=false and d.institutionType.id = " + getInstitutionType().getId() + " order by d.name";
        items = getFacade().findBySQL(sql);
        return items;
    }

    public InstitutionTypeCadreFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InstitutionTypeCadreFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = InstitutionTypeCadre.class)
    public static class InstitutionTypeCadreControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InstitutionTypeCadreController controller = (InstitutionTypeCadreController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "institutionTypeCadreController");
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
            if (object instanceof InstitutionTypeCadre) {
                InstitutionTypeCadre o = (InstitutionTypeCadre) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InstitutionTypeCadreController.class.getName());
            }
        }
    }
}
