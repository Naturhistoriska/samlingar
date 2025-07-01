package se.nrm.samlingar.api.solr.services;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j; 
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient; 
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.request.json.TermsFacetMap;
import org.apache.solr.client.solrj.response.QueryResponse;
import se.nrm.samlingar.api.logic.InitialProperties;

/**
 *
 * @author idali
 */
@Slf4j
public class Solr implements Serializable {

    private SolrClient client;
    private QueryResponse response;

//    final TermsFacetMap catalogedMonthFacet;
    final TermsFacetMap mapFacet;
    final TermsFacetMap imageFacet;
    final TermsFacetMap inSwedenFacet;
    final TermsFacetMap typeFacet;

    
    private final String colon = ":";
    private final String searchAll = "*:*";

    private final String geohashKey = "point-1";
    private final String countryKey = "country";
    private final String typeStatusKey = "typeStatus"; 
    private final String associatedMediaKey = "associatedMedia";

    private final String mapKey = "map";
    private final String inSwedenKey = "inSweden";
    private final String facetKey = "facet";

    private final String stringSweden = "Sweden";
    
    
    private final String eventDateKey = "eventDate:";
    
    private StringBuilder sb;

    @Inject
    private InitialProperties properties;

    public Solr() {
        mapFacet = new TermsFacetMap(geohashKey)
                .includeAllBucketsUnionBucket(true);

        imageFacet = new TermsFacetMap(associatedMediaKey);

        inSwedenFacet = new TermsFacetMap(countryKey)
                .setTermPrefix(stringSweden);

        typeFacet = new TermsFacetMap(typeStatusKey)
                .includeAllBucketsUnionBucket(true);

    }

    @PostConstruct
    public void init() {
        log.info("init from search...");

        client = new HttpSolrClient.Builder(properties.getSolrURL()).build();
    }

    public String getInitalData() {
        log.info("getInitalData");
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(searchAll)
                .withFacet(associatedMediaKey, imageFacet)
                .withFacet(mapKey, mapFacet.setLimit(1))
                .withFacet(inSwedenKey, inSwedenFacet)
                .withFacet(typeStatusKey, typeFacet.setLimit(1));

        try {
            response = jsonRequest.process(client);
//            log.info("json: {}", response.jsonStr()); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return response.jsonStr();
    }

    public String autoCompleteSearch(String text, String field) {
        log.info("autoCompleteSearch: {} -- {}", text, field);
        final TermsFacetMap facet = new TermsFacetMap(field).setLimit(50);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .withFacet(facetKey, facet);

        try {
            response = jsonRequest.process(client);
//            log.info("json: {}", response.jsonStr()); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return response.jsonStr();
    }

    public String scientificNameSearch(int start, int numPerPage, String text, String sort) {
        log.info("scientificNameSearch: {} -- {}", text, numPerPage);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(mapKey, mapFacet.setLimit(20000));

        try {
            response = jsonRequest.process(client);
//            log.info("json: {}", response.jsonStr()); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return response.jsonStr();
    }
    
     public String search(Map<String, String> paramMap, String text, String scientificName, 
            String dateRange, String facets, int start, int numPerPage, String sort) {
        
        log.info("search", paramMap);
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)   
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(mapKey, mapFacet.setLimit(20000));
        
        if (facets != null) {
            List<String> facetList = Arrays.asList(facets.split(","));
            facetList.stream()
                    .forEach(f -> {
                        TermsFacetMap facet = new TermsFacetMap(f)
                                .setLimit(10000);
                        jsonRequest.withFacet(f, facet);
                    });

        }
     
        if (!StringUtils.isBlank(scientificName)) {
            jsonRequest.withFilter(scientificName);
        }
        
        if(!StringUtils.isBlank(dateRange) ) { 
           jsonRequest.withFilter(eventDateKey + dateRange); 
        }   
        
        paramMap.forEach((key, value) -> {
                sb = new StringBuilder();
                sb.append(key)
                        .append(colon)
                        .append(value);
                jsonRequest.withFilter(sb.toString());
            });
         
        try {
            response = jsonRequest.process(client);   
//            log.info("simplesearch what... {}", jsonResponse);

        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
        return response.jsonStr();
    }
 
}
