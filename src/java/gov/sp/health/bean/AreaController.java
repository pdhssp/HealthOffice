package gov.sp.health.bean;
// Facades
import gov.sp.health.entity.*;
import gov.sp.health.facade.AreaFacade;
import gov.sp.health.facade.DpdhsFacade;
import gov.sp.health.facade.GnFacade;
import gov.sp.health.facade.MohFacade;
import gov.sp.health.facade.PhiFacade;
import gov.sp.health.facade.PhmFacade;
import gov.sp.health.facade.PopulationFacade;
import gov.sp.health.facade.ProvinceFacade;
import java.io.Serializable;
import java.util.ArrayList;
// Entities
// Other
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.component.datalist.DataList;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@RequestScoped
public class AreaController  implements Serializable {

    /**
     * EJBs
     */
    @EJB
    ProvinceFacade provinceFacade;
    @EJB
    DpdhsFacade dpdhsFacade;
    @EJB
    MohFacade mohFacade;
    @EJB
    PhiFacade phiFacade;
    @EJB
    PhmFacade phmFacade;
    @EJB
    GnFacade gnFacade;
    @EJB
    AreaFacade ejbFacade;
    @EJB
    PopulationFacade popFacade;
    @EJB
    AreaFacade areaFacade;
    /**
     * Lists
     */
    DataModel<Province> provinces;
    DataModel<DpdhsArea> dpdhsAreas;
    DataModel<MohArea> mohAreas;
    DataModel<PhiArea> phiAreas;
    DataModel<PhmArea> phmAreas;
    DataModel<GnArea> gnAreas;
    DataModel<Area> areas;
    //
    DpdhsArea dpdhsArea;
    MohArea mohArea;
    PhiArea phiArea;
    PhmArea phmArea;
    GnArea gnArea;
    Province province;
    /**
     * IDs
     */
    Long provinceId;
    Long dpdhsId;
    Long mohId;
    Long phiId;
    Long phmId;
    Long gnId;
    /**
     *
     *
     */
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;

    /**
     *
     */
    /**
     * Initiate Area Controller
     */
    public AreaController() {
    }

    public DataModel<Area> getAreas() {
        return new ListDataModel<Area>(getAreaFacade().findBySQL("SELECT a FROM Area a WHERE a.retired=false ORDER BY a.name"));
    }

    public void setAreas(DataModel<Area> areas) {
        this.areas = areas;
    }

    public AreaFacade getAreaFacade() {
        return areaFacade;
    }

    public void setAreaFacade(AreaFacade areaFacade) {
        this.areaFacade = areaFacade;
    }

    public PopulationFacade getPopFacade() {
        return popFacade;
    }

    public void setPopFacade(PopulationFacade popFacade) {
        this.popFacade = popFacade;
    }

    public static int intValue(long value) {
        int valueInt = (int) value;
        if (valueInt != value) {
            throw new IllegalArgumentException(
                    "The long value " + value + " is not within range of the int type");
        }
        return valueInt;
    }

    public DpdhsArea getDpdhsArea() {
        return dpdhsArea;
    }

    public void setDpdhsArea(DpdhsArea dpdhsArea) {
        this.dpdhsArea = dpdhsArea;
    }

    public DataModel<DpdhsArea> getDpdhsAreas() {
        if (province == null) {
            return new ListDataModel<DpdhsArea>(getDpdhsFacade().findBySQL("SELECT d FROM DpdhsArea d WHERE d.retired=false ORDER BY d.name"));
        } else {
            return new ListDataModel<DpdhsArea>(getDpdhsFacade().findBySQL("SELECT d FROM DpdhsArea d WHERE d.retired=false AND d.superArea.id = " + getProvince().getId() + " ORDER BY d.name"));
        }
    }

    public void setDpdhsAreas(DataModel<DpdhsArea> dpdhsAreas) {
        this.dpdhsAreas = dpdhsAreas;
    }

    public DpdhsFacade getDpdhsFacade() {
        return dpdhsFacade;
    }

    public void setDpdhsFacade(DpdhsFacade dpdhsFacade) {
        this.dpdhsFacade = dpdhsFacade;
    }

    public Long getDpdhsId() {
        if (dpdhsArea == null) {
            return 0l;
        } else {
            return dpdhsArea.getId();
        }
    }

    public void setDpdhsId(Long dpdhsId) {
        this.dpdhsArea = getDpdhsFacade().find(dpdhsId);
        this.dpdhsId = dpdhsId;
    }

    public AreaFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(AreaFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public GnArea getGnArea() {
        return gnArea;
    }

    public void setGnArea(GnArea gnArea) {
        this.gnArea = gnArea;
    }

    public DataModel<GnArea> getGnAreas() {
        if (phmArea != null) {
            return new ListDataModel<GnArea>(getGnFacade().findBySQL("SELECT p FROM GnArea p WHERE p.retired=false AND p.superArea.id = " + phmArea.getId() + " ORDER BY p.name"));
        } else if (phiArea != null) {
            return new ListDataModel<GnArea>(getGnFacade().findBySQL("SELECT p FROM GnArea p WHERE p.retired=false AND p.superArea.superArea.id = " + phiArea.getId() + " ORDER BY p.name"));
        } else if (mohArea != null) {
            return new ListDataModel<GnArea>(getGnFacade().findBySQL("SELECT p FROM GnArea p WHERE p.retired=false AND p.superArea.superArea.superArea.id = " + mohArea.getId() + " ORDER BY p.name"));
        } else if (dpdhsArea != null) {
            return new ListDataModel<GnArea>(getGnFacade().findBySQL("SELECT p FROM GnArea p WHERE p.retired=false AND p.superArea.superArea.superArea.superArea.id = " + dpdhsArea.getId() + " ORDER BY p.name"));
        } else {
            return new ListDataModel<GnArea>(getGnFacade().findBySQL("SELECT p FROM GnArea p WHERE p.retired=false ORDER BY p.name"));
        }
    }

    public void setGnAreas(DataModel<GnArea> gnAreas) {
        this.gnAreas = gnAreas;
    }

    public GnFacade getGnFacade() {
        return gnFacade;
    }

    public void setGnFacade(GnFacade gnFacade) {
        this.gnFacade = gnFacade;
    }

    public Long getGnId() {
        return gnId;
    }

    public void setGnId(Long gnId) {
        this.gnId = gnId;
    }

    public MohArea getMohArea() {
        return mohArea;
    }

    public void setMohArea(MohArea mohArea) {
        this.mohArea = mohArea;
    }

    public DataModel<MohArea> getMohAreas() {
        if (province != null) {
            return new ListDataModel<MohArea>(getMohFacade().findBySQL("SELECT m FROM MohArea m WHERE m.retired=false AND m.superArea.superArea.id = " + province.getId() + " ORDER BY m.name"));
        } else if (dpdhsArea != null) {
            return new ListDataModel<MohArea>(getMohFacade().findBySQL("SELECT m FROM MohArea m WHERE m.retired=false AND m.superArea.id = " + dpdhsArea.getId() + " ORDER BY m.name"));
        } else {
            return new ListDataModel<MohArea>(getMohFacade().findAll(true));
        }
    }

    public void setMohAreas(DataModel<MohArea> mohAreas) {
        this.mohAreas = mohAreas;
    }

    public MohFacade getMohFacade() {
        return mohFacade;
    }

    public void setMohFacade(MohFacade mohFacade) {
        this.mohFacade = mohFacade;
    }

    public Long getMohId() {
        return mohId;
    }

    public void setMohId(Long mohId) {
        this.mohId = mohId;
    }

    public PhiArea getPhiArea() {
        return phiArea;
    }

    public void setPhiArea(PhiArea phiArea) {
        this.phiArea = phiArea;
    }

    public DataModel<PhiArea> getPhiAreas() {
        if (mohArea != null) {
            return new ListDataModel<PhiArea>(getPhiFacade().findBySQL("SELECT p FROM PhiArea p WHERE p.retired=false AND p.superArea.id = " + mohArea.getId() + " ORDER BY p.name"));
        } else if (dpdhsArea != null) {
            return new ListDataModel<PhiArea>(getPhiFacade().findBySQL("SELECT p FROM PhiArea p WHERE p.retired=false AND p.superArea.superArea.id = " + dpdhsArea.getId() + " ORDER BY p.name"));
        } else {
            return new ListDataModel<PhiArea>(getPhiFacade().findBySQL("SELECT p FROM PhiArea p WHERE p.retired=false ORDER BY p.name"));
        }
    }

    public void setPhiAreas(DataModel<PhiArea> phiAreas) {
        this.phiAreas = phiAreas;
    }

    public PhiFacade getPhiFacade() {
        return phiFacade;
    }

    public void setPhiFacade(PhiFacade phiFacade) {
        this.phiFacade = phiFacade;
    }

    public Long getPhiId() {
        return phiId;
    }

    public void setPhiId(Long phiId) {
        this.phiId = phiId;
    }

    public PhmArea getPhmArea() {
        return phmArea;
    }

    public void setPhmArea(PhmArea phmArea) {
        this.phmArea = phmArea;
    }

    public DataModel<PhmArea> getPhmAreas() {
        if (phiArea != null) {
            return new ListDataModel<PhmArea>(getPhmFacade().findBySQL("SELECT p FROM PhmArea p WHERE p.retired=false AND p.superArea.id = " + phiArea.getId() + " ORDER BY p.name"));
        } else if (mohArea != null) {
            return new ListDataModel<PhmArea>(getPhmFacade().findBySQL("SELECT p FROM PhmArea p WHERE p.retired=false AND p.superArea.superArea.id = " + mohArea.getId() + " ORDER BY p.name"));
        } else if (dpdhsArea != null) {
            return new ListDataModel<PhmArea>(getPhmFacade().findBySQL("SELECT p FROM PhmArea p WHERE p.retired=false AND p.superArea.superArea.superArea.id = " + dpdhsArea.getId() + " ORDER BY p.name"));
        } else {
            return new ListDataModel<PhmArea>(getPhmFacade().findBySQL("SELECT p FROM PhmArea p WHERE p.retired=false ORDER BY p.name"));
        }
    }

    public void setPhmAreas(DataModel<PhmArea> phmAreas) {
        this.phmAreas = phmAreas;
    }

    public PhmFacade getPhmFacade() {
        return phmFacade;
    }

    public void setPhmFacade(PhmFacade phmFacade) {
        this.phmFacade = phmFacade;
    }

    public Long getPhmId() {
        return phmId;
    }

    public void setPhmId(Long phmId) {
        this.phmId = phmId;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public ProvinceFacade getProvinceFacade() {
        return provinceFacade;
    }

    public void setProvinceFacade(ProvinceFacade provinceFacade) {
        this.provinceFacade = provinceFacade;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public DataModel<Province> getProvinces() {
        return new ListDataModel<Province>(getProvinceFacade().findAll(true));
    }

    public void setProvinces(DataModel<Province> provinces) {
        this.provinces = provinces;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<DemoTblRow> listGnPopOfDPDHS() {
        String temSQL;
        double area;
        long pop;
        Population p;
        List<DemoTblRow> lst = new ArrayList<DemoTblRow>();
        if (dpdhsArea == null) {
//            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false ";
            JsfUtil.addErrorMessage("No Area Selected");
            return null;
        } else {
            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false AND i.superArea.id = " + dpdhsArea.getId();
        }
        List<MohArea> lstMOH = mohFacade.findBySQL(temSQL);

        for (MohArea temMOH : lstMOH) {
            temSQL = "SELECT i FROM PhiArea i WHERE i.retired = false AND i.superArea.id = " + temMOH.getId();
            List<PhiArea> lstPHI = phiFacade.findBySQL(temSQL);

            for (PhiArea temPHI : lstPHI) {
                temSQL = "SELECT i FROM PhmArea i WHERE i.retired = false AND i.superArea.id = " + temPHI.getId();
                List<PhmArea> lstPHM = phmFacade.findBySQL(temSQL);

                for (PhmArea temPHM : lstPHM) {
                    temSQL = "SELECT i FROM GnArea i WHERE i.retired = false AND i.superArea.id = " + temPHM.getId();
                    List<GnArea> lstGN = gnFacade.findBySQL(temSQL);

                    for (GnArea temGN : lstGN) {
                        temSQL = "SELECT i FROM Population i WHERE i.retired = false AND i.area.id= " + temGN.getId();
                        List<Population> lstPop = popFacade.findBySQL(temSQL);
                        if (lstPop.isEmpty()) {
                            p = new Population();
                            p.setPopulationNumber(0);
                            p.setAreaKm(0);
                        } else {
                            p = lstPop.get(0);
                        }
                        DemoTblRow row = new DemoTblRow();
                        row.setDpdhs(dpdhsArea);
                        row.setMoh(temMOH);
                        row.setPhi(temPHI);
                        row.setPhm(temPHM);
                        row.setGn(temGN);
                        row.setGnPop(p);
                        lst.add(row);
                    }

                }




            }
        }
        Collections.sort(lst, new GnComparator());
        return lst;
    }

    public class GnComparator implements Comparator<DemoTblRow> {

        @Override
        public int compare(DemoTblRow o1, DemoTblRow o2) {
            return o1.getGn().getName().compareTo(o2.getGn().getName());
        }
    }

    @FacesConverter(forClass = Area.class)
    public static class AreaControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AreaController controller = (AreaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "areaController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Area) {
                Area o = (Area) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + AreaController.class.getName());
            }
        }
    }
}
