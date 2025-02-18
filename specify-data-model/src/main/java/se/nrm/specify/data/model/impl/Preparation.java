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
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "preparation") 
@NamedQueries({
    @NamedQuery(name = "Preparation.findAll", query = "SELECT p FROM Preparation p"),
    @NamedQuery(name = "Preparation.findByPreparationID", query = "SELECT p FROM Preparation p WHERE p.preparationID = :preparationID")})
public class Preparation extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PreparationID")
    private Integer preparationID;
       
    @JoinColumn(name = "PrepTypeID", referencedColumnName = "PrepTypeID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Preptype prepType;
    
    @JoinColumn(name = "CollectionObjectID", referencedColumnName = "CollectionObjectID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Collectionobject collectionObject;
      
//    @JoinColumn(name = "PreparedByID", referencedColumnName = "AgentID")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Agent preparedBy;
     
    public Preparation() {
    }

    public Preparation(Integer preparationID) {
        this.preparationID = preparationID;
    }

    public Preparation(Integer preparationID, Date timestampCreated ) {
        this.preparationID = preparationID;
        this.timestampCreated = timestampCreated; 
    }

    public Integer getPreparationID() {
        return preparationID;
    }

    public void setPreparationID(Integer preparationID) {
        this.preparationID = preparationID;
    }
  
    @Override
    public String getIdentityString() {
        return String.valueOf(preparationID);
    }
     
    @Override
    public int getEntityId() {
        return preparationID;
    }
 
    public Preptype getPrepType() {
        return prepType;
    }

    public void setPrepType(Preptype prepType) {
        this.prepType = prepType;
    }

    public Collectionobject getCollectionObject() {
        return collectionObject;
    }

    public void setCollectionObject(Collectionobject collectionObject) {
        this.collectionObject = collectionObject;
    }
 
//    public Agent getPreparedBy() {
//        return preparedBy;
//    }
//
//    public void setPreparedBy(Agent preparedBy) {
//        this.preparedBy = preparedBy;
//    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (preparationID != null ? preparationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Preparation)) {
            return false;
        }
        Preparation other = (Preparation) object;
        return !((this.preparationID == null && other.preparationID != null) || (this.preparationID != null && !this.preparationID.equals(other.preparationID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Preparation[ preparationID=" + preparationID + " ]";
    }
    
}
