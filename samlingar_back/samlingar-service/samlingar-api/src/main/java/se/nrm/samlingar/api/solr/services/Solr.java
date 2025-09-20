package se.nrm.samlingar.api.solr.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
import se.nrm.samlingar.api.logic.InitialProperties;
import se.nrm.samlingar.api.utils.SolrSearchBuildChart;
import se.nrm.samlingar.api.utils.SolrSearchBuildGeoJson;
import se.nrm.samlingar.api.utils.SolrSearchHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class Solr implements Serializable {

    private SolrClient client;
    private String username;
    private String password;

    private SolrQuery query;
    private QueryRequest request;
    private QueryResponse response;

    private final String colon = ":";

    private final String star = "*";

    private final String countryKey = "country";

    private final String dataResourceNameKey = "dataResourceName";

    private final String idKey = "id";
    private final String locationKey = "location";
    private final String scientificNameKey = "scientificName";
    private final String localityKey = "locality";
    private final String catalogNumberKey = "catalogNumber";

    private final String facetKey = "facet";
    private final String facetHeatMapKey = "facet.heatmap";
    private final String facetHeatmapGeomKey = "facet.heatmap.geom";
    private final String facetHeatmepGridLevelKey = "facet.heatmap.gridLevel";

    private final String strTrue = "true";

    private final String envelope = "ENVELOPE(-180,180,90,-90)";
    private final String gridLevel = "3";

    private final String stringSweden = "Sweden";

    private final String yearGap = "+1YEAR";
    private final String monthGap = "+1MONTH";

    private final String bbox = "{!bbox}";
    private final String sfield = "sfield";

    private final String radiusKm = "10000";
    private final String ptKey = "pt";
    private final String dKey = "d";

    private final String idSort = "id asc";

    private final String responseKey = "response";

    private final String cursorMarkKey = "cursorMark";

    private final int maxExport = 50000;
    private final int batchSize = 1000;
    

    private StringBuilder sb;
    private String gap;

    private List<String> fields;
    
    

//
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
    private final String searchAll = "*:*";

    private final String associatedMediaKey = "associatedMedia";
//    private final String createDateKey = "createdDate";
    private final String collectionCodeKey = "collectionCode";
    private final String collectionNameKey = "collectionName";

    private final String defaultSort = "catalogedDate desc";
    private final int defaultStart = 0;
    private final int defaultRows = 10;
    
    private final String catalogedDateKey = "catalogedDate";

    private final String geoKey = "geo";

    private final String hasImageKey = "hasImage";
    private final String inSwedenKey = "inSweden";
    private final String coordinatesKey = "coordinates";
    private final String point1Key = "point-1";
    private final String typeStatusKey = "typeStatus";
    
    private final String pt ="59,15";

    final TermsFacetMap mapFacet;
    final TermsFacetMap imageFacet;
    final TermsFacetMap inSwedenFacet;
    final TermsFacetMap typeFacet;
//    final TermsFacetMap datasourceFacet;
    final TermsFacetMap collectionFacet;
    final TermsFacetMap collectionCodeFacet;
    
    
    
    
    
//    
//    private final String mapFacetQuery = "point-1: *";
//    private final String imageFacetQuery = "hasImage: *";
//    private final String typeStatusFacetQuery = "typeStatus: *";
//    private final String inSwedenFacetQuery = "country: Sweden";

    @Inject
    private InitialProperties properties;

    public Solr() {
        mapFacet = new TermsFacetMap(point1Key)
                .includeAllBucketsUnionBucket(true);

        imageFacet = new TermsFacetMap(hasImageKey);

        inSwedenFacet = new TermsFacetMap(countryKey)
                .setTermPrefix(stringSweden)
                .includeAllBucketsUnionBucket(true);

        typeFacet = new TermsFacetMap(typeStatusKey)
                .includeAllBucketsUnionBucket(true);

        collectionFacet = new TermsFacetMap(collectionNameKey);
        
        collectionCodeFacet = new TermsFacetMap(collectionCodeKey); 
    }

    @PostConstruct
    public void init() {
        log.info("init from search...");

//        client = SolrClientProvider.getSolrClient(properties);
        client = new HttpSolrClient.Builder(properties.getSolrURL()).build();
        username = properties.getUsername();
        password = properties.getPassword();
    }

    public String getInitalData() {
        log.info("getInitalData");
 
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(searchAll) 
                .withFacet(associatedMediaKey, imageFacet)
                .withFacet(coordinatesKey, mapFacet.setLimit(1))
                .withFacet(inSwedenKey, inSwedenFacet)
                .withFacet(typeStatusKey, typeFacet.setLimit(1))
                .setLimit(1);
        jsonRequest.setBasicAuthCredentials(username, password); 
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

    public String getChart(String collection, String startDate, String endDate, boolean isYearChart) {
        log.info("getChart : {} -- {}", startDate, endDate);
        log.info("getChart collection : {} ", collection);

        gap = isYearChart ? yearGap : monthGap; 
        try {
            query = new SolrQuery(collection); 
            query.setFacet(true);
            query.setFields(collectionCodeKey);
            query.setRows(1);
 
            query.addDateRangeFacet(catalogedDateKey,
                    Date.from(Instant.parse(startDate)),
                    Date.from(Instant.parse(endDate)),
                    gap);

            request = new QueryRequest(query); 
            request.setBasicAuthCredentials(username, password);
            response = request.process(client);
            return isYearChart
                    ? SolrSearchBuildChart.getInstance().buildChatResult(response) : response.jsonStr();
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
    
    public String autoCompleteSearch(String text, String field) {
        log.info("autoCompleteSearch: {} -- {}", text, field);
        
        final TermsFacetMap facet = new TermsFacetMap(field).setLimit(50);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .withFacet(facetKey, facet);

        jsonRequest.setBasicAuthCredentials(username, password);
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
                .withFacet(collectionNameKey, collectionCodeFacet.setLimit(20))
                .withFacet(collectionCodeKey, collectionCodeFacet.setLimit(20));
        jsonRequest.setBasicAuthCredentials(username, password);
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
    
    public String simpleSearch(Map<String, String> paramMap) { 
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(searchAll)
                .setOffset(defaultStart)
                .setLimit(defaultRows)
                .setSort(defaultSort)
                .withFacet(collectionCodeKey, collectionCodeFacet.setLimit(20))
                .withFacet(collectionNameKey, collectionFacet.setLimit(20));

     
        paramMap.forEach((key, value) -> {
            sb = new StringBuilder();
            sb.append(key)
                    .append(colon)
                    .append(value);
            jsonRequest.withFilter(sb.toString());
        });

        jsonRequest.setBasicAuthCredentials(username, password);
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
    
    public String freeTextSearch(String text) {

        query = new SolrQuery();
        query.set("defType", "edismax");
        query.set("q", text);
        query.set("qf", "catchall");  // Fields to search
        query.set("mm", "2<75%"); // Minimum number of terms that must match
        query.setStart(0);
        query.setRows(10);
        query.setSort(catalogedDateKey, SolrQuery.ORDER.desc);
        
        try {
            response = client.query(query);
 
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

    
    public String searchWithId(String id) {
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(idKey + id)    
                .setLimit(1);
        jsonRequest.setBasicAuthCredentials(username, password); 
        try {
            response = jsonRequest.process(client); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return response.jsonStr(); 
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

   

    public String search(Map<String, String> paramMap, String text, String scientificName,
            String locality, String dateRange, String facets,
            int start, int numPerPage, String sort) {

        sort = sort == null ? defaultSort : sort;

        log.info("search : {}", paramMap);
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .setSort(sort)
                .withFacet(collectionCodeKey, collectionCodeFacet.setLimit(20))
                .withFacet(collectionNameKey, collectionFacet.setLimit(20));

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

        jsonRequest.setBasicAuthCredentials(username, password);
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
    
      public String geojson(Map<String, String> paramMap, String text, String scientificName,
            String locality, String dateRange,  int start, int rows) {
        log.info("geojson... {} -- {}", start, rows);

        
        query = new SolrQuery(text);
        query.addField(idKey);
        query.addField(geoKey);
        query.addField(localityKey);
        query.addField(scientificNameKey);
        query.addFilterQuery(bbox);
        query.setParam(sfield, geoKey);
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
            request.setBasicAuthCredentials(username, password);
            response = request.process(client);  
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

    public String getHeatmap(Map<String, String> paramMap, String text, String scientificName,
            String locality, String dateRange, String pt, int start, int row) {
        log.info("heatmap");

        // Set query 
        query = new SolrQuery(text);
        query.setRows(0);
        query.set(facetKey, strTrue);
        query.set(facetHeatMapKey, geoKey);
        query.set(facetHeatmapGeomKey, envelope);
        query.set(facetHeatmepGridLevelKey, gridLevel);

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

        request = new QueryRequest(query);
        request.setBasicAuthCredentials(username, password);
        try {
            response = request.process(client);
             
            return SolrSearchBuildGeoJson.getInstance().buildGeoJson(response);
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

//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
   

    private void buildDownloadJson(String jsonString, JsonArrayBuilder builder) {

        JsonReader jsonReader;
        JsonObject jsonObj;

        jsonReader = Json.createReader(new StringReader(jsonString));

        jsonObj = jsonReader.readObject();
        JsonArray docs = jsonObj.getJsonArray(responseKey);

        docs.stream()
                .forEach(value -> {
                    builder.add(value);
                });

        jsonReader.close();
    }

    public String export(Map<String, String> paramMap, String text,
            String scientificName, String locality, String dateRange,
            int start, int rows, String sort) {
        log.info("export : {}", text);

        JsonArrayBuilder builder = Json.createArrayBuilder();
        int totalRowsToExport = rows >= maxExport ? maxExport : rows;

        int totalExported = 0;

        String cursorMark = star;
        boolean done = false;

        fields = SolrSearchHelper.getInstance().buildDataExportFields();

        try {
            final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                    .setQuery(text)
                    .setOffset(start)
                    .setLimit(batchSize)
                    .setSort(idSort);

            jsonRequest.setBasicAuthCredentials(username, password);
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

            fields.stream()
                    .forEach(field -> {
                        jsonRequest.returnFields(field);
                    });

            while (!done && totalExported < totalRowsToExport) {

                jsonRequest.withParam(cursorMarkKey, cursorMark);

                response = jsonRequest.process(client);
                buildDownloadJson(response.jsonStr(), builder);

                String nextCursorMark = response.getNextCursorMark();

                totalExported += batchSize;
                log.info("totalExported : {}", totalExported);

                if (cursorMark.equals(nextCursorMark)) {
                    done = true;
                }
                cursorMark = nextCursorMark;

            }
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        JsonArray array = builder.build();
        log.info("size : {}", array.size());

        return array.toString();
    }

    public String geojson1(Map<String, String> paramMap, String text, String scientificName,
            String locality, String dateRange, String pt, int start, int rows) {
        log.info("geojson... {} -- {}", start, rows);

        int totalExported = 0;

        String cursorMark = star;
        boolean done = false;

        JsonArrayBuilder builder = Json.createArrayBuilder();

        final JsonQueryRequest jsonRequest = new JsonQueryRequest();
        jsonRequest.setQuery(text);
        jsonRequest.setLimit(batchSize);
        jsonRequest.returnFields(idKey);
        jsonRequest.returnFields(locationKey);
        jsonRequest.returnFields(localityKey);
        jsonRequest.returnFields(scientificNameKey);
        jsonRequest.withParam(ptKey, pt);
        jsonRequest.withParam(sfield, locationKey);
        jsonRequest.withParam(dKey, radiusKm);
        jsonRequest.withFilter(bbox);
        jsonRequest.withParam("sort", "geodist() asc, id asc");

        jsonRequest.setOffset(start);
        jsonRequest.setLimit(rows);
//        jsonRequest.setSort(idKey);

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

            while (!done) {

                jsonRequest.withParam(cursorMarkKey, cursorMark);

                response = jsonRequest.process(client);
                buildDownloadJson(response.jsonStr(), builder);

                String nextCursorMark = response.getNextCursorMark();

                totalExported += batchSize;
                log.info("totalExported : {}", totalExported);

                if (cursorMark.equals(nextCursorMark)) {
                    done = true;
                }
                cursorMark = nextCursorMark;
            }
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
        JsonArray array = builder.build();
        log.info("size : {}", array.size());

        return array.toString();
    }

  
}
