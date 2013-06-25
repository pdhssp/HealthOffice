/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.*;
import gov.sp.health.facade.UnitFacade;
import gov.sp.health.facade.AppImageFacade;
import gov.sp.health.facade.CategoryFacade;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.ItemFacade;
import gov.sp.health.facade.ItemUnitFacade;
import gov.sp.health.facade.LocationFacade;
import gov.sp.health.facade.PersonFacade;
import java.io.ByteArrayInputStream;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author buddhika
 */
@ManagedBean
@SessionScoped
public class ImageController  implements Serializable {

    StreamedContent scImage;
    StreamedContent scImageById;
    private UploadedFile file;
    @EJB
    AppImageFacade appImageFacade;
    @EJB
    UnitFacade unitFacade;
    @EJB
    InstitutionFacade insFacade;
    @EJB
    LocationFacade locFacade;
    @EJB
    PersonFacade perFacade;
    @EJB
    ItemFacade itemFacade;
    @EJB
    ItemUnitFacade itemUnitFacade;
    @EJB
    CategoryFacade catFacade;
    //
    //
    Unit unit;
    Location location;
    Institution institution;
    Person person;
    Item item;
    ItemUnit itemUnit;
    Category category;
    Letter letter;
    //
    AppImage appImage;
    List<AppImage> appImages;

    public StreamedContent getScImageById() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getRenderResponse()) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Get ID value from actual request param.
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            AppImage temImg = getAppImageFacade().find(Long.valueOf(id));
            return new DefaultStreamedContent(new ByteArrayInputStream(temImg.getBaImage()), temImg.getFileType());
        }
    }

    public void setScImageById(StreamedContent scImageById) {
        this.scImageById = scImageById;
    }

    public StreamedContent getScImage() {
        return scImage;
    }

    public List<AppImage> getAppImages() {
        if (appImages == null) {
            appImages = new ArrayList<AppImage>();
        }
        System.out.println("Getting app images - count is" + appImages.size());
        return appImages;
    }

    public void setAppImages(List<AppImage> appImages) {
        this.appImages = appImages;
    }

    public void setScImage(StreamedContent scImage) {
        this.scImage = scImage;
    }

    public Unit getUnit() {
        return unit;
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
        if (letter != null) {
            prepareImages("Select ai from AppImage ai Where ai.letter.id = " + letter.getId());
        } else {
            appImages = null;
        }
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        if (unit != null) {
            prepareImages("Select ai from AppImage ai Where ai.unit.id = " + unit.getId());
        } else {
            appImages = null;
        }
    }

    public UnitFacade getUnitFacade() {
        return unitFacade;
    }

    public void setUnitFacade(UnitFacade unitFacade) {
        this.unitFacade = unitFacade;
    }

    public AppImage getAppImage() {
        return appImage;
    }

    public void setAppImage(AppImage appImage) {
        this.appImage = appImage;
    }

    public AppImageFacade getAppImageFacade() {
        return appImageFacade;
    }

    public void setAppImageFacade(AppImageFacade appImageFacade) {
        this.appImageFacade = appImageFacade;
    }

    /**
     * Creates a new instance of ImageController
     */
    public ImageController() {
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void dispplayUnitImage() {
        try {
            appImage = getAppImageFacade().findFirstBySQL("Select ai from AppImage ai Where ai.unit.id = " + getUnit().getId());
            System.out.println("Diaplaying Image " + appImage.toString());
            scImage = new DefaultStreamedContent(new ByteArrayInputStream(getAppImage().getBaImage()), getAppImage().getFileType());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void dispplayUnitImageById() {
        try {
            appImage = getAppImageFacade().findFirstBySQL("Select ai from AppImage ai Where ai.unit.id = " + getUnit().getId());
            System.out.println("Diaplaying Image " + appImage.toString());
            scImage = new DefaultStreamedContent(new ByteArrayInputStream(getAppImage().getBaImage()), getAppImage().getFileType());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void prepareImages(String sql) {
        appImages = getAppImageFacade().findBySQL(sql);
    }

    public void dispplayUnitImages() {
        if (unit == null) {
            appImages = null;
        }
        dispplayUnitImage();
        List<StreamedContent> lstSc = new ArrayList<StreamedContent>();
        List<AppImage> lstImgs = getAppImageFacade().findBySQL("Select ai from AppImage ai Where ai.unit.id = " + getUnit().getId());
        System.out.println("Going to array");
        for (AppImage ai : lstImgs) {
            System.out.println("within array");
            try {
                lstSc.add(new DefaultStreamedContent(new ByteArrayInputStream(ai.getBaImage()), ai.getFileType()));
                System.out.println("completed Try");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        appImages = lstImgs;
    }

    public CategoryFacade getCatFacade() {
        return catFacade;
    }

    public void setCatFacade(CategoryFacade catFacade) {
        this.catFacade = catFacade;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category == null && category.getId() != null) {
            prepareImages("Select ai from AppImage ai Where ai.category.id = " + category.getId());
        } else {
            appImages = null;
        }
    }

    public InstitutionFacade getInsFacade() {
        return insFacade;
    }

    public void setInsFacade(InstitutionFacade insFacade) {
        this.insFacade = insFacade;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
        if (institution == null && institution.getId() != null) {
            prepareImages("Select ai from AppImage ai Where ai.institution.id = " + institution.getId());
        } else {
            appImages = null;
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        if (item == null && item.getId() != null) {
            prepareImages("Select ai from AppImage ai Where ai.item.id = " + item.getId());
        } else {
            appImages = null;
        }
    }

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
        if (itemUnit == null && itemUnit.getId() != null) {
            prepareImages("Select ai from AppImage ai Where ai.itemUnit.id = " + itemUnit.getId());
        } else {
            appImages = null;
        }
    }

    public ItemUnitFacade getItemUnitFacade() {
        return itemUnitFacade;
    }

    public void setItemUnitFacade(ItemUnitFacade itemUnitFacade) {
        this.itemUnitFacade = itemUnitFacade;
    }

    public LocationFacade getLocFacade() {
        return locFacade;
    }

    public void setLocFacade(LocationFacade locFacade) {
        this.locFacade = locFacade;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        if (location != null && location.getId() != null) {
            prepareImages("Select ai from AppImage ai Where ai.location.id = " + location.getId());
        } else {
            appImages = null;
        }
    }

    public PersonFacade getPerFacade() {
        return perFacade;
    }

    public void setPerFacade(PersonFacade perFacade) {
        this.perFacade = perFacade;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        if (person == null && person.getId() != null) {
            prepareImages("Select ai from AppImage ai Where ai.person.id = " + person.getId());
        } else {
            appImages = null;
        }
    }

    public void saveUnitImage() {
        if (unit == null) {
            JsfUtil.addErrorMessage("Please select a unit");
            return;
        }
        appImage = new AppImage();
        appImage.setUnit(unit);
        saveImage();
        setUnit(unit);
    }

    public void saveItemUnitImage() {
        if (itemUnit == null) {
            JsfUtil.addErrorMessage("Please select a location");
            return;
        }
        appImage = new AppImage();
        appImage.setItemUnit(itemUnit);
        saveImage();
        setItemUnit(itemUnit);
    }

    public void saveLetterImage() {
        if (letter == null) {
            JsfUtil.addErrorMessage("Please select a letter");
            return;
        }
        appImage = new AppImage();
        appImage.setLetter(letter);
        saveImage();
        setLetter(letter);
    }

        public void saveLocationImage() {
        if (location == null) {
            JsfUtil.addErrorMessage("Please select a location");
            return;
        }
        appImage = new AppImage();
        appImage.setLocation(location);
        saveImage();
        setLocation(location);
    }
    
       public void savePersonImage() {
        if (person == null) {
            JsfUtil.addErrorMessage("Please select a Person");
            return;
        }
        appImage = new AppImage();
        appImage.setPerson(person);
        saveImage();
        setPerson(person);
    }

    public void saveImage() {
        InputStream in;
        if (file == null) {
            JsfUtil.addErrorMessage("Please upload an image");
            return;
        }
        JsfUtil.addSuccessMessage(file.getFileName());
        try {
            appImage.setFileName(file.getFileName());
            appImage.setFileType(file.getContentType());
            in = file.getInputstream();
            appImage.setBaImage(IOUtils.toByteArray(in));
            appImageFacade.create(appImage);
            JsfUtil.addSuccessMessage(file.getFileName() + " saved successfully");
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }

    }

//    public byte[] PersistentImage(BufferedImage image) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        //new JPEGImageEncoderImpl(out).encode(image);
//        return out.toByteArray();
//    }
//
//    public Image makeImage(byte[] data) throws IOException {
//        ByteArrayInputStream in = new ByteArrayInputStream(data);
//        return new JPEGImageDecoderImpl(in).decodeAsBufferedImage();
//    }
}
