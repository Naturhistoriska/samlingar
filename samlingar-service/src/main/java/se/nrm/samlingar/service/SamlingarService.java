package se.nrm.samlingar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedMap;
import se.nrm.samlingar.service.solr.QueryParams;
import se.nrm.samlingar.service.solr.SolrService;

@ApplicationScoped
public class SamlingarService {

    private static final Logger LOG = Logger.getLogger(SamlingarService.class);

    private final SolrService solr;

    public SamlingarService(SolrService solr) {
        LOG.info("construct");
        this.solr = solr;
    }

    public String initalData() {
        LOG.info("initalData");
        return solr.initalData();
    }

    public String simpleSearch(MultivaluedMap<String, String> queryParams) {
        Map<String, String> paramMap = new HashMap<>(); 
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            String key = entry.getKey(); 
            paramMap.put(key, entry.getValue().get(0)); 
        } 
        return solr.simpleSearch(paramMap);
    }

    public String search(MultivaluedMap<String, String> queryParams) {
        QueryParams solrQueryParams = QueryParams.build(queryParams);
        return solr.search(solrQueryParams);
    }
}
