package gov.sp.health.bean;

import gov.sp.health.facade.WebUserFacade;
import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Buddhika
 */
@ManagedBean
@SessionScoped
public class MessageProvider implements Serializable {

    public MessageProvider() {
    }
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private ResourceBundle bundle;
    private ResourceBundle msBundle;

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public ResourceBundle resetBundle() {
        Locale myLocale;
        myLocale = new Locale("si", "LK");
        try {
            if (getSessionController() == null) {
                System.out.println("Session controller is NULL");
                myLocale = new Locale("en");
            } else if (getSessionController().getDefLocale().equals("si_LK")) {
                myLocale = new Locale("si", "LK");
            } else if (getSessionController().getDefLocale().equals("ta_LK")) {
                myLocale = new Locale("ta", "LK");
            } else {
                myLocale = new Locale("en");
            }
            System.out.println(myLocale.toString());
            FacesContext context = FacesContext.getCurrentInstance();
            context.getViewRoot().setLocale(myLocale);
            bundle = context.getApplication().getResourceBundle(context, "labels");
            msBundle = context.getApplication().getResourceBundle(context, "msLabels");
            
            return bundle;
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, "labels");
            return bundle;
        }
    }

    public ResourceBundle getBundle() {
        if (bundle == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, "labels");
        }
        return bundle;
    }

    /**
     *
     * @param s
     * @return
     */
    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
    }

    public String getValue(String key) {
        Locale myLocale;
        myLocale = new Locale("si", "LK");
        if (getSessionController() == null) {
            System.out.println("Session controller is NULL");
            myLocale = new Locale("en");
        } else if (getSessionController().getDefLocale().equals("si_LK")) {
            myLocale = new Locale("si", "LK");
        } else if (getSessionController().getDefLocale().equals("ta_LK")) {
            myLocale = new Locale("ta", "LK");
        } else {
            myLocale = new Locale("en");
        }
        if (!getBundle().getLocale().equals(myLocale)) {
            bundle = null;
        }
        String result = splitCamelCase(key);
        try {
            result = getBundle().getString(key);
        } catch (MissingResourceException e) {
            System.out.println("Error in Message Provider. getValue()\n" + e.getMessage());
        }
        return result;
    }
}
