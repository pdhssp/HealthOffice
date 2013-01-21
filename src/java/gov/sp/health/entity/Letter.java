/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author buddhika
 */
@Entity
public class Letter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //Main Properties
    String name;
    String code;
    String description;
    @Lob
    byte[] letterImage;
    @Lob
    String letterContent;
    Boolean registered;
    //Created Properties
    @ManyToOne
    WebUser creater;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date createdAt;
    //Retairing properties
    boolean retired;
    @ManyToOne
    WebUser retirer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date retiredAt;
    String retireComments;
    @ManyToOne
    Institution fromInstitution;
    @ManyToOne
    Unit fromUnit;
    @ManyToOne
    Location fromLocation;
    @ManyToOne
    Person fromPerson;
    @ManyToOne
    Institution toInstitution;
    @ManyToOne
    Unit toUnit;
    @ManyToOne
    Location toLocation;
    @ManyToOne
    Person toPerson;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date lettterDate;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date letterTime;
    @Lob
    String letterComments;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date receivedDate;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date receivedTime;
    @Lob
    String receiveComments;
    Boolean sent;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date sentDate;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date sentTime;
    @Lob
    String sentComments;
    @ManyToOne
    Letter previousLetter;
    @ManyToOne
    Letter nextLetter;
    @ManyToOne
    Category category;
    @ManyToOne
    Subject subject;
    Boolean printed;

    public Boolean getPrinted() {
        return printed;
    }

    public void setPrinted(Boolean printed) {
        this.printed = printed;
    }
    
    
    
    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    
    
    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public WebUser getCreater() {
        return creater;
    }

    public void setCreater(WebUser creater) {
        this.creater = creater;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Institution getFromInstitution() {
        return fromInstitution;
    }

    public void setFromInstitution(Institution fromInstitution) {
        this.fromInstitution = fromInstitution;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Person getFromPerson() {
        return fromPerson;
    }

    public void setFromPerson(Person fromPerson) {
        this.fromPerson = fromPerson;
    }

    public Unit getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(Unit fromUnit) {
        this.fromUnit = fromUnit;
    }

    public String getLetterComments() {
        return letterComments;
    }

    public void setLetterComments(String letterComments) {
        this.letterComments = letterComments;
    }

    public String getLetterContent() {
        return letterContent;
    }

    public void setLetterContent(String letterContent) {
        this.letterContent = letterContent;
    }

    public byte[] getLetterImage() {
        return letterImage;
    }

    public void setLetterImage(byte[] letterImage) {
        this.letterImage = letterImage;
    }

    public Date getLetterTime() {
        return letterTime;
    }

    public void setLetterTime(Date letterTime) {
        this.letterTime = letterTime;
    }

    public Date getLettterDate() {
        return lettterDate;
    }

    public void setLettterDate(Date lettterDate) {
        this.lettterDate = lettterDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Letter getNextLetter() {
        return nextLetter;
    }

    public void setNextLetter(Letter nextLetter) {
        this.nextLetter = nextLetter;
    }

    public Letter getPreviousLetter() {
        return previousLetter;
    }

    public void setPreviousLetter(Letter previousLetter) {
        this.previousLetter = previousLetter;
    }

    public String getReceiveComments() {
        return receiveComments;
    }

    public void setReceiveComments(String receiveComments) {
        this.receiveComments = receiveComments;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getRetireComments() {
        return retireComments;
    }

    public void setRetireComments(String retireComments) {
        this.retireComments = retireComments;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public Date getRetiredAt() {
        return retiredAt;
    }

    public void setRetiredAt(Date retiredAt) {
        this.retiredAt = retiredAt;
    }

    public WebUser getRetirer() {
        return retirer;
    }

    public void setRetirer(WebUser retirer) {
        this.retirer = retirer;
    }

    public String getSentComments() {
        return sentComments;
    }

    public void setSentComments(String sentComments) {
        this.sentComments = sentComments;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Institution getToInstitution() {
        return toInstitution;
    }

    public void setToInstitution(Institution toInstitution) {
        this.toInstitution = toInstitution;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

    public Person getToPerson() {
        return toPerson;
    }

    public void setToPerson(Person toPerson) {
        this.toPerson = toPerson;
    }

    public Unit getToUnit() {
        return toUnit;
    }

    public void setToUnit(Unit toUnit) {
        this.toUnit = toUnit;
    }

    
    
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
        if (!(object instanceof Letter)) {
            return false;
        }
        Letter other = (Letter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.Letter[ id=" + id + " ]";
    }
}
