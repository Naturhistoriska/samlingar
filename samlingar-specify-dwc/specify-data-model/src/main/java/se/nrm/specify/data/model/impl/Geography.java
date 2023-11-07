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
@Table(name = "geography") 
@NamedQueries({
    @NamedQuery(name = "Geography.findAll", query = "SELECT g FROM Geography g"),
    @NamedQuery(name = "Geography.findByGeographyID", query = "SELECT g FROM Geography g WHERE g.geographyID = :geographyID"), 
    @NamedQuery(name = "Geography.findByFullName", query = "SELECT g FROM Geography g WHERE g.fullName = :fullName"), 
    @NamedQuery(name = "Geography.findByIsAccepted", query = "SELECT g FROM Geography g WHERE g.isAccepted = :isAccepted"),
    @NamedQuery(name = "Geography.findByIsCurrent", query = "SELECT g FROM Geography g WHERE g.isCurrent = :isCurrent"),
    @NamedQuery(name = "Geography.findByRankID", query = "SELECT g FROM Geography g WHERE g.rankID = :rankID")})
public class Geography extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "GeographyID")
    private Integer geographyID;
    
    @Size(max = 16)
    @Column(name = "Abbrev")
    private String abbrev;
 
    @Size(max = 128)
    @Column(name = "CommonName")
    private String commonName;
    
    @Size(max = 500)
    @Column(name = "FullName")
    private String fullName;
     
    @Column(name = "IsAccepted")
    private Boolean isAccepted;
    
    @Column(name = "IsCurrent")
    private Boolean isCurrent;
    
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
       
    @JoinColumn(name = "ParentID", referencedColumnName = "GeographyID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Geography parent;
    
//    @JoinColumn(name = "GeographyTreeDefID", referencedColumnName = "GeographyTreeDefID")
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    private Geographytreedef geographyTreeDef;
//    
//    @JoinColumn(name = "GeographyTreeDefItemID", referencedColumnName = "GeographyTreeDefItemID")
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    private Geographytreedefitem geographyTreeDefItem;
//     
//    @JoinColumn(name = "AcceptedID", referencedColumnName = "GeographyID")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Geography accepted; 

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
    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
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
    
}
