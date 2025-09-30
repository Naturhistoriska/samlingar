package se.nrm.specify.data.model.impl;
 
import java.util.Date; 
import java.util.HashMap;
import java.util.Map;
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
import javax.persistence.Transient; 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size; 
import se.nrm.specify.data.model.BaseEntity; 

/**
 *
 * @author idali
 */
@Entity
@Table(name = "taxon") 
@NamedQueries({
    @NamedQuery(name = "Taxon.findAll", query = "SELECT t FROM Taxon t"),
    @NamedQuery(name = "Taxon.findByTaxonID", query = "SELECT t FROM Taxon t WHERE t.taxonID = :taxonID")})
public class Taxon extends BaseEntity {
 

    @Size(max = 128)
    @Column(name = "Author")
    private String author;
      
    @Size(max = 128)
    @Column(name = "CommonName")
    private String commonName;
       
    @Size(max = 255)
    @Column(name = "FullName")
    private String fullName;
     
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid;
      
     
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
    @Column(name = "TaxonID")
    private Integer taxonID;

    @Column(name = "IsAccepted")
    private Boolean isAccepted;


    @JoinColumn(name = "AcceptedID", referencedColumnName = "TaxonID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Taxon acceptedID;

    @JoinColumn(name = "TaxonTreeDefItemID", referencedColumnName = "TaxonTreeDefItemID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Taxontreedefitem taxonTreeDefItem;

    @JoinColumn(name = "ParentID", referencedColumnName = "TaxonID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Taxon parent;
 
    @OneToMany(mappedBy = "taxon", fetch = FetchType.LAZY)
    private Set<Commonnametx> commonnametxList;

    @OneToMany(mappedBy = "acceptedID", fetch = FetchType.LAZY) 
    private Set<Taxon> synomys;

    @Transient
    private StringBuilder sb;
    @Transient
    private final String slash = "/";
    
    @Transient
    private Map<String, String> taxonMap;
    
    public Taxon() {
    }

    public Taxon(Integer taxonID) {
        this.taxonID = taxonID;
    }

    public Taxon(Integer taxonID, Date timestampCreated, String name, int rankID) {
        this.taxonID = taxonID;
        this.timestampCreated = timestampCreated;
        this.name = name;
        this.rankID = rankID;
    }

    public Integer getTaxonID() {
        return taxonID;
    }

    public void setTaxonID(Integer taxonID) {
        this.taxonID = taxonID;
    }

    @Override
    public String getIdentityString() {
        return String.valueOf(taxonID);
    }

    @Override
    public int getEntityId() {
        return taxonID;
    }


    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }


    public Taxon getAcceptedID() {
        return acceptedID;
    }

    public void setAcceptedID(Taxon acceptedID) {
        this.acceptedID = acceptedID;
    }

//    public Taxontreedefitem getTaxonTreeDefItem() {
//        return taxonTreeDefItem;
//    }
//
//    public void setTaxonTreeDefItem(Taxontreedefitem taxonTreeDefItem) {
//        this.taxonTreeDefItem = taxonTreeDefItem;
//    }

    public Taxon getParent() {
        return parent;
    }

    public void setParent(Taxon parent) {
        this.parent = parent;
    }

//    public Taxontreedef getTaxonTreeDef() {
//        return taxonTreeDef;
//    }
//
//    public void setTaxonTreeDef(Taxontreedef taxonTreeDef) {
//        this.taxonTreeDef = taxonTreeDef;
//    }

    public Set<Taxon> getSynomys() {
        return synomys;
    }

    public void setSynomys(Set<Taxon> synomys) {
        this.synomys = synomys;
    }

    public Set<Commonnametx> getCommonnametxList() {
        return commonnametxList;
    }

    public void setCommonnametxList(Set<Commonnametx> commonnametxList) {
        this.commonnametxList = commonnametxList;
    }

    public Taxontreedefitem getTaxonTreeDefItem() {
        return taxonTreeDefItem;
    }

    public void setTaxonTreeDefItem(Taxontreedefitem taxonTreeDefItem) {
        this.taxonTreeDefItem = taxonTreeDefItem;
    }
    
    public String getVariety() {
        if (rankID == 240) {
            return fullName;
        }
        if (rankID > 240) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 240) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 220) {
                return newParent.getFullName();
            }
        }
        return null;
    }


    public String getSpecies() {
        if (rankID == 220) {
            return fullName;
        }
        if (rankID > 220) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 220) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 220) {
                return newParent.getFullName();
            }
        }
        return null;
    }

    public String getGenus() {
        if (rankID == 180) {
            return fullName;
        }
        if (rankID > 180) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 180) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 180) {
                return newParent.getFullName();
            }
        }
        return null;
    }

    public String getFamily() {
        if (rankID == 140) {
            return fullName;
        }
        if (rankID > 140) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 140) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 140) {
                return newParent.getFullName();
            }
        }
        return null;
    }
    
    public String getOrder() {
        if (rankID == 100) {
            return fullName;
        }
        if (rankID > 100) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 100) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 100) {
                return newParent.getFullName();
            }
        }
        return null;
    }
    
    public String getClazz() {
        if (rankID == 60) {
            return fullName;
        }
        if (rankID > 60) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 60) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 60) {
                return newParent.getFullName();
            }
        }
        return null;
    }
    
    public String getPhylum() {
        if (rankID == 30) {
            return fullName;
        }
        if (rankID > 10) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 30) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 30) {
                return newParent.getFullName();
            }
        }
        return null;
    }
        
    public String getKingdom() {
        if (rankID == 10) {
            return fullName;
        }
        if (rankID > 10) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 10) {
                newParent = newParent.getParent();
            }
            if (newParent.getRankID() == 10) {
                return newParent.getFullName();
            }
        }
        return null;
    }

    public String getHighClassification() {
        sb = new StringBuilder();
        if (rankID > 0) {
            Taxon newParent = parent;
            while (newParent.getRankID() > 0) {
                if (sb.length() > 0) {
                    sb.insert(0, slash);
                }
                sb.insert(0, newParent.getFullName());
                newParent = newParent.getParent();
            }
        }
        return sb.toString();
    }
    
    public Map<String, String> getTaxon() {
        taxonMap = new HashMap();
        
        addTaxonInMap(rankID, fullName);
        addParents();
        return taxonMap;
    }
    
    private void addTaxonInMap(int rank, String name) {
        switch (rank) {
            case 240:
                taxonMap.put("variety", name);
                break;
            case 220:
                taxonMap.put("species", name);
                break;
            case 190:
                taxonMap.put("subgenus", name);
                break;
            case 180:
                taxonMap.put("genus", name);
                break;
            case 140:
                taxonMap.put("family", name);
                break;
            case 100:
                taxonMap.put("order", name);
                break;
            case 60:
                taxonMap.put("clazz", name);
                break;
            case 40:
                taxonMap.put("subphylum", name);
                break;
            case 30:
                taxonMap.put("phylum", name);
                break;
            case 10:
                taxonMap.put("kingdom", name);
                break;
            default:
                break;
        }
    }
    
    
    private void addParents() {
        sb = new StringBuilder();
        if (rankID > 0) {
            Taxon newParent = parent;  
            while (newParent != null && newParent.getRankID() > 0) { 
                addTaxonInMap(newParent.getRankID(), newParent.getFullName()); 
                newParent = newParent.getParent();
            }
        }
    }
    
     
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxonID != null ? taxonID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taxon)) {
            return false;
        }
        Taxon other = (Taxon) object;
        return !((this.taxonID == null && other.taxonID != null) || (this.taxonID != null && !this.taxonID.equals(other.taxonID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Taxon[ taxonID=" + taxonID + " ]";
    }
 
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
 

}
