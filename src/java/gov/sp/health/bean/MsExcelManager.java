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
import gov.sp.health.facade.*;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
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
@Named
@RequestScoped
public class MsExcelManager implements Serializable {

    /**
     *
     * EJBs
     *
     */
    @EJB
    AtmFacade atmFacade;
    @EJB
    VtmFacade vtmFacade;
    @EJB
    AmpFacade ampFacade;
    @EJB
    VmpFacade vmpFacade;
    @EJB
    AmppFacade amppFacade;
    @EJB
    VmppFacade vmppFacade;
    @EJB
    VtmInAmpFacade vtmInAmpFacade;
    @EJB
    StrengthUnitFacade strengthUnitFacade;
    @EJB
    IssueUnitFacade issueUnitFacade;
    @EJB
    PackUnitFacade packUnitFacade;
    @EJB
    PharmaceuticalItemCategoryFacade pharmaceuticalItemCategoryFacade;
    /**
     *
     * Values of Excel Columns
     *
     */
    int itemCategoryCol;
    int whareHouseCol;
    int srNoCol;
    int levelCol;
    int vtmCol;
    int atmCol;
    int vmpCol;
    int ampCol;
    int amppCol;
    int vmppCol;
    int issueUnitCol;
    int strengthOfIssueUnitCol;
    int strengthUnitCol;
    int issueUnitsPerPackCol;
    int packUnitCol;
    int startRow;
    /**
     * DataModals
     *
     */
    DataModel<Vtm> vtms;
    DataModel<Amp> amps;
    DataModel<Ampp> ampps;
    /**
     *
     * Uploading File
     *
     */
    private UploadedFile file;

    /**
     * Creates a new instance of DemographyExcelManager
     */
    public MsExcelManager() {
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public int getWhareHouseCol() {
        return whareHouseCol;
    }

    public void setWhareHouseCol(int whareHouseCol) {
        this.whareHouseCol = whareHouseCol;
    }

    public int getSrNoCol() {
        return srNoCol;
    }

    public void setSrNoCol(int srNoCol) {
        this.srNoCol = srNoCol;
    }

    public int getLevelCol() {
        return levelCol;
    }

    public void setLevelCol(int levelCol) {
        this.levelCol = levelCol;
    }

    public String importToExcel() {
        String temStr;
        String temStr1;
        String temStr2;
        String temStr3;
        String temStr4;

        PharmaceuticalItemCategory temCat;
        Vtm temVtm;
        Atm temAtm;
        Vmp temVmp;
        Amp temAmp;
        Ampp temAmpp;
        Vmpp temVmpp;
        VtmInAmp temVtmInAmp;
        Unit unit;
        IssueUnit temIssueUnit;
        StrengthUnit temStrengthUnit;
        PackUnit temPackUnit;
        Double strengthOfIssueUnit;
        Double issueUnitsPerPack;

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
                //Category
                cell = sheet.getCell(itemCategoryCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temCat = pharmaceuticalItemCategoryFacade.findFirstBySQL("SELECT c FROM PharmaceuticalItemCategory c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temCat == null) {
                        temCat = new PharmaceuticalItemCategory();
                        temCat.setName(temStr);
                        pharmaceuticalItemCategoryFacade.create(temCat);
                    } else {
                        temCat = null;
                    }
                } else {
                    temCat = null;
                }


                //Strength Unit
                cell = sheet.getCell(strengthUnitCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temStrengthUnit = strengthUnitFacade.findFirstBySQL("SELECT c FROM StrengthUnit c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temStrengthUnit == null) {
                        temStrengthUnit = new StrengthUnit();
                        temStrengthUnit.setName(temStr);
                        strengthUnitFacade.create(temStrengthUnit);
                    } else {
                        temStrengthUnit = null;
                    }
                } else {
                    temStrengthUnit = null;
                }
                //Pack Unit
                cell = sheet.getCell(packUnitCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temPackUnit = packUnitFacade.findFirstBySQL("SELECT c FROM PackUnit c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temPackUnit == null) {
                        temPackUnit = new PackUnit();
                        temPackUnit.setName(temStr);
                        packUnitFacade.create(temPackUnit);
                    } else {
                        temPackUnit = null;
                    }
                } else {
                    temPackUnit = null;
                }
                //Issue Unit
                cell = sheet.getCell(issueUnitCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temIssueUnit = issueUnitFacade.findFirstBySQL("SELECT c FROM IssueUnit c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temIssueUnit == null) {
                        temIssueUnit = new IssueUnit();
                        temIssueUnit.setName(temStr);
                        issueUnitFacade.create(temIssueUnit);
                    } else {
                        temIssueUnit = null;
                    }
                } else {
                    temIssueUnit = null;
                }
                //StrengthOfAnIssueUnit
                cell = sheet.getCell(strengthOfIssueUnitCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    try {
                        strengthOfIssueUnit = Double.parseDouble(temStr);
                    } catch (Exception e) {
                        strengthOfIssueUnit = 0.0;
                    }
                } else {
                    strengthOfIssueUnit = 0.0;
                }
                //StrengthOfAnIssueUnit
                cell = sheet.getCell(issueUnitsPerPackCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    try {
                        issueUnitsPerPack = Double.parseDouble(temStr);
                    } catch (Exception e) {
                        issueUnitsPerPack = 0.0;
                    }
                } else {
                    issueUnitsPerPack = 0.0;
                }
                //


                //Vtm
                cell = sheet.getCell(vtmCol, i);
                temStr = cell.getContents();


                if (!temStr.equals("")) {
                    temVtm = vtmFacade.findFirstBySQL("SELECT c FROM Vtm c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temVtm == null) {
                        temVtm = new Vtm();
                        temVtm.setName(temStr);
                        vtmFacade.create(temVtm);
                    } else {
                        temVtm = null;
                    }
                } else {
                    temVtm = null;
                }
                //Atm
                cell = sheet.getCell(atmCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temAtm = atmFacade.findFirstBySQL("SELECT c FROM Atm c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temAtm == null) {
                        temAtm = new Atm();
                        temAtm.setName(temStr);
                        atmFacade.create(temAtm);
                    } else {
                        temAtm = null;
                    }
                } else {
                    temAtm = null;
                }
                //Vmp

                cell = sheet.getCell(srNoCol, i);
                temStr1 = cell.getContents();
                cell = sheet.getCell(levelCol, i);
                temStr2 = cell.getContents();
                cell = sheet.getCell(whareHouseCol, i);
                temStr4 = cell.getContents();

                cell = sheet.getCell(vmpCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temVmp = vmpFacade.findFirstBySQL("SELECT c FROM Vmp c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temVmp == null) {
                        temVmp = new Vmp();
                        temVmp.setName(temStr);
                        temVmp.setCode(temStr1);
                        temVmp.setLevelNo(Integer.valueOf(temStr2));
                        temVmp.setCategory(temCat);
                        temVmp.setWhNo(temStr4);
                        temVmp.setItemUnit(temStrengthUnit);
                        temVmp.setItemQuantity(strengthOfIssueUnit);

                        vmpFacade.create(temVmp);
                    } else {
                        temVmp = null;
                    }
                } else {
                    temVmp = null;
                }
                //Amp
                cell = sheet.getCell(ampCol, i);
                temStr = cell.getContents();

                if (!temStr.equals("")) {
                    temAmp = ampFacade.findFirstBySQL("SELECT c FROM Amp c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temAmp == null) {
                        temAmp = new Amp();
                        temAmp.setName(temStr);
                        temAmp.setItemUnit(temStrengthUnit);
                        temAmp.setItemQuantity(strengthOfIssueUnit);
                        getAmpFacade().create(temAmp);
                    } else {
                        temAmp = null;
                    }
                } else {
                    temAmp = null;
                }
                //Ampp
                cell = sheet.getCell(amppCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temAmpp = amppFacade.findFirstBySQL("SELECT c FROM Ampp c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temAmpp == null) {
                        temAmpp = new Ampp();
                        temAmpp.setName(temStr);
                        temAmpp.setItemUnit(temPackUnit);
                        temAmp.setItemQuantity(issueUnitsPerPack);
                        amppFacade.create(temAmpp);
                    } else {
                        temAmpp = null;
                    }
                } else {
                    temAmpp = null;
                }
                //Vmpp
                cell = sheet.getCell(vmppCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    temVmpp = vmppFacade.findFirstBySQL("SELECT c FROM Vmpp c Where c.name = '" + temStr + "' AND c.retired=false");
                    if (temVmpp == null) {
                        temVmpp = new Vmpp();
                        temVmpp.setItemUnit(temPackUnit);
                        temVmpp.setItemQuantity(issueUnitsPerPack);
                        temVmpp.setName(temStr);
                        vmppFacade.create(temVmpp);
                    } else {
                        temVmpp = null;
                    }
                } else {
                    temVmpp = null;
                }


                //
                //
                if (temAtm != null) {
                    temAtm.setVtm(temVtm);
                    atmFacade.edit(temAtm);
                    JsfUtil.addSuccessMessage(temAmp.getName() + " saved.");
                }
                //
                if (temAmp != null) {
                    temAmp.setVmp(temVmp);
                    ampFacade.edit(temAmp);
                }
                //
                if (temAmpp != null) {
                    temAmpp.setVmpp(temVmpp);
                    temAmpp.setAmp(temAmp);
                    temAmpp.setPackUnit(temPackUnit);
                    temAmpp.setIssueUnitsPerPack(issueUnitsPerPack);
                    amppFacade.edit(temAmpp);
                }
                //
                temVtmInAmp = new VtmInAmp();
                temVtmInAmp.setAmp(temAmp);
                temVtmInAmp.setVtm(temVtm);
                temVtmInAmp.setStrengthUnit(temStrengthUnit);
                temVtmInAmp.setStrength(strengthOfIssueUnit);
                vtmInAmpFacade.create(temVtmInAmp);
                //



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

    public int getAmpCol() {
        return ampCol;
    }

    public void setAmpCol(int ampCol) {
        this.ampCol = ampCol;
    }

    public AmpFacade getAmpFacade() {
        return ampFacade;
    }

    public void setAmpFacade(AmpFacade ampFacade) {
        this.ampFacade = ampFacade;
    }

    public int getAmppCol() {
        return amppCol;
    }

    public void setAmppCol(int amppCol) {
        this.amppCol = amppCol;
    }

    public AmppFacade getAmppFacade() {
        return amppFacade;
    }

    public void setAmppFacade(AmppFacade amppFacade) {
        this.amppFacade = amppFacade;
    }

    public int getAtmCol() {
        return atmCol;
    }

    public void setAtmCol(int atmCol) {
        this.atmCol = atmCol;
    }

    public AtmFacade getAtmFacade() {
        return atmFacade;
    }

    public void setAtmFacade(AtmFacade atmFacade) {
        this.atmFacade = atmFacade;
    }

    public int getIssueUnitCol() {
        return issueUnitCol;
    }

    public void setIssueUnitCol(int issueUnitCol) {
        this.issueUnitCol = issueUnitCol;
    }

    public IssueUnitFacade getIssueUnitFacade() {
        return issueUnitFacade;
    }

    public void setIssueUnitFacade(IssueUnitFacade issueUnitFacade) {
        this.issueUnitFacade = issueUnitFacade;
    }

    public int getIssueUnitsPerPackCol() {
        return issueUnitsPerPackCol;
    }

    public void setIssueUnitsPerPackCol(int issueUnitsPerPackCol) {
        this.issueUnitsPerPackCol = issueUnitsPerPackCol;
    }

    public int getItemCategoryCol() {
        return itemCategoryCol;
    }

    public void setItemCategoryCol(int itemCategoryCol) {
        this.itemCategoryCol = itemCategoryCol;
    }

    public int getPackUnitCol() {
        return packUnitCol;
    }

    public void setPackUnitCol(int packUnitCol) {
        this.packUnitCol = packUnitCol;
    }

    public PackUnitFacade getPackUnitFacade() {
        return packUnitFacade;
    }

    public void setPackUnitFacade(PackUnitFacade packUnitFacade) {
        this.packUnitFacade = packUnitFacade;
    }

    public PharmaceuticalItemCategoryFacade getPharmaceuticalItemCategoryFacade() {
        return pharmaceuticalItemCategoryFacade;
    }

    public void setPharmaceuticalItemCategoryFacade(PharmaceuticalItemCategoryFacade pharmaceuticalItemCategoryFacade) {
        this.pharmaceuticalItemCategoryFacade = pharmaceuticalItemCategoryFacade;
    }

    public int getStrengthOfIssueUnitCol() {
        return strengthOfIssueUnitCol;
    }

    public void setStrengthOfIssueUnitCol(int strengthOfIssueUnitCol) {
        this.strengthOfIssueUnitCol = strengthOfIssueUnitCol;
    }

    public int getStrengthUnitCol() {
        return strengthUnitCol;
    }

    public void setStrengthUnitCol(int strengthUnitCol) {
        this.strengthUnitCol = strengthUnitCol;
    }

    public StrengthUnitFacade getStrengthUnitFacade() {
        return strengthUnitFacade;
    }

    public void setStrengthUnitFacade(StrengthUnitFacade strengthUnitFacade) {
        this.strengthUnitFacade = strengthUnitFacade;
    }

    public int getVmpCol() {
        return vmpCol;
    }

    public void setVmpCol(int vmpCol) {
        this.vmpCol = vmpCol;
    }

    public VmpFacade getVmpFacade() {
        return vmpFacade;
    }

    public void setVmpFacade(VmpFacade vmpFacade) {
        this.vmpFacade = vmpFacade;
    }

    public int getVmppCol() {
        return vmppCol;
    }

    public void setVmppCol(int vmppCol) {
        this.vmppCol = vmppCol;
    }

    public VmppFacade getVmppFacade() {
        return vmppFacade;
    }

    public void setVmppFacade(VmppFacade vmppFacade) {
        this.vmppFacade = vmppFacade;
    }

    public int getVtmCol() {
        return vtmCol;
    }

    public void setVtmCol(int vtmCol) {
        this.vtmCol = vtmCol;
    }

    public VtmFacade getVtmFacade() {
        return vtmFacade;
    }

    public void setVtmFacade(VtmFacade vtmFacade) {
        this.vtmFacade = vtmFacade;
    }

    public VtmInAmpFacade getVtmInAmpFacade() {
        return vtmInAmpFacade;
    }

    public void setVtmInAmpFacade(VtmInAmpFacade vtmInAmpFacade) {
        this.vtmInAmpFacade = vtmInAmpFacade;
    }

    public DataModel<Ampp> getAmpps() {
        return new ListDataModel<Ampp>(getAmppFacade().findAll());
    }

    public void setAmpps(DataModel<Ampp> ampps) {
        this.ampps = ampps;
    }

    public DataModel<Amp> getAmps() {
        return new ListDataModel<Amp>(getAmpFacade().findAll());
    }

    public void setAmps(DataModel<Amp> amps) {
        this.amps = amps;
    }

    public DataModel<Vtm> getVtms() {
        return new ListDataModel<Vtm>(getVtmFacade().findAll());
    }

    public void setVtms(DataModel<Vtm> vtms) {
        this.vtms = vtms;
    }
}
