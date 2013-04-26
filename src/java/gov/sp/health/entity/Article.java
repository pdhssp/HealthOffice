/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author Neo
 */
@Entity
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
     //Created Properties
    @ManyToOne
    private WebUser creater;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdAt;
    //Retairing properties
    private boolean retired;
    @ManyToOne
    private WebUser retirer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date retiredAt;
    @Lob
    private String retireComments;
    private String sinhalaTopic;
    private String tamilTopic;
    private String englishTopic;
    
    @Lob
    private String sinhalaContent;
    @Lob
    private String tamilContent;
    @Lob
    private String englishContent;
    
    @ManyToOne
    ArticleCategory category;
    Long orderNo;

    @Transient
    private Long i;
    @Transient
    private List<AppImage> images;
    
    
    
    
    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }
    
    

    public ArticleCategory getCategory() {
        return category;
    }

    public void setCategory(ArticleCategory category) {
        this.category = category;
    }

    public byte[] getBaImage() {
        return baImage;
    }

    public void setBaImage(byte[] baImage) {
        this.baImage = baImage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    
    
    @Lob
    byte[] baImage;
    String fileName;
    String fileType;
    
    

    
    

    public Long getId() {
        
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.Article[ id=" + id + " ]";
    }

    public WebUser getCreater() {
        return creater;
    }

    public void setCreater(WebUser creater) {
        this.creater = creater;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public WebUser getRetirer() {
        return retirer;
    }

    public void setRetirer(WebUser retirer) {
        this.retirer = retirer;
    }

    public Date getRetiredAt() {
        return retiredAt;
    }

    public void setRetiredAt(Date retiredAt) {
        this.retiredAt = retiredAt;
    }

    public String getRetireComments() {
        return retireComments;
    }

    public void setRetireComments(String retireComments) {
        this.retireComments = retireComments;
    }

    public String getSinhalaTopic() {
        return sinhalaTopic;
    }

    public void setSinhalaTopic(String sinhalaTopic) {
        this.sinhalaTopic = sinhalaTopic;
    }

    public String getTamilTopic() {
        return tamilTopic;
    }

    public void setTamilTopic(String tamilTopic) {
        this.tamilTopic = tamilTopic;
    }

    public String getEnglishTopic() {
        return englishTopic;
    }

    public void setEnglishTopic(String englishTopic) {
        this.englishTopic = englishTopic;
    }

    public String getSinhalaContent() {
        return sinhalaContent;
    }

    public void setSinhalaContent(String sinhalaContent) {
        this.sinhalaContent = sinhalaContent;
    }

    public String getTamilContent() {
        return tamilContent;
    }

    public void setTamilContent(String tamilContent) {
        this.tamilContent = tamilContent;
    }

    public String getEnglishContent() {
        return englishContent;
    }

    public void setEnglishContent(String englishContent) {
        this.englishContent = englishContent;
    }

    public AppImage getImage() {
        return image;
    }

    public void setImage(AppImage image) {
        this.image = image;
    }

    public List<AppImage> getImages() {
        return images;
    }

    public void setImages(List<AppImage> images) {
        this.images = images;
    }
    
}
