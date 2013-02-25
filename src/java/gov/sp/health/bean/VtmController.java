/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Vtm;  
import gov.sp.health.facade.VtmFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;  

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class VtmController implements Serializable {

    /**
     *
     * EJBs for facade
     *
     */
    @EJB
    private VtmFacade ejbFacade;
    /**
     * Managed Properties
     */
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    /**
     * Selected Vtm
     */
    private Vtm current;
    /**
     * All Vtms
     */
    private List<Vtm> items = null;
    String selectText = "";
    JasperPrint jasperPrint;

    public void init() throws JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(items);
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/vtms.jasper");
        jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap(), beanCollectionDataSource);
    }

    /**
     *
     * @throws JRException
     * @throws IOException
     */
    public void createPdf() throws JRException, IOException {
        System.out.println("Creating PDF");
        init();
        System.out.println("Inited");
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        System.out.println("got http response");
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=vtms.pdf");
        System.out.println("Header added");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        System.out.println("Got output stream");
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        System.out.println("Exported PDF");
        FacesContext.getCurrentInstance().responseComplete();
        System.out.println("Response completed");
    }

    public void createPdf1(ActionEvent actionEvent){
        System.out.println("pdf1");
    }
    
    public void createPdf2(){
        System.out.println("pdf2");
        
    }
    
    public void setItems(List<Vtm> items) {
        this.items = items;
    }

    public VtmController() {
    }

    public Vtm getCurrent() {
        if (current == null) {
            current = new Vtm();
        }
        return current;
    }

    public void setCurrent(Vtm current) {
        this.current = current;
    }

    private VtmFacade getFacade() {
        return ejbFacade;
    }

    public List<Vtm> getItems() {
        String temSql;
        if (selectText.trim().equals("")) {
            temSql = "select p from Vtm p where p.retired=false order by p.name";
        } else {
            temSql = "select p from Vtm p where p.retired=false and lower(p.name) like '%" + selectText.toLowerCase() + "%' order by p.name";
        }
        List<Vtm> temLstPer = getFacade().findBySQL(temSql);
        items = temLstPer;
        return items;
    }

    public static int intValue(long value) {
        int valueInt = (int) value;
        if (valueInt != value) {
            throw new IllegalArgumentException(
                    "The long value " + value + " is not within range of the int type");
        }
        return valueInt;
    }

    public void prepareAdd() {
        setCurrent(new Vtm());

    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (current == null) {
            JsfUtil.addErrorMessage("Nothing to save");
            return;
        }
        if (current.getName().trim().equals("")) {
            JsfUtil.addErrorMessage("Please enter a name to save");
            return;
        }
        if (current.getId() != null && current.getId() != 0) {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        getItems();
        selectText = "";
    }

    public void delete() {
        if (sessionController.getPrivilege().isInventoryDelete() == false) {
            JsfUtil.addErrorMessage("You are not autherized to delete any content");
            return;
        }
        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
        }
        getItems();
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public VtmFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(VtmFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = Vtm.class)
    public static class VtmControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VtmController controller = (VtmController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vtmController");
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
            if (object instanceof Vtm) {
                Vtm o = (Vtm) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + VtmController.class.getName());
            }
        }
    }
}
