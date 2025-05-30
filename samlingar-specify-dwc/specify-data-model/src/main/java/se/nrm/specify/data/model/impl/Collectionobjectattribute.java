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
@Table(name = "collectionobjectattribute") 
@NamedQueries({
    @NamedQuery(name = "Collectionobjectattribute.findAll", query = "SELECT c FROM Collectionobjectattribute c"),
    @NamedQuery(name = "Collectionobjectattribute.findByCollectionObjectAttributeID", query = "SELECT c FROM Collectionobjectattribute c WHERE c.collectionObjectAttributeID = :collectionObjectAttributeID")})
public class Collectionobjectattribute extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CollectionObjectAttributeID")
    private Integer collectionObjectAttributeID;
  
 
    @Basic(optional = false)
    @NotNull()
    @Column(name = "CollectionMemberID")
    private int collectionMemberID;
 
     
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
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Text3")
    private String text3;
    
    @Size(max = 50)
    @Column(name = "Text4")
    private String text4;
             
//    @OneToMany(mappedBy = "collectionObjectAttributeID")
//    private Collection<Collectionobject> collectionobjectCollection;
//    
     

     
    public Collectionobjectattribute() {
    }

    public Collectionobjectattribute(Integer collectionObjectAttributeID) {
        this.collectionObjectAttributeID = collectionObjectAttributeID;
    }

    public Collectionobjectattribute(Integer collectionObjectAttributeID, Date timestampCreated, int collectionMemberID) {
        this.collectionObjectAttributeID = collectionObjectAttributeID;
        this.timestampCreated = timestampCreated;
        this.collectionMemberID = collectionMemberID;
    }

    public Integer getCollectionObjectAttributeID() {
        return collectionObjectAttributeID;
    }

    public void setCollectionObjectAttributeID(Integer collectionObjectAttributeID) {
        this.collectionObjectAttributeID = collectionObjectAttributeID;
    }
 
    @Override
    public String getIdentityString() {
      return String.valueOf(collectionObjectAttributeID);
    }

    @Override
    public int getEntityId() {
      return collectionObjectAttributeID;
    }
    @Override   
    public int hashCode() {
        int hash = 0;
        hash += (collectionObjectAttributeID != null ? collectionObjectAttributeID.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Collectionobjectattribute)) {
            return false;
        }
        Collectionobjectattribute other = (Collectionobjectattribute) object;
        return !((this.collectionObjectAttributeID == null && other.collectionObjectAttributeID != null) || (this.collectionObjectAttributeID != null && !this.collectionObjectAttributeID.equals(other.collectionObjectAttributeID)));
    }
    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Collectionobjectattribute[ collectionObjectAttributeID=" + collectionObjectAttributeID + " ]";
    }
 
 
    public int getCollectionMemberID() {
        return collectionMemberID;
    }


    public void setCollectionMemberID(int collectionMemberID) {
        this.collectionMemberID = collectionMemberID;
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

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }
 
//    @XmlTransient
//    public Collection<Collectionobject> getCollectionobjectCollection() {
//        return collectionobjectCollection;
//    }
//
//    public void setCollectionobjectCollection(Collection<Collectionobject> collectionobjectCollection) {
//        this.collectionobjectCollection = collectionobjectCollection;
//    }
// 
    
}
