/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.*;
import gov.sp.health.facade.HealthInstitutionFacade;
import java.io.Serializable;
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
public final class HealthInstitutionController implements Serializable {

    @EJB
    private HealthInstitutionFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    private List<HealthInstitution> lstItems;
    private List<HealthInstitution> searchItems;
    private HealthInstitution current;
    private List<HealthInstitution> items = null;
    private String selectText = "";

    public HealthInstitutionController() {
    }

    public List<HealthInstitution> getLstItems() {
        lstItems = getFacade().findBySQL("Select d From HealthInstitution d where d.retired = false order by d.name");
        return lstItems;
    }

    public HealthInstitution getCurrent() {
        if (current == null) {
            current = new HealthInstitution();
        }
        return current;
    }

    public void setCurrent(HealthInstitution current) {
        this.current = current;
    }

    private HealthInstitutionFacade getFacade() {
        return getEjbFacade();
    }

    public List<HealthInstitution> getItems() {
        items = getFacade().findAll("name", true);
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

    public HealthInstitution searchItem(String itemName, boolean createNewIfNotPresent) {
        HealthInstitution searchedItem = null;
        setItems(getFacade().findAll("name", itemName, true));
        if (getItems().size() > 0) {
            searchedItem = getItems().get(0);
        } else if (createNewIfNotPresent) {
            searchedItem = new HealthInstitution();
            searchedItem.setName(itemName);
            searchedItem.setCreatedAt(Calendar.getInstance().getTime());
            searchedItem.setCreater(getSessionController().loggedUser);
            getFacade().create(searchedItem);
        }
        return searchedItem;
    }

    private void recreateModel() {
        setItems(null);
    }

    public void prepareAdd() {
        setCurrent(new HealthInstitution());
    }

    public HealthInstitutionFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(HealthInstitutionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public void saveSelected() {

        if (getSessionController().getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getCurrent().setOutSide(true);
            getFacade().edit(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            getCurrent().setCreatedAt(Calendar.getInstance().getTime());
            getCurrent().setCreater(getSessionController().loggedUser);
            getCurrent().setOutSide(true);
            getFacade().create(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        recreateModel();
        getItems();
        setSelectText("");
    }

    public void addDirectly() {
        JsfUtil.addSuccessMessage("1");
        try {

            getCurrent().setCreatedAt(Calendar.getInstance().getTime());
            getCurrent().setCreater(getSessionController().loggedUser);

            getFacade().create(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
            setCurrent(new HealthInstitution());
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
        }

    }

    public void delete() {
        if (getSessionController().getPrivilege().isInventoryDelete() == false) {
            JsfUtil.addErrorMessage("You are not autherized to delete any content");
            return;
        }
        if (getCurrent() != null) {
            getCurrent().setRetired(true);
            getCurrent().setRetiredAt(Calendar.getInstance().getTime());
            getCurrent().setRetirer(getSessionController().loggedUser);
            getFacade().edit(getCurrent());
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
        }
        recreateModel();
        getItems();
        setSelectText("");
        setCurrent(null);
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public Double calculateStock(Item item) {
        if (item != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + "");
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(Item item, Institution institution) {
        if (item != null && institution != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.institution.id = " + institution.getId());
        } else {
            return 0.0;
        }
    }

    public Double calculateStock(Item item, Location location) {
        if (item != item && location != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.location.id = " + location.getId());
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(Item item, Unit unit) {
        if (item != null && unit != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.unit.id = " + unit.getId());
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(Item item, Person person) {
        if (item != null && person != null) {
            return calculateStock("SELECT SUM(i.quentity) FROM ItemUnit i WHERE i.retired=false AND i.item.id = " + item.getId() + " AND i.person.id = " + person.getId());
        } else {
            return 0.00;
        }
    }

    public Double calculateStock(String strJQL) {
        System.out.println(strJQL);
        System.out.println(getFacade().toString());
        System.out.println(getFacade().findAll().toString());
        return getFacade().findAggregateDbl(strJQL);
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<HealthInstitution> items) {
        this.items = items;
    }

    public List<HealthInstitution> getSearchItems() {
        String sql;
        if (getSelectText().trim().equals("")) {
            sql = "select i from HealthInstitution i where i.retired = false order by i.name";
        } else {
            sql = "select i from HealthInstitution i where i.retired = false and lower(i.name) like '%" + getSelectText().toLowerCase() + "%' order by i.name";
        }
        System.out.println(sql);
        searchItems = getFacade().findBySQL(sql);
        if (searchItems.size() > 0) {
            setCurrent(searchItems.get(0));
        } else {
            setCurrent(null);
        }
        return searchItems;
    }

    public void setSearchItems(List<HealthInstitution> searchItems) {
        this.searchItems = searchItems;
    }

    @FacesConverter(forClass = HealthInstitution.class)
    public static class HealthInstitutionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthInstitutionController controller = (HealthInstitutionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthInstitutionController");
            return controller.getEjbFacade().find(getKey(value));
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
            if (object instanceof HealthInstitution) {
                HealthInstitution o = (HealthInstitution) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + HealthInstitutionController.class.getName());
            }
        }
    }
}
