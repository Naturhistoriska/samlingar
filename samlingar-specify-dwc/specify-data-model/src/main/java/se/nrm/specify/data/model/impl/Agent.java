package se.nrm.specify.data.model.impl;
 
import java.util.Collection;
import java.util.Date; 
import javax.persistence.Basic;  
import javax.persistence.Column;
import javax.persistence.Entity; 
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;   
import javax.xml.bind.annotation.XmlTransient;
import se.nrm.specify.data.model.BaseEntity;

/**
 *
 * @author idali
 */
@Entity
@Table(name = "agent") 
@NamedQueries({
    @NamedQuery(name = "Agent.findAll", query = "SELECT a FROM Agent a"),
    @NamedQuery(name = "Agent.findByAgentID", query = "SELECT a FROM Agent a WHERE a.agentID = :agentID"),  
    @NamedQuery(name = "Agent.findByAgentType", query = "SELECT a FROM Agent a WHERE a.agentType = :agentType") })
public class Agent extends BaseEntity {
    
 
    
    @Size(max = 50)
    @Column(name = "Abbreviation")
    private String abbreviation;
    
    @Basic(optional = false)     
    @NotNull()
    @Column(name = "AgentType")
    private short agentType;
     
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50) 
    @Column(name = "Email")
    private String email;
    
    @Size(max = 50)
    @Column(name = "FirstName")
    private String firstName; 
      
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid;
    
    @Size(max = 8)
    @Column(name = "Initials")
    private String initials;
     
    @Size(max = 50)
    @Column(name = "JobTitle")
    private String jobTitle;
    
    @Size(max = 256)
    @Column(name = "LastName")
    private String lastName;
    
    @Size(max = 50)
    @Column(name = "MiddleInitial")
    private String middleInitial;
    
    @Lob 
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @Size(max = 50)
    @Column(name = "Title")
    private String title;
   
    @JoinColumn(name = "CollectionCCID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne
    private se.nrm.specify.data.model.impl.Collection collectionCCID;
    
    
    
    @JoinColumn(name = "CollectionTCID", referencedColumnName = "UserGroupScopeId")
    @ManyToOne
    private se.nrm.specify.data.model.impl.Collection collectionTCID;
        
    @OneToMany(mappedBy = "catalogerID")
    private Collection<Collectionobject> collectionobjectCollection; 
                    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AgentID")
    private Integer agentID;
            
      
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID", fetch = FetchType.LAZY)
//    private List<Collector> collectorList;
       
    @Transient
    private StringBuilder fullNameSb;
       
    @Transient
    private final String emptySpace = " ";
    
    
    public Agent() {
    }

    public Agent(Integer agentID) {
        this.agentID = agentID;
    }

    public Agent(Integer agentID, Date timestampCreated, short agentType) {
        this.agentID = agentID;
        this.timestampCreated = timestampCreated;
        this.agentType = agentType;
    }
    
    @Override
    public String getIdentityString() {
        return String.valueOf(agentID);
    }
      
    @Override
    public int getEntityId() {
        return agentID;
    }

    public Integer getAgentID() {
        return agentID;
    }

    public void setAgentID(Integer agentID) {
        this.agentID = agentID;
    }
    public String getFullName() {
        fullNameSb = new StringBuilder();
        
        if(firstName != null) {
            fullNameSb.append(firstName);
            if(middleInitial != null) {
                fullNameSb.append(emptySpace);
                fullNameSb.append(middleInitial);
                if(lastName != null) {
                    fullNameSb.append(emptySpace);
                    fullNameSb.append(lastName);
                }
            }  else {
                if(lastName != null) {
                    fullNameSb.append(emptySpace);
                    fullNameSb.append(lastName);
                }
            }
        } else if(lastName != null)  {
            return lastName;
        }
        return fullNameSb.toString().trim();
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (agentID != null ? agentID.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agent)) {
            return false;
        }
        Agent other = (Agent) object;
        return !((this.agentID == null && other.agentID != null) || (this.agentID != null && !this.agentID.equals(other.agentID)));
    }
    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Agent[ agentID=" + agentID + " ]";
    }
 
 
//    public Division getDivision() {
//        return division;
//    }
//
//    public void setDivision(Division division) {
//        this.division = division;
//    }
  
//    public List<Collectionobject> getCollectionobjectList() {
//        return collectionobjectList;
//    }
//
//    public void setCollectionobjectList1(List<Collectionobject> collectionobjectList) {
//        this.collectionobjectList = collectionobjectList;
//    }
  
//    @XmlTransient
//    public List<Author> getAuthorList() {
//        return authorList;
//    }
//
//    public void setAuthorList(List<Author> authorList) {
//        this.authorList = authorList;
//    } 
 
//    public List<Collector> getCollectorList() {
//        return collectorList;
//    }
//
//    public void setCollectorList(List<Collector> collectorList) {
//        this.collectorList = collectorList;
//    }
 
 
  

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public short getAgentType() {
        return agentType;
    }

    public void setAgentType(short agentType) {
        this.agentType = agentType;
    }

  

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
//    public Specifyuser getSpecifyUserID() {
//        return specifyUserID;
//    }
//
//    public void setSpecifyUserID(Specifyuser specifyUserID) {
//        this.specifyUserID = specifyUserID;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

 

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
 
 
    public se.nrm.specify.data.model.impl.Collection getCollectionCCID() {
        return collectionCCID;
    }

    public void setCollectionCCID(se.nrm.specify.data.model.impl.Collection collectionCCID) {
        this.collectionCCID = collectionCCID;
    }

    public se.nrm.specify.data.model.impl.Collection getCollectionTCID() {
        return collectionTCID;
    }

    public void setCollectionTCID(se.nrm.specify.data.model.impl.Collection collectionTCID) {
        this.collectionTCID = collectionTCID;
    }
 
 

    
    @XmlTransient
    public Collection<Collectionobject> getCollectionobjectCollection() {
        return collectionobjectCollection;
    }

    public void setCollectionobjectCollection(Collection<Collectionobject> collectionobjectCollection) {
        this.collectionobjectCollection = collectionobjectCollection;
    }

    
   
}
