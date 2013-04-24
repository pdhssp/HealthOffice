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
import gov.sp.health.facade.ItemFacade;
import gov.sp.health.facade.ItemUnitFacade;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class ItemUnitController implements Serializable {

    @EJB
    private ItemUnitFacade ejbFacade;
    @EJB
    ItemFacade itemFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    List<ItemUnit> lstItems;
    private ItemUnit current;
    private Item currentItem;
    private ItemCount currentItemCount;
    private DataModel<ItemUnit> items = null;
    private DataModel<ItemUnit> itemsIns = null;
    private DataModel<ItemUnit> itemsUni = null;
    private DataModel<ItemUnit> itemsLoc = null;
    private DataModel<ItemUnit> itemsPer = null;
    //
    DataModel<ItemUnit> insItemsSingle;
    DataModel<ItemCount> itemCounts;
    DataModel<ItemCount> insItemCounts;
    DataModel<ItemCount> uniItemCounts;
    DataModel<ItemCount> locItemCounts;
    DataModel<ItemCount> perItemCounts;
    private int selectedItemIndex;
    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;
    String selectText = "";
    Unit unit;
    Location location;
    Person person;
    Institution institution;
    List<String> whNos;
    String whNo;
    Boolean allItems;
    Boolean selectedItems;
    Integer intSelect;
    List<Item> selectedConsumableOrInventoryItems;
    List<ConsumableItem> selectedConsumableItems;
    List<InventoryItem> selectedInventoryItems;

    public Integer getIntSelect() {
        return intSelect;
    }

    public void setIntSelect(Integer intSelect) {
        this.intSelect = intSelect;
        if (intSelect == 1) {
            setSelectedItems(Boolean.TRUE);
            setAllItems(Boolean.FALSE);
        } else {
            setSelectedItems(Boolean.FALSE);
            setAllItems(Boolean.TRUE);
        }
    }

    public Boolean getAllItems() {
        return allItems;
    }

    public void setAllItems(Boolean allItems) {
        this.allItems = allItems;
    }

    public Boolean getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Boolean selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<Item> getSelectedConsumableOrInventoryItems() {
        if (selectedConsumableOrInventoryItems == null) {
            selectedConsumableOrInventoryItems = new ArrayList<Item>();
        }
        return selectedConsumableOrInventoryItems;
    }

    public void setSelectedConsumableOrInventoryItems(List<Item> selectedConsumableOrInventoryItems) {
        this.selectedConsumableOrInventoryItems = selectedConsumableOrInventoryItems;
    }

    public List<ConsumableItem> getSelectedConsumableItems() {
        if (selectedConsumableItems == null) {
            selectedConsumableItems = new ArrayList<ConsumableItem>();
        }
        return selectedConsumableItems;
    }

    public void setSelectedConsumableItems(List<ConsumableItem> selectedConsumableItems) {
        this.selectedConsumableItems = selectedConsumableItems;
    }

    public List<InventoryItem> getSelectedInventoryItems() {
        if (selectedInventoryItems == null) {
            selectedInventoryItems = new ArrayList<InventoryItem>();
        }
        return selectedInventoryItems;
    }

    public void setSelectedInventoryItems(List<InventoryItem> selectedInventoryItems) {
        this.selectedInventoryItems = selectedInventoryItems;
    }

    public String getWhNo() {
        return whNo;
    }

    public void setWhNo(String whNo) {
        this.whNo = whNo;
    }

    public List<String> getWhNos() {
        String temSql = "Select DISTINCT v.whNo From Vmp v where v.retired=false order by v.whNo";
        return getFacade().findString(temSql);
    }

    public void setWhNos(List<String> whNos) {
        this.whNos = whNos;
    }

    public ItemCount getCurrentItemCount() {
        return currentItemCount;
    }

    public void setCurrentItemCount(ItemCount currentItemCount) {
        this.currentItemCount = currentItemCount;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    public void tempMethod() {
    }

    public DataModel<ItemUnit> getInsItemsSingle() {
        if (institution != null && getCurrentItem() != null) {
            return new ListDataModel<ItemUnit>(getFacade().findBySQL("SELECT i From ItemUnit i WHERE i.retired=false AND i.institution.id = " + institution.getId() + " and i.item.id = " + getCurrentItem().getId() + " "));
        } else {
            return null;
        }
    }

    public void setInsItemsSingle(DataModel<ItemUnit> insItemsSingle) {
        this.insItemsSingle = insItemsSingle;
    }

    public String insItemsList() {
        setCurrentItem(currentItemCount.getItem());
        System.out.print("Getting Item " + currentItem.getName());
        return "inventory_report_institution_items_counts_list";
    }

    public DataModel<ItemCount> getInsItemCounts() {
        if (getInstitution() == null) {
            return null;
        }
        List<ItemCount> temItemCounts = new ArrayList<ItemCount>();
        String temSql;
        temSql = "SELECT i From Item i where i.retired=false and i.id in (select iu.item.id from ItemUnit iu where iu.institution.id = " + getInstitution().getId() + " ) order by i.name";
        List<Item> temItems = getItemFacade().findBySQL(temSql);
        for (Item temItem : temItems) {
            temSql = "select count(iu) From ItemUnit iu where iu.retired=false and iu.institution.id = " + getInstitution().getId() + " and iu.item.id = " + temItem.getId();
            ItemCount temItemCount = new ItemCount();
            temItemCount.setItem(temItem);
            temItemCount.setCount(getItemFacade().countBySql(temSql));
            temItemCounts.add(temItemCount);
        }
        return new ListDataModel<ItemCount>(temItemCounts);
    }

    public DataModel<ItemCount> getInsItemSum() {
        if (getInstitution() == null) {
            return null;
        }
//        ItemUnit iu = new ItemUnit();
//        iu.getQuentity();
        List<ItemCount> temItemCounts = new ArrayList<ItemCount>();
        String temSql;
        temSql = "SELECT i From Item i where i.retired=false and i.id in (select iu.item.id from ItemUnit iu where iu.institution.id = " + getInstitution().getId() + " ) order by i.name";
        List<Item> temItems = getItemFacade().findBySQL(temSql);
        for (Item temItem : temItems) {
            temSql = "select sum(iu.quentity) From ItemUnit iu where iu.retired=false and iu.institution.id = " + getInstitution().getId() + " and iu.item.id = " + temItem.getId();
            ItemCount temItemCount = new ItemCount();
            temItemCount.setItem(temItem);
            temItemCount.setSum(getItemFacade().sumBySql(temSql));
            temItemCounts.add(temItemCount);
        }
        return new ListDataModel<ItemCount>(temItemCounts);
    }

    public DataModel<ItemCount> getInsWhItemSum() {
        if (getInstitution() == null) {
            return null;
        }
        List<ItemCount> temItemCounts = new ArrayList<ItemCount>();
        String temSql;
        temSql = "SELECT amp From Amp amp where amp.retired=false and amp.id in (select iu.item.id from ItemUnit iu where iu.institution.id = " + getInstitution().getId() + " and iu.item.whNo = " + whNo + " ) order by amp.name";
        List<Item> temItems = getItemFacade().findBySQL(temSql);
        for (Item temItem : temItems) {
            temSql = "select sum(iu.quentity) From ItemUnit iu where iu.retired=false and iu.institution.id = " + getInstitution().getId() + " and iu.item.id = " + temItem.getId();
            ItemCount temItemCount = new ItemCount();
            temItemCount.setItem(temItem);
            temItemCount.setSum(getItemFacade().sumBySql(temSql));
            temItemCounts.add(temItemCount);
        }
        return new ListDataModel<ItemCount>(temItemCounts);
    }

    public DataModel<ItemCount> getUnitItemSum() {
        if (getUnit() == null) {
            return null;
        }
//        ItemUnit iu = new ItemUnit();
//        iu.getQuentity();
        List<ItemCount> temItemCounts = new ArrayList<ItemCount>();
        String temSql;
        temSql = "SELECT i From Item i where i.retired=false and i.id in (select iu.item.id from ItemUnit iu where iu.unit.id = " + getUnit().getId() + " ) order by i.name";
        List<Item> temItems = getItemFacade().findBySQL(temSql);
        for (Item temItem : temItems) {
            temSql = "select sum(iu.quentity) From ItemUnit iu where iu.retired=false and iu.unit.id = " + getUnit().getId() + " and iu.item.id = " + temItem.getId();
            ItemCount temItemCount = new ItemCount();
            temItemCount.setItem(temItem);
            temItemCount.setSum(getItemFacade().sumBySql(temSql));
            temItemCounts.add(temItemCount);
        }
        return new ListDataModel<ItemCount>(temItemCounts);
    }

    public void setInsItemCounts(DataModel<ItemCount> insItemCounts) {
        this.insItemCounts = insItemCounts;
    }

    public DataModel<ItemCount> getItemCounts() {
        return itemCounts;
    }

    public void setItemCounts(DataModel<ItemCount> itemCounts) {
        this.itemCounts = itemCounts;
    }

    public DataModel<ItemCount> getLocItemCounts() {
        return locItemCounts;
    }

    public void setLocItemCounts(DataModel<ItemCount> locItemCounts) {
        this.locItemCounts = locItemCounts;
    }

    public DataModel<ItemCount> getPerItemCounts() {
        return perItemCounts;
    }

    public void setPerItemCounts(DataModel<ItemCount> perItemCounts) {
        this.perItemCounts = perItemCounts;
    }

    public DataModel<ItemCount> getUniItemCounts() {
        return uniItemCounts;
    }

    public void setUniItemCounts(DataModel<ItemCount> uniItemCounts) {
        this.uniItemCounts = uniItemCounts;
    }

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ItemUnitController() {
    }

    public ItemUnitFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ItemUnitFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<ItemUnit> getLstItems() {
        return getFacade().findBySQL("Select d From ItemUnit d WHERE d.retired=false ORDERBY i.name");
    }

    public void setLstItems(List<ItemUnit> lstItems) {
        this.lstItems = lstItems;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public ItemUnit getCurrent() {
        if (current == null) {
            current = new ItemUnit();
        }
        return current;
    }

    public void setCurrent(ItemUnit current) {
        this.current = current;
    }

    private ItemUnitFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<ItemUnit> getItems() {
        items = new ListDataModel(getFacade().findAll("name", true));
        return items;
    }

    public DataModel<ItemUnit> getItemsIns() {
        if (institution != null) {
            return new ListDataModel<ItemUnit>(getFacade().findBySQL("SELECT i From ItemUnit i WHERE i.retired=false AND type(i.item)=InventoryItem AND i.institution.id = " + institution.getId() + " order by i.item.name"));
        } else {
            return null;
        }
    }

    public void setItemsIns(DataModel<ItemUnit> itemsIns) {
        this.itemsIns = itemsIns;
    }

    public DataModel<ItemUnit> getItemsLoc() {
        if (location != null) {
            return new ListDataModel<ItemUnit>(getFacade().findBySQL("SELECT i From ItemUnit i WHERE i.retired=false AND i.location.id = " + location.getId()));
        } else {
            return null;
        }
    }

    public void setItemsLoc(DataModel<ItemUnit> itemsLoc) {
        this.itemsLoc = itemsLoc;
    }

    public DataModel<ItemUnit> getItemsPer() {
        if (person != null) {
            return new ListDataModel<ItemUnit>(getFacade().findBySQL("SELECT i From ItemUnit i WHERE i.retired=false AND i.person.id = " + person.getId()));
        } else {
            return null;
        }
    }

    public void setItemsPer(DataModel<ItemUnit> itemsPer) {
        this.itemsPer = itemsPer;
    }

    private String itemListToJql(List<Item> sqlItems, String itemVariable) {
        String sql = "";
        for (Item i : sqlItems) {
            if (sql == "") {
                sql = "(" + itemVariable + " = " + i.getId();
            } else {
                sql = sql + " or " + itemVariable + " = " + i.getId() + "";
            }
        }
        sql = sql + ")";
        return sql;
    }

    private String itemListToJqlC(List<ConsumableItem> sqlItems, String itemVariable) {
        String sql = "";
        for (Item i : sqlItems) {
            if (sql == "") {
                sql = "(" + itemVariable + " = " + i.getId();
            } else {
                sql = sql + " or " + itemVariable + " = " + i.getId() + "";
            }
        }
        sql = sql + ")";
        return sql;
    }

    private String itemListToJqlI(List<InventoryItem> sqlItems, String itemVariable) {
        String sql = "";
        for (InventoryItem i : sqlItems) {
            if (sql == "") {
                sql = "(" + itemVariable + " = " + i.getId();
            } else {
                sql = sql + " or " + itemVariable + " = " + i.getId() + "";
            }
        }
        sql = sql + ")";
        return sql;
    }

    public DataModel<ItemUnit> getItemsUni() {
        String sql;
        if (unit != null) {
            if (getSelectedItems() == true) {
                if (getSelectedInventoryItems().isEmpty()) {
                    return null;
                }
                sql = "SELECT i From ItemUnit i WHERE i.retired=false AND i.unit.id = " + unit.getId() + " and " + itemListToJqlI(getSelectedInventoryItems(), "i.item.id") + " order by i.item.name";
                System.out.println(sql);
            } else {
                sql = "SELECT i From ItemUnit i WHERE i.retired=false AND i.unit.id = " + unit.getId() + " and type(i.item) = InventoryItem order by i.item.name";
            }
            return new ListDataModel<ItemUnit>(getFacade().findBySQL(sql));
        } else {
            return null;
        }
    }

    public DataModel<ItemUnit> getConsumableItemsUni() {
        if (unit != null) {
            return new ListDataModel<ItemUnit>(getFacade().findBySQL("SELECT i From ItemUnit i WHERE i.retired=false AND i.unit.id = " + unit.getId() + "  and type(i.item) = ConsumableItem order by i.item.name"));
        } else {
            return null;
        }
    }

    public void setItemsUni(DataModel<ItemUnit> itemsUni) {
        this.itemsUni = itemsUni;
    }

    public static int intValue(long value) {
        int valueInt = (int) value;
        if (valueInt != value) {
            throw new IllegalArgumentException(
                    "The long value " + value + " is not within range of the int type");
        }
        return valueInt;
    }

    public DataModel searchItems() {
        recreateModel();
        if (items == null) {
            if (selectText.equals("")) {
                items = new ListDataModel(getFacade().findAll("name", true));
            } else {
                items = new ListDataModel(getFacade().findAll("name", "%" + selectText + "%",
                        true));
                if (items.getRowCount() > 0) {
                    items.setRowIndex(0);
                    current = (ItemUnit) items.getRowData();
                    Long temLong = current.getId();
                    selectedItemIndex = intValue(temLong);
                } else {
                    current = null;
                    selectedItemIndex = -1;
                }
            }
        }
        return items;

    }

    public ItemUnit searchItem(String itemName, boolean createNewIfNotPresent) {
        ItemUnit searchedItem = null;
        items = new ListDataModel(getFacade().findAll("name", itemName, true));
        if (items.getRowCount() > 0) {
            items.setRowIndex(0);
            searchedItem = (ItemUnit) items.getRowData();
        } else if (createNewIfNotPresent) {
            searchedItem = new ItemUnit();
            searchedItem.setName(itemName);
            searchedItem.setCreatedAt(Calendar.getInstance().getTime());
            searchedItem.setCreater(sessionController.loggedUser);
            getFacade().create(searchedItem);
        }
        return searchedItem;
    }

    private void recreateModel() {
        items = null;
    }

    public void prepareSelect() {
        this.prepareModifyControlDisable();
    }

    public void prepareEdit() {
        if (current != null) {
            selectedItemIndex = intValue(current.getId());
            this.prepareSelectControlDisable();
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToEdit"));
        }
    }

    public void prepareAdd() {
        selectedItemIndex = -1;
        current = new ItemUnit();
        this.prepareSelectControlDisable();
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isInventoryEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (selectedItemIndex > 0) {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }
        this.prepareSelect();
        recreateModel();
        getItems();
        selectText = "";
        selectedItemIndex = intValue(current.getId());
    }

    public void addDirectly() {
        JsfUtil.addSuccessMessage("1");
        try {

            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);

            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
            current = new ItemUnit();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
        }

    }

    public void cancelSelect() {
        this.prepareSelect();
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
        recreateModel();
        getItems();
        selectText = "";
        selectedItemIndex = -1;
        current = null;
        this.prepareSelect();
    }

    public boolean isModifyControlDisable() {
        return modifyControlDisable;
    }

    public void setModifyControlDisable(boolean modifyControlDisable) {
        this.modifyControlDisable = modifyControlDisable;
    }

    public boolean isSelectControlDisable() {
        return selectControlDisable;
    }

    public void setSelectControlDisable(boolean selectControlDisable) {
        this.selectControlDisable = selectControlDisable;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
        searchItems();
    }

    public void prepareSelectControlDisable() {
        selectControlDisable = true;
        modifyControlDisable = false;
    }

    public void prepareModifyControlDisable() {
        selectControlDisable = false;
        modifyControlDisable = true;
    }

    @FacesConverter(forClass = ItemUnit.class)
    public static class ItemUnitControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemUnitController controller = (ItemUnitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemUnitController");
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
            if (object instanceof ItemUnit) {
                ItemUnit o = (ItemUnit) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ItemUnitController.class.getName());
            }
        }
    }
}
