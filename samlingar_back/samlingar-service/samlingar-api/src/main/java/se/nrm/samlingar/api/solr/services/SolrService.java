package se.nrm.samlingar.api.solr.services;

import ch.hsr.geohash.GeoHash;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
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

    private final String jsonKey = "json";
    private final String responseKey = "response";
    private final String wildSearch = "*:*";

    private final String map = "map";
    private final String image = "image";
    private final String inSweden = "inSweden";
    private final String isType = "isType";
    private final String typeStatusFacetKey = "typeStatus";
    private final String collectionNameFacetKey = "collectionName";
    private final String familyFacetKey = "family";
    private final String genusFacetKey = "genus";
    private final String geohashFacetKey = "geohash";

    private final String textKey = "text:";
    private final String collectionNameKey = "collectionName:";
    private final String typeStatusKey = "typeStatus:";
    private final String familyKey = "family:";

    private SolrQuery query;
    private QueryResponse response;
    private NoOpResponseParser rawJsonResponseParser;
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
        log.info("simpleSearch ..... : {} -- {} ", start + " -- " + numPerPage, text);

        final TermsFacetMap mapFacet = new TermsFacetMap(map);
        final TermsFacetMap imageFacet = new TermsFacetMap(image);
        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSweden);
        final TermsFacetMap isTypeFacet = new TermsFacetMap(isType);
        final TermsFacetMap typeStatusFacet = new TermsFacetMap(typeStatusFacetKey)
                .setLimit(120);
        final TermsFacetMap collectionFacet = new TermsFacetMap(collectionNameFacetKey)
                .setLimit(50);
        final TermsFacetMap familyFacet = new TermsFacetMap(familyFacetKey)
                .setLimit(200);

//       final TermsFacetMap geoHashFacet = new TermsFacetMap(geohashFacetKey)
//                .setLimit(500)
//                .setTermPrefix("3_");
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(image, imageFacet)
                .withFacet(map, mapFacet)
                .withFacet(inSweden, inSwedenFacet)
                .withFacet(isType, isTypeFacet)
                .withFacet(typeStatusFacetKey, typeStatusFacet)
                .withFacet(familyFacetKey, familyFacet)
                .withFacet(collectionNameFacetKey, collectionFacet);
//                .withFacet(geohashFacetKey, geoHashFacet);

        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);

            log.info("simplesearch what... {}", jsonResponse);

        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return jsonResponse;
    }

    /**
     * Search all the records without any filters, sorted by cataloged date
     *
     * @param start
     * @param numPerPage
     * @param text
     * @param collection
     * @param typeStatus
     * @param family
     * @return String
     */
    public String filterSearch(int start, int numPerPage, String text,
            String collection, String typeStatus, String family) {
        log.info("filterSearch: {} -- {} ", collection + " -- " + typeStatus, text);

        final TermsFacetMap mapFacet = new TermsFacetMap(map);
        final TermsFacetMap imageFacet = new TermsFacetMap(image);
        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSweden);
        final TermsFacetMap isTypeFacet = new TermsFacetMap(isType);
        final TermsFacetMap typeStatusFacet = new TermsFacetMap(typeStatusFacetKey)
                .setLimit(100);
        final TermsFacetMap collectionFacet = new TermsFacetMap(collectionNameFacetKey)
                .setLimit(100);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(image, imageFacet)
                .withFacet(map, mapFacet)
                .withFacet(inSweden, inSwedenFacet)
                .withFacet(isType, isTypeFacet)
                .withFacet(typeStatusFacetKey, typeStatusFacet)
                .withFacet(collectionNameFacetKey, collectionFacet);

        if (family != null && family.length() > 0) {
            final TermsFacetMap genusFacet = new TermsFacetMap(genusFacetKey)
                    .setLimit(200);
            jsonRequest.withFacet(genusFacetKey, genusFacet);
        } else {
            final TermsFacetMap familyFacet = new TermsFacetMap(familyFacetKey)
                    .setLimit(300);
            jsonRequest.withFacet(familyFacetKey, familyFacet);
        }

        if (collection != null) {
            jsonRequest.withFilter(collectionNameKey + collection);
        }

        if (typeStatus != null) {
            jsonRequest.withFilter(typeStatusKey + typeStatus);
        }

        if (family != null) {
            jsonRequest.withFilter(familyKey + family);
        }

        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);

            log.info("what... {}", jsonResponse);

        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return jsonResponse;
    }

    public String mapDataSearch(String text, String collection, String typeStatus, String family) {
        log.info("mapDataSearch ..... : {} -- {} ", text);

        final TermsFacetMap geoHashFacet = new TermsFacetMap(geohashFacetKey)
                .setLimit(8000)
                .setTermPrefix("2_");

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .returnFields("geo")
                .withFacet(geohashFacetKey, geoHashFacet);
 

        if (collection != null) {
            jsonRequest.withFilter(collectionNameKey + collection);
        }

        if (typeStatus != null) {
            jsonRequest.withFilter(typeStatusKey + typeStatus);
        }

        if (family != null) {
            jsonRequest.withFilter(familyKey + family);
        }
        
        jsonRequest.setBasicAuthCredentials(properties.getUsername(), properties.getPassword());
        try { 
            response = jsonRequest.process(client);
            log.info("json: {}", response.jsonStr());
        } catch (SolrServerException | IOException ex) {
            log.warn(ex.getMessage());
            return null;
        }
        return response.jsonStr();
        

//       final TermsFacetMap geoHashFacet = new TermsFacetMap(geohashFacetKey)
//                .setLimit(500)
//                .setTermPrefix("3_");
//  
//        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
//                .setQuery(text)   
//                .withFacet(geohashFacetKey, geoHashFacet);
//        
//        if (family != null && family.length() > 0) {
//            final TermsFacetMap genusFacet = new TermsFacetMap(genusFacetKey)
//                    .setLimit(200);
//            jsonRequest.withFacet(genusFacetKey, genusFacet);
//        } else {
//            final TermsFacetMap familyFacet = new TermsFacetMap(familyFacetKey) 
//                    .setLimit(300);
//            jsonRequest.withFacet(familyFacetKey, familyFacet); 
//        }
//        
//        if(collection != null) {
//            jsonRequest.withFilter(collectionNameKey + collection);  
//        }
//        
//        if(typeStatus != null) {
//            jsonRequest.withFilter(typeStatusKey + typeStatus);
//        }
//        
//        if(family != null) {
//            jsonRequest.withFilter(familyKey + family);
//        }
//
//        
//        jsonRequest.setBasicAuthCredentials(username, password);
//        
//        String jsonResponse; 
//        try {
//            response = jsonRequest.process(client);
//            
//            rawJsonResponseParser = new NoOpResponseParser();
//            rawJsonResponseParser.setWriterType(jsonKey);
//            jsonRequest.setResponseParser(rawJsonResponseParser);
//             
//            jsonResponse = (String) client.request(jsonRequest).get(responseKey);
//
//            log.info("simplesearch what... {}", jsonResponse); 
//
//        } catch (SolrServerException | IOException ex) {
//            log.error(ex.getMessage());
//            return null;
//        }
//
//        return jsonResponse; 
    }

    public String searchStatisticData() {

        final TermsFacetMap mapFacet = new TermsFacetMap(map);
        final TermsFacetMap imageFacet = new TermsFacetMap(image);
        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSweden);
        final TermsFacetMap typeFacet = new TermsFacetMap(isType);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(wildSearch)
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
