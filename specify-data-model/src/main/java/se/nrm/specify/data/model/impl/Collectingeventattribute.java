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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID; 
import javax.xml.bind.annotation.XmlRootElement; 
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "collectingeventattribute")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Collectingeventattribute.findAll", query = "SELECT c FROM Collectingeventattribute c"),
  @NamedQuery(name = "Collectingeventattribute.findByCollectingEventAttributeID", 
          query = "SELECT c FROM Collectingeventattribute c WHERE c.collectingEventAttributeID = :collectingEventAttributeID")})
public class Collectingeventattribute extends BaseEntity {

  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CollectingEventAttributeID")
  private Integer collectingEventAttributeID;
 
  @Lob
  @Size(max = 65535)
  @Column(name = "Text1")
  private String text1;

  @Lob
  @Size(max = 65535)
  @Column(name = "Text2")
  private String text2;
  
  @Size(max = 50)
  @Column(name = "Text9")
  private String text9;
  
  public Collectingeventattribute() {
  }

  public Collectingeventattribute(Integer collectingEventAttributeID) {
    this.collectingEventAttributeID = collectingEventAttributeID;
  }

  public Collectingeventattribute(Integer collectingEventAttributeID, Date timestampCreated) {
    this.collectingEventAttributeID = collectingEventAttributeID;
    this.timestampCreated = timestampCreated;
  }

  @XmlID
  @XmlAttribute(name = "id")
  @Override
  public String getIdentityString() {
    return String.valueOf(collectingEventAttributeID);
  }

  @Override
  public int getEntityId() {
    return collectingEventAttributeID;
  }

  public Integer getCollectingEventAttributeID() {
    return collectingEventAttributeID;
  }

  public void setCollectingEventAttributeID(Integer collectingEventAttributeID) {
    this.collectingEventAttributeID = collectingEventAttributeID;
  }
 
  public String getText1() {
    return text1;
  }

  public String getText2() {
    return text2;
  }

  public void setText2(String text2) {
    this.text2 = text2;
  }
 
  public String getText9() {
    return text9;
  }

  public void setText9(String text9) {
    this.text9 = text9;
  }
 
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (collectingEventAttributeID != null ? collectingEventAttributeID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Collectingeventattribute)) {
      return false;
    }
    Collectingeventattribute other = (Collectingeventattribute) object;
    return !((this.collectingEventAttributeID == null && other.collectingEventAttributeID != null) || (this.collectingEventAttributeID != null && !this.collectingEventAttributeID.equals(other.collectingEventAttributeID)));
  }

  @Override
  public String toString() {
    return "se.nrm.dina.datamodel.Collectingeventattribute[ collectingEventAttributeID=" + collectingEventAttributeID + " ]";
  }
}
