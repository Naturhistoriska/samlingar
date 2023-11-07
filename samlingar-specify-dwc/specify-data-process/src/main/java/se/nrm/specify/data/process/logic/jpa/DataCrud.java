package se.nrm.specify.data.process.logic.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB; 
import se.nrm.specify.data.jpa.SpecifyDao; 
import se.nrm.specify.data.model.EntityBean;

/**
 *
 * @author idali
 */
public class DataCrud implements Serializable {
     
     
    @EJB
    private SpecifyDao dao;
     
    public DataCrud() {
        
    }
    
    public List<Integer> findIdsByCollectionCode(int collectionCode) {
        return dao.findAllIdsByCollectionCode(collectionCode);
    }
     
    public List<Integer> findUpdateIdsByCollectionCode(int collectionCode, 
            String jpql, Date fromDate, Date toDate) {
        return dao.findUpdateIdsByCollectionCode(collectionCode, jpql, fromDate, toDate);
    }
    
    public List<EntityBean> fetchData(int collectionCode, List<Integer> ids, 
            Map<String, String> filterMap, String jpql) { 
        return ((Stream<EntityBean>) dao
            .findByCollectonId(collectionCode, ids, filterMap, jpql))
            .collect(Collectors.toList());
    }
    
    
//    public List<Collectionobject> getCollectionobjectsByCollectionCode(int code, 
//            List<Integer> ids, String jpql) { 
//        
// 
//        Stream<Collectionobject> collectionObjectStream = 
//                dao.findCollectionObjectByCollectionCode(code, ids, jpql);  
//        
//        return collectionObjectStream.collect(Collectors.toList()); 
//    }
    
    
    
    
    
    
    

    
//    public Geography getGeographyParent(int geographyId) {
//        return dao.findGeoraphyParent(geographyId);
//    }
}
