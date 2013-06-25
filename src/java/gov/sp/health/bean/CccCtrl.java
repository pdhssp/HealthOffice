/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.CitizenCharterCategory;
import gov.sp.health.facade.CitizenCharterCategoryFacade;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author buddhika
 */
@ManagedBean
@RequestScoped
public class CccCtrl {

    @EJB
    CitizenCharterCategoryFacade facade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    @ManagedProperty(value = "#{messageProvider}")
    private MessageProvider messageProvider;
    List<CitizenCharterCategory> items;
    CitizenCharterCategory current;
    CitizenCharterCategory[] selects;
    String strSelect;

    /**
     * Creates a new instance of CccCtrl
     */
    public CccCtrl() {
    }

    public String getStrSelect() {
        return strSelect;
    }

    public void setStrSelect(String strSelect) {
        this.strSelect = strSelect;
    }

    public CitizenCharterCategoryFacade getFacade() {
        return facade;
    }

    public void setFacade(CitizenCharterCategoryFacade facade) {
        this.facade = facade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    public List<CitizenCharterCategory> getItems() {
        String temSql;
        if (strSelect == null || strSelect.trim().equals("")) {
            temSql = "select c from CitizenCharterCategory c where c.retired=false order by c.name";
        } else {
            temSql = "select c from CitizenCharterCategory c where c.retired=false and lower(c.name) like '%" + strSelect.toLowerCase() + "%' order by c.name";
        }
        items = getFacade().findBySQL(temSql);
        return items;
    }

    public void setItems(List<CitizenCharterCategory> items) {
        this.items = items;
    }

    public CitizenCharterCategory getCurrent() {
        if (current == null) {
            current = new CitizenCharterCategory();
        }
        return current;
    }

    public void setCurrent(CitizenCharterCategory current) {
        this.current = current;
    }

    public CitizenCharterCategory[] getSelects() {
        return selects;
    }

    public void setSelects(CitizenCharterCategory[] selects) {
        this.selects = selects;
    }

    public void prepareAdd() {
        current = new CitizenCharterCategory();
    }

    public void saveCurrent() {
        System.out.print("Saving current");
        if (current.getId() == null || current.getId() == 0) {
            System.out.print("Saving New One");
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage("New Item Saved");
        } else {
            System.out.print("Saving Old One");
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Item Changed");
        }
    }

    public void removeCurrent() {
        if (current != null || current.getId() == null || current.getId() == 0) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getFacade().edit(current);
        } else {
            JsfUtil.addSuccessMessage("Nothing to remove");
        }
    }
}
