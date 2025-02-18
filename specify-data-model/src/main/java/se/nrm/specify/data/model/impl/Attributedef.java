package se.nrm.specify.data.model.impl;
 
import java.util.Date; 
import javax.persistence.Basic; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; 
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery; 
import javax.persistence.Table; 
import javax.validation.constraints.Size;  
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "attributedef") 
@NamedQueries({
    @NamedQuery(name = "Attributedef.findAll", query = "SELECT a FROM Attributedef a"),
    @NamedQuery(name = "Attributedef.findByAttributeDefID", query = "SELECT a FROM Attributedef a WHERE a.attributeDefID = :attributeDefID") })
public class Attributedef extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AttributeDefID")
    private Integer attributeDefID;
      
    @Size(max = 32)
    @Column(name = "FieldName")
    private String fieldName;
      

    public Attributedef() {
    }

    public Attributedef(Integer attributeDefID) {
        this.attributeDefID = attributeDefID;
    }

    public Attributedef(Integer attributeDefID, Date timestampCreated) {
        this.attributeDefID = attributeDefID;
        this.timestampCreated = timestampCreated;
    }

    public Integer getAttributeDefID() {
        return attributeDefID;
    }

    public void setAttributeDefID(Integer attributeDefID) {
        this.attributeDefID = attributeDefID;
    }

    
    @Override
    public String getIdentityString() {
      return String.valueOf(attributeDefID);
    }

    @Override
    public int getEntityId() {
      return attributeDefID;
    }
  
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attributeDefID != null ? attributeDefID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attributedef)) {
            return false;
        }
        Attributedef other = (Attributedef) object;
        return !((this.attributeDefID == null && other.attributeDefID != null) || (this.attributeDefID != null && !this.attributeDefID.equals(other.attributeDefID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Attributedef[ attributeDefID=" + attributeDefID + " ]";
    }
    
}
