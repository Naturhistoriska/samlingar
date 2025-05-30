package se.nrm.specify.data.jpa;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import se.nrm.specify.data.model.EntityBean;
import se.nrm.specify.data.model.impl.Collection;
import se.nrm.specify.data.model.impl.Geography;
import se.nrm.specify.data.model.impl.Institution;

/**
 *
 * @author idali
 * @param <T>
 */
public interface SpecifyDao <T extends EntityBean> {
    
    public Institution getInstitutionDataByCode(String code);
    
    public Collection getCollectionByCode(String code);
     
    public List<Integer> findAllIdsByCollectionCode(String collectionCode);
     
    public List<Integer> findUpdateIdsByCollectionCode(String collectionCode, 
            String jpql, Date fromDate, Date toDate);
    
    public Stream<T> findCollectionObjectByCollectionCode(String collectionCode, 
            List<Integer> ids, String jpql); 
    
     public Geography findGeoraphyParent(int geographyId);
}
