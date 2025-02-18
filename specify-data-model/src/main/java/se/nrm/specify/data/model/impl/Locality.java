package se.nrm.specify.data.model.impl;
 
import java.math.BigDecimal;
import java.util.Date; 
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.Table; 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size; 
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "locality") 
@NamedQueries({
    @NamedQuery(name = "Locality.findAll", query = "SELECT l FROM Locality l"),
    @NamedQuery(name = "Locality.findByLocalityID", query = "SELECT l FROM Locality l WHERE l.localityID = :localityID")})
public class Locality extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LocalityID")
    private Integer localityID;
 
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LocalityName")
    private String localityName;
    
    @Size(max = 50)
    @Column(name = "Datum")
    private String datum;
     
    @Column(name = "Latitude1")
    private BigDecimal latitude1;
      
    @Column(name = "Longitude1")
    private BigDecimal longitude1;
    
    @Column(name = "LatLongAccuracy")
    private BigDecimal latLongAccuracy;
     
    
    @Column(name = "MaxElevation")
    private Double maxElevation;
    
    @Column(name = "MinElevation")
    private Double minElevation;
     
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
     
//    @JoinColumn(name = "DisciplineID", referencedColumnName = "UserGroupScopeId")
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    private Discipline discipline;
      
    @JoinColumn(name = "GeographyID", referencedColumnName = "GeographyID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Geography geography;
    
    @OneToMany(mappedBy = "locality")
    private Set<Localitydetail> localitydetails;
    
    @OneToMany(mappedBy = "locality")
    private Set<Geocoorddetail> geocoorddetails;
      
    public Locality() {
    }

    public Locality(Integer localityID) {
        this.localityID = localityID;
    }

    public Locality(Integer localityID, Date timestampCreated, String localityName ) {
        this.localityID = localityID;
        this.timestampCreated = timestampCreated;
        this.localityName = localityName; 
    }

    public Integer getLocalityID() {
        return localityID;
    }

    public void setLocalityID(Integer localityID) {
        this.localityID = localityID;
    }
     
    @Override
    public String getIdentityString() {
        return String.valueOf(localityID);
    }
     
    @Override
    public int getEntityId() {
        return localityID;
    }
 
    public BigDecimal getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(BigDecimal latitude1) {
        this.latitude1 = latitude1;
    }
 
    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }
 
    public BigDecimal getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(BigDecimal longitude1) {
        this.longitude1 = longitude1;
    }
 
    public Double getMaxElevation() {
        return maxElevation;
    }

    public void setMaxElevation(Double maxElevation) {
        this.maxElevation = maxElevation;
    }

    public Double getMinElevation() {
        return minElevation;
    }

    public void setMinElevation(Double minElevation) {
        this.minElevation = minElevation;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public BigDecimal getLatLongAccuracy() {
        return latLongAccuracy;
    }

    public void setLatLongAccuracy(BigDecimal latLongAccuracy) {
        this.latLongAccuracy = latLongAccuracy;
    }
    
    
 
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
 
    public Geography getGeography() {
        return geography;
    }

    public void setGeography(Geography geography) {
        this.geography = geography;
    }

    public Set<Localitydetail> getLocalitydetails() {
        return localitydetails;
    }

    public void setLocalitydetails(Set<Localitydetail> localitydetails) {
        this.localitydetails = localitydetails;
    }

    public Set<Geocoorddetail> getGeocoorddetails() {
        return geocoorddetails;
    }

    public void setGeocoorddetails(Set<Geocoorddetail> geocoorddetails) {
        this.geocoorddetails = geocoorddetails;
    }
    
    
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (localityID != null ? localityID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Locality)) {
            return false;
        }
        Locality other = (Locality) object;
        return !((this.localityID == null && other.localityID != null) || (this.localityID != null && !this.localityID.equals(other.localityID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Locality[ localityID=" + localityID + " ]";
    }
    
}
