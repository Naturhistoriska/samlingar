package se.nrm.specify.data.model.impl;
 
import java.util.Date; 
import javax.persistence.Basic; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "discipline") 
@NamedQueries({
    @NamedQuery(name = "Discipline.findAll", query = "SELECT d FROM Discipline d"),
    @NamedQuery(name = "Discipline.findByUserGroupScopeId", query = "SELECT d FROM Discipline d WHERE d.userGroupScopeId = :userGroupScopeId") })
public class Discipline extends BaseEntity {
 
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserGroupScopeId")
    private Integer userGroupScopeId;
    
    @Column(name = "disciplineId")
    private Integer disciplineId;
    
    @Size(max = 64)
    @Column(name = "Name")
    private String name;
          
//    @JoinColumn(name = "GeographyTreeDefID", referencedColumnName = "GeographyTreeDefID")
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    private Geographytreedef geographyTreeDef;
// 
//    @JoinColumn(name = "TaxonTreeDefID", referencedColumnName = "TaxonTreeDefID")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Taxontreedef taxonTreeDef;
      

    public Discipline() {
    }

    public Discipline(Integer userGroupScopeId) {
        this.userGroupScopeId = userGroupScopeId;
    }

    public Discipline(Integer userGroupScopeId, Date timestampCreated) {
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

    public Integer getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Integer disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
//    public Geographytreedef getGeographyTreeDef() {
//        return geographyTreeDef;
//    }
//
//    public void setGeographyTreeDefID(Geographytreedef geographyTreeDef) {
//        this.geographyTreeDef = geographyTreeDef;
//    }
//
//    public Taxontreedef getTaxonTreeDef() {
//        return taxonTreeDef;
//    }
//
//    public void setTaxonTreeDef(Taxontreedef taxonTreeDef) {
//        this.taxonTreeDef = taxonTreeDef;
//    }
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userGroupScopeId != null ? userGroupScopeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Discipline)) {
            return false;
        }
        Discipline other = (Discipline) object;
        return !((this.userGroupScopeId == null && other.userGroupScopeId != null) || (this.userGroupScopeId != null && !this.userGroupScopeId.equals(other.userGroupScopeId)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Discipline[ userGroupScopeId=" + userGroupScopeId + " ]";
    }
    
}
