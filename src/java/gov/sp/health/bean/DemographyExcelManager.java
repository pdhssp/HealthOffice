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
public class DemographyExcelManager  implements Serializable {

    /**
     *
     * EJBs
     *
     */
    @EJB
    DpdhsFacade dpdhsFacade;
    @EJB
    MohFacade mohFacade;
    @EJB
    PhiFacade phiFacade;
    @EJB
    PhmFacade phmFacade;
    @EJB
    GnFacade gnFacade;
    @EJB
    PopulationFacade poFacade;
    /**
     *
     * Values of Excel Columns
     *
     */
    int gnCol;
    int phmCol;
    int phiCol;
    int mohCol;
    int gnAreaCol;
    int gnPopCol;
    int startRow;
    /**
     * Selected
     *
     */
    DpdhsArea selectedArea;
    MohArea selectedMOH;
    PhiArea selectedPHI;
    //
    long selectedMohId;
    long selectedPhiId;
    long selectedDpdhsId;
    /**
     *
     * Areas
     *
     */
    DataModel<DpdhsArea> dpdhsAreas;
    DataModel<MohArea> mohAreas;
    DataModel<PhiArea> phiAreas;
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
    List<DemoTblRow> lstAllColRows;

    /**
     * Creates a new instance of DemographyExcelManager
     */
    public DemographyExcelManager() {
    }

    public long getSelectedDpdhsId() {
        return selectedArea.getId();
    }

    public void setSelectedDpdhsId(long selectedDpdhsId) {
        selectedArea = getDpdhsFacade().find(selectedDpdhsId);
        this.selectedDpdhsId = selectedDpdhsId;
    }

    
    
    public DpdhsFacade getDpdhsFacade() {
        return dpdhsFacade;
    }

    public void setDpdhsFacade(DpdhsFacade dpdhsFacade) {
        this.dpdhsFacade = dpdhsFacade;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String importToExcel() {
        GnArea gn;
        PhmArea phm;
        PhiArea phi;
        MohArea moh;
        PhmArea phmTem = null;
        PhiArea phiTem = null;
        MohArea mohTem = null;
        Population pop;
        String temStr;
        double temArea;
        long temPop;

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
                cell = sheet.getCell(mohCol, i);
                temStr = cell.getContents();
                if (!temStr.equals("")) {
                    mohTem = mohFacade.findByField("name", temStr, true);
                    if (mohTem == null) {
                        moh = new MohArea();
                        moh.setName(temStr);
                        moh.setSuperArea(selectedArea);
                        mohFacade.create(moh);
                        mohTem = moh;
                    } else {
                        moh = mohTem;
                    }
                } else {
                    moh = mohTem;
                }


                cell = sheet.getCell(phiCol, i);
                temStr = cell.getContents();

                if (!temStr.equals("")) {
                    phiTem = phiFacade.findByField("name", temStr, true);
                    if (phiTem == null) {
                        phi = new PhiArea();
                        phi.setName(temStr);
                        phiFacade.create(phi);
                        phiTem = phi;
                    } else {
                        phi = phiTem;
                    }
                } else {
                    phi = phiTem;
                }

                cell = sheet.getCell(phmCol, i);
                temStr = cell.getContents();

                if (!temStr.equals("")) {
                    phmTem = phmFacade.findByField("name", temStr, true);
                    if (phmTem == null) {
                        phm = new PhmArea();
                        phm.setName(temStr);
                        phmFacade.create(phm);
                        phmTem = phm;
                    } else {
                        phm = phmTem;
                    }
                } else {
                    phm = phmTem;
                }

                cell = sheet.getCell(gnPopCol, i);
                if (cell.getType() == CellType.NUMBER) {
                    temStr = cell.getContents();
                    try {
                        temPop = Long.valueOf(temStr);
                    } catch (Exception e) {
                        temPop = 0;
                    }
                } else {
                    temPop = 0;
                }

                cell = sheet.getCell(gnAreaCol, i);
                if (cell.getType() == CellType.NUMBER) {
                    temStr = cell.getContents();
                    try {
                        temArea = Double.valueOf(temStr);
                    } catch (Exception e) {
                        temArea = 0;
                    }
                } else {
                    temArea = 0;
                }



                cell = sheet.getCell(gnCol, i);
                temStr = cell.getContents();

                if (!temStr.equals("")) {
                    gn = gnFacade.findByField("name", temStr, true);
                    if (gn == null) {
                        gn = new GnArea();
                        gn.setName(temStr);
                        gnFacade.create(gn);

                    } else {
                    }
                    gn.setSuperArea(phm);
                    gnFacade.edit(gn);

                    temStr = "SELECT i FROM Population i WHERE i.retired = false AND i.area.id = " + gn.getId();
                    List<Population> lstPop = poFacade.findBySQL(temStr);
                    if (lstPop.isEmpty()) {
                        pop = new Population();
                    } else {
                        pop = lstPop.get(0);
                    }
                    pop.setAreaKm(temArea);
                    pop.setPopulationNumber(temPop);
                    pop.setArea(gn);
                    poFacade.create(pop);

                    if (phm != null) {
                        phm.setSuperArea(phi);
                        phmFacade.edit(phm);
                    }

                    if (phi != null) {
                        phi.setSuperArea(moh);
                        phiFacade.edit(phi);
                    }

                    if (moh != null) {
                        moh.setSuperArea(selectedArea);
                        mohFacade.edit(moh);
                    }

                } else {
                    gn = null;
                }
            }
            FacesMessage msg = new FacesMessage("Succesful", "All the data in Excel File Impoted to the database");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "demography_display_areas";
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
            return "";
        } catch (BiffException e) {
            JsfUtil.addErrorMessage(e.getMessage());
            return "";
        }
    }

    public String testingURL() {
        return "faces/display_areas.xhtml";
    }

    public List<DemoTblRow> listGnPopOfDPDHS() {
        String temSQL;
        double area;
        long pop;
        Population p;
        List<DemoTblRow> lst = new ArrayList<DemoTblRow>();
        if (selectedArea == null) {
            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false ";
            JsfUtil.addErrorMessage("No Area Selected");
                    
            return null;
        } else {
            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false AND i.superArea.id = " + selectedArea.getId();
        }
        List<MohArea> lstMOH = mohFacade.findBySQL(temSQL);

        for (MohArea temMOH : lstMOH) {
            temSQL = "SELECT i FROM PhiArea i WHERE i.retired = false AND i.superArea.id = " + temMOH.getId();
            List<PhiArea> lstPHI = phiFacade.findBySQL(temSQL);

            for (PhiArea temPHI : lstPHI) {
                temSQL = "SELECT i FROM PhmArea i WHERE i.retired = false AND i.superArea.id = " + temPHI.getId();
                List<PhmArea> lstPHM = phmFacade.findBySQL(temSQL);

                for (PhmArea temPHM : lstPHM) {
                    temSQL = "SELECT i FROM GnArea i WHERE i.retired = false AND i.superArea.id = " + temPHM.getId();
                    List<GnArea> lstGN = gnFacade.findBySQL(temSQL);

                    for (GnArea temGN : lstGN) {
                        temSQL = "SELECT i FROM Population i WHERE i.retired = false AND i.area.id= " + temGN.getId();
                        List<Population> lstPop = poFacade.findBySQL(temSQL);
                        if (lstPop.isEmpty()) {
                            p = new Population();
                            p.setPopulationNumber(0);
                            p.setAreaKm(0);
                        } else {
                            p = lstPop.get(0);
                        }
                        DemoTblRow row = new DemoTblRow();
                        row.setDpdhs(selectedArea);
                        row.setMoh(temMOH);
                        row.setPhi(temPHI);
                        row.setPhm(temPHM);
                        row.setGn(temGN);
                        row.setGnPop(p);
                        lst.add(row);
                    }

                }




            }
        }


        return lst;
    }

    public List<DemoTblRow> listGnPopOfMOH() {
        String temSQL;
        double area;
        long pop;
        Population p;
        List<DemoTblRow> lst = new ArrayList<DemoTblRow>();
        JsfUtil.addSuccessMessage("1");
        if (selectedArea == null) {
            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false ";
        } else {
            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false AND i.superArea.id = " + selectedArea.getId();
        }
        List<MohArea> lstMOH = mohFacade.findBySQL(temSQL);
        JsfUtil.addSuccessMessage("2");
        for (MohArea temMOH : lstMOH) {
            temSQL = "SELECT i FROM PhiArea i WHERE i.retired = false AND i.superArea.id = " + temMOH.getId();
            List<PhiArea> lstPHI = phiFacade.findBySQL(temSQL);
            JsfUtil.addSuccessMessage("3");
            for (PhiArea temPHI : lstPHI) {
                temSQL = "SELECT i FROM PhmArea i WHERE i.retired = false AND i.superArea.id = " + temPHI.getId();
                List<PhmArea> lstPHM = phmFacade.findBySQL(temSQL);
                JsfUtil.addSuccessMessage("4");
                for (PhmArea temPHM : lstPHM) {
                    temSQL = "SELECT i FROM GnArea i WHERE i.retired = false AND i.superArea.id = " + temPHM.getId();
                    List<GnArea> lstGN = gnFacade.findBySQL(temSQL);
                    JsfUtil.addSuccessMessage("5");
                    for (GnArea temGN : lstGN) {
                        temSQL = "SELECT i FROM Population i WHERE i.retired = false AND i.area.id= " + temGN.getId();
                        List<Population> lstPop = poFacade.findBySQL(temSQL);
                        if (lstPop.isEmpty()) {
                            p = new Population();
                            p.setPopulationNumber(0);
                            p.setAreaKm(0);
                        } else {
                            p = lstPop.get(0);
                        }
                        DemoTblRow row = new DemoTblRow();
                        row.setDpdhs(selectedArea);
                        row.setMoh(temMOH);
                        row.setPhi(temPHI);
                        row.setPhm(temPHM);
                        row.setGn(temGN);
                        row.setGnPop(p);
                        lst.add(row);
                    }
                }
            }
        }
        return lst;
    }

    public DataModel<MohArea> getDmMOH() {
        String temSQL;
        if (selectedArea == null) {
            return null;
        } else {
            temSQL = "SELECT i FROM MohArea i WHERE i.retired = false AND i.superArea.id = " + selectedArea.getId();
            return new ListDataModel<MohArea>(mohFacade.findBySQL(temSQL));
        }
    }

    public DataModel<PhiArea> getDmPHI() {
        String temSQL;
        if (selectedMohId == 0) {
            return null;
        } else {
            temSQL = "SELECT i FROM PhiArea i WHERE i.retired = false WHERE i.superArea.id = " + selectedMohId;
            JsfUtil.addSuccessMessage(temSQL);
            return new ListDataModel<PhiArea>(phiFacade.findBySQL(temSQL));
        }
    }

    public void deselectDpdhs() {
        selectedArea = null;
    }

    public void deselectMoh() {
        selectedMohId = 0;
        selectedMOH = null;
    }

    public void deselectPhi() {
        selectedPhiId = 0;
        selectedPHI = null;
    }

    public DataModel<DpdhsArea> getDpdhsAreas() {
        return new ListDataModel<DpdhsArea>(getDpdhsFacade().findBySQL("SELECT d FROM DpdhsArea d"));
    }

    public void setDpdhsAreas(DataModel<DpdhsArea> dpdhsAreas) {
        this.dpdhsAreas = dpdhsAreas;
    }

    public int getGnAreaCol() {
        return gnAreaCol;
    }

    public void setGnAreaCol(int gnAreaCol) {
        this.gnAreaCol = gnAreaCol;
    }

    public int getGnCol() {
        return gnCol;
    }

    public void setGnCol(int gnCol) {
        this.gnCol = gnCol;
    }

    public GnFacade getGnFacade() {
        return gnFacade;
    }

    public void setGnFacade(GnFacade gnFacade) {
        this.gnFacade = gnFacade;
    }

    public int getGnPopCol() {
        return gnPopCol;
    }

    public void setGnPopCol(int gnPopCol) {
        this.gnPopCol = gnPopCol;
    }

    public List<DemoTblRow> getLstAllColRows() {
        return listGnPopOfDPDHS();
    }

    public void setLstAllColRows(List<DemoTblRow> lstAllColRows) {
        this.lstAllColRows = lstAllColRows;
    }

    public DataModel<MohArea> getMohAreas() {
        return mohAreas;
    }

    public void setMohAreas(DataModel<MohArea> mohAreas) {
        this.mohAreas = mohAreas;
    }

    public int getMohCol() {
        return mohCol;
    }

    public void setMohCol(int mohCol) {
        this.mohCol = mohCol;
    }

    public MohFacade getMohFacade() {
        return mohFacade;
    }

    public void setMohFacade(MohFacade mohFacade) {
        this.mohFacade = mohFacade;
    }

    public DataModel<PhiArea> getPhiAreas() {
        return phiAreas;
    }

    public void setPhiAreas(DataModel<PhiArea> phiAreas) {
        this.phiAreas = phiAreas;
    }

    public int getPhiCol() {
        return phiCol;
    }

    public void setPhiCol(int phiCol) {
        this.phiCol = phiCol;
    }

    public PhiFacade getPhiFacade() {
        return phiFacade;
    }

    public void setPhiFacade(PhiFacade phiFacade) {
        this.phiFacade = phiFacade;
    }

    public int getPhmCol() {
        return phmCol;
    }

    public void setPhmCol(int phmCol) {
        this.phmCol = phmCol;
    }

    public PhmFacade getPhmFacade() {
        return phmFacade;
    }

    public void setPhmFacade(PhmFacade phmFacade) {
        this.phmFacade = phmFacade;
    }

    public PopulationFacade getPoFacade() {
        return poFacade;
    }

    public void setPoFacade(PopulationFacade poFacade) {
        this.poFacade = poFacade;
    }

    public DpdhsArea getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(DpdhsArea selectedArea) {
        this.selectedArea = selectedArea;
    }

    public MohArea getSelectedMOH() {
        return selectedMOH;
    }

    public void setSelectedMOH(MohArea selectedMOH) {
        this.selectedMOH = selectedMOH;
    }

    public long getSelectedMohId() {
        return selectedMohId;
    }

    public void setSelectedMohId(long selectedMohId) {
        this.selectedMohId = selectedMohId;
    }

    public PhiArea getSelectedPHI() {
        return selectedPHI;
    }

    public void setSelectedPHI(PhiArea selectedPHI) {
        this.selectedPHI = selectedPHI;
    }

    public long getSelectedPhiId() {
        return selectedPhiId;
    }

    public void setSelectedPhiId(long selectedPhiId) {
        this.selectedPhiId = selectedPhiId;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
}
