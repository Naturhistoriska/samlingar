package se.nrm.specify.data.model.impl;
 
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; 
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table; 
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "attachmentimageattribute")
@NamedQueries({
    @NamedQuery(name = "Attachmentimageattribute.findAll", query = "SELECT a FROM Attachmentimageattribute a"),
    @NamedQuery(name = "Attachmentimageattribute.findByAttachmentImageAttributeID", query = "SELECT a FROM Attachmentimageattribute a WHERE a.attachmentImageAttributeID = :attachmentImageAttributeID"),
    @NamedQuery(name = "Attachmentimageattribute.findByMBImageID", query = "SELECT a FROM Attachmentimageattribute a WHERE a.mBImageID = :mBImageID")})
public class Attachmentimageattribute extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AttachmentImageAttributeID")
    private Integer attachmentImageAttributeID;
       
    @Column(name = "MBImageID")
    private Integer mBImageID;
        
    @JoinColumn(name = "MorphBankViewID", referencedColumnName = "MorphBankViewID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Morphbankview morphBankWiew;
       
    public Attachmentimageattribute() {
    }

    public Attachmentimageattribute(Integer attachmentImageAttributeID) {
        this.attachmentImageAttributeID = attachmentImageAttributeID;
    }

    public Attachmentimageattribute(Integer attachmentImageAttributeID, Date timestampCreated) {
        this.attachmentImageAttributeID = attachmentImageAttributeID;
        this.timestampCreated = timestampCreated;
    }

    public Integer getAttachmentImageAttributeID() {
        return attachmentImageAttributeID;
    }

    public void setAttachmentImageAttributeID(Integer attachmentImageAttributeID) {
        this.attachmentImageAttributeID = attachmentImageAttributeID;
    }

    @Override
    public String getIdentityString() {
        return String.valueOf(attachmentImageAttributeID);
    }

    @Override
    public int getEntityId() {
        return attachmentImageAttributeID;
    }
  
    public Integer getMBImageID() {
        return mBImageID;
    }

    public void setMBImageID(Integer mBImageID) {
        this.mBImageID = mBImageID;
    }
   
    public Morphbankview getMorphBankWiew() {
        return morphBankWiew;
    }

    public void setMorphBankWiew(Morphbankview morphBankWiew) {
        this.morphBankWiew = morphBankWiew;
    }
    
    
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attachmentImageAttributeID != null ? attachmentImageAttributeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attachmentimageattribute)) {
            return false;
        }
        Attachmentimageattribute other = (Attachmentimageattribute) object;
        return !((this.attachmentImageAttributeID == null && other.attachmentImageAttributeID != null) || (this.attachmentImageAttributeID != null && !this.attachmentImageAttributeID.equals(other.attachmentImageAttributeID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Attachmentimageattribute[ attachmentImageAttributeID=" + attachmentImageAttributeID + " ]";
    }

}
