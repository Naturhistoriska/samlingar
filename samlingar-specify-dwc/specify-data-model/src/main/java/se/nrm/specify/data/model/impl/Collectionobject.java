package se.nrm.specify.data.model.impl;
 
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
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "collectionobject") 
@NamedQueries({
    @NamedQuery(name = "Collectionobject.findAll", query = "SELECT c FROM Collectionobject c"),
    @NamedQuery(name = "Collectionobject.findByCollectionObjectID", query = "SELECT c FROM Collectionobject c WHERE c.collectionObjectID in :ids"), 
    @NamedQuery(name = "Collectionobject.findAllIds",
          query = "SELECT c.collectionObjectID FROM Collectionobject c where c.collection.code = :code ORDER BY c.collectionObjectID"),
    @NamedQuery(name = "Collectionobject.findByCollectionMemberID", query = "SELECT c FROM Collectionobject c WHERE c.collectionMemberID = :collectionMemberID"),
    @NamedQuery(name = "Collectionobject.findByCollectionMemberIDAndIds", 
            query = "SELECT c FROM Collectionobject c WHERE c.collectionMemberID = :collectionMemberID AND c.collectionObjectID in :ids ORDER BY c.collectionObjectID")})
public class Collectionobject extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CollectionObjectID")
    private Integer collectionObjectID;
 
    @Basic(optional = false)
    @NotNull
    @Column(name = "CollectionMemberID")
    private int collectionMemberID;
      
    @Size(max = 32)
    @Column(name = "CatalogNumber")
    private String catalogNumber;
    
    @Column(name = "CatalogedDate")
    @Temporal(TemporalType.DATE)
    private Date catalogedDate;
    
    @Column(name = "CatalogedDatePrecision")
    private Short catalogedDatePrecision;
     
    @Column(name = "Deaccessioned")
    private Boolean deaccessioned;
    
    @Lob
    @Size(max = 16777215)
    @Column(name = "Description")
    private String description;
    
    @Size(max = 50)
    @Column(name = "FieldNumber")
    private String fieldNumber;  
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Number1")
    private Float number1;
    
    @Column(name = "Number2")
    private Float number2; 
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
     
    @Lob
    @Size(max = 65535)
    @Column(name = "Text1")
    private String text1;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Text2")
    private String text2;
       
    @Size(max = 128)
    @Column(name = "ReservedText")
    private String reservedText;
     
    @Size(max = 128)
    @Column(name = "ReservedText2")
    private String reservedText2;
    
    @Size(max = 128)
    @Column(name = "ReservedText3")
    private String reservedText3;
         
    @JoinColumn(name = "AccessionID", referencedColumnName = "AccessionID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Accession accession;
    
//    @JoinColumn(name = "CatalogerID", referencedColumnName = "AgentID")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Agent cataloger;
      
    @JoinColumn(name = "CollectionID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Collection collection;
    
    @JoinColumn(name = "CollectingEventID", referencedColumnName = "CollectingEventID")
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Collectingevent collectingEvent;

    @JoinColumn(name = "CollectionObjectAttributeID", referencedColumnName = "CollectionObjectAttributeID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Collectionobjectattribute collectionObjectAttribute;
     
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collectionObject", fetch = FetchType.LAZY) 
    private Set<Determination> determinations;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collectionObject", fetch = FetchType.LAZY)
    private Set<Preparation> preparations;
     
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collectionObject", fetch = FetchType.LAZY)
    private Set<Collectionobjectattachment> collectionobjectattachments;
     
    public Collectionobject() {
    }

    public Collectionobject(Integer collectionObjectID) {
        this.collectionObjectID = collectionObjectID;
    }

    public Collectionobject(Integer collectionObjectID, Date timestampCreated, int collectionMemberID) {
        this.collectionObjectID = collectionObjectID;
        this.timestampCreated = timestampCreated;
        this.collectionMemberID = collectionMemberID;
    }

    public Integer getCollectionObjectID() {
        return collectionObjectID;
    }

    public void setCollectionObjectID(Integer collectionObjectID) {
        this.collectionObjectID = collectionObjectID;
    } 
    
    @Override
    public String getIdentityString() {
      return String.valueOf(collectionObjectID);
    }

    @Override
    public int getEntityId() {
      return collectionObjectID == null ? 0 : collectionObjectID;
    }

    public int getCollectionMemberID() {
        return collectionMemberID;
    }

    public void setCollectionMemberID(int collectionMemberID) {
        this.collectionMemberID = collectionMemberID;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public Date getCatalogedDate() {
        return catalogedDate;
    }

    public void setCatalogedDate(Date catalogedDate) {
        this.catalogedDate = catalogedDate;
    }

    public Short getCatalogedDatePrecision() {
        return catalogedDatePrecision;
    }

    public void setCatalogedDatePrecision(Short catalogedDatePrecision) {
        this.catalogedDatePrecision = catalogedDatePrecision;
    }

    public Boolean getDeaccessioned() {
        return deaccessioned;
    }

    public void setDeaccessioned(Boolean deaccessioned) {
        this.deaccessioned = deaccessioned;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(String fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public Float getNumber1() {
        return number1;
    }

    public void setNumber1(Float number1) {
        this.number1 = number1;
    }

    public Float getNumber2() {
        return number2;
    }

    public void setNumber2(Float number2) {
        this.number2 = number2;
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

    public String getReservedText() {
        return reservedText;
    }

    public void setReservedText(String reservedText) {
        this.reservedText = reservedText;
    }

    public String getReservedText2() {
        return reservedText2;
    }

    public void setReservedText2(String reservedText2) {
        this.reservedText2 = reservedText2;
    }

    public String getReservedText3() {
        return reservedText3;
    }

    public void setReservedText3(String reservedText3) {
        this.reservedText3 = reservedText3;
    }

    public Accession getAccession() {
        return accession;
    }

    public void setAccession(Accession accession) {
        this.accession = accession;
    }
 
    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
     
    public Collectingevent getCollectingEvent() {
        return collectingEvent;
    }

    public void setCollectingEvent(Collectingevent collectingEvent) {
        this.collectingEvent = collectingEvent;
    }
       
    public Collectionobjectattribute getCollectionObjectAttribute() {
        return collectionObjectAttribute;
    }

    public void setCollectionObjectAttribute(Collectionobjectattribute collectionObjectAttribute) {
        this.collectionObjectAttribute = collectionObjectAttribute;
    }
 
    public Set<Determination> getDeterminations() {
        return determinations;
    }

    public void setDeterminations(Set<Determination> determinations) {
        this.determinations = determinations;
    }
 
    public Set<Preparation> getPreparations() {
        return preparations;
    }

    public void setPreparationList(Set<Preparation> preparations) {
        this.preparations = preparations;
    }
 
    public Set<Collectionobjectattachment> getCollectionobjectattachments() {
        return collectionobjectattachments;
    }

    public void setCollectionobjectattachments(Set<Collectionobjectattachment> collectionobjectattachments) {
        this.collectionobjectattachments = collectionobjectattachments;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (collectionObjectID != null ? collectionObjectID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Collectionobject)) {
            return false;
        }
        Collectionobject other = (Collectionobject) object;
        return !((this.collectionObjectID == null && other.collectionObjectID != null) || (this.collectionObjectID != null && !this.collectionObjectID.equals(other.collectionObjectID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Collectionobject[ collectionObjectID=" + collectionObjectID + " ]";
    }
    
}
