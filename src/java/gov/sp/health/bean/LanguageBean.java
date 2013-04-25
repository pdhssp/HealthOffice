/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.facade.WebUserFacade;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Buddhika
 */
@ManagedBean
@RequestScoped
public class LanguageBean implements Serializable {

    public LanguageBean() {
    }
    @EJB
    WebUserFacade userFacade;
    private static final long serialVersionUID = 1L;
    private String localeCode;
    private static Map<String, Object> countries;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    @ManagedProperty(value = "#{menu}")
    private Menu menu;
    Boolean inSinhala;
    Boolean inTamil;
    Boolean inEnglish;

    public Boolean getInSinhala() {
        checkLocale();
        return inSinhala;
    }

    public void setInSinhala(Boolean inSinhala) {
        this.inSinhala = inSinhala;
    }

    public Boolean getInTamil() {
        checkLocale();
        return inTamil;
    }

    public void setInTamil(Boolean inTamil) {
        this.inTamil = inTamil;
    }

    public Boolean getInEnglish() {
        checkLocale();
        return inEnglish;
    }

    public void setInEnglish(Boolean inEnglish) {
        this.inEnglish = inEnglish;
    }

    static {
        Locale sinhalaLocale = new Locale("si", "LK");
        Locale tamilLocale = new Locale("ta", "LK");
        countries = new LinkedHashMap<String, Object>();
        countries.put("English", Locale.ENGLISH); //label, value
        countries.put("Sinhala", "si_LK");
        countries.put("Tamil", "ta_LK");

    }

    public void languageToSinhala() {
        sessionController.loggedUser.setDefLocale("si_LK");
        userFacade.edit(sessionController.loggedUser);
        Locale myLocale;
        myLocale = new Locale("si", "LK");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(myLocale);
        menu.createMenu();
    }

    public void languageToTamil() {
        sessionController.loggedUser.setDefLocale("ta_LK");
        userFacade.edit(sessionController.loggedUser);
        Locale myLocale;
        myLocale = new Locale("si", "LK");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(myLocale);
        menu.createMenu();
    }

    public void languageToEnglish() {
        sessionController.loggedUser.setDefLocale("en");
        userFacade.edit(sessionController.loggedUser);
        Locale myLocale;
        myLocale = new Locale("si", "LK");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(myLocale);
        menu.createMenu();
    }

    public void checkLocale() {
        inEnglish = false;
        inTamil = false;
        inSinhala = false;
        if (localeCode.equals("si_LK")) {
            inSinhala = true;
        } else if (localeCode.equals("ta_LK")) {
            inTamil = true;
        } else {
            inEnglish = true;
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public WebUserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(WebUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public static Map<String, Object> getCountries() {
        return countries;
    }

    public static void setCountries(Map<String, Object> countries) {
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public String getLocaleCode() {
        if (getSessionController() == null) {
            return "si_LK";
        }
        if (getSessionController().getDefLocale() == null) {
            getSessionController().getLoggedUser().setDefLocale("si_LK");
            getUserFacade().edit(getSessionController().getLoggedUser());
            getSessionController().setDefLocale("si_LK");
        }
        return sessionController.getDefLocale();
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void changeLocale() {
        sessionController.loggedUser.setDefLocale(localeCode);
        userFacade.edit(sessionController.loggedUser);
        Locale myLocale;
        if (localeCode.equals("si_LK")) {
            System.out.println("Sinhala");
            myLocale = new Locale("si", "LK");
        } else if (localeCode.equals("ta_LK")) {
            System.out.println("Tamil");
            myLocale = new Locale("ta", "LK");
        } else {
            System.out.println("English");
            myLocale = new Locale("en");
        }
        FacesContext.getCurrentInstance().getViewRoot().setLocale(myLocale);
        menu.createMenu();
    }
    //value change event listener
//    public void countryLocaleCodeChanged(ValueChangeEvent e) {
//
//        sessionController.loggedUser.setDefLocale(e.getNewValue().toString());
//        userFacade.edit(sessionController.loggedUser);
//
//
//
//
//        String newLocaleValue = e.getNewValue().toString();
//        //loop country map to compare the locale code
//        for (Map.Entry<String, Object> entry : countries.entrySet()) {
//
//            if (entry.getValue().toString().equals(newLocaleValue)) {
//
//                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
//
//            }
//        }
//    }
}
