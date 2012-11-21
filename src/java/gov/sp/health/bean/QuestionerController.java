package gov.sp.health.bean;
// Facades
import gov.sp.health.entity.*;
import gov.sp.health.facade.*;
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
public class QuestionerController  implements Serializable {

    /**
     * EJBs
     */
    @EJB
    QuestionerFacade qFacade;
    @EJB
    QuestionerAnswerFacade qaFacade;
    @EJB
    QuestionerItemFacade qiFacade;
    @EJB
    QuestionerAnswerItemFacade qaiFacade;
    @EJB
    PersonFacade personFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    AreaFacade areaFacade;
    /**
     * Lists
     */
    DataModel<Questioner> questioners;
    DataModel<QuestionerItem> questionerItems;
    DataModel<QuestionerAnswer> questionerAnswers;
    DataModel<QuestionerAnswerItem> questionerAnswerItems;
    DataModel<Person> persons;
    DataModel<Institution> institutions;
    DataModel<Unit> units;
    DataModel<Area> areas;
    //
    Questioner questioner;
    QuestionerItem questionerItem;
    QuestionerAnswer questionerAnswer;
    QuestionerAnswerItem questionerAnswerItem;
    Area area;
    Institution institution;
    Unit unit;
    Person person;
    /**
     * IDs
     */

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
    public QuestionerController() {
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


    public static int intValue(long value) {
        int valueInt = (int) value;
        if (valueInt != value) {
            throw new IllegalArgumentException(
                    "The long value " + value + " is not within range of the int type");
        }
        return valueInt;
    }

   

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

//    public List<DemoTblRow> listGnPopOfDPDHS() {
//        String temSQL;
//        double area;
//        long pop;
//        Population p;
//        List<DemoTblRow> lst = new ArrayList<DemoTblRow>();
//        if (dpdhsArea == null) {
////            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false ";
//            JsfUtil.addErrorMessage("No Area Selected");
//            return null;
//        } else {
//            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false AND i.superArea.id = " + dpdhsArea.getId();
//        }
//        List<MohArea> lstMOH = mohFacade.findBySQL(temSQL);
//
//        for (MohArea temMOH : lstMOH) {
//            temSQL = "SELECT i FROM PhiArea i WHERE i.retired = false AND i.superArea.id = " + temMOH.getId();
//            List<PhiArea> lstPHI = phiFacade.findBySQL(temSQL);
//
//            for (PhiArea temPHI : lstPHI) {
//                temSQL = "SELECT i FROM PhmArea i WHERE i.retired = false AND i.superArea.id = " + temPHI.getId();
//                List<PhmArea> lstPHM = phmFacade.findBySQL(temSQL);
//
//                for (PhmArea temPHM : lstPHM) {
//                    temSQL = "SELECT i FROM GnArea i WHERE i.retired = false AND i.superArea.id = " + temPHM.getId();
//                    List<GnArea> lstGN = gnFacade.findBySQL(temSQL);
//
//                    for (GnArea temGN : lstGN) {
//                        temSQL = "SELECT i FROM Population i WHERE i.retired = false AND i.area.id= " + temGN.getId();
//                        List<Population> lstPop = popFacade.findBySQL(temSQL);
//                        if (lstPop.isEmpty()) {
//                            p = new Population();
//                            p.setPopulationNumber(0);
//                            p.setAreaKm(0);
//                        } else {
//                            p = lstPop.get(0);
//                        }
//                        DemoTblRow row = new DemoTblRow();
//                        row.setDpdhs(dpdhsArea);
//                        row.setMoh(temMOH);
//                        row.setPhi(temPHI);
//                        row.setPhm(temPHM);
//                        row.setGn(temGN);
//                        row.setGnPop(p);
//                        lst.add(row);
//                    }
//
//                }
//
//
//
//
//            }
//        }
//        Collections.sort(lst, new GnComparator());
//        return lst;
//    }

    public class GnComparator implements Comparator<DemoTblRow> {

        @Override
        public int compare(DemoTblRow o1, DemoTblRow o2) {
            return o1.getGn().getName().compareTo(o2.getGn().getName());
        }
    }

    
}
