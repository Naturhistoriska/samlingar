package se.nrm.specify.data.jpa.impl;

import java.io.Serializable;  
import java.sql.Date;
import java.util.List;
import java.util.stream.Stream;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import se.nrm.specify.data.jpa.SpecifyDao;
import se.nrm.specify.data.model.EntityBean; 
import se.nrm.specify.data.model.impl.Collection;
import se.nrm.specify.data.model.impl.Geography; 
import se.nrm.specify.data.model.impl.Institution;

/**
 *
 * @author idali
 * @param <T>
 */
@Slf4j
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(NOT_SUPPORTED)
public class SpecifyDaoImpl<T extends EntityBean> implements SpecifyDao<T>, Serializable {
    
    private final String paramCollectionMemberId = "collectionMemberID"; 
    private final String paramIds = "ids";
    private final String paramGeographyId = "geographyID";
    private final String paramCode = "code";
    
    private final String paramFrom = "fromDate";
    private final String paramTo = "toDate";
    
    private final String fromDateKey = "fromDate";
    private final String toDateKey = "toDate";
    
    private final String institutionFindByCodeNamedQuery = "Institution.findByCode";
    private final String collectionObjectIdsFindByCollectionCodeNamedQuery = "Collectionobject.findAllIdsByCollectionCode";
    private final String updateIdsFindByCollectionCodeNamedQuery = "Collectionobject.findUpdatedIdsByCollectionCode";
    
    private final String collectionFindByCodeNamedQuery = "Collection.findByCollectionCode";
    
    
     
    private final String jpql = "SELECT g FROM Geography g LEFT JOIN FETCH g.parent WHERE g.geographyID = :geographyID";
//    private final String collectionObjectFindByCollectionCodeAndIds = "Collectionobject.findByCollectionMemberIDAndIds"; 
    private Query query;
    
    private List<Integer> idList;
    private Stream<T> results;
    private Institution institution;
    
    @PersistenceContext(unitName = "jpaNrmPU")
    private EntityManager entityManager;
    
    @Override
    public Institution getInstitutionDataByCode(String code) {
        institution = (Institution) entityManager
                .createNamedQuery(institutionFindByCodeNamedQuery)
                .setParameter(paramCode, code)
                .getSingleResult(); 
        entityManager.clear();
        return institution;
    }
     
    
    @Override
    public List<Integer> findUpdateIdsByCollectionCode(String collectionCode,
        Date fromDate, Date toDate) {
        
        idList = entityManager.createNamedQuery(updateIdsFindByCollectionCodeNamedQuery)
            .setParameter(paramCode, collectionCode)
            .setParameter(paramFrom, fromDate)
             .setParameter(paramTo, toDate)
            .getResultList(); 
        
        entityManager.clear();
        return idList;
    }
     
    @Override
    public List<Integer> findAllIdsByCollectionCode(String collectionCode) {
        idList = entityManager.createNamedQuery(collectionObjectIdsFindByCollectionCodeNamedQuery)
            .setParameter(paramCode, collectionCode).getResultList();
        entityManager.clear();
        return idList;
    }

    @Override
    public Stream<T> findCollectionObjectByCollectionCode(String collectionCode, 
            List<Integer> ids, String jpql) { 
        log.info("findCollectionObjectByCollectionCode : {} -- {}", 
                collectionCode, ids.size());
          
        query = entityManager.createQuery(jpql);    
        query.setParameter(paramIds, ids);
       
        results = query.getResultStream(); 
        
        entityManager.clear();
        return results;
    } 
 

    @Override
    public Collection getCollectionByCode(String code) { 
        return (Collection) entityManager
                .createNamedQuery(collectionFindByCodeNamedQuery)
                .setParameter(paramCode, code).getSingleResult();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

 


    
        
  
    
    
 
    
    
    
    



     
    @Override
    public Geography findGeoraphyParent(int geographyId) { 
        query = entityManager.createQuery(jpql)
            .setParameter(paramGeographyId, geographyId);
        return (Geography)query.getSingleResult();
    } 
}
