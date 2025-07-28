package se.nrm.samlingar.api.logic;

import ch.hsr.geohash.GeoHash; 
import java.io.IOException; 
import java.io.StringReader;
import java.io.StringWriter;  
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader; 
import javax.ws.rs.core.MultivaluedMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter; 
import org.apache.commons.lang3.StringUtils; 
import se.nrm.samlingar.api.solr.services.Solr;
import se.nrm.samlingar.api.solr.services.SolrService;
import se.nrm.samlingar.api.utils.SolrSearchHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class SamlingarLogic {

    @Inject
    private SolrService service;
    
    @Inject
    private Solr solr;

    private final String valKey = "val";
    private final String countKey = "count";

    private final String geohashKey = "geohash";
    private final String facetsKey = "facets";
    private final String bucketsKey = "buckets";
    private final String responseKey = "response";
    private final String docsKey = "docs";

    private final String latitudeKey = "latitude";
    private final String longitudeKey = "longitude";
    private final String geoDataKey = "geoData";

    private final String prefix = "4_";


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
    private final String remarksField = "remarks";
    private final String speciesField = "species";
    private final String stateField = "state";
    private final String stationFieldNumberField = "stationFieldNumber";
    private final String synonymAuthorField = "synonymAuthor";
    private final String txFullNameField = "txFullName";
    private final String typeStatusField = "typeStatus";

    private final String catalogedDateField = "catalogedDate";
    
    private final String collectorField = "collector";
    private final String commonNameField = "commonName";
    private final String preprationField = "prepration";
    
    
    
    
    

    private final String collectionCodeKey = "collectionCode";
    private final String endDateKey = "endDate";
    private final String fuzzySearch = "fuzzySearch";
    private final String numPerPageKey = "numPerPage"; 
    private final String scientificNameKey = "scientificName";
    private final String searchModeKey = "searchMode";
    private final String startKey = "start";
    private final String startDateKey = "startDate";
    private final String textKey = "text";
    private final String localityKey = "locality";
    private final String eventDateKey = "eventDate:";

    
    
    
    private final String sortKey = "sort";
    
    
    
    // Search field key 
 
    
//    private int total;
//
//    private GeoHash geohash;
//
//    private JsonObjectBuilder attBuilder;
    
    
    
    private JsonArrayBuilder arrayBuilder;
//    
    
    
    private final int downloadSize = 5000;
    private final int maxDownload = 50000;
    private final String numRowsKey = "numRows";
    private final String contains = "contains";
    
    private final String wildCard = "*";
    private final String leftBlacket = "[";
    private final String rightBlacket = "]";
    
    private final String to = " TO ";
    private final String toWithStar = " TO *]";
    
    private int start = 0;
    private int numPerPage = 10;
    private boolean isFuzzySearch;
    private String scientificName;
    private String locality;
    private String searchMode; 
    private String text;
    private String sort;
    private String startDate;
    private String endDate;
    private String facets;
    private String dateRange;
    private boolean yearChart;
    
    private StringBuilder dateRangeSb;
        
    
    public String getInitalData() {
        log.info("getInitalData");
        return solr.getInitalData();
    }
     
            
    public String autoCompleteSearch(String text, String field) {
        text = SolrSearchHelper.getInstance()
                .buildSearchText(text, field, true);
      
        return solr.autoCompleteSearch(text, field);
    }
    
    public String scientificNameSearch(String text, String searchMode, 
            int start, int numPerPage, String sort ) {
        log.info("scientificNameSearch : {}", text);
        
        text = SolrSearchHelper.getInstance().buildSearchText(text, scientificNameKey,  searchMode, false);
        log.info("text... {}", text);
        return solr.scientificNameSearch(start, numPerPage, text, sort);
    }
    
    
     public String search(MultivaluedMap<String, String> queryParams) {
         
        Map<String, String> paramMap = new HashMap<>();
        text = wildCard;
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
             
            switch (entry.getKey()) {
                case scientificNameKey: 
                    scientificName = queryParams.get(scientificNameKey).get(0); 
                    break;
                case localityKey: 
                    locality = queryParams.get(localityKey).get(0); 
                    break;
                case textKey: 
                    text = queryParams.get(textKey).get(0); 
                    break;
                case startDateKey: 
                    startDate = queryParams.get(startDateKey).get(0); 
                    break;
                case endDateKey: 
                    endDate = queryParams.get(endDateKey).get(0); 
                    break;
                case startKey:
                    start = Integer.parseInt(queryParams.get(startKey).get(0)); 
                    break;
                case numPerPageKey:
                    numPerPage = Integer.parseInt(queryParams.get(numPerPageKey).get(0));
                    break;  
                case fuzzySearch:
                    isFuzzySearch = Boolean.parseBoolean(queryParams.get(fuzzySearch).get(0));
                    break;
                case searchModeKey: 
                    searchMode = queryParams.get(searchModeKey).get(0); 
                    break;
                case sortKey:
                    sort =  queryParams.get(sortKey).get(0);
                    break;
                case facetsKey:
                    facets = queryParams.get(facetsKey).get(0);
                    break;
                default:
                    paramMap.put(entry.getKey(), entry.getValue().get(0));
                    break;
            }
        }
        
        
        
        if(scientificName != null) {
            scientificName = SolrSearchHelper.getInstance().buildSearchText(
                scientificName, scientificNameKey, searchMode, isFuzzySearch);
        }
        if(locality != null) {
            locality = SolrSearchHelper.getInstance().buildSearchText(
                locality, localityKey, contains, true);
        }
        if(text != null && !text.equals(wildCard)) {
            text = SolrSearchHelper.getInstance().buildSearchText(
                text, textKey, true);
        }  
        
        dateRangeSb = new StringBuilder();
        if(!StringUtils.isBlank(startDate)) {
            dateRangeSb.append(eventDateKey);
            dateRangeSb.append(leftBlacket);
            dateRangeSb.append(startDate);
            
            if(!StringUtils.isBlank(endDate)) {
                dateRangeSb.append(to);
                dateRangeSb.append(endDate);
                dateRangeSb.append(rightBlacket);
            } else {
                dateRangeSb.append(toWithStar);
            }
            dateRange = dateRangeSb.toString().trim();
        } else {
            dateRange = null;
        }
        
        log.info("scientificName : {} -- {}", scientificName, locality);
        
        
          
        return solr.search(paramMap, text, scientificName, locality, dateRange, facets,
                start, numPerPage, sort);
        
    } 
     
    public String getChart(String collectionCode, String isYearChart) {
        
        collectionCode = SolrSearchHelper.getInstance().buildSearchCollectionCode(
                collectionCodeKey, collectionCode); 
        
        yearChart = Boolean.parseBoolean(isYearChart);
        startDate = SolrSearchHelper.getInstance().getStartDate(yearChart);  
        endDate = SolrSearchHelper.getInstance().getEndDate();  

        return solr.getChart(collectionCode, startDate, endDate, yearChart); 
    }
    
    public String download(MultivaluedMap<String, String> queryParams) {
        
        int numperOfRows = 0;

        Map<String, String> paramMap = new HashMap<>();
        text = wildCard;
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
             
            switch (entry.getKey()) {
                case scientificNameKey: 
                    scientificName = queryParams.get(scientificNameKey).get(0); 
                    break;
                case textKey: 
                    text = queryParams.get(textKey).get(0); 
                    break;
                case startDateKey: 
                    startDate = queryParams.get(startDateKey).get(0); 
                    break;
                case endDateKey: 
                    endDate = queryParams.get(endDateKey).get(0); 
                    break;
                case startKey:
                    start = Integer.parseInt(queryParams.get(startKey).get(0)); 
                    break;
                case numRowsKey:
                    numperOfRows = Integer.parseInt(queryParams.get(numRowsKey).get(0));
                    break;  
                case fuzzySearch:
                    isFuzzySearch = Boolean.parseBoolean(queryParams.get(fuzzySearch).get(0));
                    break;
                case sortKey:
                    sort =  queryParams.get(sortKey).get(0);
                    break;
    
                default:
                    paramMap.put(entry.getKey(), entry.getValue().get(0));
                    break;
            }
        }
        
        
        if(scientificName != null) {
            scientificName = SolrSearchHelper.getInstance().buildSearchText(
                scientificName, scientificNameKey, isFuzzySearch);
        }
        if(text != null && !text.equals(wildCard)) {
            text = SolrSearchHelper.getInstance().buildSearchText(
                text, textKey, true);
        }  
        
        dateRangeSb = new StringBuilder();
        if(!StringUtils.isBlank(startDate)) {
            dateRangeSb.append(eventDateKey);
            dateRangeSb.append(leftBlacket);
            dateRangeSb.append(startDate);
            
            if(!StringUtils.isBlank(endDate)) {
                dateRangeSb.append(to);
                dateRangeSb.append(endDate);
                dateRangeSb.append(rightBlacket);
            } else {
                dateRangeSb.append(toWithStar);
            }
            dateRange = dateRangeSb.toString().trim();
        } else {
            dateRange = null;
        }
         
        JsonReader jsonReader = null;
        JsonObject jsonObj;
        JsonArrayBuilder builder = Json.createArrayBuilder();

        String results;
        int totalDownload = numperOfRows <= maxDownload ? numperOfRows : maxDownload;
        for (int i = 0; i < totalDownload; i += downloadSize) {
            arrayBuilder = Json.createArrayBuilder();

            results = solr.download(paramMap, text, scientificName, dateRange, i, downloadSize, sort);
            
            

            jsonReader = Json.createReader(new StringReader(results));

            jsonObj = jsonReader.readObject();
            JsonArray docs = jsonObj.getJsonArray(responseKey);
 
            docs.stream()
                    .forEach(value -> { 
                        builder.add(value);
                    });
        }
        if (jsonReader != null) {
            jsonReader.close();
        }

        JsonArray array = builder.build();
        log.info("size : {}", array.size());
        return array.toString();
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
//    
//    
//    
//    
//    
//    
//    
//    public String getMonthChart(String collectionCode) {
//        collectionCode = SolrSearchHelper.getInstance().buildSearchCollectionCode(
//                collectionCodeKey, collectionCode);
//        
//        startDate = SolrSearchHelper.getInstance().getFirstDayOfLastTwelveMohth();
//        endDate = SolrSearchHelper.getInstance().getTomorrow();
        
//        return Boolean.parseBoolean(isYearChart) ? solr.getYearChart(collectionCode, startDate, endDate) :
//                solr.getMonthChart(collectionCode, startDate, endDate);
//    }
    
//    public String getYearChart(String collectionCode) { 
//        collectionCode = SolrSearchHelper.getInstance().buildSearchCollectionCode(
//                collectionCodeKey, collectionCode);
//        
//        startDate = SolrSearchHelper.getInstance().getFirstDayOfLastTenYears();  
//        endDate = SolrSearchHelper.getInstance().getTomorrow();  
//        
//        return solr.getYearChart(collectionCode, startDate, endDate);

//        
//        return solr.getMonthChart(collectionCode, startDate, endDate);
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     
    public String freeTextSearch(String text, boolean hasImage, boolean hasCoordinates,
            boolean isType, boolean isInSweden, String collections,
            int start, int numPerPage, String sort ) {
        log.info("simpleSearch : {}", text);
        
        text = SolrSearchHelper.getInstance().buildSearchText(text, textKey, true);
        log.info("text : {}", text);
         
        return service.freeTextSearch(start, hasImage, hasCoordinates, 
                isType, isInSweden, collections, numPerPage, text, sort);
    }



     
    public String getChartData(String collection) {
        return service.getChartData(collection);
    } 

    
    public String search(String text, String scientificName, boolean fuzzySearch,
            boolean hasImages, boolean hasCoordinates, boolean isType, 
            boolean isInSweden, String collections, String startDate, String endDate,
            int start, int numPerPage, String sort ) {
        log.info("search : {}", text);
         
        if(scientificName != null) {
            scientificName = SolrSearchHelper.getInstance().buildSearchText(
                scientificName, scientificNameKey, fuzzySearch);
        }
         
        return service.search(text, scientificName, hasImages, hasCoordinates, 
                isType, isInSweden, collections, startDate, endDate ,
                start, numPerPage, sort);
    }
    
    public String searchWithId(String id) {
        log.info("searchWithId : {}", id);
        return service.searchWithId(id);
    }
    
        
//    public String search(Map<String, String> paramMap, String text, String scientificName, 
//            String startDate, String endDate, boolean fuzzySearch, int start, 
//            int numPerPage, String sort ) {
//        
//        if(scientificName != null) {
//            scientificName = SolrSearchHelper.getInstance().buildSearchText(
//                scientificName, scientificNameFieldKey, fuzzySearch);
//        }
//        return service.search(paramMap, text, scientificName, startDate, endDate, null,
//                start, numPerPage, sort);
//        
//    }
    
    
    
    
   
    
    
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
    
    
    
    
    
    
    
    
    
    
    

 
    public String simpleSearch(String text, boolean fuzzySearch, 
            int start, int numPerPage, String sort ) {
        log.info("simpleSearch : {}", text);
        
        text = SolrSearchHelper.getInstance().buildSearchText(text, scientificNameKey, fuzzySearch);
        return service.simpleSearch(start, numPerPage, text, sort);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        
    public String getStatisticData() {
        return service.getStatisticData();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


//    public String filterSerch(int start, int numPerPage, String text,
//            String collection, String collections, String typeStatus, 
//            String family, String hasCoordinates, String hasImage, 
//            String inSweden, String isType, String sort) {
//        log.info("filterSerch : {} -- {}", collection, typeStatus);
//
//        return service.filterSearch(start, numPerPage, text, collection, collections, 
//                typeStatus, family, hasCoordinates, hasImage, inSweden, isType, sort);
//    }
    
//    public String mapDataSearch(String text, String collection, String collections,
//            String typeStatus, String family, String hasCoordinates, 
//            String hasImage, String inSweden, String isType) {
//         
//
//        return service.mapDataSearch(text, collection, collections, 
//                typeStatus, family, hasCoordinates, hasImage, inSweden, isType);
 
//        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
//        JsonObject jsonObj = jsonReader.readObject();
//
//        JsonArray docs = jsonObj.getJsonArray(responseKey);
//        JsonObject facetJson = jsonObj.getJsonObject(facetsKey);
//
//        JsonArray array = facetJson.getJsonObject(geohashKey)
//                .getJsonArray(bucketsKey);
//
//        log.info("geo length : {}", array.size());
//
//        arrayBuilder = Json.createArrayBuilder();
//        attBuilder = Json.createObjectBuilder();
//        array.getValuesAs(JsonObject.class)
//                .stream()
//                .forEach(json -> {
//                    String geohashString = json.getString(valKey);
//                    int count = json.getInt(countKey);
//                    
//                    total += count;
//                    geohash = GeoHash.fromGeohashString(
//                            StringUtils.substringAfter(geohashString, prefix));
//                    
//            
//
//                    double originLat = geohash.getOriginatingPoint().getLatitude();
//                    double originLong = geohash.getOriginatingPoint().getLongitude();
//
//                    attBuilder.add(geohashKey, geohashString);
//                    attBuilder.add(latitudeKey, originLat);
//                    attBuilder.add(longitudeKey, originLong);
//                    attBuilder.add(countKey, count);
//                    arrayBuilder.add(attBuilder);
//                });
//        JsonObjectBuilder jsonBuild = Json.createObjectBuilder();
//        jsonBuild.add(geoDataKey, arrayBuilder.build());
//        jsonBuild.add(countKey, total);
//        jsonBuild.add(facetsKey, facetJson);
//        jsonBuild.add(docsKey, docs);
//        JsonObject json = jsonBuild.build();
//
//        log.info("total : {}", total);
//        jsonReader.close();
//        return json.toString();
//    }

//    public String mapDataSearch(String text, String collection, String collections,
//            String typeStatus, String family, String hasCoordinates, 
//            String hasImage, String inSweden, String isType) {
//        
//        total = 0;
//
//        String jsonString = service.mapDataSearch(text, collection, collections, 
//                typeStatus, family, hasCoordinates, hasImage, inSweden, isType);
// 
//        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
//        JsonObject jsonObj = jsonReader.readObject();
//
//        JsonArray docs = jsonObj.getJsonArray(responseKey);
//        JsonObject facetJson = jsonObj.getJsonObject(facetsKey);
//
//        JsonArray array = facetJson.getJsonObject(geohashKey)
//                .getJsonArray(bucketsKey);
//
//        log.info("geo length : {}", array.size());
//
//        arrayBuilder = Json.createArrayBuilder();
//        attBuilder = Json.createObjectBuilder();
//        array.getValuesAs(JsonObject.class)
//                .stream()
//                .forEach(json -> {
//                    String geohashString = json.getString(valKey);
//                    int count = json.getInt(countKey);
//                    
//                    total += count;
//                    geohash = GeoHash.fromGeohashString(
//                            StringUtils.substringAfter(geohashString, prefix));
//                    
//            
//
//                    double originLat = geohash.getOriginatingPoint().getLatitude();
//                    double originLong = geohash.getOriginatingPoint().getLongitude();
//
//                    attBuilder.add(geohashKey, geohashString);
//                    attBuilder.add(latitudeKey, originLat);
//                    attBuilder.add(longitudeKey, originLong);
//                    attBuilder.add(countKey, count);
//                    arrayBuilder.add(attBuilder);
//                });
//        JsonObjectBuilder jsonBuild = Json.createObjectBuilder();
//        jsonBuild.add(geoDataKey, arrayBuilder.build());
//        jsonBuild.add(countKey, total);
//        jsonBuild.add(facetsKey, facetJson);
//        jsonBuild.add(docsKey, docs);
//        JsonObject json = jsonBuild.build();
//
//        log.info("total : {}", total);
//        jsonReader.close();
//        return json.toString();
//    }
    
//    public String getTypeStatus() {
//        return service.getTypeStatus();
//    }

    

//    public void download1(String text, String collection,
//            String typeStatus, String family, int numRows) throws IOException {
//
//        StringWriter sw = new StringWriter();
//
//        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
//                .setHeader(CsvHeader.class)
//                .build();
//        JsonReader jsonReader = null;
//        JsonObject jsonObj;
//        JsonArrayBuilder builder = Json.createArrayBuilder();
//
//        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
//
//            int totalDownload = numRows <= maxDownload ? numRows : maxDownload;
//            for (int i = 0; i < totalDownload; i += downloadSize) {
//                arrayBuilder = Json.createArrayBuilder();
//
//                String results = service.download(text, collection, typeStatus, family, i, downloadSize);
//
//                jsonReader = Json.createReader(new StringReader(results));
//
//                jsonObj = jsonReader.readObject();
//                JsonArray docs = jsonObj.getJsonArray(responseKey);
//                log.info("docs... {}", docs);
//
//                JsonObject json;
//                for (int j = 0; j < docs.size(); j++) {
//                    json = docs.getJsonObject(j);
//                    try {
//                        printer.printRecord(
//                                json.getString(catalogNumberField),
//                                json.getString(collectionNameField),
//                                json.getString(higherTxField),
//                                json.getString(familyField),
//                                json.getString(genusField),
//                                json.getString(speciesField),
//                                json.getString(authorField),
//                                json.getString(collectorField),
//                                json.getString(startDateKey),
//                                json.getString(catalogedDateField),
//                                json.getString(localityField),
//                                json.getString(countyField),
//                                json.getString(stateField),
//                                json.getString(countryField),
//                                json.getString(continentField),
//                                json.getString(oceanOrSeaField),
//                                json.getString(latitudeField),
//                                json.getString(longitudeField),
//                                json.getString(stationFieldNumberField),
//                                json.getString(determinerField),
//                                json.getString(preprationField),
//                                json.getString(typeStatusField),
//                                json.getString(remarksField));
//
//                    } catch (IOException ex) {
//                        log.error(ex.getMessage());
//                    }
//
//                }
//            }
//        }
//            
//            
//            final OutputStream os = new FileOutputStream("/tmp/out");
//final PrintStream printStream = new PrintStream(os);
//printStream.print("String");
//printStream.close();
//            
//            StreamingOutput stream = (OutputStream out) -> { 
//      try(FileInputStream inp = new FileInputStream(sw)) {
//        byte[] buff = new byte[1024];
//        int len;
//        while ((len = inp.read(buff)) >= 0) {
//          out.write(buff, 0, len);
//        }
//        out.flush();
//      } catch (Exception e) {
//        log.error(e.getMessage());
//      } finally {
//   
//      } 
//    }
//            
//            
//
//            InputStream stream = new ByteArrayInputStream(sw.toString().getBytes());
//
//            File file = new DefaultStreamedContent(stream, mimetype, "downloadFile.csv");
//            if (jsonReader != null) {
//                jsonReader.close();
//            }
//
//        }

//         
//        InputStream stream = new ByteArrayInputStream(sw.toString().getBytes());
//     
//        File file = new DefaultStreamedContent(stream, mimetype, "downloadFile.csv");  
//    }
    

}
