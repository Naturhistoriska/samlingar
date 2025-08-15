package se.nrm.samlingar.api.solr.services;
  
import java.io.IOException; 
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader; 
import java.net.URLEncoder;
import java.time.Instant; 
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;   
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils; 
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery; 
import org.apache.solr.client.solrj.SolrServerException; 
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest; 
import org.apache.solr.client.solrj.request.json.TermsFacetMap;
import org.apache.solr.client.solrj.response.QueryResponse; 
import org.apache.solr.common.SolrDocumentList;   
import se.nrm.samlingar.api.logic.InitialProperties; 
import se.nrm.samlingar.api.utils.SolrSearchHelper;


  
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients; 
  

/**
 *
 * @author idali
 */
@Slf4j
public class Solr implements Serializable {

    private SolrClient client;
    private SolrQuery query;
    private QueryRequest request;
    private QueryResponse response;
 
    final TermsFacetMap mapFacet;
    final TermsFacetMap imageFacet;
    final TermsFacetMap inSwedenFacet;
    final TermsFacetMap typeFacet;
    final TermsFacetMap datasourceFacet;
 
    private final String colon = ":";
    private final String searchAll = "*:*"; 
    private final String utf_8 = "UTF-8";

    private final String geohashKey = "point-1";
    
    
    private final String associatedMediaKey = "associatedMedia";
    
 
 
    private final String countryKey = "country"; 
    private final String typeStatusKey = "typeStatus"; 
    private final String dataResourceNameKey = "dataResourceName";
    private final String idKey = "id";
    private final String locationKey = "location";
    private final String scientificNameKey = "scientificName";
    private final String localityKey = "locality";
    private final String catalogNumberKey = "catalogNumber";

    private final String mapKey = "map";
    private final String inSwedenKey = "inSweden";
    private final String facetKey = "facet";

    private final String stringSweden = "Sweden";

    
    private final String createDateKey = "createdDate_dt";
    
    private final String collectionCodeKey = "collectionCode";
    
    
    
    
    private final String yearGap = "+1YEAR";
    private final String monthGap = "+1MONTH";
    private final String total = "total";
    
    private final String defaultSort = "createdDate_dt desc";
    private final String bbox = "{!bbox}"; 
    private final String sfield = "sfield";
    
    private final String radiusKm = "10000"; 
    private final String ptKey = "pt";
    private final String dKey = "d";
    
    private final String idSort = "id asc";
    private final String returnFields = "id, catalogNumber, scientificName, lat_long, locality";
    
    private final String exportPath = "/export?q=";
    private final String flKey = "&fl=";
    private final String fqKey = "&fq=";
    private final String sortKey = "&sort=";
    
    private StringBuilder sb;
    private String gap;
    
    private List<String> fields;

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
        
        datasourceFacet = new TermsFacetMap(dataResourceNameKey);

    }

    @PostConstruct
    public void init() {
        log.info("init from search...");

        client = new HttpSolrClient.Builder(properties.getSolrURL()).build(); 
         
    }



    public String getChart(String collection, String startDate, String endDate, boolean isYearChart) {
        log.info("getChart : {} -- {}", startDate, endDate);
        log.info("getChart collection : {} ",  collection);
        
        gap = isYearChart ? yearGap : monthGap;
        
        try {
            query = new SolrQuery(collection);
            query.setFacet(true); 
            query.setFields(collectionCodeKey);
            query.setRows(1);
             
            query.addDateRangeFacet(createDateKey,
                    Date.from(Instant.parse(startDate)),
                    Date.from(Instant.parse(endDate)),
                    gap);
              
            request = new QueryRequest(query);
             
            response = request.process(client);
            return isYearChart ? buildResult(response) : response.jsonStr(); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
               
            }
        } 
    }

    private String buildResult(QueryResponse response) {
        SolrDocumentList docs = response.getResults();

        JsonReader reader = Json.createReader(new StringReader(response.jsonStr()));
        JsonObject jsonObject = reader.readObject();

        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (String key : jsonObject.keySet()) {
            builder.add(key, jsonObject.get(key));
        }
        builder.add(total, docs.getNumFound());
        return builder.build().toString();
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
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
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
            return response.jsonStr();
//            log.info("json: {}", response.jsonStr()); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        } 
    }
    
    

    public String scientificNameSearch(int start, int numPerPage, String text, String sort) {
        log.info("scientificNameSearch: {} -- {}", text, sort);

        sort = sort == null ? defaultSort : sort;
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .setSort(sort) 
                .withFacet(dataResourceNameKey, datasourceFacet.setLimit(20));
//                .withFacet(mapKey, mapFacet.setLimit(20000));
         

        try {
            response = jsonRequest.process(client);
//            log.info("json: {}", response.jsonStr()); 
            return response.jsonStr();
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        } 
    }

    public String search(Map<String, String> paramMap, String text, String scientificName,
            String locality, String dateRange, String facets, int start, int numPerPage, String sort) {
        
        sort = sort == null ? defaultSort : sort;

        log.info("search : {}", paramMap);
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text) 
                .setOffset(start)
                .setLimit(numPerPage)
                .setSort(sort) 
//                .withFacet(mapKey, mapFacet.setLimit(20000))
                .withFacet(dataResourceNameKey, datasourceFacet.setLimit(20));

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
        
        if (!StringUtils.isBlank(locality)) {
            jsonRequest.withFilter(locality);
        }


        if (!StringUtils.isBlank(dateRange)) {
            jsonRequest.withFilter(dateRange);
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
            return response.jsonStr();
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }  
    } 
    
    
    public String download(Map<String, String> paramMap, String text, String scientificName,
            String dateRange, int start, int rows, String sort) { 

        sort = sort == null ? defaultSort : sort;
         final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)  
                .setOffset(start)
                .setLimit(rows)
                .setSort(sort);
 

        if (!StringUtils.isBlank(scientificName)) {
            jsonRequest.withFilter(scientificName);
        }

        if (!StringUtils.isBlank(dateRange)) {
            jsonRequest.withFilter(dateRange);
        }

        paramMap.forEach((key, value) -> {
            sb = new StringBuilder();
            sb.append(key)
                    .append(colon)
                    .append(value); 
            jsonRequest.withFilter(sb.toString());
        });

        fields = SolrSearchHelper.getInstance().buildDataExportFields();
        
        fields.stream()
                .forEach(field -> {
                jsonRequest.returnFields(field);
            });
         
        try { 
            response = jsonRequest.process(client);
//            response = request.process(client);
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
        return response.jsonStr();
    }

    
    public String geojson(Map<String, String> paramMap, String text, String scientificName,
            String locality, String dateRange, String pt, int start, int rows ) { 
        log.info("geojson... {} -- {}", start, rows); 
         
        query = new SolrQuery(text); 
        query.addField(idKey); 
        query.addField(locationKey); 
        query.addFilterQuery(bbox);
        query.setParam(sfield, locationKey);
        query.setParam(ptKey, pt); 
        query.setParam(dKey, radiusKm);
        query.setStart(start);
        query.setRows(rows);  
         
        if (!StringUtils.isBlank(scientificName)) {
            query.addFilterQuery(scientificName); 
        }
        
        if (!StringUtils.isBlank(locality)) {
            query.addFilterQuery(locality);  
        }


        if (!StringUtils.isBlank(dateRange)) {
            query.addFilterQuery(dateRange);  
        }

        paramMap.forEach((key, value) -> {
            sb = new StringBuilder();
            sb.append(key)
                    .append(colon)
                    .append(value); 
            query.addFilterQuery(sb.toString());   
        });
  
        try { 
            request = new QueryRequest(query); 
            response = request.process(client);
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
        
        
        return response.jsonStr(); 
    }
}
