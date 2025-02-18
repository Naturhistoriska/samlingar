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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;  
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
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AgentID")
    private Integer agentID;
 
    @Size(max = 50)
    @Column(name = "Abbreviation")
    private String abbreviation;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "AgentType")
    private short agentType;
         
    @Size(max = 50)
    @Column(name = "FirstName")
    private String firstName;
     
    @Size(max = 8)
    @Column(name = "Initials")
    private String initials; 
     
    @Size(max = 256)
    @Column(name = "LastName")
    private String lastName;
    
    @Size(max = 50)
    @Column(name = "MiddleInitial")
    private String middleInitial;
 
    @Size(max = 50)
    @Column(name = "Title")
    private String title;
    
//    @JoinColumn(name = "DivisionID", referencedColumnName = "UserGroupScopeId")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Division division;
     
//    @OneToMany(mappedBy = "catalogerID", fetch = FetchType.LAZY)
//    private List<Collectionobject> collectionobjectList;
           
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentID", fetch = FetchType.LAZY)
//    private List<Author> authorList;
      
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
 
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
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
 
    public String getTitle() {
        return title;
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
            } 
        } else if(lastName != null)  {  
            return lastName; 
        }
        return fullNameSb.toString().trim();
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
    
}
