/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.*;
import gov.sp.health.facade.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Buddhika
 */
@ManagedBean
@RequestScoped
public class UserApproveController implements Serializable {

    DataModel<WebUser> toApproveUsers;
    DataModel<WebUser> users;
    WebUser selectedUser;
    Person selectedPerson;
    //
    @EJB
    WebUserFacade userFacade;
    @EJB
    PrivilegeFacade priFacade;
    @EJB
    AreaFacade areaFacade;
    @EJB
    InstitutionFacade institutionFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    LocationFacade locationFacade;
    @EJB
    PersonFacade personFacade;
    //
    String activateComments;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    Privilege privilege;
    DataModel<Area> areas;
    DataModel<Institution> institutions;
    DataModel<Unit> units;
    DataModel<Location> locations;
    @ManagedProperty(value = "#{imageController}")
    private ImageController imageController;

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    
    
    public ImageController getImageController() {
        return imageController;
    }

    public void setImageController(ImageController imageController) {
        this.imageController = imageController;
    }

    public String viewPerImage() {
        System.out.println("VIew Per Img");
        System.out.println("VIew Per Img" + selectedUser.getWebUserPerson().getName());
        imageController.setPerson(selectedUser.getWebUserPerson());
        System.out.println("person_image");
        return "person_image";
    }

    public DataModel<WebUser> getUsers() {
        String temSql;
        if (sessionController.getPrivilege().getRestrictedInstitution() != null) {
            temSql = "SELECT a FROM WebUser a WHERE a.retired=false AND a.webUserPerson.institution.id = " + sessionController.getPrivilege().getRestrictedInstitution().getId() + " ORDER BY a.name ";
        } else if (sessionController.getPrivilege().getRestrictedUnit() != null) {
            temSql = "SELECT a FROM WebUser a WHERE a.retired=false AND a.webUserPerson.unit.id =  " + sessionController.getPrivilege().getRestrictedUnit().getId() + "  ORDER BY a.name ";
        } else {
            temSql = "SELECT a FROM WebUser a WHERE a.retired=false ORDER BY a.name ";
        }
        return new ListDataModel<WebUser>(getUserFacade().findBySQL(temSql));
    }

    public void setUsers(DataModel<WebUser> users) {
        this.users = users;
    }

    public AreaFacade getAreaFacade() {
        return areaFacade;
    }

    public void setAreaFacade(AreaFacade areaFacade) {
        this.areaFacade = areaFacade;
    }

    public DataModel<Area> getAreas() {
        String temSql;
        if (sessionController.getPrivilege().getRestrictedArea() == null) {
            temSql = "SELECT a FROM Area a WHERE a.retired=false ORDER BY a.name ";
        } else {
            temSql = "SELECT a FROM Area a WHERE a.retired=false AND a.id = " + sessionController.getPrivilege().getRestrictedArea().getId();
        }
        return new ListDataModel<Area>(getAreaFacade().findBySQL(temSql));
    }

    public void setAreas(DataModel<Area> areas) {
        this.areas = areas;
    }

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public DataModel<Institution> getInstitutions() {
        String temSql;
        if (sessionController.getPrivilege().getRestrictedInstitution() == null) {
            temSql = "SELECT a FROM Institution a WHERE a.retired=false ORDER BY a.name ";
        } else {
            temSql = "SELECT a FROM Institution a WHERE a.retired=false AND a.id = " + sessionController.getPrivilege().getRestrictedInstitution().getId();

        }
        return new ListDataModel<Institution>(getInstitutionFacade().findBySQL(temSql));
    }

    public void setInstitutions(DataModel<Institution> institutions) {
        this.institutions = institutions;
    }

    public LocationFacade getLocationFacade() {
        return locationFacade;
    }

    public void setLocationFacade(LocationFacade locationFacade) {
        this.locationFacade = locationFacade;
    }

    public DataModel<Location> getLocations() {
        String temSql;
        temSql = "SELECT a FROM Location a WHERE a.retired=false ORDER BY a.name ";
        return new ListDataModel<Location>(getLocationFacade().findBySQL(temSql));
    }

    public void setLocations(DataModel<Location> locations) {
        this.locations = locations;
    }

    public UnitFacade getUnitFacade() {
        return unitFacade;
    }

    public void setUnitFacade(UnitFacade unitFacade) {
        this.unitFacade = unitFacade;
    }

    public DataModel<Unit> getUnits() {
        String temSql;
        if (getPrivilege().getRestrictedInstitution() != null) {
            temSql = "SELECT a FROM Unit a WHERE a.retired=false AND a.institution.id = " + getPrivilege().getRestrictedInstitution().getId() + " ORDER BY a.name ";
        } else if (sessionController.getPrivilege().getRestrictedUnit() != null) {
            temSql = "SELECT a FROM Unit a WHERE a.retired=false AND a.unit.id = " + sessionController.getPrivilege().getRestrictedUnit().getId() + "  ORDER BY a.name ";
        } else {
            temSql = "SELECT a FROM Unit a WHERE a.retired=false ORDER BY a.name ";
        }
        return new ListDataModel<Unit>(getUnitFacade().findBySQL(temSql));
    }

    public void setUnits(DataModel<Unit> units) {
        this.units = units;
    }

    /**
     * Creates a new instance of UserApproveController
     */
    public UserApproveController() {
    }

    public String getActivateComments() {
        return activateComments;
    }

    public void setActivateComments(String activateComments) {
        this.activateComments = activateComments;
    }

    public Privilege getPrivilege() {

        if (privilege == null) {
            if (getSelectedUser() != null) {
                privilege = getPriFacade().findFirstBySQL("SELECT p FROM Privilege p WHERE p.webUser.id = " + getSelectedUser().getId());
                if (privilege == null) {
                    privilege = new Privilege();
                }
            } else {
                privilege = new Privilege();
            }
        }
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public WebUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(WebUser selectedUser) {
        this.selectedUser = selectedUser;
        this.privilege = null;
        this.privilege = getPrivilege();
    }

    public DataModel<WebUser> getToApproveUsers() {
        String temSQL;
        temSQL = "SELECT u FROM WebUser u WHERE u.retired=false AND u.activated=false";
        List<WebUser> lst;
        lst = getUserFacade().findBySQL(temSQL);
        return new ListDataModel<WebUser>(lst);
    }

    public void setToApproveUsers(DataModel<WebUser> toApproveUsers) {
        this.toApproveUsers = toApproveUsers;
    }

    public WebUserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(WebUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void addUserToPerson(){
        
    }
    
    public void saveUser() {
        if (selectedUser == null) {
            JsfUtil.addErrorMessage("Please select a user");
            return;
        }

        userFacade.edit(selectedUser);

        privilege.setWebUser(selectedUser);
        priFacade.edit(privilege);

        selectedUser = null;
        privilege = null;

        JsfUtil.addSuccessMessage("Successfully activated");
    }

    public void approveUser() {
        if (selectedUser == null) {
            JsfUtil.addErrorMessage("Please select a user to approve");
            return;
        }
        selectedUser.setActivated(true);
        selectedUser.setActivatedAt(Calendar.getInstance().getTime());
        selectedUser.setActivator(sessionController.loggedUser);
        selectedUser.setActivateComments(activateComments);
        userFacade.edit(selectedUser);

        privilege.setWebUser(selectedUser);
        priFacade.create(privilege);

        selectedUser = null;
        privilege = null;

        JsfUtil.addSuccessMessage("Successfully activated");
    }

    
    public void removeUser() {
        if (selectedUser == null) {
            JsfUtil.addErrorMessage("Please select a user to remove");
            return;
        }
        selectedUser.setActivated(false);
        selectedUser.setRetired(true);
        selectedUser.setRetiredAt(Calendar.getInstance().getTime());
        selectedUser.setRetirer(sessionController.loggedUser);
        selectedUser.setRetireComments(activateComments);
        userFacade.edit(selectedUser);

        selectedUser = null;
        privilege = null;

        JsfUtil.addSuccessMessage("Successfully activated");
    }
    
    
    public PrivilegeFacade getPriFacade() {
        return priFacade;
    }

    public void setPriFacade(PrivilegeFacade priFacade) {
        this.priFacade = priFacade;
    }
}
