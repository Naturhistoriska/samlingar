package se.nrm.specify.data.model.impl;
 
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn; 
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table; 
import javax.validation.constraints.Size;
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "localitydetail") 

@NamedQueries({
    @NamedQuery(name = "Localitydetail.findAll", query = "SELECT l FROM Localitydetail l"),
    @NamedQuery(name = "Localitydetail.findByLocalitydetailID", query = "SELECT l FROM Localitydetail l WHERE l.localityDetailID = :localityDetailID")})
public class Localitydetail extends BaseEntity  {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LocalityDetailID")
    private Integer localityDetailID;
      
    @Size(max = 64)
    @Column(name = "Drainage")
    private String drainage;
        
    @Size(max = 64)
    @Column(name = "Island")
    private String island;  
    
    @Size(max = 64)
    @Column(name = "IslandGroup")
    private String islandGroup;
    
    @Size(max = 64)
    @Column(name = "NationalParkName")
    private String nationalParkName;
           
    @Size(max = 64)
    @Column(name = "WaterBody")
    private String waterBody;
 
    @JoinColumn(name = "LocalityID", referencedColumnName = "LocalityID")
    @ManyToOne
    private Locality localityID;

    public Localitydetail() {
    }

    public Localitydetail(Integer localityDetailID) {
        this.localityDetailID = localityDetailID;
    }


    public Localitydetail(Integer localityDetailID, Date timestampCreated, String localityName ) {
        this.localityDetailID = localityDetailID;
        this.timestampCreated = timestampCreated; 
    }
    
    @Override
    public String getIdentityString() {
         return String.valueOf(localityDetailID);
    }
    
    
    @Override
    public int getEntityId() {
        return localityDetailID;
    }

 
    public String getDrainage() {
        return drainage;
    }

    public void setDrainage(String drainage) {
        this.drainage = drainage;
    }
 
 
 
    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public String getIslandGroup() {
        return islandGroup;
    }

    public void setIslandGroup(String islandGroup) {
        this.islandGroup = islandGroup;
    }

    public String getNationalParkName() {
        return nationalParkName;
    }

    public void setNationalParkName(String nationalParkName) {
        this.nationalParkName = nationalParkName;
    }
 
    public String getWaterBody() {
        return waterBody;
    }

    public void setWaterBody(String waterBody) {
        this.waterBody = waterBody;
    }
 
    public Locality getLocalityID() {
        return localityID;
    }

    public void setLocalityID(Locality localityID) {
        this.localityID = localityID;
    }
    
        @Override
    public int hashCode() {
        int hash = 0;
        hash += (localityDetailID != null ? localityDetailID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localitydetail)) {
            return false;
        }
        Localitydetail other = (Localitydetail) object;
        return !((this.localityDetailID == null && other.localityDetailID != null) || (this.localityDetailID != null && !this.localityID.equals(other.localityID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.localityDetailID[ LocalityDetailID=" + localityDetailID + " ]";
    }
    
}
