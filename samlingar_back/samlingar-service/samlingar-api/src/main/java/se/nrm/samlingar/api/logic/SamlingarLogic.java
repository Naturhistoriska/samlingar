package se.nrm.samlingar.api.logic;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import se.nrm.samlingar.api.solr.services.SolrService;

/**
 *
 * @author idali
 */
@Slf4j
public class SamlingarLogic {
   
    @Inject
    private SolrService service;
    
    public String getStaticData() {
        log.info("getStaticData");  
        return service.searchStatisticData();
    }
    
    public String simpleSearch(String text, int start, int numPerPage) {
        log.info("simpleSearch : {}", text);
        return service.simpleSearch(start, numPerPage, text);
    }
    
    public String filterSerch(int start, int numPerPage, String text,
            String collection, String typeStatus, String family) {
        log.info("filterSerch : {} -- {}", collection, typeStatus);
        
        return service.filterSearch(start, numPerPage, text, collection, typeStatus, family);
    }
}
