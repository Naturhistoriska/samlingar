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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;  
import javax.persistence.Table;  
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;  
import se.nrm.specify.data.model.BaseEntity; 

/**
 *
 * @author idali
 */
@Entity
@Table(name = "storage") 
@NamedQueries({
    @NamedQuery(name = "Storage.findAll", query = "SELECT s FROM Storage s"),
    @NamedQuery(name = "Storage.findByStorageID", query = "SELECT s FROM Storage s WHERE s.storageID = :storageID")})
public class Storage extends BaseEntity {
 
    @Size(max = 16)
    @Column(name = "Abbrev")
    private String abbrev;
    
    @Size(max = 255)
    @Column(name = "FullName")
    private String fullName;
     
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "Name")
    private String name;
     
    @Basic(optional = false)
    @NotNull
    @Column(name = "RankID")
    private int rankID;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "StorageID")
    private Integer storageID;
     
    
    @Column(name = "IsAccepted")
    private Boolean isAccepted;
    
     
    @JoinColumn(name = "AcceptedID", referencedColumnName = "StorageID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Storage accepted;
     
    @JoinColumn(name = "ParentID", referencedColumnName = "StorageID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Storage parent;

    public Storage() {
    }

    public Storage(Integer storageID) {
        this.storageID = storageID;
    }

    public Storage(Integer storageID, Date timestampCreated, String name, int rankID) {
        this.storageID = storageID;
        this.timestampCreated = timestampCreated;
        this.name = name;
        this.rankID = rankID;
    }

    public Integer getStorageID() {
        return storageID;
    }

    public void setStorageID(Integer storageID) {
        this.storageID = storageID;
    }

    @Override
    public String getIdentityString() {
        return String.valueOf(storageID);
    }
     
    @Override
    public int getEntityId() {
        return storageID;
    }

 
    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

 
    public Storage getAccepted() {
        return accepted;
    }

    public void setAccepted(Storage accepted) {
        this.accepted = accepted;
    }

     
    public Storage getParent() {
        return parent;
    }

    public void setParentID(Storage parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storageID != null ? storageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Storage)) {
            return false;
        }
        Storage other = (Storage) object;
        return !((this.storageID == null && other.storageID != null) || (this.storageID != null && !this.storageID.equals(other.storageID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Storage[ storageID=" + storageID + " ]";
    }
 
    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 

    public int getRankID() {
        return rankID;
    }

    public void setRankID(int rankID) {
        this.rankID = rankID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
 
 
    
    
}
