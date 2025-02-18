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
@Table(name = "accession") 
@NamedQueries({
    @NamedQuery(name = "Accession.findAll", query = "SELECT a FROM Accession a"),
    @NamedQuery(name = "Accession.findByAccessionID", query = "SELECT a FROM Accession a WHERE a.accessionID = :accessionID"),  
    @NamedQuery(name = "Accession.findByAccessionNumber", query = "SELECT a FROM Accession a WHERE a.accessionNumber = :accessionNumber") })
public class Accession extends BaseEntity {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AccessionID")
    private Integer accessionID;
       
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "AccessionNumber")
    private String accessionNumber;
      
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
     
    public Accession() {
    }

    public Accession(Integer accessionID) {
        this.accessionID = accessionID; 
    }

    public Accession(Integer accessionID, Date timestampCreated, String accessionNumber) {
        this.accessionID = accessionID;
        this.timestampCreated = timestampCreated;
        this.accessionNumber = accessionNumber;
    }

    @Override
    public String getIdentityString() {
        return String.valueOf(accessionID);
    }

    @Override
    public int getEntityId() {
        return accessionID;
    }

    public Integer getAccessionID() {
        return accessionID;
    }

    public void setAccessionID(Integer accessionID) {
        this.accessionID = accessionID;
    }
 
    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
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
        hash += (accessionID != null ? accessionID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accession)) {
            return false;
        }
        Accession other = (Accession) object;
        return !((this.accessionID == null && other.accessionID != null) || (this.accessionID != null && !this.accessionID.equals(other.accessionID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Accession[ accessionID=" + accessionID + " ]";
    } 
}
