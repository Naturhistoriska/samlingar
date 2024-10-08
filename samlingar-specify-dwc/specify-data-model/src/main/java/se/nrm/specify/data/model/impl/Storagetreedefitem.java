package se.nrm.specify.data.model.impl;
 
import java.util.Date; 
import javax.persistence.Basic; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; 
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
@Table(name = "storagetreedefitem") 
@NamedQueries({
    @NamedQuery(name = "Storagetreedefitem.findAll", query = "SELECT s FROM Storagetreedefitem s"),
    @NamedQuery(name = "Storagetreedefitem.findByStorageTreeDefItemID", query = "SELECT s FROM Storagetreedefitem s WHERE s.storageTreeDefItemID = :storageTreeDefItemID")})
public class Storagetreedefitem extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "StorageTreeDefItemID")
    private Integer storageTreeDefItemID;
      
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "Name")
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "RankID")
    private int rankID;
      
    @Size(max = 64)
    @Column(name = "Title")
    private String title;
     
    public Storagetreedefitem() {
    }

    public Storagetreedefitem(Integer storageTreeDefItemID) {
        this.storageTreeDefItemID = storageTreeDefItemID;
    }

    public Storagetreedefitem(Integer storageTreeDefItemID, Date timestampCreated, String name, int rankID) {
        this.storageTreeDefItemID = storageTreeDefItemID;
        this.timestampCreated = timestampCreated;
        this.name = name;
        this.rankID = rankID;
    }

    public Integer getStorageTreeDefItemID() {
        return storageTreeDefItemID;
    }

    public void setStorageTreeDefItemID(Integer storageTreeDefItemID) {
        this.storageTreeDefItemID = storageTreeDefItemID;
    }
     
    @Override
    public String getIdentityString() {
        return String.valueOf(storageTreeDefItemID);
    }
     
     
    @Override
    public int getEntityId() {
        return storageTreeDefItemID;
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
 
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storageTreeDefItemID != null ? storageTreeDefItemID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Storagetreedefitem)) {
            return false;
        }
        Storagetreedefitem other = (Storagetreedefitem) object;
        return !((this.storageTreeDefItemID == null && other.storageTreeDefItemID != null) || (this.storageTreeDefItemID != null && !this.storageTreeDefItemID.equals(other.storageTreeDefItemID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Storagetreedefitem[ storageTreeDefItemID=" + storageTreeDefItemID + " ]";
    }
    
}
