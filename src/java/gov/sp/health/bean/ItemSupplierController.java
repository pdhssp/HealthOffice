/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Item;
import gov.sp.health.entity.ItemSupplier;
import gov.sp.health.entity.Supplier;
import gov.sp.health.facade.ItemFacade;
import gov.sp.health.facade.ItemSupplierFacade;
import gov.sp.health.facade.SupplierFacade;
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
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class ItemSupplierController implements Serializable {

    /**
     *
     * EJBs for facade
     *
     */
    @EJB
    private ItemSupplierFacade ejbFacade;
    @EJB
    SupplierFacade supFacade;
    @EJB
    ItemFacade itemFacade;
    /**
     * Managed Properties
     */
    @Inject
    SessionController sessionController;
    /**
     * Selected ItemSupplier
     */
    private ItemSupplier current;
    Item currentItem;
    Supplier currentSupplier;
    /**
     * All ItemSuppliers
     */
    private List<ItemSupplier> itemSuppliers = null;
    private List<ItemSupplier> supplierItems = null;
    /**
     * Selected ItemSuppliers
     */
    private List<ItemSupplier> selectedItemSuppliers = null;
    String selectText = "";

    public ItemSupplierFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ItemSupplierFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SupplierFacade getSupFacade() {
        return supFacade;
    }

    public void setSupFacade(SupplierFacade supFacade) {
        this.supFacade = supFacade;
    }

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public ItemSupplier getCurrent() {
        if (current == null) {
            current = new ItemSupplier();
        }
        return current;
    }

    public void setCurrent(ItemSupplier current) {
        this.current = current;
    }

    public Item getCurrentItem() {
        if (currentItem == null) {
            currentItem = new Item();
        }
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
        itemSuppliers = new ArrayList<ItemSupplier>();
        selectedItemSuppliers = new ArrayList<ItemSupplier>();
        if (currentItem == null) {
            return;
        }
        if (currentItem.getId() == null) {
            return;
        }
        if (currentItem.getId() == 0) {
            return;
        }
        String temSql;
        temSql = "select isup from ItemSupplier isup where isup.retired = false and isup.item.id = " + getCurrentItem().getId() + " order by isup.orderNo";
        itemSuppliers = getEjbFacade().findBySQL(temSql);
    }

    public List<ItemSupplier> getSupplierItems() {
        if (supplierItems == null) {
            supplierItems = new ArrayList<ItemSupplier>();
        }
        return supplierItems;
    }

    public void setSupplierItems(List<ItemSupplier> supplierItems) {
        this.supplierItems = supplierItems;
    }

    public void addSupplierItem() {
        if (currentSupplier == null) {
            return;
        }
        if (currentItem == null) {
            return;
        }
        try {
            ItemSupplier addingIs = new ItemSupplier();
            addingIs.setSupplier(currentSupplier);
            addingIs.setItem(currentItem);
            addingIs.setCreatedAt(Calendar.getInstance().getTime());
            addingIs.setCreater(sessionController.loggedUser);
            getEjbFacade().create(addingIs);
            JsfUtil.addSuccessMessage("Successfully Added");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error. \n" + e.getMessage());
        }
    }

    public void removeSupplierItem() {
        if (current == null) {
            return;
        }
        if (current.getId() == null) {
            return;
        }
        if (current.getId() == 0) {
            return;
        }
        try {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getEjbFacade().edit(current);
            JsfUtil.addSuccessMessage("Successfully Removed");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error. \n" + e.getMessage());
        }
    }

    public Supplier getCurrentSupplier() {
        if (currentSupplier == null) {
            currentSupplier = new Supplier();
        }
        return currentSupplier;
    }

    public void setCurrentSupplier(Supplier currentSupplier) {
        this.currentSupplier = currentSupplier;


        supplierItems = new ArrayList<ItemSupplier>();
        selectedItemSuppliers = new ArrayList<ItemSupplier>();
        if (currentSupplier == null) {
            return;
        }
        if (currentSupplier.getId() == null) {
            return;
        }
        if (currentSupplier.getId() == 0) {
            return;
        }
        String temSql;
        temSql = "select isup from ItemSupplier isup where isup.retired = false and isup.supplier.id = " + getCurrentItem().getId() + " order by isup.orderNo";
        supplierItems = getEjbFacade().findBySQL(temSql);
    }

    public List<ItemSupplier> getItemSuppliers() {
        return itemSuppliers;
    }

    public void setItemSuppliers(List<ItemSupplier> itemSuppliers) {
        this.itemSuppliers = itemSuppliers;
    }

    public List<ItemSupplier> getSelectedItemSuppliers() {
        return selectedItemSuppliers;
    }

    public void setSelectedItemSuppliers(List<ItemSupplier> selectedSuppliers) {
        this.selectedItemSuppliers = selectedSuppliers;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    /**
     * Returns the selected contact If no contact is selected, new contact is
     * created for the current user
     *
     */
    public static int intValue(long value) {
        int valueInt = (int) value;
        if (valueInt != value) {
            throw new IllegalArgumentException(
                    "The long value " + value + " is not within range of the int type");
        }
        return valueInt;




    }

    @FacesConverter(forClass = ItemSupplier.class)
    public static class ItemSupplierControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemSupplierController controller = (ItemSupplierController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemSupplierController");
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
            if (object instanceof ItemSupplier) {
                ItemSupplier o = (ItemSupplier) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ItemSupplierController.class.getName());
            }
        }
    }
}
