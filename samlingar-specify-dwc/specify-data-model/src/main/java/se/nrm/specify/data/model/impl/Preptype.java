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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size; 
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "preptype") 
@NamedQueries({
    @NamedQuery(name = "Preptype.findAll", query = "SELECT p FROM Preptype p"),
    @NamedQuery(name = "Preptype.findByPrepTypeID", query = "SELECT p FROM Preptype p WHERE p.prepTypeID = :prepTypeID")})
public class Preptype extends BaseEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PrepTypeID")
    private Integer prepTypeID;
      
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "Name")
    private String name;
  
    public Preptype() {
    }

    public Preptype(Integer prepTypeID) {
        this.prepTypeID = prepTypeID;
    }

    public Preptype(Integer prepTypeID, Date timestampCreated, String name) {
        this.prepTypeID = prepTypeID;
        this.timestampCreated = timestampCreated; 
        this.name = name;
    }

    public Integer getPrepTypeID() {
        return prepTypeID;
    }

    public void setPrepTypeID(Integer prepTypeID) {
        this.prepTypeID = prepTypeID;
    }
    @Override
    public String getIdentityString() {
        return String.valueOf(prepTypeID);
    }
     
    @Override
    public int getEntityId() {
        return prepTypeID;
    } 
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prepTypeID != null ? prepTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Preptype)) {
            return false;
        }
        Preptype other = (Preptype) object;
        return !((this.prepTypeID == null && other.prepTypeID != null) || (this.prepTypeID != null && !this.prepTypeID.equals(other.prepTypeID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Preptype[ prepTypeID=" + prepTypeID + " ]";
    }
    
}
