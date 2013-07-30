/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.facade.PopulationFacade;
import gov.sp.health.facade.DpdhsFacade;
import gov.sp.health.facade.GnFacade;
import gov.sp.health.facade.PhiFacade;
import gov.sp.health.facade.PhmFacade;
import gov.sp.health.facade.MohFacade;
import gov.sp.health.entity.*;
import gov.sp.health.facade.InventoryItemCategoryFacade;
import gov.sp.health.facade.InventoryItemFacade;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Buddhika
 */
@ManagedBean
@RequestScoped
public class InventoryExcelManager  implements Serializable {

    /**
     *
     * EJBs
     *
     */
    @EJB
    InventoryItemCategoryFacade inventoryItemCategoryFacade;
    @EJB
    InventoryItemFacade inventoryItemFacade;
    /**
     *
     * Values of Excel Columns
     *
     */
    int iiCol;
    int startRow;
    /**
     * Selected
     *
     */
    InventoryItemCategory inventoryItemCategory;
    //
    long selectedInventoryItemCategoryId;
    /**
     *
     * Areas
     *
     */
    DataModel<InventoryItem> inventoryItems;
    /**
     *
     * Uploading File
     *
     */
    private UploadedFile file;
    /**
     *
     * Tables Columns
     *
     *
     */


    /**
     * Creates a new instance of DemographyExcelManager
     */
    public InventoryExcelManager() {
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String importToExcel() {
        
        String temStr;
        InventoryItem temItem;
        
        File inputWorkbook;
        Workbook w;
        Cell cell;
        InputStream in;
        JsfUtil.addSuccessMessage(file.getFileName());
        try {
            JsfUtil.addSuccessMessage(file.getFileName());
            in = file.getInputstream();
            File f = new File("D:\\Tem", Calendar.getInstance().getTimeInMillis() + file.getFileName());
            FileOutputStream out = new FileOutputStream(f);
            //            OutputStream out = new FileOutputStream(new File(fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            inputWorkbook = new File(f.getAbsolutePath());
            JsfUtil.addSuccessMessage("Excel File Opened");
            w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(0);
            //
            //
            //
            for (int i = startRow; i < sheet.getRows(); i++) {
                //
                cell = sheet.getCell(iiCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temItem = inventoryItemFacade.findByField("name", temStr, true);
                    if (temItem == null) {
                        temItem = new InventoryItem();
                        temItem.setName(temStr);
                        temItem.setCategory(inventoryItemCategory);
                        inventoryItemFacade.create(temItem);
                    } else {
                        
                    }
                } else {
                    
                }

            }
            FacesMessage msg = new FacesMessage("Succesful", "All the data in Excel File Impoted to the database");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
            return "";
        } catch (BiffException e) {
            JsfUtil.addErrorMessage(e.getMessage());
            return "";
        }
    }

   
    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getIiCol() {
        return iiCol;
    }

    public void setIiCol(int iiCol) {
        this.iiCol = iiCol;
    }

    public InventoryItemCategory getInventoryItemCategory() {
        return inventoryItemCategory;
    }

    public void setInventoryItemCategory(InventoryItemCategory inventoryItemCategory) {
        this.inventoryItemCategory = inventoryItemCategory;
    }

    public InventoryItemCategoryFacade getInventoryItemCategoryFacade() {
        return inventoryItemCategoryFacade;
    }

    public void setInventoryItemCategoryFacade(InventoryItemCategoryFacade inventoryItemCategoryFacade) {
        this.inventoryItemCategoryFacade = inventoryItemCategoryFacade;
    }


    public InventoryItemFacade getInventoryItemFacade() {
        return inventoryItemFacade;
    }

    public void setInventoryItemFacade(InventoryItemFacade inventoryItemFacade) {
        this.inventoryItemFacade = inventoryItemFacade;
    }

    public DataModel<InventoryItem> getInventoryItems() {
        return new ListDataModel<InventoryItem>(getInventoryItemFacade().findBySQL("SELECT i FROM InventoryItem i WHERE i.retired=false ORDER BY i.name"));
    }

    public void setInventoryItems(DataModel<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public long getSelectedInventoryItemCategoryId() {
        return selectedInventoryItemCategoryId;
    }

    public void setSelectedInventoryItemCategoryId(long selectedInventoryItemCategoryId) {
        this.selectedInventoryItemCategoryId = selectedInventoryItemCategoryId;
    }
}
