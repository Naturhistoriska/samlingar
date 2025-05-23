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
@Table(name = "institution")
@NamedQueries({
  @NamedQuery(name = "Institution.findAll", query = "SELECT i FROM Institution i"),
  @NamedQuery(name = "Institution.findByUserGroupScopeId", query = "SELECT i FROM Institution i WHERE i.userGroupScopeId = :userGroupScopeId"),
  @NamedQuery(name = "Institution.findByCode", query = "SELECT i FROM Institution i WHERE i.code = :code")})
public class Institution extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "UserGroupScopeId")
  private Integer userGroupScopeId;

  @Size(max = 64)
  @Column(name = "Code")
  private String code;
 
  @Column(name = "institutionId")
  private Integer institutionId;

  @Lob
  @Size(max = 65535)
  @Column(name = "Copyright")
  private String copyright;

  @Size(max = 128)
  @Column(name = "AltName")
  private String altName;
  
  @Size(max = 255)
  @Column(name = "Name")
  private String name;

  @Lob
  @Size(max = 65535)
  @Column(name = "TermsOfUse")
  private String termsOfUse;
 

  public Institution() {
  }

  public Institution(Integer userGroupScopeId) {
    this.userGroupScopeId = userGroupScopeId;
  }

  public Institution(Integer userGroupScopeId, Date timestampCreated) {
    this.userGroupScopeId = userGroupScopeId;
    this.timestampCreated = timestampCreated;
  }

  @Override
  public String getIdentityString() {
    return String.valueOf(userGroupScopeId);
  }

  @Override
  public int getEntityId() {
    return institutionId;
  }

  public Integer getUserGroupScopeId() {
    return userGroupScopeId;
  }

  public void setUserGroupScopeId(Integer userGroupScopeId) {
    this.userGroupScopeId = userGroupScopeId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
 
  public Integer getInstitutionId() {
    return institutionId;
  }

  public void setInstitutionId(Integer institutionId) {
    this.institutionId = institutionId;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getTermsOfUse() {
    return termsOfUse;
  }

  public void setTermsOfUse(String termsOfUse) {
    this.termsOfUse = termsOfUse;
  }

  public String getAltName() {
    return altName;
  }

  public void setAltName(String altName) {
    this.altName = altName;
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
    hash += (userGroupScopeId != null ? userGroupScopeId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Institution)) {
      return false;
    }
    Institution other = (Institution) object;
    return !((this.userGroupScopeId == null && other.userGroupScopeId != null)
            || (this.userGroupScopeId != null && !this.userGroupScopeId.equals(other.userGroupScopeId)));
  }

  @Override
  public String toString() {
    return "se.nrm.dina.datamodel.Institution[ userGroupScopeId=" + userGroupScopeId + " ]";
  }
}
