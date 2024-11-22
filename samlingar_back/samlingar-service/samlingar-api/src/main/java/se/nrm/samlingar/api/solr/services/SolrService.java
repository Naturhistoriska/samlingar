package se.nrm.samlingar.api.solr.services;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
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

    private final String jsonKey = "json";
    private final String responseKey = "response";
    private final String wildSearch = "*:*";

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

    private final String catalogedMonthStringFacetKey = "catalogedMonthString";
    private final String catalogedMonthFacetKey = "catalogedMonth";
    private final String catalogedYearFacetKey = "catalogedYear";
    private final String mapFacetKey = "map";
    private final String imageFacetKey = "image";
    private final String inSwedenFacetKey = "inSweden";
    private final String isTypeFacetKey = "isType";
    private final String typeStatusFacetKey = "typeStatus";
    private final String collectionNameFacetKey = "collectionName";
    private final String collectionIdFacetKey = "collectionId";
    private final String familyFacetKey = "family";
    private final String genusFacetKey = "genus";
    private final String geohashFacetKey = "geohash";
    private final String txFullNameFacetKey = "txFullName";
    private final String txFacetKey = "tx";

    private final String textKey = "text:";
    private final String collectionNameKey = "collectionName:";
    private final String collectionIdKey = "collectionId:";
    private final String typeStatusKey = "typeStatus:";
    private final String familyKey = "family:";
    private final String txFullNameKey = "txFullName:";

    private final int autoCompleteNumRowsReturn = 100;
    private final int defaultNumPerPage = 10;
    private final String geohashPreFix = "4_";

    private final int collectionFacetLimit = 100;

    private SolrQuery query;
    private QueryResponse response;
    private NoOpResponseParser rawJsonResponseParser;
    private QueryRequest request;

    private YearMonth yearMonth;
    private int yearOfToday;
    private int lastTenYear;
    private int nextYear;

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
     * @param sort
     * @return String
     */
    public String simpleSearch(int start, int numPerPage, String text,
            String sort) {
        log.info("simpleSearch ..... : {} -- {} ", start + " -- " + numPerPage, text);

        // sort :    (e.g. "startdate asc")
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

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(text)
                .setOffset(start)
                .setLimit(numPerPage)
                .withFacet(imageFacetKey, imageFacet)
                .withFacet(mapFacetKey, mapFacet)
                .withFacet(inSwedenFacetKey, inSwedenFacet)
                .withFacet(isTypeFacetKey, isTypeFacet)
                .withFacet(typeStatusFacetKey, typeStatusFacet)
                .withFacet(familyFacetKey, familyFacet)
                .withFacet(collectionNameFacetKey, collectionFacet);

        if (!StringUtils.isBlank(sort)) {
            jsonRequest.setSort(sort);
        }

//                .withFacet(geohashFacetKey, geoHashFacet);
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
 
    public String getInitalData() {

        yearMonth = YearMonth.from(LocalDate.now());
        yearOfToday = yearMonth.getYear();

        nextYear = yearOfToday + 1;
        lastTenYear = yearOfToday - 10;

        final TermsFacetMap catalogedMonthFacet
                = new TermsFacetMap(catalogedMonthStringFacetKey)
                        .setLimit(collectionFacetLimit);

        final RangeFacetMap rangeFacet = new RangeFacetMap(
                "catalogedYear", lastTenYear, nextYear, 1);
        rangeFacet.withSubFacet(catalogedMonthFacetKey, catalogedMonthFacet);

        final TermsFacetMap mapFacet = new TermsFacetMap(mapFacetKey);
        final TermsFacetMap imageFacet = new TermsFacetMap(imageFacetKey);
        final TermsFacetMap inSwedenFacet = new TermsFacetMap(inSwedenFacetKey);
        final TermsFacetMap typeFacet = new TermsFacetMap(isTypeFacetKey);
        final TermsFacetMap collectionFacet = new TermsFacetMap(collectionIdFacetKey)
                .setLimit(50);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(wildSearch)
                .returnFields(collectionNameFacetKey)
                .withFacet(imageFacetKey, imageFacet)
                .withFacet(mapFacetKey, mapFacet)
                .withFacet(inSwedenFacetKey, inSwedenFacet)
                .withFacet(isTypeFacetKey, typeFacet)
                .withFacet(collectionIdFacetKey, collectionFacet)
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

    public String autoCompleteSearch(String text) {

        final TermsFacetMap txFacet = new TermsFacetMap(txFullNameFacetKey);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(txFullNameKey + text)
                .returnFields(txFullNameField)
                .withFacet(txFullNameFacetKey, txFacet)
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

    public String getStatisticData() {
        yearMonth = YearMonth.from(LocalDate.now());
        yearOfToday = yearMonth.getYear();

        nextYear = yearOfToday + 1;
        lastTenYear = yearOfToday - 10;

        final TermsFacetMap catalogedMonthFacet
                = new TermsFacetMap("catalogedMonthString")
                        .setLimit(collectionFacetLimit);

        final RangeFacetMap rangeFacet = new RangeFacetMap(
                "catalogedYear", lastTenYear, nextYear, 1);
        rangeFacet.withSubFacet("catalogedMonth", catalogedMonthFacet);

        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(wildSearch)
                .returnFields("collectionName")
                .withFacet("catalogedYear", rangeFacet);

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
