/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.data.ArticleType;
import gov.sp.health.data.CommonFunctions;
import gov.sp.health.entity.AppImage;
import gov.sp.health.facade.ArticleFacade;
import gov.sp.health.entity.Article;
import gov.sp.health.entity.ArticleCategory;
import gov.sp.health.facade.AppImageFacade;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class ArticleController implements Serializable {

    @EJB
    private ArticleFacade ejbFacade;
    @EJB
    private AppImageFacade imageFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    @ManagedProperty(value = "#{articleCategoryController}")
    ArticleCategoryController articleCategoryController;
    List<Article> lstItems;
    private Article current;
    ArticleType articleType;
    private DataModel<Article> items = null;
    String selectText = "";
    AppImage currentImg;
    List<AppImage> currentImgs;
    StreamedContent scImage;
    StreamedContent scImageById;
    private UploadedFile file;
    List<Article> welcomes;
    private List<Article> newsItems;
    private List<Article> eventItems;
    private List<Article> announcements;
    private List<Article> mchItems;
    private List<Article> ncdItems;
    private List<Article> epidItems;
    private List<Article> curativeItems;
    private List<Article> generalInfoItems;
    private List<Article> regulations;
    private List<Article> trainings;
    private List<Article> circulars;
    private List<Article> tenders;
    private List<Article> gallaryItems;
    private List<Article> otherItems;
    List<Article> allExceptWelcomesAndEvents;
    private List<Article> selectedArticles;
    private Article welcome;
    private Article newsItem;
    private Article eventItem;
    private Article announcement;
    private Article mchItem;
    private Article ncdItem;
    private Article epidItem;
    private Article curativeItem;
    private Article generalInfoItem;
    private Article regulation;
    private Article training;
    private Article circular;
    private Article tender;
    private Article gallaryItem;
    private Article otherItem;

    public List<Article> getAllExceptWelcomesAndEvents() {
        if (allExceptWelcomesAndEvents == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name <> 'Welcome' and a.category.name <> 'EventItem' order by a.orderNo";
            allExceptWelcomesAndEvents = getFacade().findBySQL(sql);
        }
        return allExceptWelcomesAndEvents;
    }

    public void setAllExceptWelcomesAndEvents(List<Article> allExceptWelcomesAndEvents) {
        this.allExceptWelcomesAndEvents = allExceptWelcomesAndEvents;
    }

    public ArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
    }

    public StreamedContent getScImage() {
        return scImage;
    }

    public void setScImage(StreamedContent scImage) {
        this.scImage = scImage;
    }

    public StreamedContent getScImageById() {
        return scImageById;
    }

    public void setScImageById(StreamedContent scImageById) {
        this.scImageById = scImageById;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void saveWithImage() {
        saveSelected();
        InputStream in;
        if (file == null) {
            JsfUtil.addErrorMessage("Please upload an image");
            return;
        }
        JsfUtil.addSuccessMessage(file.getFileName());
        try {
            if (!file.getFileName().trim().equals("")) {
                System.out.println("Article is " + current.toString());
                System.out.println("Img is " + getCurrentImg().toString());
                getCurrentImg().setArticle(current);
                getCurrentImg().setFileName(file.getFileName());
                getCurrentImg().setFileType(file.getContentType());
                in = file.getInputstream();
                getCurrentImg().setBaImage(IOUtils.toByteArray(in));
                if (getCurrentImg().getId() == null || getCurrentImg().getId() == 0) {
                    imageFacade.create(getCurrentImg());
                } else {
                    imageFacade.edit(getCurrentImg());
                }
                System.out.println("current image article id is " + getCurrentImg().getArticle().toString());
                saveArticleImages(current);
                JsfUtil.addSuccessMessage(file.getFileName() + " saved successfully");
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }

    }

    public void saveArticleImages(Article a) {
        System.out.println("saving article images");
        System.out.println("Article is " + a.toString());
        if (a.getId() != null && a.getId() != 0) {
            System.out.println("Article is existing");
            String sql;
            sql = "Select ai.id from AppImage ai Where ai.article.id = " + a.getId() + " order by ai.id desc";
            System.out.println("sql is " + sql);
            List<Long> lstLng;
            lstLng = getImageFacade().longListBySql(sql);
            Long temLng;
            sql = "Select max(ai.id) from AppImage ai Where ai.article.id = " + a.getId() + " order by ai.id desc";
//            temLng= getImageFacade().findAggregateLong(sql);
            System.out.println("Long list size is " + lstLng.size());
            System.out.println("Long List is " + lstLng.toString());
            System.out.println("going to set image list");
            a.setImgIds(lstLng);
            System.out.println("sat the image list");
            System.out.println("imgList in article is " + a.getImgIds().toString());
            if (!lstLng.isEmpty()) {
                System.out.println("enter to id not empty");
                System.out.println("going to get long value");
                temLng = lstLng.get(0);
                System.out.println("got long value as " + temLng);
                System.out.println("setting the long value to article");
                a.setImgId(temLng);
                System.out.println("finished not empty");
            } else {
                System.out.println("is empty");
                a.setImgId(0l);
                System.out.println("finished empty");
            }
            System.out.println("going to save article");
            getFacade().edit(a);
            System.out.println("saved article");
            System.out.println("article is " + a.toString());
        } else {
            System.out.println("id and list null");
            a.setImgId(0l);
            a.setImgIds(new ArrayList<Long>());
        }
    }

    public StreamedContent getImageById() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getRenderResponse()) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Get ID value from actual request param.
            String id;
            id = context.getExternalContext().getRequestParameterMap().get("id");
            System.out.println("Id is " + id);
            
            long idVal;

            if (CommonFunctions.isLongPositive(id)) {
                idVal = Long.valueOf(id);
            } else {
                idVal = 0l;
            }


            if (idVal != 0) {
                AppImage temImg = getImageFacade().find(Long.valueOf(id));
                if (temImg != null) {
                    return new DefaultStreamedContent(new ByteArrayInputStream(temImg.getBaImage()), temImg.getFileType());
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public AppImageFacade getImageFacade() {
        return imageFacade;
    }

    public void setImageFacade(AppImageFacade imageFacade) {
        this.imageFacade = imageFacade;
    }

    public void prepareImages(String sql) {
        currentImgs = getImageFacade().findBySQL(sql);
        if (currentImgs.size() > 0) {
            currentImg = currentImgs.get(0);
        } else {
            currentImg = null;
        }
    }

    public AppImage getCurrentImg() {
        if (currentImg == null) {
            currentImg = new AppImage();
        }
        return currentImg;
    }

    public void setCurrentImg(AppImage currentImg) {
        this.currentImg = currentImg;
    }

    public List<AppImage> getCurrentImgs() {
        return currentImgs;
    }

    public void setCurrentImgs(List<AppImage> currentImgs) {
        this.currentImgs = currentImgs;
    }

    public ArticleCategoryController getArticleCategoryController() {
        return articleCategoryController;
    }

    public void setArticleCategoryController(ArticleCategoryController articleCategoryController) {
        this.articleCategoryController = articleCategoryController;
    }

    public String addWelcome() {
        setArticleType(ArticleType.Welcome);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addNewsItem() {
        setArticleType(ArticleType.NewsItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().name(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addEventItem() {
        setArticleType(ArticleType.EventItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addAnnouncement() {
        setArticleType(ArticleType.Announcement);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addMchItem() {
        setArticleType(ArticleType.MchItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addNcdItem() {
        setArticleType(ArticleType.NcdItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addEpidItem() {
        setArticleType(ArticleType.EpidItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addCurativeItem() {
        setArticleType(ArticleType.CurativeItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addGeneralInfoItem() {
        setArticleType(ArticleType.GeneralInfoItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addRegulation() {
        setArticleType(ArticleType.Regulation);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addTraining() {
        setArticleType(ArticleType.Training);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addCircular() {
        setArticleType(ArticleType.Circular);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addTender() {
        setArticleType(ArticleType.Tender);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addGallaryItem() {
        setArticleType(ArticleType.GallaryItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String addOtherItem() {
        setArticleType(ArticleType.OtherItem);
        current = new Article();
        ArticleCategory cat;
        cat = articleCategoryController.searchItem(getArticleType().toString(), true);
        current.setCategory(cat);
        return "article";
    }

    public String listWelcome() {
        setSelectedArticles(getWelcomes());
        return "article_list";
    }

    public String listNewsItem() {
        setSelectedArticles(getNewsItems());
        return "article_list";
    }

    public String listEventItem() {
        setSelectedArticles(getEventItems());
        return "article_list";
    }

    public String listAnnouncement() {
        setSelectedArticles(getAnnouncements());
        return "article_list";
    }

    public String listMchItem() {
        setSelectedArticles(getMchItems());
        return "article_list";
    }

    public String listNcdItem() {
        setSelectedArticles(getNcdItems());
        return "article_list";
    }

    public String listEpidItem() {
        setSelectedArticles(getEpidItems());
        return "article_list";
    }

    public String listCurativeItem() {
        setSelectedArticles(getCurativeItems());
        return "article_list";
    }

    public String listGeneralInfoItem() {
        setSelectedArticles(getGeneralInfoItems());
        return "article_list";
    }

    public String listRegulation() {
        setSelectedArticles(getRegulations());
        return "article_list";
    }

    public String listTraining() {
        setSelectedArticles(getTrainings());
        return "article_list";
    }

    public String listCircular() {
        setSelectedArticles(getCirculars());
        return "article_list";
    }

    public String listTender() {
        setSelectedArticles(getTenders());
        return "article_list";
    }

    public String listGallaryItem() {
        setSelectedArticles(getGallaryItems());
        return "article_list";
    }

    public String listOtherItem() {
        setSelectedArticles(getOtherItems());
        return "article_list";
    }

    public ArticleFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ArticleFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<Article> getWelcomes() {
        if (welcomes == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='Welcome' order by a.orderNo";
            welcomes = getFacade().findBySQL(sql);
        }
        return welcomes;
    }

    public void setWelcomes(List<Article> welcomes) {
        this.welcomes = welcomes;
    }

    public ArticleController() {
    }

    public List<Article> getLstItems() {
        return getFacade().findBySQL("Select d From Article d");
    }

    public void setLstItems(List<Article> lstItems) {
        this.lstItems = lstItems;
    }

    public Article getCurrent() {
        if (current == null) {
            current = new Article();
        }
        return current;
    }

    public void setCurrent(Article current) {
        this.current = current;
        String sql;
        sql = "Select ai from AppImage ai Where ai.article.id = " + current.getId() + " order by ai.id desc";
        prepareImages(sql);
    }

    private ArticleFacade getFacade() {
        return ejbFacade;
    }

    public DataModel<Article> getItems() {
        items = new ListDataModel(getFacade().findAll());
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
                    current = (Article) items.getRowData();
                } else {
                    current = null;
                }
            }
        }
        return items;

    }

    private void recreateModel() {
        items = null;
        welcomes = null;
    }

    public void saveSelected() {
        if (sessionController.getPrivilege().isMsEdit() == false) {
            JsfUtil.addErrorMessage("You are not autherized to make changes to any content");
            return;
        }
        if (getCurrent().getId() == null || getCurrent().getId() == 0) {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        } else {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        }
        recreateModel();
        selectText = "";
    }

    public void addDirectly() {
        JsfUtil.addSuccessMessage("1");
        try {

            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);

            getFacade().create(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
            current = new Article();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
        }

    }

    public void delete() {
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
        current = null;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
        searchItems();
    }

    public List<Article> getNewsItems() {
        if (newsItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='NewsItem' order by a.orderNo";
            newsItems = getFacade().findBySQL(sql);
        }
        if (newsItems.size() > 0) {
            newsItem = newsItems.get(0);
        } else {
            newsItem = new Article();
        }
        return newsItems;
    }

    public void setNewsItems(List<Article> newsItems) {
        this.newsItems = newsItems;
    }

    public List<Article> getEventItems() {
        if (eventItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='EventItem' order by a.orderNo";
            eventItems = getFacade().findBySQL(sql);
        }
        if (eventItems.size() > 0) {
            eventItem = eventItems.get(0);
        } else {
            eventItem = new Article();
        }
        return eventItems;
    }

    public void setEventItems(List<Article> eventItems) {
        this.eventItems = eventItems;
    }

    public List<Article> getAnnouncements() {
        if (announcements == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='Announcement' order by a.orderNo";
            announcements = getFacade().findBySQL(sql);
        }
        if (announcements.size() > 0) {
            announcement = announcements.get(0);
        } else {
            announcement = new Article();
        }
        return announcements;
    }

    public void setAnnouncements(List<Article> announcements) {
        this.announcements = announcements;
    }

    public List<Article> getMchItems() {
        if (mchItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='MchItem' order by a.orderNo";
            mchItems = getFacade().findBySQL(sql);
        }
        if (mchItems.size() > 0) {
            mchItem = mchItems.get(0);
        } else {
            mchItem = new Article();
        }
        return mchItems;
    }

    public void setMchItems(List<Article> mchItems) {
        this.mchItems = mchItems;
    }

    public List<Article> getNcdItems() {
        if (ncdItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='NcdItem' order by a.orderNo";
            ncdItems = getFacade().findBySQL(sql);
        }
        if (ncdItems.size() > 0) {
            ncdItem = ncdItems.get(0);
        } else {
            ncdItem = new Article();
        }
        return ncdItems;
    }

    public void setNcdItems(List<Article> ncdItems) {
        this.ncdItems = ncdItems;
    }

    public List<Article> getEpidItems() {
        if (epidItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='EpidItem' order by a.orderNo";
            epidItems = getFacade().findBySQL(sql);
        }
        if (epidItems.size() > 0) {
            epidItem = epidItems.get(0);
        } else {
            epidItem = new Article();
        }
        return epidItems;
    }

    public void setEpidItems(List<Article> epidItems) {
        this.epidItems = epidItems;
    }

    public List<Article> getCurativeItems() {
        if (curativeItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='CurativeItem' order by a.orderNo";
            curativeItems = getFacade().findBySQL(sql);
        }
        if (curativeItems.size() > 0) {
            curativeItem = curativeItems.get(0);
        } else {
            curativeItem = new Article();
        }
        return curativeItems;
    }

    public void setCurativeItems(List<Article> curativeItems) {
        this.curativeItems = curativeItems;
    }

    public List<Article> getGeneralInfoItems() {
        if (generalInfoItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='GeneralInfoItem' order by a.orderNo";
            generalInfoItems = getFacade().findBySQL(sql);
        }
        if (generalInfoItems.size() > 0) {
            generalInfoItem = generalInfoItems.get(0);
        } else {
            generalInfoItem = new Article();
        }
        return generalInfoItems;
    }

    public void setGeneralInfoItems(List<Article> generalInfoItems) {
        this.generalInfoItems = generalInfoItems;
    }

    public List<Article> getRegulations() {
        if (regulations == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='Regulation' order by a.orderNo";
            regulations = getFacade().findBySQL(sql);
        }
        if (regulations.size() > 0) {
            regulation = regulations.get(0);
        } else {
            regulation = new Article();
        }
        return regulations;
    }

    public void setRegulations(List<Article> regulations) {
        this.regulations = regulations;
    }

    public List<Article> getTrainings() {
        if (trainings == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='Training' order by a.orderNo";
            trainings = getFacade().findBySQL(sql);
        }
        if (trainings.size() > 0) {
            training = trainings.get(0);
        } else {
            training = new Article();
        }
        return trainings;
    }

    public void setTrainings(List<Article> trainings) {
        this.trainings = trainings;
    }

    public List<Article> getCirculars() {
        if (circulars == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='Circular' order by a.orderNo";
            circulars = getFacade().findBySQL(sql);
        }
        if (circulars.size() > 0) {
            circular = circulars.get(0);
        } else {
            circular = new Article();
        }
        return circulars;
    }

    public void setCirculars(List<Article> circulars) {
        this.circulars = circulars;
    }

    public List<Article> getTenders() {
        if (tenders == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='Tender' order by a.orderNo";
            tenders = getFacade().findBySQL(sql);
        }
        if (tenders.size() > 0) {
            tender = tenders.get(0);
        } else {
            tender = new Article();
        }
        return tenders;
    }

    public void setTenders(List<Article> tenders) {
        this.tenders = tenders;
    }

    public List<Article> getGallaryItems() {
        if (gallaryItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='GallaryItem' order by a.orderNo";
            gallaryItems = getFacade().findBySQL(sql);
        }
        if (gallaryItems.size() > 0) {
            gallaryItem = gallaryItems.get(0);
        } else {
            gallaryItem = new Article();
        }
        return gallaryItems;
    }

    public void setGallaryItems(List<Article> gallaryItems) {
        this.gallaryItems = gallaryItems;
    }

    public List<Article> getOtherItems() {
        if (otherItems == null) {
            String sql = "select a from Article a where a.retired=false and a.category.name ='OtherItem' order by a.orderNo";
            otherItems = getFacade().findBySQL(sql);
        }
        if (otherItems.size() > 0) {
            otherItem = otherItems.get(0);
        } else {
            otherItem = new Article();
        }
        return otherItems;
    }

    public void setOtherItems(List<Article> otherItems) {
        this.otherItems = otherItems;
    }

    public Article getWelcome() {
        return welcome;
    }

    public void setWelcome(Article welcome) {
        this.welcome = welcome;
    }

    public Article getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(Article newsItem) {
        this.newsItem = newsItem;
    }

    public Article getEventItem() {
        return eventItem;
    }

    public void setEventItem(Article eventItem) {
        this.eventItem = eventItem;
    }

    public Article getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Article announcement) {
        this.announcement = announcement;
    }

    public Article getMchItem() {
        return mchItem;
    }

    public void setMchItem(Article mchItem) {
        this.mchItem = mchItem;
    }

    public Article getNcdItem() {
        return ncdItem;
    }

    public void setNcdItem(Article ncdItem) {
        this.ncdItem = ncdItem;
    }

    public Article getEpidItem() {
        return epidItem;
    }

    public void setEpidItem(Article epidItem) {
        this.epidItem = epidItem;
    }

    public Article getCurativeItem() {
        return curativeItem;
    }

    public void setCurativeItem(Article curativeItem) {
        this.curativeItem = curativeItem;
    }

    public Article getGeneralInfoItem() {
        return generalInfoItem;
    }

    public void setGeneralInfoItem(Article generalInfoItem) {
        this.generalInfoItem = generalInfoItem;
    }

    public Article getRegulation() {
        return regulation;
    }

    public void setRegulation(Article regulation) {
        this.regulation = regulation;
    }

    public Article getTraining() {
        return training;
    }

    public void setTraining(Article training) {
        this.training = training;
    }

    public Article getCircular() {
        return circular;
    }

    public void setCircular(Article circular) {
        this.circular = circular;
    }

    public Article getTender() {
        return tender;
    }

    public void setTender(Article tender) {
        this.tender = tender;
    }

    public Article getGallaryItem() {
        return gallaryItem;
    }

    public void setGallaryItem(Article gallaryItem) {
        this.gallaryItem = gallaryItem;
    }

    public Article getOtherItem() {
        return otherItem;
    }

    public void setOtherItem(Article otherItem) {
        this.otherItem = otherItem;
    }

    public List<Article> getSelectedArticles() {
        return selectedArticles;
    }

    public void setSelectedArticles(List<Article> selectedArticles) {
        this.selectedArticles = selectedArticles;
    }

    @FacesConverter(forClass = Article.class)
    public static class ArticleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ArticleController controller = (ArticleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "articleController");
            return controller.ejbFacade.find(getKey(value));
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

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Article) {
                Article o = (Article) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ArticleController.class.getName());
            }
        }
    }
}
