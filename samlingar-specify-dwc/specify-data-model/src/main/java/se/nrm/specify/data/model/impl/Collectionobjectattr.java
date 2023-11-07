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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table; 
import javax.validation.constraints.NotNull;  
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "collectionobjectattr") 
@NamedQueries({
    @NamedQuery(name = "Collectionobjectattr.findAll", query = "SELECT c FROM Collectionobjectattr c"),
    @NamedQuery(name = "Collectionobjectattr.findByAttrID", query = "SELECT c FROM Collectionobjectattr c WHERE c.attrID = :attrID")})
public class Collectionobjectattr extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AttrID")
    private Integer attrID;
     
    @Basic(optional = false)
    @NotNull
    @Column(name = "CollectionMemberID")
    private int collectionMemberID;
      
    @JoinColumn(name = "CollectionObjectID", referencedColumnName = "CollectionObjectID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Collectionobject collectionObject; 
 
    @JoinColumn(name = "AttributeDefID", referencedColumnName = "AttributeDefID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Attributedef attributeDef;

    public Collectionobjectattr() {
    }

    public Collectionobjectattr(Integer attrID) {
        this.attrID = attrID;
    }

    public Collectionobjectattr(Integer attrID, Date timestampCreated, int collectionMemberID) {
        this.attrID = attrID;
        this.timestampCreated = timestampCreated;
        this.collectionMemberID = collectionMemberID;
    }

    public Integer getAttrID() {
        return attrID;
    }

    public void setAttrID(Integer attrID) {
        this.attrID = attrID;
    }
    
    @Override
    public String getIdentityString() {
        return String.valueOf(attrID);
    }
     
    @Override
    public int getEntityId() {
        return attrID;
    }
    
    public int getCollectionMemberID() {
        return collectionMemberID;
    }

    public void setCollectionMemberID(int collectionMemberID) {
        this.collectionMemberID = collectionMemberID;
    }

    public Collectionobject getCollectionObject() {
        return collectionObject;
    }

    public void setCollectionObject(Collectionobject collectionObject) {
        this.collectionObject = collectionObject;
    }

    public Attributedef getAttributeDef() {
        return attributeDef;
    }

    public void setAttributeDef(Attributedef attributeDef) {
        this.attributeDef = attributeDef;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attrID != null ? attrID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Collectionobjectattr)) {
            return false;
        }
        Collectionobjectattr other = (Collectionobjectattr) object;
        return !((this.attrID == null && other.attrID != null) || (this.attrID != null && !this.attrID.equals(other.attrID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Collectionobjectattr[ attrID=" + attrID + " ]";
    }
    
}
