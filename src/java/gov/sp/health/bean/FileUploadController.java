/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class FileUploadController  implements Serializable {

    /**
     * Creates a new instance of FileUploadController
     */
    public FileUploadController() {
    }
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void toExcel() {
        File inputWorkbook;
        Workbook w;
        InputStream in = null;
        try {

            in = file.getInputstream();
            File f = new File("D:\\Tem", file.getFileName());
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
            
            




            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            for (int j = 0; j < sheet.getColumns(); j++) {
                for (int i = 0; i < sheet.getRows(); i++) {
                    Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    if (cell.getType() == CellType.LABEL) {
                        System.out.println("I got a label "
                                + cell.getContents());
                    }

                    if (cell.getType() == CellType.NUMBER) {
                        System.out.println("I got a number "
                                + cell.getContents());
                    }

                }
            }
            FacesMessage msg = new FacesMessage("Succesful", file.getContents().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException ex) {
            Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException e) {
        }
    }

    public void upload() {
        if (file != null) {
            InputStream in = null;
            try {
                //            FacesMessage msg = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
                FacesMessage msg = new FacesMessage("Succesful", file.getContents().toString());
                in = file.getInputstream();
                File f = new File("D:\\Tem", file.getFileName());
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
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (IOException ex) {
                Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
