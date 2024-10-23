package se.nrm.samlingar.api.solr.services;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.request.json.TermsFacetMap;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import se.nrm.samlingar.api.logic.InitialProperties; 

/**
 *
 * @author idali
 */
@Slf4j
public class SolrService implements Serializable {

    private SolrClient client;

    private String username;
    private String password;

    private final String map = "map";
    private final String image = "image";
    private final String inSweden = "inSweden";
    private final String isType = "isType";
    
    private final String textKey = "text:";

    private SolrQuery query;
    private QueryResponse response;
    private QueryRequest request;

    @Inject
    private InitialProperties properties;

    public SolrService() {
    }

    @PostConstruct
    public void init() {
        log.info("init from search...");

        client = new HttpSolrClient.Builder(properties.getSolrURL()).build();
        username = properties.getUsername();
        password = properties.getPassword();
    }
    
    /**
     * Search all the records without any filters, sorted by cataloged date
     *
     * @param start
     * @param numPerPage
     * @param text
     * @return String
     */
    public String simpleSearch(int start, int numPerPage, String text) {
        log.info("searchAll: {} -- {} ", start + " -- " + numPerPage, text);

        String jsonResponse;
        query = new SolrQuery();
        query.setQuery(textKey + text)
                .setStart(start)
                .setRows(numPerPage);
        try { 
            request = new QueryRequest(query);
            request.setBasicAuthCredentials(username, password);
//            response = request.process(client);
            
            NoOpResponseParser rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType("json");
            request.setResponseParser(rawJsonResponseParser);

            NamedList<Object> resp = client.request(request);
            jsonResponse = (String) resp.get("response");
            
            log.info("what... {}", jsonResponse);
 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
        return jsonResponse;
    }

    public String searchTotalRecordsWithMap() {

        final TermsFacetMap mapFacet = new TermsFacetMap(map);
        final TermsFacetMap imageFacet = new TermsFacetMap(image);
        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSweden);
        final TermsFacetMap typeFacet = new TermsFacetMap(isType);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery("*:*")
                .returnFields("country", "catalogNumber")
                .withFacet(image, imageFacet)
                .withFacet(map, mapFacet)
                .withFacet(inSweden, inSwedenFacet)
                .withFacet(isType, typeFacet);

        jsonRequest.setBasicAuthCredentials(username, password);
        try {
            response = jsonRequest.process(client); 
            
            log.info("json: {}", response.jsonStr()); 
            
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return response.jsonStr();
    }
}
