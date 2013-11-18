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
import gov.sp.health.entity.InstitutionTypeCadre;
import gov.sp.health.entity.ItemCategory;
import gov.sp.health.facade.InstitutionTypeCadreFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class InstitutionCadreController implements Serializable {

    @EJB
    private InstitutionCadreFacade ejbFacade;
    @EJB
    InstitutionTypeCadreFacade typeCarderFacade;
    @Inject
    SessionController sessionController;
    private InstitutionCadre current;
    private List<InstitutionCadre> items = null;
    Institution institution;
    Designation designation;
    long caderCount;
    long maleIn;
    long femaleIn;
    long totalIn;
    long vacantCount;
    Date carderDate;
    int carderYear;
    int carderMonth;
    int carderYearLast;
    int carderMonthLast;
    long institutionMaleInCount;
    long institutionFemaleInCount;
    long institutionTotalInCount;
    long institutionVacantInCount;
    long institutionApprovedCount;

    public long getMaleIn() {
        return maleIn;
    }

    public void setMaleIn(long maleIn) {
        this.maleIn = maleIn;
    }

    public long getFemaleIn() {
        return femaleIn;
    }

    public void setFemaleIn(long femaleIn) {
        this.femaleIn = femaleIn;
    }

    public long getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(long totalIn) {
        this.totalIn = totalIn;
    }

    public long getVacantCount() {
        return vacantCount;
    }

    public void setVacantCount(long vacantCount) {
        this.vacantCount = vacantCount;
    }

    public int getCarderYear() {
        return carderYear;
    }

    public void setCarderYear(int carderYear) {
        this.carderYear = carderYear;
    }

    public int getCarderMonth() {
        return carderMonth;
    }

    public void setCarderMonth(int carderMonth) {
        this.carderMonth = carderMonth;
    }

    public int getCarderYearLast() {
        return carderYearLast;
    }

    public void setCarderYearLast(int carderYearLast) {
        this.carderYearLast = carderYearLast;
    }

    public int getCarderMonthLast() {
        return carderMonthLast;
    }

    public void setCarderMonthLast(int carderMonthLast) {
        this.carderMonthLast = carderMonthLast;
    }

    public long getInstitutionMaleInCount() {
        return institutionMaleInCount;
    }

    public void setInstitutionMaleInCount(long institutionMaleInCount) {
        this.institutionMaleInCount = institutionMaleInCount;
    }

    public long getInstitutionFemaleInCount() {
        return institutionFemaleInCount;
    }

    public void setInstitutionFemaleInCount(long institutionFemaleInCount) {
        this.institutionFemaleInCount = institutionFemaleInCount;
    }

    public long getInstitutionTotalInCount() {
        return institutionTotalInCount;
    }

    public void setInstitutionTotalInCount(long institutionTotalInCount) {
        this.institutionTotalInCount = institutionTotalInCount;
    }

    public long getInstitutionVacantInCount() {
        return institutionVacantInCount;
    }

    public void setInstitutionVacantInCount(long institutionVacantInCount) {
        this.institutionVacantInCount = institutionVacantInCount;
    }

    public long getInstitutionApprovedCount() {
        return institutionApprovedCount;
    }

    public void setInstitutionApprovedCount(long institutionApprovedCount) {
        this.institutionApprovedCount = institutionApprovedCount;
    }

    public void setCarderYearLast(Integer carderYearLast) {
        this.carderYearLast = carderYearLast;
    }

    public void setCarderMonthLast(Integer carderMonthLast) {
        this.carderMonthLast = carderMonthLast;
    }

    public InstitutionTypeCadreFacade getTypeCarderFacade() {
        return typeCarderFacade;
    }

    public void setTypeCarderFacade(InstitutionTypeCadreFacade typeCarderFacade) {
        this.typeCarderFacade = typeCarderFacade;
    }

    public void fillInsTypeCarder() {
        String sql;
        if (getInstitution() == null) {
            JsfUtil.addErrorMessage("Please select the institute");
            return;
        }
        if (getInstitution().getInstitutionType() == null) {
            JsfUtil.addErrorMessage("Please add a type to the institution " + getInstitution().getName());
            return;
        }
        for (InstitutionCadre ic : items) {
            ic.setRetired(true);
            //TODO:Add retireer properties
            getFacade().edit(ic);
        }
        sql = "Select d From InstitutionTypeCadre d where d.retired=false and d.institutionType.id = " + getInstitution().getInstitutionType().getId() + " order by d.name";
        List<InstitutionTypeCadre> typItems = getTypeCarderFacade().findBySQL(sql);
        for (InstitutionTypeCadre itc : typItems) {
            InstitutionCadre ic = new InstitutionCadre();
            ic.setDesignation(itc.getDesignation());
            ic.setInstitution(getInstitution());
            ic.setMaleAndFemaleIn(itc.getCadreCount());
            ic.setApproved(itc.getCadreCount());
            ic.setIntMonth(carderMonth);
            ic.setIntYear(carderYear);
            ic.setCreatedAt(Calendar.getInstance().getTime());
            ic.setCreater(sessionController.loggedUser);
            getEjbFacade().create(ic);
        }
        recreateItems();
    }

    public void fillLastMonthCarder() {
        String sql;
        if (getInstitution() == null) {
            items = new ArrayList<InstitutionCadre>();
        }
        for (InstitutionCadre ic : items) {
            ic.setRetired(true);
            //TODO:Add retireer properties
            getFacade().edit(ic);
        }
        sql = "Select d From InstitutionCadre d where d.retired=false and d.institution.id = " + getInstitution().getId() + " and d.intYear = " + getCarderYearLast() + " and d.intMonth = " + getCarderMonthLast() + " order by d.name";
        List<InstitutionCadre> typItems = getFacade().findBySQL(sql);
        for (InstitutionCadre itc : typItems) {
            InstitutionCadre ic = new InstitutionCadre();
            ic.setDesignation(itc.getDesignation());
            ic.setInstitution(getInstitution());
            ic.setMaleIn(itc.getMaleIn());
            ic.setFemaleIn(itc.getFemaleIn());
            ic.setVac(itc.getVac());
            ic.setMaleAndFemaleIn(itc.getMaleAndFemaleIn());
            ic.setApproved(itc.getApproved());
            ic.setIntMonth(carderMonth);
            ic.setIntYear(carderYear);
            ic.setCreatedAt(Calendar.getInstance().getTime());
            ic.setCreater(sessionController.loggedUser);
            getEjbFacade().create(ic);
        }
        recreateItems();
    }

    public void setCarderYear(Integer carderYear) {
        this.carderYear = carderYear;
    }

    public void setCarderMonth(Integer carderMonth) {
        this.carderMonth = carderMonth;
    }

    public Date getCarderDate() {
        return carderDate;
    }

    public void setCarderDate(Date carderDate) {
        System.out.println("1");
        this.carderDate = carderDate;
        System.out.println("2");
        Calendar cal = Calendar.getInstance();
        System.out.println("3");
        cal.setTime(carderDate);
        System.out.println("4");
        Integer temY = cal.get(Calendar.YEAR);
        System.out.println("5");
        Integer temM = cal.get(Calendar.MONTH);
        System.out.println("6");
        setCarderYear(temY);
        System.out.println("7");
        setCarderMonth(temM);
        System.out.println("8");
        if (temM == 1) {
            System.out.println("9");
            temM = 11;
            temY = temY - 1;
        } else {
            System.out.println("10");
            temM = temM - 1;
        }
        System.out.println("11");
        setCarderMonthLast(temM);
        System.out.println("12");
        setCarderYearLast(temY);
        System.out.println("13");
        System.out.println("This month is " + getCarderYear() + " " + getCarderMonth());
        System.out.println("This month is " + getCarderYearLast() + " " + getCarderMonthLast());
        recreateItems();
    }

    public void setMaleIn(Long maleIn) {
        this.maleIn = maleIn;
    }

    public void setFemaleIn(Long femaleIn) {
        this.femaleIn = femaleIn;
    }

    public void setTotalIn(Long totalIn) {
        this.totalIn = totalIn;
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
        if (caderCount == 0l) {
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
//        JsfUtil.addSuccessMessage("Added Successfully");
        setDesignation(null);
        setCaderCount(0);
        System.out.println("saved for " + itc.getIntYear() + " " + itc.getIntMonth());
        items = null;
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
        recreateItems();
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

    public long getCaderCount() {
        return caderCount;
    }

    public void setCaderCount(long caderCount) {
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

    public void calculateCountr() {
        long t = 0l;
        long mi = 0l;
        long fi = 0l;
        long a = 0l;
        long v = 0l;
        for (InstitutionCadre c : items) {
            t = t + c.getMaleAndFemaleIn();
            mi = mi + c.getMaleIn();
            fi = fi + c.getFemaleIn();
            a = a + c.getApproved();
            v = v + c.getVac();
        }
        institutionMaleInCount = mi;
        institutionFemaleInCount = fi;
        institutionVacantInCount = v;
        institutionApprovedCount = a;
        institutionTotalInCount = t;
    }

    public List<InstitutionCadre> getItems() {
        if (items == null) {
            String sql;
            if (getInstitution() == null) {
                return new ArrayList<InstitutionCadre>();
            }
            sql = "Select d From InstitutionCadre d where d.retired=false and d.institution.id = " + getInstitution().getId() + " and d.intYear = " + getCarderYear() + " and d.intMonth = " + getCarderMonth() + " order by d.name";
            items = getFacade().findBySQL(sql);
            calculateCountr();
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
