package se.nrm.specify.data.process.logic.jpa;

import java.io.Serializable;  
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB; 
import se.nrm.specify.data.jpa.SpecifyDao; 
import se.nrm.specify.data.model.EntityBean;
import se.nrm.specify.data.model.impl.Collection;
import se.nrm.specify.data.model.impl.Collectionobject;
import se.nrm.specify.data.model.impl.Institution;

/**
 *
 * @author idali
 */
public class DataCrud implements Serializable {
     
     
    @EJB
    private SpecifyDao dao;
     
    public DataCrud() {
        
    }
    
    public Institution getInstitutionByCode(String code) {
        return dao.getInstitutionDataByCode(code);
    }
    
 
    public List<Integer> findUpdateIdsByCollectionCode(String collectionCode,
            String jpql, Date fromDate, Date toDate) {
        return dao.findUpdateIdsByCollectionCode(collectionCode, jpql, fromDate, toDate);
    }
  
    public List<Collectionobject> fetchDataByIds(String collectionCode, 
            List<Integer> ids, String jpql) { 
        return ((Stream<Collectionobject>) dao
            .findCollectionObjectByCollectionCode(collectionCode, ids, jpql))
            .collect(Collectors.toList());
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
     

    public Collection getCollectionByCode(String code) {
        return dao.getCollectionByCode(code);
       
    }
    
    public List<Integer> findIdsByCollectionCode(String collectionCode) {
        return dao.findAllIdsByCollectionCode(collectionCode);
    }

    
    
    
    
    
    
    
    
    
    


    

    public List<EntityBean> fetchData(String collectionCode, List<Integer> ids, 
            Map<String, String> filterMap, String jpql) { 
        return null;
//        return ((Stream<EntityBean>) dao
//            .findByCollectonId(collectionCode, ids, filterMap, jpql))
//            .collect(Collectors.toList());
    } 

  
}
