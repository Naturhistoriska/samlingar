package se.nrm.specify.data.model.impl;
 
import java.math.BigDecimal; 
import java.util.Collection;
import java.util.Date; 
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size; 
import javax.xml.bind.annotation.XmlTransient;
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

      
    
    @Size(max = 50)
    @Column(name = "Datum")
    private String datum;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ElevationAccuracy")
    private Double elevationAccuracy;
    
    @Size(max = 50)
    @Column(name = "ElevationMethod")
    private String elevationMethod;
     
    @Lob()
    @Size(max = 65535)
    @Column(name = "GML")
    private String gml;
    
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid;
    
    @Size(max = 50)
    @Column(name = "Lat1Text")
    private String lat1Text;
    
    @Size(max = 50)
    @Column(name = "Lat2Text")
    private String lat2Text;
    
    @Size(max = 50)
    @Column(name = "LatLongMethod")
    private String latLongMethod;
    
    @Size(max = 50)
    @Column(name = "LatLongType")
    private String latLongType;
    
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 255)
    @Column(name = "LocalityName")
    private String localityName;
    
    @Size(max = 50)
    @Column(name = "Long1Text")
    private String long1Text;
    
    @Size(max = 50)
    @Column(name = "Long2Text")
    private String long2Text;
    
    @Size(max = 255)
    @Column(name = "NamedPlace")
    private String namedPlace;
       
    @Size(max = 50)
    @Column(name = "OriginalElevationUnit")
    private String originalElevationUnit;
      
    @Column(name = "OriginalLatLongUnit")
    private Integer originalLatLongUnit;
    
    @Size(max = 120)
    @Column(name = "RelationToNamedPlace")
    private String relationToNamedPlace;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @Size(max = 32)
    @Column(name = "ShortName")
    private String shortName;
          
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LocalityID")
    private Integer localityID;
    
    @Column(name = "LatLongAccuracy")
    private Double latLongAccuracy;
        
//    @OneToMany(mappedBy = "localityID")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localityID", fetch = FetchType.LAZY) 
    private Set<Localitydetail> localitydetail1Collection;

    @Column(name = "Latitude1")
    private BigDecimal latitude1;
     
    
    @Column(name = "Longitude1")
    private BigDecimal longitude1;
     
    
    @Column(name = "MaxElevation")
    private Double maxElevation;
    
    @Column(name = "MinElevation")
    private Double minElevation;
     
      
    @JoinColumn(name = "GeographyID", referencedColumnName = "GeographyID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Geography geography;
      
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
    public Double getLatLongAccuracy() {
        return latLongAccuracy;
    }
    public void setLatLongAccuracy(Double latLongAccuracy) {
        this.latLongAccuracy = latLongAccuracy;
    }
    @XmlTransient
    public Set<Localitydetail> getLocalitydetail1Collection() {
        return localitydetail1Collection;
    }
    public void setLocalitydetail1Collection(Set<Localitydetail> localitydetail1Collection) {
        this.localitydetail1Collection = localitydetail1Collection;
    }

   
    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
 
    public Double getElevationAccuracy() {
        return elevationAccuracy;
    }

    public void setElevationAccuracy(Double elevationAccuracy) {
        this.elevationAccuracy = elevationAccuracy;
    }

    public String getElevationMethod() {
        return elevationMethod;
    }

    public void setElevationMethod(String elevationMethod) {
        this.elevationMethod = elevationMethod;
    }

    public String getGml() {
        return gml;
    }

    public void setGml(String gml) {
        this.gml = gml;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public String getLatLongMethod() {
        return latLongMethod;
    }

    public void setLatLongMethod(String latLongMethod) {
        this.latLongMethod = latLongMethod;
    }

    public String getLatLongType() {
        return latLongType;
    }

    public void setLatLongType(String latLongType) {
        this.latLongType = latLongType;
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

    public String getNamedPlace() {
        return namedPlace;
    }

    public void setNamedPlace(String namedPlace) {
        this.namedPlace = namedPlace;
    }

    public String getOriginalElevationUnit() {
        return originalElevationUnit;
    }

    public void setOriginalElevationUnit(String originalElevationUnit) {
        this.originalElevationUnit = originalElevationUnit;
    }

    public Integer getOriginalLatLongUnit() {
        return originalLatLongUnit;
    }

    public void setOriginalLatLongUnit(Integer originalLatLongUnit) {
        this.originalLatLongUnit = originalLatLongUnit;
    }

    public String getRelationToNamedPlace() {
        return relationToNamedPlace;
    }

    public void setRelationToNamedPlace(String relationToNamedPlace) {
        this.relationToNamedPlace = relationToNamedPlace;
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
 
    
}
