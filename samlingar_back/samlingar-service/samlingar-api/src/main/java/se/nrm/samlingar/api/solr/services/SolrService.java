package se.nrm.samlingar.api.solr.services;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.json.HeatmapFacetMap;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.request.json.RangeFacetMap;
import org.apache.solr.client.solrj.request.json.TermsFacetMap;
import org.apache.solr.client.solrj.response.QueryResponse;
import se.nrm.samlingar.api.logic.InitialProperties;
import se.nrm.samlingar.api.utils.SolrSearchHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class SolrService implements Serializable {

    private SolrClient client;

    private String username;
    private String password;
    
    private final String catchAllField = "catchall:";

    private final String jsonKey = "json";
    private final String responseKey = "response";
    private final String wildSearch = "*:*";
    private final String star = "*";
    private final String emptySpace = " ";
    private final String imageFilter = "hasImage:*";
    private final String mapFilter = "point-1:*";
    private final String typeFilter = "typeStatus:*";
    private final String isInSwedenFilter = "country:Sweden";

    private final String authorField = "author";
    private final String catalogNumberField = "catalogNumber";
    private final String collectionNameField = "collectionName";
    private final String continentField = "continent";
    private final String countryField = "country";
    private final String countyField = "county";
    private final String currentDeterminationField = "currentDetermination";
    private final String determinerField = "determiner";
    private final String districtField = "district";
    private final String familyField = "family";
    private final String genusField = "genus";
    private final String higherTxField = "higherTx";
    private final String latitudeField = "latitudeText";
    private final String localityField = "locality";
    private final String longitudeField = "longitudeText";
    private final String oceanOrSeaField = "oceanOrSea";
    private final String preservationField = "preservation";
    private final String speciesField = "species";
    private final String remarksField = "remarks";
    
    private final String stateField = "state";
    private final String stationFieldNumberField = "stationFieldNumber";
    private final String txFullNameField = "txFullName";
    private final String typeStatusField = "typeStatus";

    private final String catalogedDateField = "catalogedDate";
    private final String startDateField = "startDate";
    private final String collectorField = "collector";
    private final String commonNameField = "commonName";
    private final String preprationField = "prepration";

    private final String catalogedMonthStringFacetKey = "catalogedMonth";
    
    
    
    private final String catalogedMonthFacetKey = "catalogedMonth";
    private final String catalogedYearFacetKey = "catalogedYear";
    private final String countryFacetKey = "country";
    
    private final String mapFacetKey = "map";
    private final String imageFacetKey = "hasImage";
    private final String inSwedenFacetKey = "inSweden";
    private final String isTypeFacetKey = "isType";
    
    private final String typeStatusFacetKey = "typeStatus";
    private final String collectionNameFacetKey = "collectionName";
    private final String collectionCodeFacetKey = "collectionCode";
    private final String familyFacetKey = "family";
    private final String genusFacetKey = "genus";
    private final String geohashFacetKey = "geohash";
    private final String point1FacetKey = "point-1";
    private final String point01FacetKey = "point-0.1";
    private final String point001FacetKey = "point-0.01";
    private final String point0001FacetKey = "point-0.001";
     
    private final String geoFacetKey = "geo";
    
    private final String scientificNameFacetKey = "scientificName";
    
    private final String txFullNameFacetKey = "txFullName";
    private final String verbatimCoordinatesKey = "verbatimCoordinates";
    
    
    private final String txFacetKey = "tx";

    private final String textKey = "text:";
    private final String idKey = "id:";
    private final String collectionNameKey = "collectionName:";
    private final String collectionIdKey = "collectionId:";
    private final String collectionCodeKey = "collectionCode:";
    private final String typeStatusKey = "typeStatus:";
    private final String familyKey = "family:";
    private final String eventDateKey = "eventDate:";
    
    private final String txFullNameKey = "txFullName:";
    
   
    
    // field key search
    private final String scientificNameKey = "scientificName:";
    
    
    // field
    private final String scientificNameField = "scientificName";
    
    private final String stringSweden = "Sweden";
    
    
    // query
    private final String collectionCodeQuery = "collectionCode:*";

    private final int autoCompleteNumRowsReturn = 100;
    private final int defaultNumPerPage = 10;
    private final String geohashPreFix = "4_";
    private final String pointPreFix = "0_";
    private final String leftBlacket = "[";
    private final String rightBlacket = "]";
    
    private final String to = " TO ";
    private final String toWithStar = " TO *]";
    private final int collectionFacetLimit = 100;
    private final int catalogedMonthLimit = 12;

    private SolrQuery query;
    private QueryResponse response;
    private NoOpResponseParser rawJsonResponseParser;
    private QueryRequest request;

    private YearMonth yearMonth;
    private int yearOfToday;
    private int lastTenYear;
    private int nextYear;
    private String dateRange;
    
    private StringBuilder fuzzySeachTextSb;
    
    final TermsFacetMap catalogedMonthFacet;
    final TermsFacetMap mapFacet;
    final TermsFacetMap imageFacet;
    final TermsFacetMap inSwedenFacet;
    final TermsFacetMap typeFacet;
    final TermsFacetMap collectionFacet;
    final TermsFacetMap geoFacet;
     
    @Inject
    private InitialProperties properties;

    public SolrService() {
        catalogedMonthFacet
                = new TermsFacetMap(catalogedMonthFacetKey)
                        .setLimit(catalogedMonthLimit);
        
        mapFacet = new TermsFacetMap(point1FacetKey)
                .includeAllBucketsUnionBucket(true)
                .setLimit(1);
        
        imageFacet = new TermsFacetMap(imageFacetKey);
        
        inSwedenFacet = new TermsFacetMap(countryFacetKey)
                .setTermPrefix(stringSweden);
        
        typeFacet = new TermsFacetMap(typeStatusFacetKey)
                .includeAllBucketsUnionBucket(true)
                .setLimit(1);
        
        collectionFacet = new TermsFacetMap(collectionCodeFacetKey)
                .setLimit(50); 
        
        geoFacet = new TermsFacetMap(point01FacetKey)
                .setLimit(50000);
        
    }

 
    
    
    
    
    
    
    
    @PostConstruct
    public void init() {
        log.info("init from search...");

        client = new HttpSolrClient.Builder(properties.getSolrURL()).build();
        username = properties.getUsername();
        password = properties.getPassword();
        
        yearMonth = YearMonth.from(LocalDate.now());
        yearOfToday = yearMonth.getYear();

        nextYear = yearOfToday + 1;
        lastTenYear = yearOfToday - 10;  
    }
    
    public String getInitalData() { 
        final RangeFacetMap rangeFacet = new RangeFacetMap(
                catalogedYearFacetKey, lastTenYear, nextYear, 1);
        rangeFacet.withSubFacet(catalogedMonthFacetKey, catalogedMonthFacet);
     
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(collectionCodeQuery) 
                .returnFields(collectionNameFacetKey)
                .withFacet(imageFacetKey, imageFacet) 
                .withFacet(mapFacetKey, mapFacet) 
                .withFacet(inSwedenFacetKey, inSwedenFacet) 
                .withFacet(typeStatusFacetKey, typeFacet) 
//                .withFacet(collectionCodeFacetKey, collectionFacet)
                .withFacet(catalogedYearFacetKey, rangeFacet);
//                .setSort("index asc");

        jsonRequest.setBasicAuthCredentials(username, password);
        try {
            response = jsonRequest.process(client);

//            log.info("json: {}", response.jsonStr()); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return response.jsonStr();
    }
    
    public String getChartData(String collectionCode) {
        log.info("getChartData : {}", collectionCode);
     
        final RangeFacetMap rangeFacet = new RangeFacetMap(
                catalogedYearFacetKey, lastTenYear, nextYear, 1); 
        rangeFacet.withSubFacet(catalogedMonthFacetKey, catalogedMonthFacet);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(collectionCodeKey + collectionCode)
                .returnFields(collectionNameField)
                .withFacet(collectionCodeFacetKey, rangeFacet);

        jsonRequest.setBasicAuthCredentials(username, password);
        try {
            response = jsonRequest.process(client);

//            log.info("json: {}", response.jsonStr());
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
        return response.jsonStr();
    }
    
    public String freeTextSearch(int start, boolean hasImages, boolean hasCoordinates,
            boolean isType, boolean isInSweden, String collections,
            int numPerPage, String text, String sort) {
        log.info("freeTextSearch ..... : {} -- {} ", collections + " -- " + numPerPage, text);
  
//        final TermsFacetMap geoHashFacet = new TermsFacetMap(point01FacetKey)
//                .setLimit(30000);
        
//        final HeatmapFacetMap heatMap = new HeatmapFacetMap(geoFacetKey)
//                .setGridLevel(2)
//                ;
//        
        log.info("freeTextSearch search text : {}", text);
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)  
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(geoFacetKey, geoFacet);
        
        if (hasImages) {
            jsonRequest.withFilter(imageFilter);
        }

        if (hasCoordinates) {
            jsonRequest.withFilter(mapFilter);
        }
        
        if (isType) {
            jsonRequest.withFilter(typeFilter);
        }
        
        if (isInSweden) {
            jsonRequest.withFilter(isInSwedenFilter);
        }
        
        if (collections != null) {
            jsonRequest.withFilter(collectionCodeKey + collections);
        }


        if (!StringUtils.isBlank(sort)) {
            jsonRequest.setSort(sort);
        }
 
        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);
//            log.info("simplesearch what... {}", jsonResponse);

        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return jsonResponse;
    }
    
    public String search(Map<String, String> paramMap, String text, 
            String scientificName, int start, int numPerPage, String sort) {
        
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)  
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(geoFacetKey, geoFacet);
         
        return null;
    }
    
    public String search(String text, String scientificName, 
            boolean hasImages, boolean hasCoordinates, boolean isType, 
            boolean isInSweden, String collections, String startDate, String endDate,
            int start, int numPerPage, String sort ) {
         
         
        text = scientificName != null ? scientificName : catchAllField + text;
         
        log.info("search text : {}", text);
        
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)  
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(geoFacetKey, geoFacet);
         
        if (hasImages) {
            jsonRequest.withFilter(imageFilter);
        }

        if (hasCoordinates) {
            jsonRequest.withFilter(mapFilter);
        }
        
        if (isType) {
            jsonRequest.withFilter(typeFilter);
        }
        
        if (isInSweden) {
            jsonRequest.withFilter(isInSwedenFilter);
        }
        
        if (collections != null) {
            jsonRequest.withFilter(collectionCodeKey + collections);
        }


        if (!StringUtils.isBlank(sort)) {
            jsonRequest.setSort(sort);
        }
 
        if(!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate) ) {
           dateRange = leftBlacket + startDate + to + endDate + rightBlacket;
           jsonRequest.withFilter(eventDateKey + dateRange); 
        } else if(!StringUtils.isBlank(startDate)) {
            dateRange = leftBlacket + startDate + toWithStar;
            jsonRequest.withFilter(eventDateKey + dateRange); 
        }
        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);
//            log.info("simplesearch what... {}", jsonResponse);

        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
        return jsonResponse; 
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
    
     
    public String autoCompleteSearch(String text) {
        log.info("autoCompleteSearch: {} -- {}", text);
        final TermsFacetMap scientificNameFacet = new TermsFacetMap(scientificNameFacetKey);
         
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .returnFields(scientificNameField)
                .withFacet(scientificNameFacetKey, scientificNameFacet)
                .setLimit(50);

        jsonRequest.setBasicAuthCredentials(username, password); 
        try {
            response = jsonRequest.process(client);

//            log.info("json: {}", response.jsonStr()); 
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return response.jsonStr();
    }
    
    
    public String scientificNameSearch(int start, int numPerPage, String text,
            String sort) {
        log.info("scientificNameSearch ..... : {} -- {} ", start + " -- " + numPerPage, text);
         
   
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(geoFacetKey, geoFacet);

        if (!StringUtils.isBlank(sort)) {
            jsonRequest.setSort(sort);
        }
 
        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);
//            log.info("simplesearch what... {}", jsonResponse);

        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return jsonResponse;
    }
    
    
    
    
    public String getMapData(String text, int start, int numPerPage) {
        
        final HeatmapFacetMap heatMap = new HeatmapFacetMap(geoFacetKey);
           
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage);

        
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     


     
    
    /**
     * Search all the records without any filters, sorted by cataloged date
     *
     * @param start
     * @param numPerPage
     * @param text
     * @param sort
     * @return String
     */
    public String simpleSearch(int start, int numPerPage, String text,
            String sort) {
        log.info("simpleSearch ..... : {} -- {} ", start + " -- " + numPerPage, text);

        // sort :    (e.g. "startdate asc")
//        final TermsFacetMap mapFacet = new TermsFacetMap(mapFacetKey);
//        final TermsFacetMap imageFacet = new TermsFacetMap(imageFacetKey);
//        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSwedenFacetKey);
//        final TermsFacetMap isTypeFacet = new TermsFacetMap(isTypeFacetKey);
//        final TermsFacetMap typeStatusFacet = new TermsFacetMap(typeStatusFacetKey)
//                .setLimit(120);
//        final TermsFacetMap collectionFacet = new TermsFacetMap(collectionNameFacetKey)
//                .setLimit(50);
        
        final TermsFacetMap familyFacet = new TermsFacetMap(familyFacetKey)
                .setLimit(200);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(imageFacetKey, imageFacet)
                .withFacet(mapFacetKey, mapFacet)
                .withFacet(inSwedenFacetKey, inSwedenFacet) 
                .withFacet(typeStatusFacetKey, typeFacet)
                .withFacet(familyFacetKey, familyFacet)
                .withFacet(collectionNameFacetKey, collectionFacet);

        if (!StringUtils.isBlank(sort)) {
            jsonRequest.setSort(sort);
        }
 
        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);
//            log.info("simplesearch what... {}", jsonResponse);

        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return jsonResponse;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     
    public String getStatisticData() {
        yearMonth = YearMonth.from(LocalDate.now());
        yearOfToday = yearMonth.getYear();

        nextYear = yearOfToday + 1;
        lastTenYear = yearOfToday - 10;
        
//        final TermsFacetMap collectionFacet = new TermsFacetMap(collectionCodeFacetKey)
//                .setLimit(50); 
//
//        final TermsFacetMap catalogedMonthFacet
//                = new TermsFacetMap(catalogedMonthStringFacetKey)
//                        .setLimit(collectionFacetLimit);

        final RangeFacetMap rangeFacet = new RangeFacetMap(
                catalogedYearFacetKey, lastTenYear, nextYear, 1);
        rangeFacet.withSubFacet(catalogedMonthFacetKey, catalogedMonthFacet);
        
        collectionFacet.withSubFacet("catalogedYear", rangeFacet);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(wildSearch)
                .returnFields(collectionNameField)
                .withFacet(collectionCodeFacetKey, collectionFacet);

        jsonRequest.setBasicAuthCredentials(username, password);
        try {
            response = jsonRequest.process(client);

//            log.info("json: {}", response.jsonStr());
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
        return response.jsonStr();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    

    /**
     * Search all the records without any filters, sorted by cataloged date
     *
     * @param start
     * @param numPerPage
     * @param text
     * @param collection
     * @param collections
     * @param typeStatus
     * @param family
     * @param hasCoordinates
     * @param hasImage
     * @param inSweden
     * @param isType
     * @param sort
     * @return String
     */
    public String filterSearch(int start, int numPerPage, String text,
            String collection, String collections, String typeStatus, String family,
            String hasCoordinates, String hasImage, String inSweden, String isType, String sort) {
        log.info("filterSearch: {} -- {} ", collection + " -- " + typeStatus, text);

        final TermsFacetMap mapFacet = new TermsFacetMap(mapFacetKey);
        final TermsFacetMap imageFacet = new TermsFacetMap(imageFacetKey);
        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSwedenFacetKey);
        final TermsFacetMap isTypeFacet = new TermsFacetMap(isTypeFacetKey);
        final TermsFacetMap typeStatusFacet = new TermsFacetMap(typeStatusFacetKey)
                .setLimit(100);
        final TermsFacetMap collectionFacet = new TermsFacetMap(collectionNameFacetKey)
                .setLimit(100);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(imageFacetKey, imageFacet)
                .withFacet(mapFacetKey, mapFacet)
                .withFacet(inSwedenFacetKey, inSwedenFacet)
                .withFacet(isTypeFacetKey, isTypeFacet)
                .withFacet(typeStatusFacetKey, typeStatusFacet)
                .withFacet(collectionNameFacetKey, collectionFacet);

        if (!StringUtils.isBlank(sort)) {
            jsonRequest.setSort(sort);
        }

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

        if (collections != null) {
            jsonRequest.withFilter(collectionIdKey + collections);
        }

        if (typeStatus != null) {
            jsonRequest.withFilter(typeStatusKey + typeStatus);
        }

        if (family != null) {
            jsonRequest.withFilter(familyKey + family);
        }

        if (hasImage != null) {
            jsonRequest.withFilter(hasImage);
        }

        if (hasCoordinates != null) {
            jsonRequest.withFilter(hasCoordinates);
        }

        if (inSweden != null) {
            jsonRequest.withFilter(inSweden);
        }

        if (isType != null) {
            jsonRequest.withFilter(isType);
        }

        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);

//            log.info("what... {}", jsonResponse);
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return jsonResponse;
    }

    public String getTypeStatus() {
        log.info("typestatus");

        final TermsFacetMap typeStatusFacet = new TermsFacetMap(typeStatusFacetKey)
                .setLimit(120);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(wildSearch)
                .withFacet(typeStatusFacetKey, typeStatusFacet)
                .setLimit(1);

        jsonRequest.setBasicAuthCredentials(username, password);

        String jsonResponse;
        try {
            response = jsonRequest.process(client);

            rawJsonResponseParser = new NoOpResponseParser();
            rawJsonResponseParser.setWriterType(jsonKey);
            jsonRequest.setResponseParser(rawJsonResponseParser);

            jsonResponse = (String) client.request(jsonRequest).get(responseKey);

//            log.info("type status... {}", jsonResponse);
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }

        return jsonResponse;
    }

    public String download(String text, String collection, String typeStatus,
            String family, int start, int rows) {
        log.info("download");

        query = new SolrQuery();
        query.setQuery(text)
                .addField(authorField)
                .addField(catalogNumberField)
                .addField(collectionNameField)
                .addField(continentField)
                .addField(countryField)
                .addField(countyField)
                .addField(currentDeterminationField)
                .addField(determinerField)
                .addField(districtField)
                .addField(familyField)
                .addField(genusField)
                .addField(higherTxField)
                .addField(continentField)
                .addField(latitudeField)
                .addField(localityField)
                .addField(longitudeField)
                .addField(oceanOrSeaField)
                .addField(preservationField)
                .addField(speciesField)
                .addField(stateField)
                .addField(stationFieldNumberField)
                .addField(txFullNameField)
                .addField(catalogedDateField)
                .addField(startDateField)
                .addField(collectorField)
                .addField(commonNameField)
                .addField(remarksField)
                .addField(preprationField)
                .addField(typeStatusField)
                .setStart(start)
                .setRows(rows);

        if (collection != null) {
            query.addFilterQuery(collectionNameKey + collection);
        }

        if (typeStatus != null) {
            query.addFilterQuery(typeStatusKey + typeStatus);
        }

        if (family != null) {
            query.addFilterQuery(familyKey + family);
        }

        try {
            request = new QueryRequest(query);
            request.setBasicAuthCredentials(username, password);
            response = request.process(client);
        } catch (SolrServerException | IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
//        log.info("json: {}", response.jsonStr());
        return response.jsonStr();
    }

    public String mapDataSearch(String text, String collection, String collections,
            String typeStatus, String family, String hasCoordinates, String hasImage,
            String inSweden, String isType) {
        log.info("mapDataSearch ..... : {} -- {} ", text, collection);

        final TermsFacetMap mapFacet = new TermsFacetMap(mapFacetKey);
        final TermsFacetMap imageFacet = new TermsFacetMap(imageFacetKey);
        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSwedenFacetKey);
        final TermsFacetMap isTypeFacet = new TermsFacetMap(isTypeFacetKey);
        final TermsFacetMap typeStatusFacet = new TermsFacetMap(typeStatusFacetKey)
                .setLimit(120);
        final TermsFacetMap collectionFacet = new TermsFacetMap(collectionNameFacetKey)
                .setLimit(50);
        final TermsFacetMap familyFacet = new TermsFacetMap(familyFacetKey)
                .setLimit(200);

        final TermsFacetMap geoHashFacet = new TermsFacetMap(geohashFacetKey)
                .setLimit(20000)
                .setTermPrefix(geohashPreFix);

//        final TermsFacetMap geohash5Facet = new TermsFacetMap(geohashFacetKey)
//                .setLimit(5000)
//                .setTermPrefix("5_");
//        
//        geoHashFacet.withSubFacet(geohashFacetKey, geohash5Facet);
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setLimit(defaultNumPerPage)
                .withFacet(imageFacetKey, imageFacet)
                .withFacet(mapFacetKey, mapFacet)
                .withFacet(inSwedenFacetKey, inSwedenFacet)
                .withFacet(isTypeFacetKey, isTypeFacet)
                .withFacet(typeStatusFacetKey, typeStatusFacet)
                .withFacet(familyFacetKey, familyFacet)
                .withFacet(collectionNameFacetKey, collectionFacet)
                .withFacet(geohashFacetKey, geoHashFacet);

        if (collection != null) {
            jsonRequest.withFilter(collectionNameKey + collection);
        }

        if (collections != null) {
            jsonRequest.withFilter(collectionIdKey + collections);
        }

        if (typeStatus != null) {
            jsonRequest.withFilter(typeStatusKey + typeStatus);
        }

        if (family != null) {
            jsonRequest.withFilter(familyKey + family);
        }

        if (hasImage != null) {
            jsonRequest.withFilter(hasImage);
        }

        if (hasCoordinates != null) {
            jsonRequest.withFilter(hasCoordinates);
        }

        if (inSweden != null) {
            jsonRequest.withFilter(inSweden);
        }

        if (isType != null) {
            jsonRequest.withFilter(isType);
        }

        jsonRequest.setBasicAuthCredentials(properties.getUsername(), properties.getPassword());
        try {
            response = jsonRequest.process(client);
//            log.info("json: {}", response.jsonStr());
        } catch (SolrServerException | IOException ex) {
            log.warn(ex.getMessage());
            return null;
        }
        return response.jsonStr();
    }
  

   

  



//    public String getStatisticData() {  
//       log.info("getStatisticData");
//       
//       String dateRange = SolrSearchHelper.getInstance().buildTwelveMonthDateRange();
//        
//        final TermsFacetMap catalogedMonthFacet
//                = new TermsFacetMap("catalogedMonthString")
//                        .setLimit(collectionFacetLimit);
////        final TermsFacetMap catalogedYear = new TermsFacetMap("catalogedYear")
////                .setLimit(1); 
////        catalogedMonthFacet.withSubFacet("catalogedYear", catalogedYear);
//        RangeFacetMap rangeFacet = new RangeFacetMap(
//                "catalogedYear", 2014, 2025, 1);
//        rangeFacet.withSubFacet("catalogedMonth", catalogedMonthFacet);
//      
//        
//        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
////                .setQuery(dateRange)
//                .setQuery(wildSearch)
//                .returnFields("collectionName")
////                .withFacet("catalogedMonth", catalogedMonthFacet)
//                .withFacet("catalogedYear", rangeFacet);
//        
//            jsonRequest.setBasicAuthCredentials(username, password);
//        
//        try {
//            response = jsonRequest.process(client);
//
//            log.info("json: {}", response.jsonStr()); 
//        } catch (SolrServerException | IOException ex) {
//            log.error(ex.getMessage());
//            return null;
//        } 
//        return response.jsonStr();
//    }
}
