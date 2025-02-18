package se.nrm.specify.data.jpa.impl;

import java.io.Serializable; 
import java.util.Date;
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
    
    
    private final String fromDateKey = "fromDate";
    private final String toDateKey = "toDate";
    
    private final String institutionFindByCodeNamedQuery = "Institution.findByCode";
    private final String collectionObjectIdsFindByCollectionCodeNamedQuery = "Collectionobject.findAllIdsByCollectionCode";
     private final String collectionFindByCodeNamedQuery = "Collection.findByCollectionCode";
    
    
    
    
    private final String collectionObjectFindAllIdNamedQuery = "Collectionobject.findAllIds";
    private final String jpql = "SELECT g FROM Geography g LEFT JOIN FETCH g.parent WHERE g.geographyID = :geographyID";
//    private final String collectionObjectFindByCollectionCodeAndIds = "Collectionobject.findByCollectionMemberIDAndIds"; 
    private Query query;
    
    @PersistenceContext(unitName = "jpaNrmPU")
    private EntityManager entityManager;
    
    
    @Override
    public Institution getInstitutionDataByCode(String code) {
        return (Institution) entityManager
                .createNamedQuery(institutionFindByCodeNamedQuery)
                .setParameter(paramCode, code)
                .getSingleResult(); 
    }
    
 
    @Override
    public Collection getCollectionByCode(String code) { 
        return (Collection) entityManager
                .createNamedQuery(collectionFindByCodeNamedQuery)
                .setParameter(paramCode, code).getSingleResult();
    }
 
    @Override
    public List<Integer> findAllIdsByCollectionCode(String collectionCode) {
        return entityManager.createNamedQuery(collectionObjectIdsFindByCollectionCodeNamedQuery)
            .setParameter(paramCode, collectionCode).getResultList();
    }
    
        
    @Override
    public Stream<T> findCollectionObjectByCollectionCode(String collectionCode, 
            List<Integer> ids, String jpql) { 
        log.info("findCollectionObjectByCollectionCode : {} -- {}", collectionCode, ids.size());
          
        query = entityManager.createQuery(jpql);
//            .setParameter(paramCollectionMemberId, collectionCode); 
        query.setParameter(paramIds, ids);
       
        return query.getResultStream(); 
    } 
    
    
    
    
    
    
    
    
    

    @Override
    public List<Integer> findUpdateIdsByCollectionCode(String collectionCode, 
            String jpql, Date fromDate, Date toDate) {
        query = entityManager.createQuery(jpql)
            .setParameter(paramCollectionMemberId, collectionCode)
            .setParameter(fromDateKey, fromDate)
            .setParameter(toDateKey, toDate); 
        return query.getResultList(); 
    }

     
    @Override
    public Geography findGeoraphyParent(int geographyId) { 
        query = entityManager.createQuery(jpql)
            .setParameter(paramGeographyId, geographyId);
        return (Geography)query.getSingleResult();
    } 
}
