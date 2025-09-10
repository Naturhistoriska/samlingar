package se.nrm.samlingar.api.solr.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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
import org.apache.solr.common.util.SimpleOrderedMap;
import se.nrm.samlingar.api.logic.InitialProperties;
import se.nrm.samlingar.api.utils.SolrSearchHelper;

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
    private final String star = "*";

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
    private final String facetHeatMapKey = "facet.heatmap";
    private final String facetHeatmapGeomKey = "facet.heatmap.geom";
    private final String facetHeatmepGridLevelKey = "facet.heatmap.gridLevel";

    private final String rowsKey = "rows";
    private final String columnsKey = "columns";

    private final String minXKey = "minX";
    private final String maxXKey = "maxX";

    private final String minYKey = "minY";
    private final String maxYKey = "maxY";

    private final String coordinatesKey = "coordinates";
    private final String propertiesKey = "properties";
    private final String featuresKey = "features";

    private final String strTrue = "true";
    private final String geohashField = "geohash";
    private final String envelope = "ENVELOPE(-180,180,90,-90)";
    private final String gridLevel = "3";

    private final String facetCount = "facet_counts";
    private final String facetHeatMap = "facet_heatmaps";
    private final String countsInts2D = "counts_ints2D";
    private final String countKey = "count";
    private final String geometryKey = "geometry";

    private final String typeKey = "type";
    private final String featureKey = "Feature";
    private final String polygonKey = "Polygon";

    private final String featureCollectionKey = "FeatureCollection";

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

    private final String responseKey = "response";

    private final String cursorMarkKey = "cursorMark";

    private final int maxExport = 50000;
    private final int batchSize = 1000;

    private StringBuilder sb;
    private String gap;

    private List<String> fields;

    private final ObjectMapper mapper = new ObjectMapper();

    private SimpleOrderedMap<Object> facetCounts;
    private SimpleOrderedMap<Object> facetHeatmaps;

    private SimpleOrderedMap<Object> heatmap;
    
    private List<List<Integer>> counts;
    private Map<String, Object> geojson;
    private List<Map<String, Object>> features;
    
    private double west;
    private double east;
    private double north;
    private double south;

    
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
        log.info("getChart collection : {} ", collection);

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
            String locality, String dateRange, String facets,
            int start, int numPerPage, String sort) {

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

    public String geojson(Map<String, String> paramMap, String text, String scientificName,
            String locality, String dateRange, String pt, int start, int rows) {
        log.info("geojson... {} -- {}", start, rows);

        query = new SolrQuery(text);
        query.addField(idKey);
        query.addField(locationKey);
        query.addField(localityKey);
        query.addField(scientificNameKey);
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
        query.set(facetHeatMapKey, geohashField);
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


        try {
            response = client.query(query);
            return buildGeoJson(response); 
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

    private String buildGeoJson(QueryResponse resp) throws JsonProcessingException {

        facetCounts = (SimpleOrderedMap<Object>) resp.getResponse().get(facetCount);
        facetHeatmaps = (SimpleOrderedMap<Object>) facetCounts.get(facetHeatMap);
        heatmap = (SimpleOrderedMap<Object>) facetHeatmaps.get(geohashField);

        counts = (List<List<Integer>>) heatmap.get(countsInts2D);

        int rows = (Integer) heatmap.get(rowsKey);
        int cols = (Integer) heatmap.get(columnsKey);
        double minX = ((Number) heatmap.get(minXKey)).doubleValue();
        double maxX = ((Number) heatmap.get(maxXKey)).doubleValue();
        double minY = ((Number) heatmap.get(minYKey)).doubleValue();
        double maxY = ((Number) heatmap.get(maxYKey)).doubleValue();

        double cellWidth = (maxX - minX) / cols;
        double cellHeight = (maxY - minY) / rows;

        geojson = new HashMap<>();
        geojson.put(typeKey, featureCollectionKey);

        byte[] responseBytes;

        features = new ArrayList<>();

        for (int y = 0; y < counts.size(); y++) {
            List<Integer> rowList = counts.get(y);
            if (rowList == null) {
                continue;
            }

            for (int x = 0; x < rowList.size(); x++) {
                Integer count = rowList.get(x);
                if (count == null || count == 0) {
                    continue;
                }

                west = minX + x * cellWidth;
                east = west + cellWidth;
                north = maxY - y * cellHeight;
                south = north - cellHeight;

                Map<String, Object> feature = new HashMap<>();
                feature.put(typeKey, featureKey);

                Map<String, Object> geometry = new HashMap<>();
                geometry.put(typeKey, polygonKey);

                List<List<Double>> ring = new ArrayList<>();
                ring.add(Arrays.asList(west, north));
                ring.add(Arrays.asList(east, north));
                ring.add(Arrays.asList(east, south));
                ring.add(Arrays.asList(west, south));
                ring.add(Arrays.asList(west, north)); // close the polygon

                List<List<List<Double>>> coordinates = new ArrayList<>();
                coordinates.add(ring);

                geometry.put(coordinatesKey, coordinates);

                feature.put(geometryKey, geometry);

                Map<String, Object> property = new HashMap<>();
                property.put(countKey, count);
                feature.put(propertiesKey, property);

                features.add(feature);
            }
        }
        geojson.put(featuresKey, features);

        responseBytes = mapper.writeValueAsBytes(geojson);

        return new String(responseBytes, StandardCharsets.UTF_8);

    }
}
