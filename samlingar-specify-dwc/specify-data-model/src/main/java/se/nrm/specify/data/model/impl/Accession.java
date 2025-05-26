package se.nrm.specify.data.model.impl;
 
import java.math.BigDecimal; 
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
@Table(name = "accession") 
@NamedQueries({
    @NamedQuery(name = "Accession.findAll", query = "SELECT a FROM Accession a"),
    @NamedQuery(name = "Accession.findByAccessionID", query = "SELECT a FROM Accession a WHERE a.accessionID = :accessionID"),  
    @NamedQuery(name = "Accession.findByAccessionNumber", query = "SELECT a FROM Accession a WHERE a.accessionNumber = :accessionNumber"),
    @NamedQuery(name = "Accession.findByDateAccessioned", query = "SELECT a FROM Accession a WHERE a.dateAccessioned = :dateAccessioned"), 
    @NamedQuery(name = "Accession.findByDateReceived", query = "SELECT a FROM Accession a WHERE a.dateReceived = :dateReceived"), 
    @NamedQuery(name = "Accession.findByStatus", query = "SELECT a FROM Accession a WHERE a.status = :status") })
public class Accession extends BaseEntity {
    
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AccessionID")
    private Integer accessionID;
      
 
    @Size(max = 255)
    @Column(name = "AccessionCondition")
    private String accessionCondition;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "AccessionNumber")
    private String accessionNumber;
 
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @Size(max = 32)
    @Column(name = "Status")
    private String status;
    @Lob
    @Size(max = 65535)
    @Column(name = "Text1")
    private String text1;
    @Lob
    @Size(max = 65535)
    @Column(name = "Text2")
    private String text2;
    @Lob
    @Size(max = 65535)
    @Column(name = "Text3")
    private String text3;
    @Size(max = 32)
    @Column(name = "Type")
    private String type;
    @Size(max = 50)

    
    @Column(name = "DateAccessioned")
    @Temporal(TemporalType.DATE)
    private Date dateAccessioned;
    
    @Column(name = "DateAcknowledged")
    @Temporal(TemporalType.DATE)
    private Date dateAcknowledged;
    
    @Column(name = "DateReceived")
    @Temporal(TemporalType.DATE)
    private Date dateReceived; 
    
     
    @Column(name = "TotalValue")
    private BigDecimal totalValue;
    
         
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
        return accessionID == null ? 0 : accessionID;
    }

    public Integer getAccessionID() {
        return accessionID;
    }

    public void setAccessionID(Integer accessionID) {
        this.accessionID = accessionID;
    }


    public Date getDateAccessioned() {
        return dateAccessioned;
    }

    public void setDateAccessioned(Date dateAccessioned) {
        this.dateAccessioned = dateAccessioned;
    }

    public Date getDateAcknowledged() {
        return dateAcknowledged;
    }

    public void setDateAcknowledged(Date dateAcknowledged) {
        this.dateAcknowledged = dateAcknowledged;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }
  

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
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
 
    public String getAccessionCondition() {
        return accessionCondition;
    }

    public void setAccessionCondition(String accessionCondition) {
        this.accessionCondition = accessionCondition;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

 
 
}
