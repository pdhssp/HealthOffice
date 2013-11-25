/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Institution;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.entity.Person;
import gov.sp.health.entity.PersonContact;
import gov.sp.health.facade.ContactTypeFacade;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.PersonContactFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.ManagedProperty;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class PersonController implements Serializable {
    
/**
 *
 * EJBs for facade
 * 
 */    
    @EJB
    private PersonFacade ejbFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    PersonContactFacade perConFacade;
    @EJB
    ContactTypeFacade ctFacade;
    /**
     * Managed Properties
     */
    @Inject
    SessionController sessionController;
    /**
     * Selected Person
     */
    private Person current;
    /**
     * All Persons
     */
    private List<Person> items = null;
    /**
     * Selected Persons contacts
     */
    List<PersonContact> currentContacts;
    /**
     * Selected contact
     */
    PersonContact currentContact;
    /**
     * Search text
     */
    String selectText = "";

    public ContactTypeFacade getCtFacade() {
        return ctFacade;
    }

    public void setCtFacade(ContactTypeFacade ctFacade) {
        this.ctFacade = ctFacade;
    }

/**
 * Returns the selected contact
 * If no contact is selected, new contact is created for the current user
 *
 */
    public PersonContact getCurrentContact() {
        if (currentContact == null) {
            currentContact = new PersonContact();
            currentContact.setContactType(getCtFacade().findFirstBySQL("select ct from ContactType ct"));
            currentContact.setPerson(current);
        }
        return currentContact;
    }

    public void addContact() {
        if (currentContact == null) {
            JsfUtil.addErrorMessage("No Contact to add");
            return;
        }
        if (current==null){
            JsfUtil.addErrorMessage("No Person to add contact");
            return;
        }
        if (currentContact.getContactType()==null){
            JsfUtil.addErrorMessage("No Contact type selected");
            return;
        }
        currentContact.setPerson(current);
        getCurrentContacts().add(currentContact);
        if (currentContact.getId()==null || currentContact.getId() == 0 ){
            getPerConFacade().create(currentContact);
        }else{
            getPerConFacade().edit(currentContact);
        }
        currentContact=null;
        getCurrentContact();
    }

    public void removeContact() {
        System.out.println("Going to remove current contact");
        if (currentContact == null) {
            System.out.println("Nothing to remove");
            return;
        }
        if (currentContact.getId()!=null && currentContact.getId() != 0) {
            System.out.println("Removing " + currentContact);
            getPerConFacade().remove(currentContact);
        }else{
            System.out.println("Contact id is null or 0");
        }
        currentContact = getCurrentContact();
    }

    
    public List<PersonContact> getCurrentContacts() {
        if (currentContacts == null) {
            currentContacts = new ArrayList<PersonContact>();
        }
        return currentContacts;
    }

    public void setCurrentContacts(List<PersonContact> currentContacts) {
        this.currentContacts = currentContacts;
    }

    public PersonContactFacade getPerConFacade() {
        return perConFacade;
    }

    public void setPerConFacade(PersonContactFacade perConFacade) {
        this.perConFacade = perConFacade;
    }

    public void setItems(List<Person> items) {
        this.items = items;
    }

    public void setCurrentContact(PersonContact currentContact) {
        this.currentContact = currentContact;
    }

    
    
    public PersonController() {
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    
     public Person getCurrent() {
        if (current == null) {
            current = new Person();
        }
        return current;
    }

    public void setCurrent(Person current) {
        this.current = current;
        String temSql = "";
        if (current != null && current.getId() != null) {
            temSql = "select c from PersonContact c where c.retired = false and c.person.id = " + current.getId();
            currentContacts = getPerConFacade().findBySQL(temSql);
            System.out.println("The person selected is " + current.getName() + "\n The number of contacts is " + currentContacts.size());
        } else {
            System.out.println("The current is null");
            currentContacts = null;
        }
        currentContact = new PersonContact();
    }

    private PersonFacade getFacade() {
        return ejbFacade;
    }

    public List<Person> getItems() {
        String temSql;
        if (selectText.trim().equals("")) {
            temSql = "select p from Person p where p.retired=false order by p.name";
        } else {
            temSql = "select p from Person p where p.retired=false and lower(p.name) like '%" + selectText.toLowerCase() + "%' order by p.name";
        }
        List<Person> temLstPer = getFacade().findBySQL(temSql);
        items =temLstPer;
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
        setCurrent(new Person());
        
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit()==false){
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }            
        if (current==null){
            JsfUtil.addErrorMessage("Nothing to save");
            return;
        }
        if (current.getName().trim().equals("")){
            JsfUtil.addErrorMessage("Please enter a name to save");
            return;
        }
        if (current.getId()!=null  && current.getId() !=0) {
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
        if (sessionController.getPrivilege().isInventoryDelete()==false){
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

    public PersonFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PersonFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = Person.class)
    public static class PersonControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
            return controller.getEjbFacade().find(getKey(value));
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
            if (object instanceof Person) {
                Person o = (Person) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + PersonController.class.getName());
            }
        }
    }
}
