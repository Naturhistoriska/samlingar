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
        
        return service.searchTotalRecordsWithMap();
    }
    
    public String simpleSearch(String text, int start, int numPerPage) {
        return service.simpleSearch(start, numPerPage, text);
    }
}
