package se.nrm.specify.data.model.impl;
 
import java.math.BigDecimal;
import java.util.Collection;
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
@Table(name = "geography") 
@NamedQueries({
    @NamedQuery(name = "Geography.findAll", query = "SELECT g FROM Geography g"),
    @NamedQuery(name = "Geography.findByGeographyID", query = "SELECT g FROM Geography g WHERE g.geographyID = :geographyID"), 
    @NamedQuery(name = "Geography.findByFullName", query = "SELECT g FROM Geography g WHERE g.fullName = :fullName"), 
    @NamedQuery(name = "Geography.findByIsAccepted", query = "SELECT g FROM Geography g WHERE g.isAccepted = :isAccepted"),
    @NamedQuery(name = "Geography.findByIsCurrent", query = "SELECT g FROM Geography g WHERE g.isCurrent = :isCurrent"),
    @NamedQuery(name = "Geography.findByRankID", query = "SELECT g FROM Geography g WHERE g.rankID = :rankID")})
public class Geography extends BaseEntity {
  
    @Size(max = 128)
    @Column(name = "CommonName")
    private String commonName;
    
    @Size(max = 500)
    @Column(name = "FullName")
    private String fullName;
    
    @Size(max = 24)
    @Column(name = "GeographyCode")
    private String geographyCode;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "GML")
    private String gml;
    
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid;
     
    @Size(max = 128)
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
    
    @Size(max = 32)
    @Column(name = "Text1")
    private String text1;
    
    @Size(max = 32)
    @Column(name = "Text2")
    private String text2;
        
//    @OneToMany(mappedBy = "acceptedID")
//    private Collection<Geography> geographyCollection1;
    
    @JoinColumn(name = "AcceptedID", referencedColumnName = "GeographyID")
    @ManyToOne 
    private Geography acceptedID;
    
//    @OneToMany(mappedBy = "geographyID")
//    private Collection<Locality> localityCollection;
// 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "GeographyID")
    private Integer geographyID;
 
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CentroidLat")
    private BigDecimal centroidLat;
    
    @Column(name = "CentroidLon")
    private BigDecimal centroidLon;
     
    
    

     
    @Column(name = "IsAccepted")
    private Boolean isAccepted;
    
    @Column(name = "IsCurrent")
    private Boolean isCurrent;
    
       
    @JoinColumn(name = "ParentID", referencedColumnName = "GeographyID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Geography parent;
   

    public Geography() {
    }

    public Geography(Integer geographyID) {
        this.geographyID = geographyID;
    }

    public Geography(Integer geographyID, Date timestampCreated, int rankID) {
        this.geographyID = geographyID;
        this.timestampCreated = timestampCreated;
        this.rankID = rankID;
    }

    public Integer getGeographyID() {
        return geographyID;
    }

    public void setGeographyID(Integer geographyID) {
        this.geographyID = geographyID;
    }
     
    @Override
    public String getIdentityString() {
        return String.valueOf(geographyID);
    }
     
    
    @Override
    public int getEntityId() {
        return geographyID;
    }
  
    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }


    public Geography getParent() {
        return parent;
    }

    public void setParent(Geography parent) {
        this.parent = parent;
    }

//    public Geographytreedef getGeographyTreeDef() {
//        return geographyTreeDef;
//    }
//
//    public void setGeographyTreeDef(Geographytreedef geographyTreeDef) {
//        this.geographyTreeDef = geographyTreeDef;
//    }
//
//    public Geographytreedefitem getGeographyTreeDefItem() {
//        return geographyTreeDefItem;
//    }
//
//    public void setGeographyTreeDefItem(Geographytreedefitem geographyTreeDefItem) {
//        this.geographyTreeDefItem = geographyTreeDefItem;
//    }
//
//    public Geography getAccepted() {
//        return accepted;
//    }
//
//    public void setAccepted(Geography accepted) {
//        this.accepted = accepted;
//    }
    
    public String getCounty() {
        if(rankID == 400) {
            return name;
        }
        if(rankID > 400) {
            Geography newParent = parent;
            while(newParent.getRankID() > 400) {
                newParent = newParent.getParent();
            }
            if(newParent.getRankID() == 400) {
                return newParent.getName();
            } 
        }
        return null;
    }

    public String getCountry() {
        if (rankID == 200) {
            return name;
        }
        if (rankID > 200) {
            Geography newParent = parent;
            while (newParent.getRankID() > 200) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 200) {
                return newParent.getName();
            }
        }
        return null;
    }

    public String getState() {
        if (rankID == 300) {
            return name;
        }
        if (rankID > 300) {
            Geography newParent = parent;
            while (newParent.getRankID() > 300) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 300) {
                return newParent.getName();
            }
        }
        return null;
    }

    public String getContinent() {
        if(rankID == 100) {
            return name;
        }
        if(rankID > 100) {
            Geography newParent = parent;
            while(newParent.getRankID() > 100) {
                newParent = newParent.getParent();
            }
            if(newParent.getRankID() == 100) {
                return newParent.getName();
            } 
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (geographyID != null ? geographyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Geography)) {
            return false;
        }
        Geography other = (Geography) object;
        return !((this.geographyID == null && other.geographyID != null) || (this.geographyID != null && !this.geographyID.equals(other.geographyID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Geography[ geographyID=" + geographyID + " ]";
    }

  


    public BigDecimal getCentroidLat() {
        return centroidLat;
    }

    public void setCentroidLat(BigDecimal centroidLat) {
        this.centroidLat = centroidLat;
    }

    public BigDecimal getCentroidLon() {
        return centroidLon;
    }

    public void setCentroidLon(BigDecimal centroidLon) {
        this.centroidLon = centroidLon;
    }

    

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
 
    public String getGeographyCode() {
        return geographyCode;
    }

    public void setGeographyCode(String geographyCode) {
        this.geographyCode = geographyCode;
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

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
 

//    @XmlTransient
//    public Collection<Geography> getGeographyCollection1() {
//        return geographyCollection1;
//    }
//
//    public void setGeographyCollection1(Collection<Geography> geographyCollection1) {
//        this.geographyCollection1 = geographyCollection1;
//    }

    public Geography getAcceptedID() {
        return acceptedID;
    }

    public void setAcceptedID(Geography acceptedID) {
        this.acceptedID = acceptedID;
    }

//    @XmlTransient
//    public Collection<Locality> getLocalityCollection() {
//        return localityCollection;
//    }
//
//    public void setLocalityCollection(Collection<Locality> localityCollection) {
//        this.localityCollection = localityCollection;
//    }

 
}
