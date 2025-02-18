package se.nrm.specify.data.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import se.nrm.specify.data.model.EntityBean;
import se.nrm.specify.data.model.impl.Collection;
import se.nrm.specify.data.model.impl.Institution;
//import se.nrm.specify.data.model.impl.Geography;

/**
 *
 * @author idali
 * @param <T>
 */
public interface SpecifyDao <T extends EntityBean> {
    
    public List<Integer> findAllIdsByCollectionCode(String collectionCode);
    
    public List<Integer> findUpdateIdsByCollectionCode(String collectionCode, 
            String jpql, Date fromDate, Date toDate);
    
//    public Stream<T> findCollectionObjectByCollectionCode(int collectionCode, 
//            List<Integer> ids, String jpql); 
    
    
    public Stream<T> findDataByCollectionCodeAndIds(String collectionCode, List<Integer> ids, String jpql);
    
    public Stream<T> findByCollectonId(String collectionCode, List<Integer> ids, 
            Map<String, String> filterMap, String jpql);
    
    
    public Institution getInstitutionByCode(String code);
    
    public Collection getCollectionByCode(String code);
        
//    public Geography findGeoraphyParent(int geographyId);
}
