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
  @NamedQuery(name = "Localitydetail.findByLocalityDetailID", 
          query = "SELECT l FROM Localitydetail l WHERE l.localityDetailID = :localityDetailID")})
public class Localitydetail extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "LocalityDetailID")
  private Integer localityDetailID;
    
  
  @Size(max = 64)
  @Column(name = "WaterBody")
  private String waterBody;
  
  @Size(max = 64)
  @Column(name = "Island")
  private String island;
  
  @Size(max = 64)
  @Column(name = "IslandGroup")
  private String islandGroup;

  @JoinColumn(name = "LocalityID", referencedColumnName = "LocalityID")
  @ManyToOne(fetch = FetchType.LAZY)
  private Locality locality;

  public Localitydetail() {
  }

  public Localitydetail(Integer localityDetailID) {
    this.localityDetailID = localityDetailID;
  }

  public Localitydetail(Integer localityDetailID, Date timestampCreated) {
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

  public Integer getLocalityDetailID() {
    return localityDetailID;
  }

  public void setLocalityDetailID(Integer localityDetailID) {
    this.localityDetailID = localityDetailID;
  }
 
  public String getWaterBody() {
    return waterBody;
  }

  public void setWaterBody(String waterBody) {
    this.waterBody = waterBody;
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

  public Locality getLocality() {
    return locality;
  }

  public void setLocality(Locality locality) {
    this.locality = locality;
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
    return !((this.localityDetailID == null && other.localityDetailID != null)
            || (this.localityDetailID != null && !this.localityDetailID.equals(other.localityDetailID)));
  }

  @Override
  public String toString() {
    return "se.nrm.dina.datamodel.Localitydetail[ localityDetailID=" + localityDetailID + " ]";
  }
}
