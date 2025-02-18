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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size; 
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "determination") 
@NamedQueries({
    @NamedQuery(name = "Determination.findAll", query = "SELECT d FROM Determination d"),
    @NamedQuery(name = "Determination.findByDeterminationID", query = "SELECT d FROM Determination d WHERE d.determinationID = :determinationID")})
public class Determination extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DeterminationID")
    private Integer determinationID;
      
    @Column(name = "DeterminedDate")
    @Temporal(TemporalType.DATE)
    private Date determinedDate;
      
    @Basic(optional = false)
    @NotNull
    @Column(name = "IsCurrent")
    private boolean isCurrent;
        
    @Size(max = 50)
    @Column(name = "TypeStatusName")
    private String typeStatusName;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
 
    @JoinColumn(name = "TaxonID", referencedColumnName = "TaxonID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Taxon taxon;
    
    @JoinColumn(name = "PreferredTaxonID", referencedColumnName = "TaxonID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Taxon preferredTaxon;
     
     
    @JoinColumn(name = "CollectionObjectID", referencedColumnName = "CollectionObjectID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Collectionobject collectionObject;
     
    @JoinColumn(name = "DeterminerID", referencedColumnName = "AgentID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent determiner;

    public Determination() {
    }

    public Determination(Integer determinationID) {
        this.determinationID = determinationID;
    }

    public Determination(Integer determinationID, Date timestampCreated, boolean isCurrent) {
        this.determinationID = determinationID;
        this.timestampCreated = timestampCreated; 
        this.isCurrent = isCurrent;
    }

    public Integer getDeterminationID() {
        return determinationID;
    }

    public void setDeterminationID(Integer determinationID) {
        this.determinationID = determinationID;
    }
        
    @Override
    public String getIdentityString() {
        return String.valueOf(determinationID);
    }
     
    @Override
    public int getEntityId() {
        return determinationID;
    }
  
    public Date getDeterminedDate() {
        return determinedDate;
    }

    public void setDeterminedDate(Date determinedDate) {
        this.determinedDate = determinedDate;
    }

    public boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
 
    public String getTypeStatusName() {
        return typeStatusName;
    }

    public void setTypeStatusName(String typeStatusName) {
        this.typeStatusName = typeStatusName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
 
    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    public Taxon getPreferredTaxon() {
        return preferredTaxon;
    }

    public void setPreferredTaxon(Taxon preferredTaxon) {
        this.preferredTaxon = preferredTaxon;
    }
 
    public Collectionobject getCollectionObject() {
        return collectionObject;
    }

    public void setCollectionObject(Collectionobject collectionObject) {
        this.collectionObject = collectionObject;
    }
 
    public Agent getDeterminer() {
        return determiner;
    }

    public void setDeterminer(Agent determiner) {
        this.determiner = determiner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (determinationID != null ? determinationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Determination)) {
            return false;
        }
        Determination other = (Determination) object;
        return !((this.determinationID == null && other.determinationID != null) || (this.determinationID != null && !this.determinationID.equals(other.determinationID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Determination[ determinationID=" + determinationID + " ]";
    }
    
}
