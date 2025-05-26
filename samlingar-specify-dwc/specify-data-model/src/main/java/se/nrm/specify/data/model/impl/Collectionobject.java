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
            query = "SELECT c.collectionObjectID FROM Collectionobject c where c.collectionMemberID = :collectionMemberID ORDER BY c.collectionObjectID"),
    @NamedQuery(name = "Collectionobject.findByCollectionMemberID", query = "SELECT c FROM Collectionobject c WHERE c.collectionMemberID = :collectionMemberID"),
    @NamedQuery(name = "Collectionobject.findAllIdsByCollectionCode",
            query = "SELECT c.collectionObjectID FROM Collectionobject c where c.collection.code = :code ORDER BY c.collectionObjectID"),
    @NamedQuery(name = "Collectionobject.findByCollectionMemberIDAndIds", 
            query = "SELECT c FROM Collectionobject c WHERE c.collectionMemberID = :collectionMemberID AND c.collectionObjectID in :ids ORDER BY c.collectionObjectID")})
public class Collectionobject extends BaseEntity {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CollectionObjectID")
    private Integer collectionObjectID;
    
    @Basic(optional = false)
    @NotNull()
    @Column(name = "CollectionMemberID")
    private int collectionMemberID;
    
    @Size(max = 64)
    @Column(name = "AltCatalogNumber")
    private String altCatalogNumber;
     
    @Size(max = 32)
    @Column(name = "CatalogNumber")
    private String catalogNumber;
    
    @Size(max = 32)
    @Column(name = "CatalogedDateVerbatim")
    private String catalogedDateVerbatim;
    
    @Column(name = "CountAmt")
    private Integer countAmt;
    
    @Lob
    @Size(max = 16777215)
    @Column(name = "Description")
    private String description;
    
    @Size(max = 50)
    @Column(name = "FieldNumber")
    private String fieldNumber;
    
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid;
     
//    @Size(max = 50)
//    @Column(name = "Modifier")
//    private String modifier;
       
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
    @Lob     
    @Size(max = 65535)
    @Column(name = "Text3")
    private String text3;
     
    @Size(max = 128)
    @Column(name = "ReservedText2")
    private String reservedText2;
    
    @Size(max = 128)
    @Column(name = "ReservedText3")
    private String reservedText3;
       
     
    @Lob
    @Size(max = 65535)
    @Column(name = "Text4")
    private String text4;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Text5")
    private String text5;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Text6")
    private String text6;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Text7")
    private String text7;
    
    @JoinColumn(name = "CatalogerID", referencedColumnName = "AgentID")
    @ManyToOne
    private Agent catalogerID; 
  
    @Column(name = "CatalogedDate")
    @Temporal(TemporalType.DATE)
    private Date catalogedDate;
    
    @Column(name = "CatalogedDatePrecision")
    private Short catalogedDatePrecision;
     
    @Column(name = "Deaccessioned")
    private Boolean deaccessioned;
    
     
    @JoinColumn(name = "AccessionID", referencedColumnName = "AccessionID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Accession accession;
     
      
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
    private Set<Determination> determinationList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collectionObject", fetch = FetchType.LAZY)
    private Set<Preparation> preparationList;
     
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collectionObject", fetch = FetchType.LAZY)
    private Set<Collectionobjectattachment> collectionobjectattachmentList;
     
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

    public Integer getCountAmt() {
        return countAmt;
    }

    public void setCountAmt(Integer countAmt) {
        this.countAmt = countAmt;
    }
 
    
    
    
    
//    public Integer getCountAmt() {
//        return countAmt;
//    }
//
//    public void setCountAmt(Integer countAmt) {
//        this.countAmt = countAmt;
//    }
    
    public Boolean getDeaccessioned() {
        return deaccessioned;
    }

    public void setDeaccessioned(Boolean deaccessioned) {
        this.deaccessioned = deaccessioned;
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
 
    public Set<Determination> getDeterminationList() {
        return determinationList;
    }

    public void setDeterminationList(Set<Determination> determinationList) {
        this.determinationList = determinationList;
    }
 
    public Set<Preparation> getPreparationList() {
        return preparationList;
    }

    public void setPreparationList(Set<Preparation> preparationList) {
        this.preparationList = preparationList;
    }
 
    public Set<Collectionobjectattachment> getCollectionobjectattachmentList() {
        return collectionobjectattachmentList;
    }

    public void setCollectionobjectattachmentList(Set<Collectionobjectattachment> collectionobjectattachmentList) {
        this.collectionobjectattachmentList = collectionobjectattachmentList;
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
 
 
    public int getCollectionMemberID() {
        return collectionMemberID;
    }

    public void setCollectionMemberID(int collectionMemberID) {
        this.collectionMemberID = collectionMemberID;
    }

    public String getAltCatalogNumber() {
        return altCatalogNumber;
    }

    public void setAltCatalogNumber(String altCatalogNumber) {
        this.altCatalogNumber = altCatalogNumber;
    }
 
    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public String getCatalogedDateVerbatim() {
        return catalogedDateVerbatim;
    }

    public void setCatalogedDateVerbatim(String catalogedDateVerbatim) {
        this.catalogedDateVerbatim = catalogedDateVerbatim;
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
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
 

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getText5() {
        return text5;
    }

    public void setText5(String text5) {
        this.text5 = text5;
    }

    public String getText6() {
        return text6;
    }

    public void setText6(String text6) {
        this.text6 = text6;
    }

    public String getText7() {
        return text7;
    }

    public void setText7(String text7) {
        this.text7 = text7;
    }

    public Agent getCatalogerID() {
        return catalogerID;
    }

    public void setCatalogerID(Agent catalogerID) {
        this.catalogerID = catalogerID;
    }

 
}
