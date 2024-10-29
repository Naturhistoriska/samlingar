package se.nrm.samlingar.api.logic;
 
import ch.hsr.geohash.GeoHash;
import java.io.StringReader;   
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder; 
import javax.json.JsonReader; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.api.solr.services.SolrService;

/**
 *
 * @author idali
 */
@Slf4j
public class SamlingarLogic {
   
    @Inject
    private SolrService service;
    
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
    private final int downloadSize = 5000;
    private final int maxDownload = 50000;
    
    private GeoHash geohash; 
    
    private JsonObjectBuilder attBuilder;
    private JsonArrayBuilder arrayBuilder;
    
    public String getStaticData() {
        log.info("getStaticData");  
        return service.searchStatisticData();
    }
    
    public String simpleSearch(String text, int start, int numPerPage) {
        log.info("simpleSearch : {}", text);
        return service.simpleSearch(start, numPerPage, text);
    }
    
    public String autoCompleteSearch(String text) {
        return service.autoCompleteSearch(text);
    }
    
    public String filterSerch(int start, int numPerPage, String text,
            String collection, String typeStatus, String family,
            String hasCoordinates, String hasImage, String inSweden, String isType) {
        log.info("filterSerch : {} -- {}", collection, typeStatus);
 
        return service.filterSearch(start, numPerPage, text, collection, typeStatus, 
                family, hasCoordinates, hasImage, inSweden, isType);
    }
    
    public String mapDataSearch(String text, String collection, 
            String typeStatus, String family) {
        
        String jsonString = service.mapDataSearch( text, collection, typeStatus, family);
         
//        JsonObject jsonObj = Json.createReader(new StringReader(jsonString)).readObject(); 
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObj = jsonReader.readObject();
        
        
        JsonArray docs = jsonObj.getJsonArray(responseKey); 
        JsonObject facetJson =jsonObj.getJsonObject(facetsKey);
          
        JsonArray array = facetJson.getJsonObject(geohashKey)
                .getJsonArray(bucketsKey); 
        
        log.info("geo length : {}", array.size());
    
        arrayBuilder = Json.createArrayBuilder();
        attBuilder = Json.createObjectBuilder(); 
        array.getValuesAs(JsonObject.class)
                .stream()
                .forEach(json -> { 
                    String geohashString = json.getString(valKey); 
                    int count = json.getInt(countKey);
                    geohash =GeoHash.fromGeohashString(
                            StringUtils.substringAfter(geohashString, prefix));  
                    
                    double originLat = geohash.getOriginatingPoint().getLatitude();
                    double originLong = geohash.getOriginatingPoint().getLongitude();
                     
                     
                    attBuilder.add(geohashKey, geohashString);
                    attBuilder.add(latitudeKey, originLat);
                    attBuilder.add(longitudeKey, originLong);
                    attBuilder.add(countKey, count);
                    arrayBuilder.add(attBuilder);   
                }); 
        JsonObjectBuilder jsonBuild = Json.createObjectBuilder(); 
        jsonBuild.add(geoDataKey, arrayBuilder.build());
        jsonBuild.add(facetsKey, facetJson);
        jsonBuild.add(docsKey, docs);
        JsonObject json = jsonBuild.build();  
        
        jsonReader.close();
        return json.toString();
    }

    public String download(String text, String collection,
            String typeStatus, String family, int numRows) {
 

        JsonReader jsonReader = null; 
        JsonObject jsonObj;
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        String results;
        int totalDownload = numRows <= maxDownload ? numRows : maxDownload;
        for (int i = 0; i < totalDownload; i += downloadSize) {
            arrayBuilder = Json.createArrayBuilder();

            results = service.download(text, collection, typeStatus, family, i, downloadSize); 
            
            jsonReader = Json.createReader(new StringReader(results));
            
            jsonObj = jsonReader.readObject(); 
            JsonArray docs = jsonObj.getJsonArray(responseKey); 
             
//            for (JsonValue value : docs) {
//                builder.add(value);
//            } 
            docs.stream() 
                    .forEach(value -> {
                        builder.add(value);
                    }) ;
        }
        if(jsonReader != null) {
            jsonReader.close();
        }
        
        JsonArray array = builder.build();
        log.info("size : {}", array.size());
        return array.toString();   
    }
}
