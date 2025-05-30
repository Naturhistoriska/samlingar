package se.nrm.specify.data.model.impl;
 
//import java.util.Collection;
import java.util.Date;  
import java.util.Set;
import javax.persistence.Basic; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;  
import javax.validation.constraints.Size;  
//import javax.xml.bind.annotation.XmlTransient;
import se.nrm.specify.data.model.BaseEntity; 

/**
 *
 * @author idali
 */
@Entity
@Table(name = "collectingevent") 
@NamedQueries({
    @NamedQuery(name = "Collectingevent.findAll", query = "SELECT c FROM Collectingevent c"),
    @NamedQuery(name = "Collectingevent.findByCollectingEventID", query = "SELECT c FROM Collectingevent c WHERE c.collectingEventID = :collectingEventID"),  
    @NamedQuery(name = "Collectingevent.findByStationFieldNumber", query = "SELECT c FROM Collectingevent c WHERE c.stationFieldNumber = :stationFieldNumber") })
public class Collectingevent extends BaseEntity {

 
    @Size(max = 50)
    @Column(name = "EndDateVerbatim")
    private String endDateVerbatim;
    
    @Size(max = 50)
    @Column(name = "Method")
    private String method;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "Remarks")
    private String remarks;
    
    @Size(max = 50)
    @Column(name = "StartDateVerbatim")
    private String startDateVerbatim;
    
    @Size(max = 50)
    @Column(name = "StationFieldNumber")
    private String stationFieldNumber;
    
    @Size(max = 50)
    @Column(name = "VerbatimDate")
    private String verbatimDate;
     
    @Size(max = 128)
    @Column(name = "GUID")
    private String guid;
            
//    @OneToMany(mappedBy = "collectingEventID")
//    private Collection<Collectionobject> collectionobjectCollection;
    
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CollectingEventID")
    private Integer collectingEventID;
     
    @Column(name = "EndDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @Column(name = "EndDatePrecision")
    private Short endDatePrecision;
    
    
    @Column(name = "EndTime")
    private Short endTime;
    
    
    @Column(name = "StartDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "StartDatePrecision")
    private Short startDatePrecision;
    
    
    @Column(name = "StartTime")
    private Short startTime;
    
               
    @JoinColumn(name = "LocalityID", referencedColumnName = "LocalityID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Locality locality;
     
    @OneToMany(mappedBy = "collectingEvent", fetch = FetchType.LAZY)
    private Set<Collector> collectorList;

    public Collectingevent() {
    }

    public Collectingevent(Integer collectingEventID) {
        this.collectingEventID = collectingEventID;
    }

    public Collectingevent(Integer collectingEventID, Date timestampCreated) {
        this.collectingEventID = collectingEventID;
        this.timestampCreated = timestampCreated;
    }

    public Integer getCollectingEventID() {
        return collectingEventID;
    }

    public void setCollectingEventID(Integer collectingEventID) {
        this.collectingEventID = collectingEventID;
    }
 
    @Override
    public String getIdentityString() {
        return String.valueOf(collectingEventID);
    }
     
    @Override
    public int getEntityId() {
        return collectingEventID;
    }
    
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Short getEndDatePrecision() {
        return endDatePrecision;
    }

    public void setEndDatePrecision(Short endDatePrecision) {
        this.endDatePrecision = endDatePrecision;
    }


    public Short getEndTime() {
        return endTime;
    }

    public void setEndTime(Short endTime) {
        this.endTime = endTime;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Short getStartDatePrecision() {
        return startDatePrecision;
    }

    public void setStartDatePrecision(Short startDatePrecision) {
        this.startDatePrecision = startDatePrecision;
    }


    public Short getStartTime() {
        return startTime;
    }

    public void setStartTime(Short startTime) {
        this.startTime = startTime;
    }

    
    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }
  
    public Set<Collector> getCollectorList() {
        return collectorList;
    }

    public void setCollectorList(Set<Collector> collectorList) {
        this.collectorList = collectorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (collectingEventID != null ? collectingEventID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Collectingevent)) {
            return false;
        }
        Collectingevent other = (Collectingevent) object;
        return !((this.collectingEventID == null && other.collectingEventID != null) || (this.collectingEventID != null && !this.collectingEventID.equals(other.collectingEventID)));
    }

    @Override
    public String toString() {
        return "se.nrm.specify.data.model.impl.Collectingevent[ collectingEventID=" + collectingEventID + " ]";
    }
 

    public String getEndDateVerbatim() {
        return endDateVerbatim;
    }

    public void setEndDateVerbatim(String endDateVerbatim) {
        this.endDateVerbatim = endDateVerbatim;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStartDateVerbatim() {
        return startDateVerbatim;
    }

    public void setStartDateVerbatim(String startDateVerbatim) {
        this.startDateVerbatim = startDateVerbatim;
    }

    public String getStationFieldNumber() {
        return stationFieldNumber;
    }

    public void setStationFieldNumber(String stationFieldNumber) {
        this.stationFieldNumber = stationFieldNumber;
    }

    public String getVerbatimDate() {
        return verbatimDate;
    }

    public void setVerbatimDate(String verbatimDate) {
        this.verbatimDate = verbatimDate;
    }
 
 
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

 

//    @XmlTransient
//    public Collection<Collectionobject> getCollectionobjectCollection() {
//        return collectionobjectCollection;
//    }
//
//    public void setCollectionobjectCollection(Collection<Collectionobject> collectionobjectCollection) {
//        this.collectionobjectCollection = collectionobjectCollection;
//    }
 
}
