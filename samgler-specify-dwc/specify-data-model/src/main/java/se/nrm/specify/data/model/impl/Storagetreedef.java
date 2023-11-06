package se.nrm.specify.data.model.impl;
 
import java.util.Date; 
import javax.persistence.Basic; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; 
import javax.persistence.Lob; 
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
@Table(name = "storagetreedef") 
@NamedQueries({
    @NamedQuery(name = "Storagetreedef.findAll", query = "SELECT s FROM Storagetreedef s"),
    @NamedQuery(name = "Storagetreedef.findByStorageTreeDefID", query = "SELECT s FROM Storagetreedef s WHERE s.storageTreeDefID = :storageTreeDefID"),
    @NamedQuery(name = "Storagetreedef.findByFullNameDirection", query = "SELECT s FROM Storagetreedef s WHERE s.fullNameDirection = :fullNameDirection"),
    @NamedQuery(name = "Storagetreedef.findByName", query = "SELECT s FROM Storagetreedef s WHERE s.name = :name")})
public class Storagetreedef extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "StorageTreeDefID")
    private Integer storageTreeDefID;
     
    @Column(name = "FullNameDirection")
    private Integer fullNameDirection;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "Name")
    private String name;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks; 

    public Storagetreedef() {
    }

    public Storagetreedef(Integer storageTreeDefID) {
        this.storageTreeDefID = storageTreeDefID;
    }

    public Storagetreedef(Integer storageTreeDefID, Date timestampCreated, String name) {
        this.storageTreeDefID = storageTreeDefID;
        this.timestampCreated = timestampCreated;
        this.name = name;
    }

    public Integer getStorageTreeDefID() {
        return storageTreeDefID;
    }

    public void setStorageTreeDefID(Integer storageTreeDefID) {
        this.storageTreeDefID = storageTreeDefID;
    }
     
    @Override
    public String getIdentityString() {
        return String.valueOf(storageTreeDefID);
    }
     
    @Override
    public int getEntityId() {
        return storageTreeDefID;
    }
    
    public Integer getFullNameDirection() {
        return fullNameDirection;
    }

    public void setFullNameDirection(Integer fullNameDirection) {
        this.fullNameDirection = fullNameDirection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storageTreeDefID != null ? storageTreeDefID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Storagetreedef)) {
            return false;
        }
        Storagetreedef other = (Storagetreedef) object;
        if ((this.storageTreeDefID == null && other.storageTreeDefID != null) || (this.storageTreeDefID != null && !this.storageTreeDefID.equals(other.storageTreeDefID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Storagetreedef[ storageTreeDefID=" + storageTreeDefID + " ]";
    }
    
}
