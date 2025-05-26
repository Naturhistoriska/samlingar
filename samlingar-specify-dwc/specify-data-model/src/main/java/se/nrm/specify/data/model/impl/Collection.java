package se.nrm.specify.data.model.impl;
 
import java.util.Date; 
import javax.persistence.Basic;  
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "collection") 
@NamedQueries({
    @NamedQuery(name = "Collection.findAll", query = "SELECT c FROM Collection c"),
    @NamedQuery(name = "Collection.findByCollectionCode", query = "SELECT c FROM Collection c WHERE c.code = :code"),
    @NamedQuery(name = "Collection.findByUserGroupScopeId", query = "SELECT c FROM Collection c WHERE c.userGroupScopeId = :userGroupScopeId")})
public class Collection extends BaseEntity {
     
             
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserGroupScopeId")
    private Integer userGroupScopeId;
    
     
    
    @Size(max = 50)
    @Column(name = "Code")
    private String code;
    
    @Size(max = 50)
    @Column(name = "CollectionName")
    private String collectionName;
    
    @Size(max = 32)
    @Column(name = "CollectionType")
    private String collectionType;
      
    @Lob
    @Size(max = 65535)
    @Column(name = "Description")
    private String description;
     
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid; 
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collectionID")
//    private java.util.Collection<Collectionobject> collectionobjectCollection;

 

    
    @Column(name = "collectionId")
    private Integer collectionId;
    
           
    public Collection() {
    }

    public Collection(Integer userGroupScopeId) {
        this.userGroupScopeId = userGroupScopeId;
    }

    public Collection(Integer userGroupScopeId, Date timestampCreated) {
        this.userGroupScopeId = userGroupScopeId;
        this.timestampCreated = timestampCreated; 
    }

    public Integer getUserGroupScopeId() {
        return userGroupScopeId;
    }

    public void setUserGroupScopeId(Integer userGroupScopeId) {
        this.userGroupScopeId = userGroupScopeId;
    }
    
    @Override
    public String getIdentityString() {
        return String.valueOf(userGroupScopeId);
    }
     
    @Override
    public int getEntityId() {
        return userGroupScopeId;
    }


    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }
    @Override    
    public int hashCode() {
        int hash = 0;
        hash += (userGroupScopeId != null ? userGroupScopeId.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Collection)) {
            return false;
        }
        Collection other = (Collection) object;
        return !((this.userGroupScopeId == null && other.userGroupScopeId != null) || (this.userGroupScopeId != null && !this.userGroupScopeId.equals(other.userGroupScopeId)));
    }
    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Collection[ userGroupScopeId=" + userGroupScopeId + " ]";
    }
 

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCollectionName() {
        return collectionName;
    }



    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    } 

 
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
 
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
  
  
//   
//    @XmlTransient
//    public java.util.Collection<Collectionobject> getCollectionobjectCollection() {
//        return collectionobjectCollection;
//    }
//
//    public void setCollectionobjectCollection(java.util.Collection<Collectionobject> collectionobjectCollection) {
//        this.collectionobjectCollection = collectionobjectCollection;
//    }
 
 
 
}
