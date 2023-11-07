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
       
    @Size(max = 50)
    @Column(name = "Lat1Text")
    private String lat1Text;
    
    @Size(max = 50)
    @Column(name = "Lat2Text")
    private String lat2Text;
     
    @Column(name = "Latitude1")
    private BigDecimal latitude1;
    
    @Column(name = "Latitude2")
    private BigDecimal latitude2;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LocalityName")
    private String localityName;
    
    @Size(max = 50)
    @Column(name = "Long1Text")
    private String long1Text;
    
    @Size(max = 50)
    @Column(name = "Long2Text")
    private String long2Text;
    
    @Column(name = "Longitude1")
    private BigDecimal longitude1;
    
    @Column(name = "Longitude2")
    private BigDecimal longitude2;
    
    @Column(name = "MaxElevation")
    private Double maxElevation;
    
    @Column(name = "MinElevation")
    private Double minElevation;
    
    @Size(max = 255)
    @Column(name = "NamedPlace")
    private String namedPlace;
      
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @Size(max = 32)
    @Column(name = "ShortName")
    private String shortName;
        
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
     
    public String getLat1Text() {
        return lat1Text;
    }

    public void setLat1Text(String lat1Text) {
        this.lat1Text = lat1Text;
    }

    public String getLat2Text() {
        return lat2Text;
    }

    public void setLat2Text(String lat2Text) {
        this.lat2Text = lat2Text;
    }
 
    public BigDecimal getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(BigDecimal latitude1) {
        this.latitude1 = latitude1;
    }

    public BigDecimal getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(BigDecimal latitude2) {
        this.latitude2 = latitude2;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getLong1Text() {
        return long1Text;
    }

    public void setLong1Text(String long1Text) {
        this.long1Text = long1Text;
    }

    public String getLong2Text() {
        return long2Text;
    }

    public void setLong2Text(String long2Text) {
        this.long2Text = long2Text;
    }

    public BigDecimal getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(BigDecimal longitude1) {
        this.longitude1 = longitude1;
    }

    public BigDecimal getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(BigDecimal longitude2) {
        this.longitude2 = longitude2;
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

    public String getNamedPlace() {
        return namedPlace;
    }

    public void setNamedPlace(String namedPlace) {
        this.namedPlace = namedPlace;
    }
 
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
 
//    public Discipline getDiscipline() {
//        return discipline;
//    }
//
//    public void setDiscipline(Discipline discipline) {
//        this.discipline = discipline;
//    }
 
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
